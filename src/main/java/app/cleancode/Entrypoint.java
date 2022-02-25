package app.cleancode;

import app.cleancode.profiler.Profiler;

public class Entrypoint {
    private static int REPETITIONS = 1000000;
    private static int maxPeople = 100;

    // Change to true to enable profiling
    private static final boolean profile = false;

    public static void main(String[] args) {
        Profiler profiler = null;
        if (profile) {
            profiler = new Profiler();
            Thread profilerThread = new Thread(profiler, "Profiler thread");
            profilerThread.start();
        }
        long start = System.currentTimeMillis();
        UsedBirthdays usedBirthdays = new UsedBirthdays();
        for (int n = 2; n <= maxPeople; n++) {
            int numIntersections = 0;
            for (int i = 0; i < REPETITIONS; i++) {
                boolean intersected = false;
                for (int j = 0; j < n; j++) {
                    int birthday = Birthday.random();
                    if (usedBirthdays.isUsed(birthday)) {
                        intersected = true;
                        break;
                    } else {
                        usedBirthdays.add(birthday);
                    }
                }
                if (intersected) {
                    numIntersections++;
                }
                usedBirthdays.clear();
            }
            double intersectionProbability = (double) numIntersections / REPETITIONS;
            System.out.printf("%.2f%%\n", intersectionProbability * 100);
        }
        long time = System.currentTimeMillis() - start;
        System.out.printf("It took %.3fs\n", time / 1000d);
        if (profile) {
            profiler.stop();
            profiler.dump();
        }
    }
}
