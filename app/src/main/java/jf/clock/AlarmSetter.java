package jf.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import java.util.Calendar;

public class AlarmSetter {
    private static final String TAG = "AlarmSetter";

    private Context mContext;

    public AlarmSetter(Context context){
        mContext = context;
    }

    public void setAlarm(Calendar c, int id){
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);

        c = checkDate(c);

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("date", c);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id,
                intent, 0);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                pendingIntent);
    }

    public void cancelAlarm(int id){
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id,
                intent, PendingIntent.FLAG_NO_CREATE);
        if (alarmManager != null && pendingIntent != null)
            alarmManager.cancel(pendingIntent);
    }

    private Calendar checkDate(Calendar alarmTime){
        Calendar currentTime = Calendar.getInstance();
        if (alarmTime.getTimeInMillis() < currentTime.getTimeInMillis()){
            int hour = alarmTime.get(Calendar.HOUR_OF_DAY);
            int minute = alarmTime.get(Calendar.MINUTE);
            alarmTime = currentTime;
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
            alarmTime.set(Calendar.HOUR_OF_DAY, hour);
            alarmTime.set(Calendar.MINUTE, minute);

            return alarmTime;
        }
        return alarmTime;
    }
}
