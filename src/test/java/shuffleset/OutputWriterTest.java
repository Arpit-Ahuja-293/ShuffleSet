package shuffleset;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.util.List;

public class OutputWriterTest {
    @TempDir
    Path tempDirectory;

    @Test
    void headerTest() throws IOException {
        Path out = tempDirectory.resolve("set.txt");
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));

        OutputWriter.write(tracks, "data/samples/main.csv", out.toString(), SetOrderMaker.total(tracks));
        List<String> lines = Files.readAllLines(out);

        assertLinesMatch(List.of(
                "ShuffleSet - Set List",
                "Source:  data/samples/main.csv",
                "Tracks: 1",
                "#   Track                                   Artist                     BPM  Key  Energy"),
                lines.subList(0, 4));
    }

    @Test
    void rowFormat() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));
        ArrayList<String> lines = writeAndRead(tracks);
        assertEquals("1   La Mouche                               Cassius                    131  5A   68", lines.get(4));
        assertEquals("", lines.get(5), "table should end after one row");
    }

    @Test
    void titleBoundaryNotTruncated() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "A".repeat(38), "Artist", 120, "8A", 50));
        assertEquals("A".repeat(38), writeAndRead(tracks).get(4).substring(4, 44).trim());
    }

    @Test
    void titleLimitTruncated() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "A".repeat(39), "Artist", 120, "8A", 50));
        assertEquals("A".repeat(35) + "...", writeAndRead(tracks).get(4).substring(4, 44).trim());
    }

    @Test
    void creatingMissingFolder() throws IOException {
        Path out = tempDirectory.resolve("a/b/set.txt");
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));
        OutputWriter.write(tracks, "data/samples/main.csv", out.toString(), SetOrderMaker.total(tracks));
        assertTrue(Files.exists(out));
    }

    @Test
    void overwritingExistingFile() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "First", "A", 120, "8A", 50));
        tracks.add(new Track("u2", "Second", "B", 122, "9A", 60));
        writeAndRead(tracks);

        tracks.remove(1);
        ArrayList<String> lines = writeAndRead(tracks);
        assertEquals("Tracks: 1", lines.get(2));
        assertEquals("", lines.get(5), "old second row should be gone");
        assertTrue(lines.contains("No transitions"));
    }

    @Test
    void transitionCountTest() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "First", "A", 128, "8A", 70));
        tracks.add(new Track("u2", "Second", "B", 128, "9A", 70));
        tracks.add(new Track("u3", "Third", "C", 128, "10A", 70));
        ArrayList<String> lines = writeAndRead(tracks);

        int start = lines.indexOf("Transitions");
        assertTrue(start >= 0, "missing Transitions heading");
        assertEquals("#         Keys         Score  Tip", lines.get(start + 1));
        assertTrue(lines.get(start + 2).startsWith("1 -> 2    8A -> 9A     92"));
        assertTrue(lines.get(start + 3).startsWith("2 -> 3    9A -> 10A    92"));
        assertEquals("", lines.get(start + 4), "expected exactly 2 transitions");
    }

    @Test
    void singleTrackSummaryTest() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));
        ArrayList<String> lines = writeAndRead(tracks);

        assertTrue(lines.contains("No transitions"));
        assertTrue(lines.contains("Total score: 0"));
        assertTrue(lines.contains("No weak transitions"));
        for (String line : lines) {
            assertFalse(line.startsWith("Average"), "no average with zero transitions");
        }
    }

    @Test
    void beforeAfterTest() throws IOException {
        Path out = tempDirectory.resolve("set.txt");
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "First", "A", 128, "8A", 70));
        tracks.add(new Track("u2", "Second", "B", 128, "8A", 70));
        OutputWriter.write(tracks, "data/samples/main.csv", out.toString(), 40);
        List<String> lines = Files.readAllLines(out);

        assertTrue(lines.contains("Total score: 100"));
        assertTrue(lines.contains("Before -> after: 40 -> 100"));
        assertTrue(lines.contains("Average per transition: 100.0"));
    }

    @Test
    void weakSpotTest() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "First", "A", 128, "8A", 70));
        tracks.add(new Track("u2", "Second", "B", 90, "2B", 20));
        ArrayList<String> lines = writeAndRead(tracks);
        assertTrue(lines.contains("Weak Spots (score below 50)"));
        assertTrue(lines.contains("1 -> 2 (score 0): Key clash - quick cut or mix on a drum break"));
        assertFalse(lines.contains("No weak transitions"));
    }

    @Test
    void noWeakSpotsTest() throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "First", "A", 128, "8A", 70));
        tracks.add(new Track("u2", "Second", "B", 128, "9A", 70));
        ArrayList<String> lines = writeAndRead(tracks);
        assertTrue(lines.contains("No weak transitions"));
    }

    @Test
    void filenameNoFolder() throws IOException {
        Path out = Path.of("outputwriter-test.txt");
        try {
            ArrayList<Track> tracks = new ArrayList<>();
            tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));
            OutputWriter.write(tracks, "data/samples/main.csv", out.toString(), SetOrderMaker.total(tracks));
            assertTrue(Files.exists(out));
        } finally {
            Files.deleteIfExists(out);
        }
    }

    //helper method
    private ArrayList<String> writeAndRead(ArrayList<Track> tracks) throws IOException {
        Path out = tempDirectory.resolve("set.txt");
        OutputWriter.write(tracks, "data/samples/main.csv", out.toString(), SetOrderMaker.total(tracks));
        return new ArrayList<>(Files.readAllLines(out));
    }
}
