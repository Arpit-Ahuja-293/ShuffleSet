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
        assertTrue(e.getMessage().contains("BPM"));
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
    void escapedCommaInArtist() throws IOException {
        assertEquals("Earth\\, Wind & Fire, The Emotions", findName(TrackParser.parse(directory + "edge-cases.csv"), "Boogie Wonderland (with The Emotions)").getArtist());
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

    private Track findName(ArrayList<Track> tracks, String name) {
        for (Track track : tracks) {
            if (track.getName().equals(name)) {
                return track;
            }
        }
        return null;
    }
}
