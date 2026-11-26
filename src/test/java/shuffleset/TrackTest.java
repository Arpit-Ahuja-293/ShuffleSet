package shuffleset;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {

    @Test
    void oneDigitKeyTest() {
        Track test = new Track("uri", "oneDigitTest", "Artist", 120, "1A", 50);
        assertEquals(1, test.getCamelotNumber());
        assertEquals('A', test.getCamelotLetter());
    }

    @Test
    void twoDigitKeyTest() {
        Track test = new Track("uri", "twoDigitTest", "Artist", 120, "12B", 50);
        assertEquals(12, test.getCamelotNumber());
        assertEquals('B', test.getCamelotLetter());
    }

    @Test
    void toStringTest() {
        Track stringTest = new Track("uri", "La Mouche", "Cassius", 131, "5A", 68);
        assertEquals("La Mouche - Cassius (131 BPM, 5A, energy 68)", stringTest.toString());
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
