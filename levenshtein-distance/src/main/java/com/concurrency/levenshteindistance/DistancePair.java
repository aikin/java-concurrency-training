package com.concurrency.levenshteindistance;

public class DistancePair {
    private final double distance;
    private final String word;

    public DistancePair(int distance, String word) {
        this.distance = distance;
        this.word = word;
    }

    public DistancePair(int distance) {
        this.distance = distance;
        this.word = "NONE";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistancePair that = (DistancePair) o;

        if (Double.compare(that.distance, distance) != 0) return false;
        return word != null ? word.equals(that.word) : that.word == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(distance);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }
}
