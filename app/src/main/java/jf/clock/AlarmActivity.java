package jf.clock;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_receiver);

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
//            setTurnScreenOn(true);
//            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//            keyguardManager.requestDismissKeyguard(this, null);
//
//
//        }
        mMediaPlayer= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();


    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.stop();
        super.onDestroy();

    }
}
