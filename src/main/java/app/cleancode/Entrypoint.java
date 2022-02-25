package app.cleancode;

import java.util.concurrent.atomic.AtomicInteger;
import app.cleancode.profiler.Profiler;

public class Entrypoint {
    private static int REPETITIONS = 1000000;
    private static int maxPeople = 100;

    // Change to true to enable profiling
    private static final boolean profile = true;

    public static void main(String[] args) throws Exception {
        Profiler profiler = null;
        if (profile) {
            profiler = new Profiler();
            Thread profilerThread = new Thread(profiler, "Profiler thread");
            profilerThread.start();
        }
        long start = System.currentTimeMillis();
        AtomicInteger[] IntersectionCounts = new AtomicInteger[maxPeople + 1];
        for (int i = 2; i <= maxPeople; i++) {
            IntersectionCounts[i] = new AtomicInteger();
        }
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] workers = new Thread[numThreads];
        int repetitionsPerWorker = REPETITIONS / numThreads;
        for (int thread = 0; thread < numThreads; thread++) {
            Thread worker = new Thread(() -> {
                Birthday birthdayGenerator = new Birthday();
                UsedBirthdays usedBirthdays = new UsedBirthdays();
                for (int n = 2; n <= maxPeople; n++) {
                    int numIntersections = 0;
                    for (int i = 0; i < repetitionsPerWorker; i++) {
                        boolean intersected = false;
                        for (int j = 0; j < n; j++) {
                            int birthday = birthdayGenerator.random();
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
                    IntersectionCounts[n].addAndGet(numIntersections);
                }
            }, "worker " + thread);
            worker.start();
            workers[thread] = worker;
        }
        for (Thread worker : workers) {
            worker.join();
        }
        for (int i = 2; i <= maxPeople; i++) {
            double probability = IntersectionCounts[i].doubleValue() / REPETITIONS;
            System.out.printf("%.2f%% for %d\n", probability * 100, i);
        }
        long time = System.currentTimeMillis() - start;
        System.out.printf("It took %.3fs\n", time / 1000d);
        if (profile) {
            profiler.stop();
            profiler.dump();
        }
    }
}
