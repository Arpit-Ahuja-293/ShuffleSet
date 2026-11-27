package shuffleset;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class SetOrderMakerTest {

    private ArrayList<Track> mixedTracks() {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u1", "One", "Artist", 128, "8A", 70));
        tracks.add(new Track("u2", "Two", "Artist", 90, "2B", 30));
        tracks.add(new Track("u3", "Three", "Artist", 127, "9A", 72));
        tracks.add(new Track("u4", "Four", "Artist", 91, "3B", 35));
        tracks.add(new Track("u5", "Five", "Artist", 126, "7A", 68));
        tracks.add(new Track("u6", "Six", "Artist", 92, "2B", 32));
        return tracks;
    }

    @Test
    void keepsAllTracksTest() {
        ArrayList<Track> tracks = mixedTracks();
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        assertEquals(tracks.size(), ordered.size());
        HashSet<String> uris = new HashSet<>();
        for (Track t : ordered) {
            assertTrue(uris.add(t.getUri()), "duplicate track: " + t.getUri());
        }
        for (Track t : tracks) {
            assertTrue(uris.contains(t.getUri()), "missing track: " + t.getUri());
        }
    }

    @Test
    void doesNotChangeInputTest() {
        ArrayList<Track> tracks = mixedTracks();
        ArrayList<Track> copy = new ArrayList<>(tracks);
        SetOrderMaker.order(tracks);
        assertEquals(copy, tracks);
    }

    @Test
    void improvesOrNotWorseTest() {
        ArrayList<Track> tracks = mixedTracks();
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        int before = SetOrderMaker.total(tracks), after = SetOrderMaker.total(ordered);
        assertTrue(after >= before, "ordered total " + after + " less than original " + before);
        assertTrue(after > before, "expected ordering to improve a bad order");
    }

    @Test
    void obviousChainTest() {
        // 6A-7A-8A-9A at the same tempo and energy is the only best chain.
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track("u9", "Nine", "Artist", 128, "9A", 70));
        tracks.add(new Track("u6", "Six", "Artist", 128, "6A", 70));
        tracks.add(new Track("u8", "Eight", "Artist", 128, "8A", 70));
        tracks.add(new Track("u7", "Seven", "Artist", 128, "7A", 70));

        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        String keys = "";
        for (Track t : ordered) {
            keys += t.getCamelot() + " ";
        }
        keys = keys.trim();

        assertTrue(keys.equals("6A 7A 8A 9A") || keys.equals("9A 8A 7A 6A"), "got " + keys);
    }

    @Test
    void picksBestStartTest() {
        // Starting at index 0 (X) gives a worse greedy set (172),
        // so order() must find a better start like A for the best total (184).
        Track x = new Track("ux", "X", "Artist", 90, "1B", 20);
        Track a = new Track("ua", "A", "Artist", 128, "8A", 70);
        Track b = new Track("ub", "B", "Artist", 128, "9A", 70);
        Track c = new Track("uc", "C", "Artist", 128, "10A", 70);
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(x);
        tracks.add(b);
        tracks.add(a);
        tracks.add(c);

        ArrayList<Track> ordered = SetOrderMaker.order(tracks);

        assertEquals(184, SetOrderMaker.total(ordered));
        assertTrue(ordered.get(0) == x || ordered.get(ordered.size() - 1) == x, "X should be at one end");
    }

    @Test
    void emptyListTest() {
        ArrayList<Track> ordered = SetOrderMaker.order(new ArrayList<>());
        assertNotNull(ordered);
        assertTrue(ordered.isEmpty());
    }

    @Test
    void singleTrackTest() {
        Track only = new Track("u1", "Only", "Artist", 128, "8A", 70);
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(only);
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        assertEquals(1, ordered.size());
        assertSame(only, ordered.get(0));
    }

    @Test
    void twoTracksTest() {
        Track first = new Track("u1", "First", "Artist", 128, "8A", 70);
        Track second = new Track("u2", "Second", "Artist", 100, "3B", 40);
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(first);
        tracks.add(second);
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        assertEquals(2, ordered.size());
        assertTrue(ordered.contains(first));
        assertTrue(ordered.contains(second));
    }

    @Test
    void identicalTracksTest() {
        ArrayList<Track> tracks = new ArrayList<>();
        for (int i = 1; i <= 5; ++i) {
            tracks.add(new Track("u" + i, "Same " + i, "Artist", 128, "8A", 70));
        }
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);
        assertEquals(5, ordered.size());
        HashSet<String> uris = new HashSet<>();
        for (Track t : ordered) {
            assertTrue(uris.add(t.getUri()), "duplicate track: " + t.getUri());
        }
        assertEquals(400, SetOrderMaker.total(ordered));
    }

    @Test
    void sampleFileTest() throws IOException {
        ArrayList<Track> tracks = TrackParser.parse("data/samples/small.csv");
        ArrayList<Track> ordered = SetOrderMaker.order(tracks);

        assertEquals(tracks.size(), ordered.size());
        assertTrue(SetOrderMaker.total(ordered) >= SetOrderMaker.total(tracks));
    }
}
