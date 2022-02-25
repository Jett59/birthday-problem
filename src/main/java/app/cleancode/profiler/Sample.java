package app.cleancode.profiler;

import java.util.HashMap;
import java.util.Map;

public class Sample implements Comparable<Sample> {
    final String claz, method;
    final Map<Integer, Integer> lineNumbers = new HashMap<>();
    int occurrences;

    public Sample(String claz, String method) {
        this.claz = claz;
        this.method = method;
        this.occurrences = 0;
    }

    public void addOccurance(int lineNumber) {
        occurrences++;
        Integer previousLineNumberOccurrences = lineNumbers.get(lineNumber);
        lineNumbers.put(lineNumber,
                (previousLineNumberOccurrences == null) ? 1 : previousLineNumberOccurrences + 1);
    }

    @Override
    public int compareTo(Sample o) { // TODO Auto-generated method stub
        return Integer.compare(occurrences, o.occurrences);
    }

    public boolean equals(Object other) {
        return other instanceof Sample && ((Sample) other).claz.equals(claz)
                && ((Sample) other).method.equals(method);
    }

    @Override
    public String toString() { // TODO Auto-generated method stub
        return String.format("%d: %s:%s", occurrences, claz, method);
    }

}
