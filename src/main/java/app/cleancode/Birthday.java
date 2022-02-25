package app.cleancode;

import java.util.SplittableRandom;

public class Birthday {
    private static final SplittableRandom rand = new SplittableRandom();

    public static int random() {
        return rand.nextInt(365);
    }
}
