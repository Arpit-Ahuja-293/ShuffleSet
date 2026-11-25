package shuffleset;

public class Track {
    private final String uri;
    private final String name;
    private final String artist;
    private final int bpm;
    private final String camelot;
    private final int energy;

    public Track(String uri, String name, String artist, int bpm, String camelot, int energy) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.bpm = bpm;
        this.camelot = camelot;
        this.energy = energy;
    }

    public int getCamelotNumber() {
        String num = camelot.substring(0, camelot.length() - 1);
        return Integer.parseInt(String.valueOf(num));
    }

    public char getCamelotLetter() {
        char letter = camelot.charAt(camelot.length() - 1);
        return letter;
    }

    public int getBpm() {
        return bpm;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getArtist() {
        return artist;
    }

    public String getCamelot() {
        return camelot;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return name + " - " + artist + " (" + bpm + " BPM, " + camelot + ", " + "energy " + energy + ")";
    }
}
