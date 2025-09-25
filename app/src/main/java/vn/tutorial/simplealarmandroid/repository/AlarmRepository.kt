package vn.tutorial.simplealarmandroid.repository

import androidx.lifecycle.LiveData
import vn.tutorial.simplealarmandroid.data.model.AlarmModel

interface AlarmRepository {
    fun getAllAlarms(): LiveData<List<AlarmModel>>
    suspend fun addAlarm(alarm: AlarmModel)
    suspend fun deleteAlarm(alarm: AlarmModel)
    suspend fun updateAlarm(alarm: AlarmModel)
    suspend fun activeAlarm(alarm: AlarmModel, isActive: Boolean)
    fun getAlarmById(id: String): LiveData<AlarmModel?>
}