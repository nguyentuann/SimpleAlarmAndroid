package vn.tutorial.simplealarmandroid.data.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.data.entity.toAlarmEntity
import vn.tutorial.simplealarmandroid.data.entity.toAlarmModel
import vn.tutorial.simplealarmandroid.data.entity.toDataString
import vn.tutorial.simplealarmandroid.data.local.dao.AppDAO
import vn.tutorial.simplealarmandroid.data.scheduler.AlarmScheduler
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val appDAO: AppDAO,
    private val alarmScheduler: AlarmScheduler
) : AlarmRepository {

    private val alarmList = MutableLiveData<List<AlarmModel>>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val fromDB = appDAO.getAllAlarms()
            Log.d(
                Tag.AlarmTag,
                "Loaded ${fromDB.size} alarms from DB + ${fromDB.map { it.toDataString() }}"
            )

            val models = fromDB.map { it.toAlarmModel() }
            Log.d(
                Tag.AlarmTag,
                "Loaded ${models.size} alarms from DB + ${models.map { it.toString() }}"
            )
            alarmList.postValue(models)
        }
    }

    override fun getAllAlarms(): LiveData<List<AlarmModel>> = alarmList

    override suspend fun addAlarm(alarmModel: AlarmModel) {
        alarmList.value = alarmList.value?.toMutableList()?.apply {
            add(alarmModel)
        }

        // todo save to database
        val entity = alarmModel.toAlarmEntity()
        appDAO.addAlarm(entity)
        Log.d(Tag.AlarmTag, " Save to DB${entity.toDataString()}")

        // todo schedule alarm with AlarmManager
        alarmScheduler.scheduleAlarm(alarmModel)
    }

    override suspend fun deleteAlarm(alarmModel: AlarmModel) {
        alarmList.value = alarmList.value?.filter { it.id != alarmModel.id }

        // todo  delete from database
        appDAO.deleteAlarm(alarmModel.toAlarmEntity())

        // todo cancel alarm with AlarmManager
        alarmScheduler.cancelAlarm(alarmModel)
    }

    override suspend fun updateAlarm(alarm: AlarmModel) {
        TODO("Not yet implemented")
    }

    override suspend fun activeAlarm(
        alarm: AlarmModel,
        isActive: Boolean
    ) {
        val updatedAlarm = alarm.copy(isOn = isActive)
        alarmList.value = alarmList.value?.map {
            if (it.id == alarm.id) updatedAlarm else it
        }

        // todo update database
        appDAO.deleteAlarm(alarm.toAlarmEntity())
        appDAO.addAlarm(updatedAlarm.toAlarmEntity())

        // todo change alarm with AlarmManager
        if (isActive) {
            alarmScheduler.scheduleAlarm(updatedAlarm)
        } else {
            alarmScheduler.cancelAlarm(updatedAlarm)
        }
    }
}