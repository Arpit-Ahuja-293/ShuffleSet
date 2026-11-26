package shuffleset;

public class TransitionScore {
    private static final int MAX_BPM_PERCENT = 8;
    private static final double key = 0.4;
    private static final double BPM = 0.4;
    private static final double energy = 0.2;

    public static int bpmScore(Track a, Track b) {
        double bpmA = a.getBpm();
        double bpmB = b.getBpm();
        double[] targets = {bpmA, bpmA * 2, bpmA / 2};
        double smallestGap = Double.MAX_VALUE;
        for (double target : targets) {
            double gapPercent = Math.abs(bpmB - target) / target * 100;
            if (gapPercent < smallestGap) {
                smallestGap = gapPercent;
            }
        }

        double score = 100 - smallestGap * (100.0 / MAX_BPM_PERCENT);
        return (int) Math.round(Math.max(0, score));
    }

    public static int energyScore(Track a, Track b) {
        int energyA = a.getEnergy();
        int energyB = b.getEnergy();
        int diff = Math.abs(energyA - energyB);
        if(diff == 0) {
            return 100;
        } else {
            return Math.max(0, 100 - diff * 2);
        }
    }

    public static int score(Track a, Track b) {
        int camScore = CamelotKey.score(a, b);
        int bpmVal = bpmScore(a, b);
        int energyVal = energyScore(a, b);
        double total = camScore * key + bpmVal * BPM + energyVal * energy;
        return (int) Math.round(total);
    }
}
