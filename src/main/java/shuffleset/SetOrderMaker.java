package shuffleset;

import java.util.ArrayList;

public class SetOrderMaker {
    public static ArrayList<Track> order(ArrayList<Track> tracks) {
        ArrayList<Track> bestSet = null;
        int bestTotal = -1;

        for (int i = 0; i < tracks.size(); ++i) {
            ArrayList<Track> left = new ArrayList<>(tracks);
            ArrayList<Track> setlist = new ArrayList<>();
            setlist.add(left.remove(i));
            while (!left.isEmpty()) {
                Track last = setlist.get(setlist.size() - 1);
                int bestIndex = 0;
                int bestScore = -1;
                for (int j = 0; j < left.size(); ++j) {
                    int score = TransitionScore.score(last, left.get(j));
                    if (score > bestScore) {
                        bestScore = score;
                        bestIndex = j;
                    }
                }
                setlist.add(left.remove(bestIndex));
            }
            int total = 0;
            for (int k = 0; k < setlist.size() - 1; ++k) {
                total += TransitionScore.score(setlist.get(k), setlist.get(k + 1));
            }

            if (total > bestTotal) {
                bestTotal = total;
                bestSet = setlist;
            }
        }
        return bestSet;
    }
}
