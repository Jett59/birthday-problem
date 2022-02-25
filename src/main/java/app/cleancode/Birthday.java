package app.cleancode;

import java.util.SplittableRandom;

public class Birthday {
    private final SplittableRandom rand = new SplittableRandom();

    public int random() {
        return rand.nextInt(365);
    }
}
