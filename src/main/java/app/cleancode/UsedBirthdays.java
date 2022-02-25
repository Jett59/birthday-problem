package app.cleancode;

public class UsedBirthdays {
    private static final boolean[] EMPTY = new boolean[365];

    private boolean[] usedBirthdays = new boolean[365];

    public void add(int birthday) {
        usedBirthdays[birthday] = true;
    }

    public boolean isUsed(int birthday) {
        return usedBirthdays[birthday];
    }

    public void clear() {
        System.arraycopy(EMPTY, 0, usedBirthdays, 0, usedBirthdays.length);
    }
}
