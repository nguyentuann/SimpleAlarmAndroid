package vn.tutorial.simplealarmandroid.data.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.data.mock.MockData
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor() : AlarmRepository {

    private val alarmList = MutableLiveData<List<AlarmModel>>(MockData.AlarmData)

    override fun getAllAlarms(): LiveData<List<AlarmModel>> {
        return alarmList
    }

    override suspend fun addAlarm(alarm: AlarmModel) {
        alarmList.value = alarmList.value?.toMutableList()?.apply {
            add(alarm)
        }
        Log.d(Tag.AlarmTag + "Repository", alarmList.value!!.toString())
    }

    override suspend fun deleteAlarm(alarm: AlarmModel) {
        alarmList.value = alarmList.value?.filter { it.id != alarm.id }
    }
}