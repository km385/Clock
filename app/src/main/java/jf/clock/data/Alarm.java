package jf.clock.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Alarm implements Serializable {
    // todo represent days of week
    @PrimaryKey(autoGenerate = true)
    private long mId;

    @ColumnInfo(name = "hour")
    private int mHour;

    @ColumnInfo(name = "minutes")
    private int mMinutes;

    @ColumnInfo(name = "is_alarm_set")
    private boolean mAlarmSet;

    @ColumnInfo(name = "is_vibrate")
    private boolean mVibrate;

    @ColumnInfo(name = "week_days")
    private boolean[] mWeekdays;

    public boolean[] getWeekdays() {
        return mWeekdays;
    }

    public void setWeekdays(boolean[] weekdays) {
        mWeekdays = weekdays;
    }

    public boolean isVibrate() {
        return mVibrate;
    }

    public void setVibrate(boolean vibrate) {
        mVibrate = vibrate;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        mMinutes = minutes;
    }

    public boolean isAlarmSet() {
        return mAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        mAlarmSet = alarmSet;
    }
}
