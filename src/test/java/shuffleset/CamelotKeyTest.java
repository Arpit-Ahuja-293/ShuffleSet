package shuffleset;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CamelotKeyTest {
    private final String clashTip = "Key clash – quick cut or mix on a drum break";
    private final String diagnolTip = "Diagonal – short blend, transition quickly";
    private final String energyBoost = "Energy lift – use a shorter blend";
    private final String otherLetter = "Mood shift minor to major – blend on a quiet section";
    private final String neighbor = "Smooth – blend over 16–32 bars";
    private final String sameKey = "Perfect match – long blend works";

    @Test
    void sameKeyTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "8A", 50);
        assertEquals(100, CamelotKey.score(a, b));
        assertEquals(sameKey, CamelotKey.tip(a, b));
    }

    @Test
    void neighborSameLetterTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "9A", 50);
        assertEquals(80, CamelotKey.score(a, b));
        assertEquals(neighbor, CamelotKey.tip(a, b));
    }

    @Test
    void neighborSameLetterTestWrap() {
        Track a = new Track("u1", "First", "Artist", 120, "12A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "1A", 50);
        assertEquals(80, CamelotKey.score(a, b));
        assertEquals(neighbor, CamelotKey.tip(a, b));
    }

    @Test
    void neighborOtherLetterTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "8B", 50);
        assertEquals(80, CamelotKey.score(a, b));
        assertEquals(otherLetter, CamelotKey.tip(a, b));
    }

    @Test
    void energyTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "10A", 50);
        assertEquals(50, CamelotKey.score(a, b));
        assertEquals(energyBoost, CamelotKey.tip(a, b));
    }

    @Test
    void diagonalUpTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "9B", 50);
        assertEquals(30, CamelotKey.score(a, b));
        assertEquals(diagnolTip, CamelotKey.tip(a, b));
    }

    @Test
    void diagonalDownTest() {
        Track a = new Track("u1", "First", "Artist", 120, "9B", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "8A", 50);
        assertEquals(30, CamelotKey.score(a, b));
        assertEquals(diagnolTip, CamelotKey.tip(a, b));
    }

    @Test
    void diagonalUpTestWrap() {
        Track a = new Track("u1", "First", "Artist", 120, "12A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "1B", 50);
        assertEquals(30, CamelotKey.score(a, b));
        assertEquals(diagnolTip, CamelotKey.tip(a, b));
    }

    @Test
    void diagonalDownTestWrap() {
        Track a = new Track("u1", "First", "Artist", 120, "1B", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "12A", 50);
        assertEquals(30, CamelotKey.score(a, b));
        assertEquals(diagnolTip, CamelotKey.tip(a, b));
    }

    @Test
    void wrongWayDiagonalATest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "7B", 50);
        assertEquals(0, CamelotKey.score(a, b));
        assertEquals(clashTip, CamelotKey.tip(a, b));
    }

    @Test
    void wrongWayDiagonalBTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8B", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "9A", 50);
        assertEquals(0, CamelotKey.score(a, b));
        assertEquals(clashTip, CamelotKey.tip(a, b));
    }

    @Test
    void wrongWayDiagonalTestWrap() {
        Track a = new Track("u1", "First", "Artist", 120, "1A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "12B", 50);
        assertEquals(0, CamelotKey.score(a, b));
        assertEquals(clashTip, CamelotKey.tip(a, b));
    }

    @Test
    void clashTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 120, "3B", 50);
        assertEquals(0, CamelotKey.score(a, b));
        assertEquals(clashTip, CamelotKey.tip(a, b));
    }
}
