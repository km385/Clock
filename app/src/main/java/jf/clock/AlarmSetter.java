package jf.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import java.util.Calendar;

public class AlarmSetter {
    private static final String TAG = "AlarmManager";

    private Context mContext;

    public AlarmSetter(Context context){
        mContext = context;
    }

    public void setAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("date", c);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1,
                intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                pendingIntent);
    }
}
