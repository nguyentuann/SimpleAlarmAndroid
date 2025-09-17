package vn.tutorial.simplealarmandroid.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.domain.repository.AlarmRepository
import javax.inject.Inject

@HiltViewModel
class ListAlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
) : ViewModel() {

    val alarmList: LiveData<List<AlarmModel>> = repository.getAllAlarms()

    fun saveAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            Log.d(Tag.AlarmTag, "Save alarm: $alarm")
            repository.addAlarm(alarm)
        }
    }

    fun removeAlarm(alarm: AlarmModel) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }
}
