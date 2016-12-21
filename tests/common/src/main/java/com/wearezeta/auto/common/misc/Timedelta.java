package com.wearezeta.auto.common.misc;

public class Timedelta implements Comparable<Timedelta>{
    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;

    private double milliSeconds;

    private Timedelta(double milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public static Timedelta fromSeconds(double seconds) {
        return new Timedelta(seconds * MILLISECONDS_IN_SECOND);
    }

    public int asSeconds() {
        return (int) this.asFloatSeconds();
    }

    public double asFloatSeconds() {
        return this.milliSeconds / MILLISECONDS_IN_SECOND;
    }

    public static Timedelta fromMilliSeconds(double milliSeconds) {
        return new Timedelta(milliSeconds);
    }

    public long asMilliSeconds() {
        return (long) this.asFloatMilliSeconds();
    }

    public double asFloatMilliSeconds() {
        return this.milliSeconds;
    }

    public static Timedelta fromMinutes(double minutes) {
        return new Timedelta(minutes * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND);
    }

    public int asMinutes() {
        return (int) this.asFloatMinutes();
    }

    public double asFloatMinutes() {
        return this.milliSeconds / SECONDS_IN_MINUTE / MILLISECONDS_IN_SECOND;
    }

    public static Timedelta fromHours(double hours) {
        return new Timedelta(hours * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND);
    }

    public int asHours() {
        return (int) this.asFloatHours();
    }

    public double asFloatHours() {
        return this.milliSeconds / MINUTES_IN_HOUR / SECONDS_IN_MINUTE / MILLISECONDS_IN_SECOND;
    }

    public Timedelta sum(Timedelta other) {
        return new Timedelta(this.milliSeconds + other.milliSeconds);
    }

    public Timedelta diff(Timedelta other) {
        return new Timedelta(this.milliSeconds - other.milliSeconds);
    }

    public static Timedelta now() {
        return new Timedelta(System.currentTimeMillis());
    }

    public boolean isDiffGreaterOrEqual(Timedelta other, Timedelta maxDelta) {
        return this.diff(other).compareTo(maxDelta) >= 0;
    }

    public boolean isDiffGreater(Timedelta other, Timedelta maxDelta) {
        return this.diff(other).compareTo(maxDelta) > 0;
    }

    public boolean isDiffLessOrEqual(Timedelta other, Timedelta maxDelta) {
        return this.diff(other).compareTo(maxDelta) <= 0;
    }

    public boolean isDiffLess(Timedelta other, Timedelta maxDelta) {
        return this.diff(other).compareTo(maxDelta) < 0;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Timedelta) && (((Timedelta) other).asMilliSeconds() == this.asMilliSeconds());
    }

    @Override
    public String toString() {
        final int hours = this.asHours();
        final int minutes = this.asMinutes() % MINUTES_IN_HOUR;
        final int seconds = this.asSeconds() % SECONDS_IN_MINUTE;
        final long milliSeconds = this.asMilliSeconds() % MILLISECONDS_IN_SECOND;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d::%d", hours, minutes, seconds, milliSeconds);
        } else if (minutes > 0) {
            return String.format("%02d:%02d::%d", minutes, seconds, milliSeconds);
        } else if (seconds > 0) {
            return String.format("%02d::%d", seconds, milliSeconds);
        }
        return String.format("%d ms", milliSeconds);
    }

    @Override
    public int compareTo(Timedelta o) {
        if (this.equals(o)) {
            return 0;
        }
        if (o == null) {
            throw new NullPointerException("null value is not comparable");
        }
        if (this.milliSeconds > o.milliSeconds) {
            return 1;
        }
        return -1;
    }
}
