package app.cleancode.profiler;

import java.lang.Thread.State;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Profiler implements Runnable {
    private final Map<String, Statistics> samples = new HashMap<>();
    private int numSamples = 0;
    private volatile boolean shouldBeRunning = true;

    @Override
    public void run() {
        while (shouldBeRunning) {
            try {
                Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
                    if (!samples.containsKey(thread.getName())) {
                        samples.put(thread.getName(), new Statistics());
                    }
                    Statistics threadStatistics = samples.get(thread.getName());
                    for (StackTraceElement stackTraceElement : stackTrace) {
                        String className = stackTraceElement.getClassName();
                        String methodName = stackTraceElement.getMethodName();
                        Sample sample = new Sample(className, methodName);
                        if (!threadStatistics.samples.contains(sample)) {
                            threadStatistics.samples.add(sample);
                        } else {
                            sample = threadStatistics.samples
                                    .get(threadStatistics.samples.indexOf(sample));
                        }
                        sample.occurrences++;
                    }
                    if (thread.getState().equals(State.RUNNABLE)) {
                        threadStatistics.running++;
                    } else {
                        threadStatistics.blocking++;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            numSamples++;
        }
    }

    public void dump() {
        System.out.println();
        System.out.println("Profiler results:");
        System.out.printf("Recorded %d samples\n", numSamples);
        samples.forEach((thread, threadStatistics) -> {
            System.out.println("For thread: " + thread);
            System.out.printf("Spent %.2f%% actually running\n", threadStatistics.running
                    / (double) (threadStatistics.running + threadStatistics.blocking) * 100);
            Collections.sort(threadStatistics.samples);
            Collections.reverse(threadStatistics.samples);
            for (Sample sample : threadStatistics.samples) {
                System.out.printf("%.2f%%: %s:%s\n",
                        sample.occurrences / (double) numSamples * 100d, sample.claz,
                        sample.method);
            }
        });
    }

    public void stop() {
        this.shouldBeRunning = false;
    }
}
