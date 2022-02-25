package app.cleancode;

import java.util.HashSet;
import java.util.Set;

public class Entrypoint {
    private static int REPETITIONS = 1000000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int n = 2; n <= 100; n++) {
            int numIntersections = 0;
            for (int i = 0; i < REPETITIONS; i++) {
                Set<Birthday> birthdays = new HashSet<>(n);
                boolean intersected = false;
                for (int j = 0; j < n; j++) {
                    Birthday birthday = Birthday.random();
                    if (birthdays.contains(birthday)) {
                        intersected = true;
                        break;
                    } else {
                        birthdays.add(birthday);
                    }
                }
                if (intersected) {
                    numIntersections++;
                }
            }
            double intersectionProbability = (double) numIntersections / REPETITIONS;
            System.out.printf("%.2f%%\n", intersectionProbability * 100);
        }
        long time = System.currentTimeMillis() - start;
        System.out.printf("It took %.3fs\n", time / 1000d);
    }
}
