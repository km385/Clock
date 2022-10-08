package jf.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

import jf.clock.data.Alarm;

public class AlarmSetter {
    private static final String TAG = "AlarmSetter";

    private Context mContext;

    public AlarmSetter(Context context){
        mContext = context;
    }

    public void setAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = getNextAlarmTime(alarm.getHour(), alarm.getMinutes(), alarm.getWeekdays());
        Log.i(TAG, "alarm date" + calendar.getTime());
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, Math.toIntExact(alarm.getId()),
                intent, 0);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
        makeToast(calendar);
    }

    private void makeToast(Calendar calendar) {
        Calendar calendar1 = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis() - calendar1.getTimeInMillis();
        calendar1.setTimeInMillis(timeInMillis);
        String string = "Alarm set for " +
                (calendar1.get(Calendar.HOUR_OF_DAY) - 1) +
                " hours and " +
                calendar1.get(Calendar.MINUTE) +
                " minutes from now on";
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, Math.toIntExact(alarm.getId()),
                intent, PendingIntent.FLAG_NO_CREATE);
        if (alarmManager != null && pendingIntent != null)
            alarmManager.cancel(pendingIntent);
    }

    private Calendar getNextAlarmTime(int hour, int minutes, boolean[] dayOfWeek){
        Calendar nextTime = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        nextTime.set(Calendar.HOUR_OF_DAY, hour);
        nextTime.set(Calendar.MINUTE, minutes);
        nextTime.set(Calendar.SECOND, 0);
        nextTime.set(Calendar.MILLISECOND, 0);

        if (nextTime.getTimeInMillis() <= currentTime.getTimeInMillis()){
            nextTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        // check if user set specific day for an alarm
        for (boolean b : dayOfWeek) {
            if (b) {
                checkDeyOfWeek(dayOfWeek, nextTime);
                break;
            }
        }

        return nextTime;
    }

    private void checkDeyOfWeek(boolean[] dayOfWeek, Calendar nextTime) {
        loop: while(true){
            switch (nextTime.get(Calendar.DAY_OF_WEEK)){
                case 1:
                    if (dayOfWeek[6]) break loop;
                    break;
                case 2:
                    if (dayOfWeek[0]) break loop;
                    break;
                case 3:
                    if (dayOfWeek[1]) break loop;
                    break;
                case 4:
                    if (dayOfWeek[2]) break loop;
                    break;
                case 5:
                    if (dayOfWeek[3]) break loop;
                    break;
                case 6:
                    if (dayOfWeek[4]) break loop;
                    break;
                case 7:
                    if (dayOfWeek[5]) break loop;
                    break;

            }
            nextTime.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
