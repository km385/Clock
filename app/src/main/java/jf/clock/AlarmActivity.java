package jf.clock;

import android.app.KeyguardManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import java.util.Arrays;

import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.FindAlarmByIdAsync;
import jf.clock.repositories.UpdateAlarmAsync;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";

    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;
    private Slider mSlider;
    private Alarm mAlarm = new Alarm();
    private float mStartTrackingValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_receiver);
        // todo cancel the alarm when deleting alarm record
        // todo xiaomi phone, possibly battery settings prevent alarm from starting after few hours of sleep
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            mAlarm = (Alarm) bundle.getSerializable("alarm");
            Log.i(TAG, "onCreate: " + mAlarm.getMinutes());
        } else {
            finish();
        }

        mSlider = findViewById(R.id.slider);
        mSlider.setCustomThumbDrawable(R.drawable.ic_baseline_keyboard_double_arrow_right_24);
        mSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                mStartTrackingValue = slider.getValue();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                if (slider.getValue() == 1 && mStartTrackingValue < 0.2){
                    finish();
                } else {
                    slider.setValue(0);
                }

            }
        });
        if (mAlarm.isVibrate()) setupVibrator();
        setupSound();


    }

    private void setupVibrator() {
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (!mVibrator.hasVibrator()){
            return;
        }

        long[] pattern = {0, 10, 200, 500, 700, 1000, 300, 200, 50, 10};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createWaveform(pattern,0));
        } else {
            mVibrator.vibrate(pattern,0);
        }
    }

    private void reScheduleAlarm(){
        AlarmSetter alarmSetter = new AlarmSetter(this);
        new FindAlarmByIdAsync(mAlarm.getId(), this, new DatabaseCallback<Alarm>() {
            @Override
            public void handleResponse(Alarm response) {
                if (checkIfAbleToReSchedule(response)){
                    alarmSetter.setAlarm(response);
                }else{
                    response.setAlarmSet(false);
                    updateAlarm(response);
                }

            }

            @Override
            public void handleError(Exception e) {

            }
        });
    }

    private void updateAlarm(Alarm alarm){
        new UpdateAlarmAsync(alarm, this, null);
    }

    private boolean checkIfAbleToReSchedule(Alarm alarm){
        final boolean[] value = new boolean[1];

        for (boolean b : alarm.getWeekdays()) {
            if (b) {
                value[0] = true;
                break;

            }
        }
        return value[0];
    }


    private void setupSound() {
        mMediaPlayer= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
            Log.i(TAG, "KeyguardManager");
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);

        } else {
            Log.i(TAG, "WindowsManager");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.stop();
        if (mVibrator != null) mVibrator.cancel();
        reScheduleAlarm();
        super.onDestroy();

    }

//    private void hideNavBar(){
//        // not working
//        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }

    @Override
    public void onBackPressed() {
        // don't allow back button to dismiss
    }
}
