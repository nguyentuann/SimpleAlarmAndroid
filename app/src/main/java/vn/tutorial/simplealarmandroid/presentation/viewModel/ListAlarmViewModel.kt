package vn.tutorial.simplealarmandroid.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import javax.inject.Inject

@HiltViewModel
class ListAlarmViewModel @Inject constructor() : ViewModel() {

    private val _alarmList = MutableLiveData<List<AlarmModel>>(emptyList())
    val alarmList: LiveData<List<AlarmModel>> = _alarmList

    fun getAllAlarms(): List<AlarmModel> {
        return _alarmList.value ?: emptyList()
    }

    fun addAlarm(alarm: AlarmModel) {
        _alarmList.value = _alarmList.value?.plus(alarm)
    }

    fun removeAlarm(alarm: AlarmModel) {
        _alarmList.value = _alarmList.value?.filter { it != alarm }
    }
}
