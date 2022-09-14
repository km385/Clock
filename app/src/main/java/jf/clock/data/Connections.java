package jf.clock.data;

import android.content.Context;

import androidx.room.Room;

public class Connections {
    public static Connections mInstance;
    private AppDatabase mDatabase;

    private Connections(Context context){
        mDatabase = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "db_alarms")
                .build();
    }

    public static Connections getInstance(Context context) {
        synchronized (Connections.class){
            if (mInstance == null){
                mInstance = new Connections(context);
            }
            return mInstance;
        }
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }
}
