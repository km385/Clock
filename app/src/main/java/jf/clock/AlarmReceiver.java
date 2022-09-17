package jf.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO receives only first alarm
        Calendar calendar = (Calendar) intent.getSerializableExtra("date");
        Log.i(TAG, "onReceive: " + calendar.getTime());
        Log.i(TAG, "alarm: " +
                calendar.get(Calendar.HOUR_OF_DAY) +
                "" +
                calendar.get(Calendar.MINUTE));
    }
}
