package com.example.quizapp.CustomJavaObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Timer implements Parcelable {

    private long seconds;
    private boolean isRunning;

    // Normal constructor, since this is still a normal object
    public Timer() {
        this.seconds = 0;
        this.isRunning = false;
    }

    public void reset() {
        seconds = 0;
    }

    public void allowTimerToRun() {
        isRunning = true;
    }

    public void stopTimer() {
        isRunning = false;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getTimeRepresentation() {
        long hours = seconds/3600;
        long minutes = (seconds%3600)/60;
        long secs = seconds%60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(seconds);
        parcel.writeByte((byte) (isRunning ? 1 : 0));
    }

    // Basically is restoreUsingParcel
    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private Timer(Parcel in) {
        seconds = in.readInt();
        isRunning = in.readByte() != 0;
    }

    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Creator<Timer> CREATOR = new Creator<Timer>() {
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Timer createFromParcel(Parcel in) {
            return new Timer(in);
        }

        // We just need to copy this and change the type to match our class
        @Override
        public Timer[] newArray(int size) {
            return new Timer[size];
        }
    };

}
