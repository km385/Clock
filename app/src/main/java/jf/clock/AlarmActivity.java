package jf.clock;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";

    private MediaPlayer mMediaPlayer;
    private Slider mSlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_receiver);
        mSlider = findViewById(R.id.slider);
        mSlider.setCustomThumbDrawable(R.drawable.ic_baseline_keyboard_double_arrow_right_24);
        mSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                if (slider.getValue() == 1){
                    finish();
                } else {
                    slider.setValue(0);
                }

            }
        });


        setupSound();


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
        super.onDestroy();

    }
}
