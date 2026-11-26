package shuffleset;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TrackParserTest {
    private static final String directory = "data/samples/";

    @Test
    void smallCsvParsesFourTrack() throws IOException {
        assertEquals(4, TrackParser.parse(directory + "small.csv").size());
    }

    @Test
    void mainCsvParsesTwelveTrack() throws IOException {
        assertEquals(12, TrackParser.parse(directory + "main.csv").size());
    }

    @Test
    void edgeCsvParsesSixTrack() throws IOException {
        assertEquals(6, TrackParser.parse(directory + "edge-cases.csv").size());
    }

    @Test
    void badHeaderNamesMissingColumn() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> TrackParser.parse(directory + "bad-header.csv"));
        assertEquals("Missing required column: BPM", e.getMessage());
    }

    @Test
    void missingFileThrowsIOException() {
        assertThrows(IOException.class, () -> TrackParser.parse(directory + "nope.csv"));
    }

    @Test
    void commaInQuotedTitleKept() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "edge-cases.csv");
        assertNotNull(findName(tracks, "Stay, Don't Go"));
    }

    @Test
    void escapedQuotesInTitleUnescaped() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "edge-cases.csv");
        assertNotNull(findName(tracks, "The \"Real\" Mix"));
    }

    @Test
    void utfAccentCase() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "edge-cases.csv");
        assertNotNull(findName(tracks,"Café Del Mar"));
    }

    @Test
    void utfAccentCaseTwo() throws IOException {
        assertEquals("Röyksopp", findName(TrackParser.parse(directory + "edge-cases.csv"), "Café Del Mar").getArtist());
    }

    @Test
    void escapedCommaInArtist() throws IOException {
        assertEquals("Earth, Wind & Fire, The Emotions", findName(TrackParser.parse(directory + "edge-cases.csv"), "Boogie Wonderland (with The Emotions)").getArtist());
    }

    @Test
    void invalidRowsSkipped() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "edge-cases.csv");
        String[] skipped = {"Love Sensation", "Good Times", "Hollywood Swinging",
                "Disco Inferno", "Higher Ground", "I Will Survive", "Keep Forgettin'"};
        for (String name : skipped) {
            for (Track track : tracks) {
                assertNotEquals(name, track.getName(), name + " should have been skipped");
            }
        }
    }

    @Test
    void halfTimeBpmKept() throws IOException {
        assertEquals(64, findName(TrackParser.parse(directory + "edge-cases.csv"), "Half Time Groove").getBpm());
    }

    @Test
    void laMoucheBpmTrimmed() throws IOException {
        assertEquals(131, findName(TrackParser.parse(directory + "edge-cases.csv"), "La Mouche").getBpm());
    }

    @Test
    void laMoucheCamelotTrimmedAndUppercased() throws IOException {
        assertEquals("5A", findName(TrackParser.parse(directory + "edge-cases.csv"), "La Mouche").getCamelot());
    }

    @Test
    void laMoucheEnergyTrimmed() throws IOException {
        assertEquals(68, findName(TrackParser.parse(directory + "edge-cases.csv"), "La Mouche").getEnergy());
    }

    @Test
    void emptyCsvThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> TrackParser.parse(directory + "empty.csv"));
    }

    @Test
    void boundaryValuesKept() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "boundaries.csv");
        assertEquals("12B", findName(tracks, "Camelot Max").getCamelot());
        assertEquals("1A", findName(tracks, "Camelot Min").getCamelot());
        assertEquals(0, findName(tracks, "Energy Zero").getEnergy());
        assertEquals(100, findName(tracks, "Energy Max").getEnergy());
    }

    @Test
    void outOfRangeValuesSkipped() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse(directory + "boundaries.csv");
        assertEquals(4, tracks.size());
        String[] skipped = {"Camelot Zero", "Camelot No Letter", "Negative BPM"};
        for (String name : skipped) {
            for (Track track : tracks) {
                assertNotEquals(name, track.getName(), name + " should have been skipped");
            }
        }
    }

    @Test
    void duplicateKeepsFirst() throws IOException {
        Track boogie = findName(TrackParser.parse(directory + "edge-cases.csv"), "Boogie Wonderland (with The Emotions)");
        assertNotEquals("Earth, Wind & Fire (duplicate)", boogie.getArtist());
    }

    private Track findName(ArrayList<Track> tracks, String name) {
        for (Track track : tracks) {
            if (track.getName().equals(name)) {
                return track;
            }
        }
        fail("Track not found: " + name);
        return null;
    }
}
