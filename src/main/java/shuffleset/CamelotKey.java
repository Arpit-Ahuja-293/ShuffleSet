package shuffleset;

public class CamelotKey {

    public static int score(Track a, Track b) {
        int numberA = a.getCamelotNumber();
        int numberB = b.getCamelotNumber();
        char letterA = a.getCamelotLetter();
        char letterB = b.getCamelotLetter();

        int difference = Math.abs(numberA - numberB);
        if(difference > 6) {
            difference = 12 - difference;
        }

        boolean matchingLetters = (letterA == letterB);
        int stepUp = (numberB - numberA + 12) % 12;
        boolean diagonal = (letterA == 'A' && letterB == 'B' && stepUp == 1) || (letterA == 'B' && letterB == 'A' && stepUp == 11);

        if (difference == 0 && matchingLetters) return 100;
        else if (difference == 1 && matchingLetters) return 80;
        else if (difference == 0) return 80;
        else if (difference == 2 && matchingLetters) return 50;
        else if (diagonal) return 30;
        else return 0;
    }

    public static String tip(Track a, Track b) {
        int val = score(a, b);
        String clashTip = "Key clash – quick cut or mix on a drum break";
        String diagnolTip = "Diagonal – short blend, transition quickly";
        String energyBoost = "Energy lift – use a shorter blend";
        String otherLetter = "Mood shift minor↔major – blend on a quiet section";
        String neighbor = "Smooth – blend over 16–32 bars";
        String sameKey = "Perfect match – long blend works";

        if(val == 0) {
            return clashTip;
        } else if(val == 30) {
            return diagnolTip;
        } else if(val == 50) {
            return energyBoost;
        } else if(val == 80) {
            if(a.getCamelotLetter() == b.getCamelotLetter()) {
                return neighbor;
            } else {
                return otherLetter;
            }
        } else if(val == 100) {
            return sameKey;
        } else {
            throw new IllegalStateException("Unexpected Score " + val);
        }
    }
}
