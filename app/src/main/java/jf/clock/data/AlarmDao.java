package jf.clock.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface AlarmDao {
    @Insert
    public void insertAlarm(Alarm... alarms);

    @Update
    public void updateAlarm(Alarm... alarms);

    @Delete
    public void deleteAlarm(Alarm... alarms);

    @Query("SELECT * FROM alarm")
    public List<Alarm> getAlarms();

    @Query("UPDATE alarm SET is_alarm_set = :value WHERE mId = :id")
    public void updateField(int id, boolean value);

}
