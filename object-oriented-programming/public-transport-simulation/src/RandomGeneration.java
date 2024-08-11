import java.util.Random;

public class RandomGeneration {
    public static int generate (int lower, int upper) {
        Random rand = new Random();
        return rand.nextInt(upper - lower + 1) + lower;
    }
}
