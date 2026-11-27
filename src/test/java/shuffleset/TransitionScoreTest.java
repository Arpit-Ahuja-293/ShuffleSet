package shuffleset;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TransitionScoreTest {

    @Test
    void bpmSameTempoTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 50);
        assertEquals(100, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmSmallGapTest() {
        Track a = new Track("u1", "First", "Artist", 124, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 126, "8A", 50);
        assertEquals(80, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmHalfwayTest() {
        Track a = new Track("u1", "First", "Artist", 125, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 130, "8A", 50);
        assertEquals(50, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmHalfTimeTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 64, "8A", 50);
        assertEquals(100, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmDoubleTimeTest() {
        Track a = new Track("u1", "First", "Artist", 64, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 50);
        assertEquals(100, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmAtLimitTest() {
        Track a = new Track("u1", "First", "Artist", 125, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 135, "8A", 50);
        assertEquals(0, TransitionScore.bpmScore(a, b));
    }

    @Test
    void bpmPastLimitTest() {
        Track a = new Track("u1", "First", "Artist", 120, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 135, "8A", 50);
        assertEquals(0, TransitionScore.bpmScore(a, b));
    }

    @Test
    void energyEqualTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 70);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 70);
        assertEquals(100, TransitionScore.energyScore(a, b));
    }

    @Test
    void energyGapUpTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 75);
        assertEquals(50, TransitionScore.energyScore(a, b));
    }

    @Test
    void energyGapDownTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 75);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 50);
        assertEquals(50, TransitionScore.energyScore(a, b));
    }

    @Test
    void energyAtLimitTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 0);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 50);
        assertEquals(0, TransitionScore.energyScore(a, b));
    }

    @Test
    void energyPastLimitTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 20);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 90);
        assertEquals(0, TransitionScore.energyScore(a, b));
    }

    @Test
    void scorePerfectPairTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 70);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 70);
        assertEquals(100, TransitionScore.score(a, b));
    }

    @Test
    void scoreWorkedExampleTest() {
        Track a = new Track("u1", "First", "Artist", 124, "8A", 70);
        Track b = new Track("u2", "Second", "Artist", 126, "9A", 75);
        assertEquals(82, TransitionScore.score(a, b));
    }

    @Test
    void scoreWorstPairTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 10);
        Track b = new Track("u2", "Second", "Artist", 90, "3B", 95);
        assertEquals(0, TransitionScore.score(a, b));
    }

    @Test
    void scoreBpmWeightTest() {
        Track a = new Track("u1", "First", "Artist", 125, "8A", 50);
        Track b = new Track("u2", "Second", "Artist", 135, "8A", 50);
        assertEquals(60, TransitionScore.score(a, b));
    }

    @Test
    void scoreEnergyWeightTest() {
        Track a = new Track("u1", "First", "Artist", 128, "8A", 20);
        Track b = new Track("u2", "Second", "Artist", 128, "8A", 90);
        assertEquals(80, TransitionScore.score(a, b));
    }
}
