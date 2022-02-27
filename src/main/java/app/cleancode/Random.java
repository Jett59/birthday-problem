package app.cleancode;

public class Random {
    private int seed;

    public Random(int seed) {
        this.seed = seed;
    }

    public int rand(int range) {
        int temp = seed;
        temp ^= temp << 13;
        temp ^= temp >> 17;
        temp ^= temp << 5;
        return Math.abs(seed = temp) % range;
    }

    public void split(Random[] others) {
        for (int i = 0; i < others.length; i++) {
            others[i] = new Random(seed + 31 * i);
        }
    }
}
