package jf.clock.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jf.clock.data.Alarm;
import jf.clock.data.Connections;

public class DeleteAlarmAsync {
    private static final String TAG = "DeleteAlarmAsync";
    private DatabaseCallback<List<Alarm>> mCallback;
    private long mId;
    private Context mContext;

    public DeleteAlarmAsync(long id, Context context, DatabaseCallback<List<Alarm>> callback){
        mId = id;
        mContext = context;
        mCallback = callback;

        startTask();
    }

    private void startTask(){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {


            Connections.getInstance(mContext).getDatabase().mAlarmDao().deleteAlarm(mId);
            List<Alarm> list = Connections.getInstance(mContext).getDatabase().mAlarmDao().getAlarms();
            handler.post(() -> {
                if (mCallback != null){
                    mCallback.handleResponse(list);
                }

            });
        });
    }
}
