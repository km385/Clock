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
        /*
            in intent is only id
            get alarm
            if exists fire the alarm
            else finish
         */

        // todo check if alarm hasn't been deleted


        Bundle bundle = intent.getBundleExtra("bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");
        Log.i(TAG, "onReceive: " + alarm.getHour() + alarm.getMinutes() + alarm.isVibrate());

        Intent AlarmIntent = new Intent(context, AlarmActivity.class);
        AlarmIntent.putExtra("bundle", bundle);
        AlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(AlarmIntent);
    }
}
