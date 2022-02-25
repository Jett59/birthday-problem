package app.cleancode;

import java.util.Random;

public record Birthday(int day) {
    private static Random rand = new Random();

    public static Birthday random() {
        return new Birthday(rand.nextInt(365) + 1);
    }
}
