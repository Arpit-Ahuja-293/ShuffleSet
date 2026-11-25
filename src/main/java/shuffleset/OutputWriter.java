package shuffleset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OutputWriter {
    public static void write(ArrayList<Track> tracks, String inputPath, String outputPath) throws IOException {
        boolean made = new File(outputPath).getParentFile().mkdirs();
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
