package jf.clock.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "alarm_time")
    private Date mAlarmTime;

    @ColumnInfo(name = "is_alarm_set")
    private boolean mAlarmSet;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Date getAlarmTime() {
        return mAlarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        mAlarmTime = alarmTime;
    }

    public boolean isAlarmSet() {
        return mAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        mAlarmSet = alarmSet;
    }
}
