package shuffleset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.*;

public class TrackParser {
    private static final String[] requiredColumns = {
            "Track URI", "Track Name", "Artist Name(s)", "BPM", "Camelot", "Energy"};

    public static ArrayList<Track> parse(String filePath) throws IOException {
        ArrayList<Track> tracks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader().setSkipHeaderRecord(true).get()
                     .parse(reader)) {
            for (String column : requiredColumns) {
                if (!csvParser.getHeaderMap().containsKey(column)) {
                    throw new IllegalArgumentException("Missing required column: " + column);
                }
            }

            HashSet<String> seenUris = new HashSet<>();
            for (CSVRecord record : csvParser) {
                if (!record.isConsistent()) {
                    System.err.println("Warning: row " + record.getRecordNumber() + " skipped - wrong number of columns");
                    continue;
                }
                String name = record.get("Track Name").trim();
                String bpmValue = record.get("BPM").trim();
                Integer bpm = properParse(bpmValue);
                if (bpm == null || bpm <= 0) {
                    System.err.println("Warning: row " + record.getRecordNumber() + " \"" + name + "\" skipped - invalid BPM \"" + bpmValue + "\"");
                    continue;
                }
                String uriValue = record.get("Track URI").trim();
                String artist = record.get("Artist Name(s)").trim();
                String camelotVal;
                camelotVal = record.get("Camelot").trim().toUpperCase();
                boolean validCamelot = false;
                if (camelotVal.length() >= 2) {
                    char letter = camelotVal.charAt(camelotVal.length() - 1);
                    Integer number = properParse(camelotVal.substring(0, camelotVal.length() - 1));
                    validCamelot = (letter == 'A' || letter == 'B') && number != null && number >= 1 && number <= 12;
                }

                if (!validCamelot) {
                    System.err.println("Warning: row " + record.getRecordNumber() + " \"" + name + "\" skipped - invalid Camelot \"" + camelotVal + "\"");
                    continue;
                }
                String energyLevel = record.get("Energy").trim();
                Integer energy = properParse(energyLevel);
                if (energy == null || energy < 0 || energy > 100) {System.err.println("Warning: row " + record.getRecordNumber() + " \"" + name + "\" skipped - invalid Energy \"" + energyLevel + "\"");
                    continue;
                }
                if (!seenUris.add(uriValue)) {
                    System.err.println("Warning: row " + record.getRecordNumber()
                            + " \"" + name + "\" skipped - duplicate track");
                    continue;
                }
                tracks.add(new Track(uriValue, name, artist, bpm, camelotVal, energy));
            }
        }
        if (tracks.isEmpty()) {
            throw new IllegalArgumentException("No valid tracks found in " + filePath);
        }
        return tracks;
    }

    private static Integer properParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
