package vn.tutorial.simplealarmandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import vn.tutorial.simplealarmandroid.data.entity.AlarmEntity

@Dao
interface AppDAO {
    @Insert
    fun addAlarm(newAlarm: AlarmEntity)

    @Delete
    fun removeAlarm(alarm: AlarmEntity)

    @Query("SELECT dateOfWeek FROM alarm WHERE id = :id")
    fun getDateOfWeek(id: Int): Int

    @Query("SELECT * FROM alarm")
    fun getAllAlarm(): List<AlarmEntity>
}
