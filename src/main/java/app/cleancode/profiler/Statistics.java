package app.cleancode.profiler;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    List<Sample> samples;
    int running, blocking;

    public Statistics() {
        samples = new ArrayList<>();
    }

    public int getSampleCount() {
        return running + blocking;
    }
}
