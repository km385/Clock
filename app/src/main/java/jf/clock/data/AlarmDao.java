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
    public void insertAlarm(Alarm alarm);

    @Update
    public void updateAlarm(Alarm alarm);

    @Delete
    public void deleteAlarm(Alarm alarm);

    @Query("SELECT * FROM alarm")
    public List<Alarm> getAlarms();

    @Query("SELECT * from alarm WHERE mId = :id")
    public Alarm getAlarmById(long id);

    @Query("UPDATE alarm SET is_alarm_set = :value WHERE mId = :id")
    public void updateField(long id, boolean value);

    @Query("DELETE FROM alarm WHERE mId = :id")
    public void deleteAlarm(long id);

}
