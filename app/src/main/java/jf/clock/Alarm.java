package jf.clock;

import java.util.Date;

public class Alarm {
    private Date mAlarmTime;
    private boolean mAlarmSet;

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
