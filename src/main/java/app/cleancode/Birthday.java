package app.cleancode;

import java.util.Random;

public class Birthday {
    private static Random rand = new Random();

    public static int random() {
        return rand.nextInt(365);
    }
}
