package jf.clock.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jf.clock.data.Alarm;
import jf.clock.data.Connections;

public class UpdateAlarmStatusAsync {
    private static final String TAG = "UpdateAlarmStatusAsync";
    private DatabaseCallback<List<Alarm>> mCallback;
    private int mId;
    private boolean mStatus;
    private Context mContext;

    public UpdateAlarmStatusAsync(int id, boolean status, Context context, DatabaseCallback<List<Alarm>> callback){
        mId = id;
        mStatus = status;
        mContext = context;
        mCallback = callback;

        startTask();
    }

    private void startTask(){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Connections.getInstance(mContext).getDatabase().mAlarmDao().updateField(mId, mStatus);
            List<Alarm> list = Connections.getInstance(mContext).getDatabase().mAlarmDao().getAlarms();
            handler.post(() -> {
                if (mCallback != null) {
                    mCallback.handleResponse(list);
                }
            });
        });
    }
}
