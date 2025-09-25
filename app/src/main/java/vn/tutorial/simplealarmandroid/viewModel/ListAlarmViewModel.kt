package vn.tutorial.simplealarmandroid.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.repository.AlarmRepository
import vn.tutorial.simplealarmandroid.data.model.AlarmModel
import javax.inject.Inject

@HiltViewModel
class ListAlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
) : ViewModel() {

    val alarmList: LiveData<List<AlarmModel>> = repository.getAllAlarms()

    fun saveAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.addAlarm(alarm)
        }
    }

    fun delete(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }

    fun active(alarm: AlarmModel, isEnable: Boolean) {
        viewModelScope.launch {
            repository.activeAlarm(alarm, isEnable)
        }
    }

    fun getAlarmById(id: String): LiveData<AlarmModel?> {
        return repository.getAlarmById(id)
    }

    fun updateAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.updateAlarm(alarm)
        }
    }

}
