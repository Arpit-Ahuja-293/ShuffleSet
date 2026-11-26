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

        OutputWriter.write(tracks, "data/samples/main.csv", out.toString());
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
        assertEquals(5, lines.size());
        assertEquals("1   La Mouche                               Cassius                    131  5A   68", lines.get(4));
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
        OutputWriter.write(tracks, "data/samples/main.csv", out.toString());
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
        assertEquals(5, lines.size());
        assertEquals("Tracks: 1", lines.get(2));
    }

    @Test
    void filenameNoFolder() throws IOException {
        Path out = Path.of("outputwriter-test.txt");
        try {
            ArrayList<Track> tracks = new ArrayList<>();
            tracks.add(new Track("uri", "La Mouche", "Cassius", 131, "5A", 68));
            OutputWriter.write(tracks, "data/samples/main.csv", out.toString());
            assertTrue(Files.exists(out));
        } finally {
            Files.deleteIfExists(out);
        }
    }

    //helper method
    private ArrayList<String> writeAndRead(ArrayList<Track> tracks) throws IOException {
        Path out = tempDirectory.resolve("set.txt");
        OutputWriter.write(tracks, "data/samples/main.csv", out.toString());
        return new ArrayList<>(Files.readAllLines(out));
    }
}
