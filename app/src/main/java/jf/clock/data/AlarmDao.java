package jf.clock.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface AlarmDao {
    @Insert
    public void insertAlarm(Alarm... alarms);

    @Delete
    public void deleteAlarm(Alarm... alarms);

    @Query("SELECT * FROM alarm")
    public List<Alarm> getAlarms();
}
