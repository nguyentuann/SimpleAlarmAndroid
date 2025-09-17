package vn.tutorial.simplealarmandroid.domain.repository

import androidx.lifecycle.LiveData
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel

interface AlarmRepository {
    fun getAllAlarms(): LiveData<List<AlarmModel>>
    suspend fun addAlarm(alarm: AlarmModel)
    suspend fun deleteAlarm(alarm: AlarmModel)
    suspend fun updateAlarm(alarm: AlarmModel)
    suspend fun activeAlarm(alarm: AlarmModel, isActive: Boolean)
}