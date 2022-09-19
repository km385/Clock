package jf.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.UpdateAlarmStatusAsync;

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

        Intent AlarmIntent = new Intent(context, AlarmActivity.class);
        AlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(AlarmIntent);
    }
}
