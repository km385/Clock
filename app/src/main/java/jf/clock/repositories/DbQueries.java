package jf.clock.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jf.clock.data.Alarm;
import jf.clock.data.AlarmDao;
import jf.clock.data.Connections;

public class DbQueries {

    private Context mContext;
    private final AlarmDao mDao;

    public DbQueries(Context context){
        mContext = context;
        mDao = Connections.getInstance(mContext).getDatabase().mAlarmDao();
    }

    public void deleteAlarm(long id, DatabaseCallback<List<Alarm>> callback){

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {


            mDao.deleteAlarm(id);
            List<Alarm> list = mDao.getAlarms();
            handler.post(() -> {
                if (callback != null){
                    callback.handleResponse(list);
                }

            });
        });
    }

    public void insertAlarm(Alarm alarm, DatabaseCallback<List<Alarm>> callback){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            mDao.insertAlarm(alarm);
            List<Alarm> list = mDao.getAlarms();
            handler.post(() -> {
                if (callback != null) {
                    callback.handleResponse(list);
                }
            });
        });

    }

    public void updateAlarm(Alarm alarm, DatabaseCallback<List<Alarm>> callback){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            mDao.updateAlarm(alarm);
            List<Alarm> list = mDao.getAlarms();
            handler.post(() -> {
                if (callback != null) {
                    callback.handleResponse(list);
                }
            });
        });
    }

    public void findAlarm(long id, DatabaseCallback<Alarm> callback){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Exception exception = null;
            Alarm alarm = mDao.getAlarmById(id);
            try{
                if (alarm == null){
                    throw new Exception("alarm with that id doesn't exists");
                }
            }catch (Exception e){
                exception = e;
            }

            Exception finalException = exception;
            handler.post(() -> {
                if (callback != null) {
                    if (finalException == null) {
                        callback.handleResponse(alarm);
                    } else {
                        callback.handleError(finalException);
                    }
                }
            });
        });
    }

    public void getAlarms(DatabaseCallback<List<Alarm>> callback){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Alarm> list = mDao.getAlarms();

            handler.post(() -> {
                if (callback != null) {
                    callback.handleResponse(list);
                }
            });
        });
    }

    public void updateStatus(long id, boolean status, DatabaseCallback<List<Alarm>> callback){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            mDao.updateField(id, status);
            List<Alarm> list = mDao.getAlarms();
            handler.post(() -> {
                if (callback != null) {
                    callback.handleResponse(list);
                }
            });
        });

    }
}
