package com.basic.reminder.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rafi on 8/1/15.
 */
public class Reminder implements Parcelable {

    private long reminderId;
    private String desc;

    private long dateTime;

    private boolean isActive;

    public long getReminderId() {
        return reminderId;
    }

    public void setReminderId(long reminderId) {
        this.reminderId = reminderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reminderId);
        dest.writeString(this.desc);
        dest.writeLong(this.dateTime);
        dest.writeByte(isActive ? (byte) 1 : (byte) 0);
    }

    public Reminder() {
    }

    private Reminder(Parcel in) {
        this.reminderId = in.readLong();
        this.desc = in.readString();
        this.dateTime = in.readLong();
        this.isActive = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
