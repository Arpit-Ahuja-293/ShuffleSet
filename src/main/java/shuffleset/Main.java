package shuffleset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter filename: ");
            String filename = scanner.nextLine();
            ArrayList<Track> tracks = TrackParser.parse(filename);
            System.out.println("Loaded " + tracks.size() + " tracks.");
            ArrayList<Track> orderedTracks = SetOrderMaker.order(tracks);
            System.out.println("Set Order: ");
            for(int i = 0; i < orderedTracks.size(); ++i) {
                System.out.println(orderedTracks.get(i).toString());
            }
            String baseName = new File(filename).getName().replace(".csv", "");
            String outputPath = "output/" + baseName + "-setlist.txt";
            int originalTotal = SetOrderMaker.total(tracks);
            OutputWriter.write(orderedTracks, filename, outputPath, originalTotal);
            System.out.println("Set list written to " + outputPath);
            System.out.println("Score: " + originalTotal + " -> " + SetOrderMaker.total(orderedTracks));
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error: file problem - " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
