package shuffleset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OutputWriter {
    public static void write(ArrayList<Track> tracks, String inputPath, String outputPath, int originalTotal) throws IOException {
        File parent = new File(outputPath).getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try(PrintWriter out = new PrintWriter(new FileWriter(outputPath))) {
            out.println("ShuffleSet - Set List");
            out.println("Source:  " + inputPath);
            out.println("Tracks: " + tracks.size());
            out.println("#   Track                                   Artist                     BPM  Key  Energy");
            int count = 1;
            for(Track a : tracks) {
                out.printf("%-4d%-40s%-27s%-5d%-5s%d%n", count, fit(a.getName(), 38), fit(a.getArtist(), 25), a.getBpm(), a.getCamelot(), a.getEnergy());
                count++;
            }
            out.println();
            out.println("Transitions");
            int transitions = tracks.size() - 1;
            if (transitions < 1) {
                out.println("No transitions");
            } else {
                out.println("#         Keys         Score  Tip");
                for (int i = 0; i < transitions; ++i) {
                    Track a = tracks.get(i);
                    Track b = tracks.get(i + 1);
                    out.printf("%-10s%-13s%-7d%s%n", (i + 1) + " -> " + (i + 2), a.getCamelot() + " -> " + b.getCamelot(), TransitionScore.score(a, b), CamelotKey.tip(a, b));
                }
            }
            int newTotal = SetOrderMaker.total(tracks);
            out.println();
            out.println("Summary");
            out.println("Total score: " + newTotal);
            out.println("Before -> after: " + originalTotal + " -> " + newTotal);
            if (transitions >= 1) {
                out.printf("Average per transition: %.1f%n", (double) newTotal / transitions);
            }

            out.println();
            out.println("Weak Spots (score below " + 50 + ")");
            boolean anyWeak = false;
            for (int i = 0; i < transitions; ++i) {
                Track a = tracks.get(i);
                Track b = tracks.get(i + 1);
                int score = TransitionScore.score(a, b);
                if (score < 50) {
                    out.println((i + 1) + " -> " + (i + 2) + " (score " + score + "): " + CamelotKey.tip(a, b));
                    anyWeak = true;
                }
            }
            if (!anyWeak) {
                out.println("No weak transitions");
            }

            if (out.checkError()) {
                throw new IOException("Failed writing to " + outputPath);
            }
        }
    }

    private static String fit(String text, int width) {
        if (text.length() > width) {
            return text.substring(0, width - 3) + "...";
        }
        return text;
    }
}
