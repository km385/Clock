package jf.clock.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jf.clock.data.Alarm;
import jf.clock.data.Connections;

public class FindAlarmByIdAsync {
    private static final String TAG = "FindAlarmByIdAsync";
    private DatabaseCallback<Alarm> mCallback;
    private int mId;
    private Context mContext;
    private Exception mException;

    public FindAlarmByIdAsync(int id, Context context, DatabaseCallback<Alarm> callback){
        mId = id;
        mContext = context;
        mCallback = callback;

        startTask();
    }

    private void startTask(){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            mException = null;
            Alarm alarm = Connections.getInstance(mContext).getDatabase().mAlarmDao().getAlarmById(mId);
            try{
                if (alarm == null){
                    throw new Exception("alarm with that id doesn't exists");
                }
            }catch (Exception e){
                mException = e;
            }

            handler.post(() -> {
                if (mCallback != null) {
                    if (mException == null) {
                        mCallback.handleResponse(alarm);
                    } else {
                        mCallback.handleError(mException);
                    }
                }
            });
        });
    }
}
