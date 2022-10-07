package jf.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        Calendar calendar = getNextAlarmTime(alarm.getHour(), alarm.getMinutes());
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

    private Calendar getNextAlarmTime(int hour, int minutes){
        Calendar nextTime = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        nextTime.set(Calendar.HOUR_OF_DAY, hour);
        nextTime.set(Calendar.MINUTE, minutes);
        nextTime.set(Calendar.SECOND, 0);
        nextTime.set(Calendar.MILLISECOND, 0);

        if (nextTime.getTimeInMillis() <= currentTime.getTimeInMillis()){
            nextTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        return nextTime;
    }
}
