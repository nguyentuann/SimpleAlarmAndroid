package vn.tutorial.simplealarmandroid.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.domain.repository.AlarmRepository
import javax.inject.Inject

@HiltViewModel
class ListAlarmViewModel @Inject constructor(
    private val repository: AlarmRepository
) : ViewModel() {

    val alarmList: LiveData<List<AlarmModel>> = repository.getAllAlarms()

    fun saveAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.addAlarm(alarm)
        }
    }

    fun removeAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }
}
