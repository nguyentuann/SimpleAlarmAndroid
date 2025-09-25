package vn.tutorial.simplealarmandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.data.entity.toAlarmEntity
import vn.tutorial.simplealarmandroid.data.entity.toAlarmModel
import vn.tutorial.simplealarmandroid.local.dao.AppDAO
import vn.tutorial.simplealarmandroid.data.model.AlarmModel
import vn.tutorial.simplealarmandroid.ui.alarm.AlarmScheduler
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val appDAO: AppDAO,
    private val alarmScheduler: AlarmScheduler
) : AlarmRepository {

    private val alarmList = MutableLiveData<List<AlarmModel>>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val fromDB = appDAO.getAllAlarms()

            val models = fromDB.map { it.toAlarmModel() }

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
        alarmList.value = alarmList.value?.map {
            if (it.id == alarm.id) alarm else it
        }

        // todo update database
        appDAO.updateAlarm(alarm.toAlarmEntity())

        // todo change alarm with AlarmManager
        alarmScheduler.editAlarm(alarm)
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

    override fun getAlarmById(id: String): LiveData<AlarmModel?> {
        alarmList.value?.forEach {
            if (it.id == id) return MutableLiveData(it)
        }
        return MutableLiveData(null)
    }
}