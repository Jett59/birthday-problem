package app.cleancode;

import java.util.concurrent.atomic.AtomicInteger;
import app.cleancode.profiler.Profiler;

public class Entrypoint {
    private static int REPETITIONS = 1000000;
    private static int maxPeople = 100;

    // Change to true to enable profiling
    private static final boolean profile = false;

    public static void main(String[] args) throws Exception {
        Profiler profiler = null;
        if (profile) {
            profiler = new Profiler();
            Thread profilerThread = new Thread(profiler, "Profiler thread");
            profilerThread.start();
        }
        long start = System.nanoTime();
        AtomicInteger[] IntersectionCounts = new AtomicInteger[maxPeople + 1];
        for (int i = 2; i <= maxPeople; i++) {
            IntersectionCounts[i] = new AtomicInteger();
        }
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.printf("Using %d worker threads\n", numThreads);
        Thread[] workers = new Thread[numThreads];
        int repetitionsPerWorker = REPETITIONS / numThreads;
        for (int thread = 0; thread < numThreads; thread++) {
            Thread worker = new Thread(() -> {
                Random primaryRand = new Random((int) System.nanoTime());
                UsedBirthdays usedBirthdays = new UsedBirthdays();
                int[] localIntersectionCounts = new int[maxPeople + 1];
                for (int n = 2; n <= maxPeople; n++) {
                    for (int i = 0; i < repetitionsPerWorker; i++) {
                        for (int j = 0; j < n; j++) {
                            int birthday = primaryRand.rand(365);
                            if (usedBirthdays.isUsed(birthday)) {
                                localIntersectionCounts[n]++;
                                break;
                            } else {
                                usedBirthdays.add(birthday);
                            }
                        }
                        usedBirthdays.clear();
                    }
                }
                for (int i = 2; i <= maxPeople; i++) {
                    IntersectionCounts[i].addAndGet(localIntersectionCounts[i]);
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
        long time = System.nanoTime() - start;
        System.out.printf("It took %.3fs\n", time / 1000000000d);
        if (profile) {
            profiler.stop();
            profiler.dump();
        }
    }
}
