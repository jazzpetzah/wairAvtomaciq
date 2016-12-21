package com.wearezeta.auto.common.misc;

public class TimeInterval {
    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;

    private double milliSeconds;

    private TimeInterval(double milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public static TimeInterval fromSeconds(double seconds) {
        return new TimeInterval(seconds * MILLISECONDS_IN_SECOND);
    }

    public int asSeconds() {
        return (int) this.asFloatSeconds();
    }

    public double asFloatSeconds() {
        return this.milliSeconds * MILLISECONDS_IN_SECOND;
    }

    public static TimeInterval fromMilliSeconds(double milliSeconds) {
        return new TimeInterval(milliSeconds);
    }

    public long asMilliSeconds() {
        return (long) this.asFloatMilliSeconds();
    }

    public double asFloatMilliSeconds() {
        return this.milliSeconds;
    }

    public static TimeInterval fromMinutes(double minutes) {
        return new TimeInterval(minutes * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND);
    }

    public int asMinutes() {
        return (int) this.asFloatMinutes();
    }

    public double asFloatMinutes() {
        return this.milliSeconds * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
    }

    public static TimeInterval fromHours(double hours) {
        return new TimeInterval(hours * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND);
    }

    public int asHours() {
        return (int) this.asFloatHours();
    }

    public double asFloatHours() {
        return this.milliSeconds * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
    }

    public TimeInterval sum(TimeInterval other) {
        return new TimeInterval(this.milliSeconds + other.milliSeconds);
    }

    public TimeInterval diff(TimeInterval other) {
        return new TimeInterval(this.milliSeconds - other.milliSeconds);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof TimeInterval) && (((TimeInterval) other).milliSeconds == this.milliSeconds);
    }

    @Override
    public String toString() {
        final int hours = this.asHours();
        final int minutes = this.asMinutes() % MINUTES_IN_HOUR;
        final int seconds = this.asSeconds() % SECONDS_IN_MINUTE;
        final long milliSeconds = this.asMilliSeconds() % MILLISECONDS_IN_SECOND;
        if (hours > 0) {
            return String.format("%02dh:%02dm:%02ds::%dms", hours, minutes, seconds, milliSeconds);
        } else if (minutes > 0) {
            return String.format("%02dm:%02ds::%dms", minutes, seconds, milliSeconds);
        } else if (seconds > 0) {
            return String.format("%02ds::%dms", seconds, milliSeconds);
        }
        return String.format("%.2f ms", this.milliSeconds);
    }
}
