package jf.clock.repositories;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jf.clock.data.Alarm;
import jf.clock.data.Connections;

public class InsertAlarmAsync {
    private static final String TAG = "InsertAlarmAsync";
    private DatabaseCallback<List<Alarm>> mCallback;
    private Alarm mAlarm;
    private Context mContext;

    public InsertAlarmAsync(Alarm alarm, Context context, DatabaseCallback<List<Alarm>> callback){
        mAlarm = alarm;
        mContext = context;
        mCallback = callback;

        startTask();
    }

    private void startTask(){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Connections.getInstance(mContext).getDatabase().mAlarmDao().insertAlarm(mAlarm);
            List<Alarm> list = Connections.getInstance(mContext).getDatabase().mAlarmDao().getAlarms();
            handler.post(() -> {
                if (mCallback != null) {
                    mCallback.handleResponse(list);
                }
            });
        });
    }
}
