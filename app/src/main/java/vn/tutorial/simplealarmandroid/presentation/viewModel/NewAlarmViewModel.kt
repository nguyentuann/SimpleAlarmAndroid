package vn.tutorial.simplealarmandroid.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewAlarmViewModel @Inject constructor() : ViewModel() {

    private val _newAlarm = MutableLiveData<AlarmModel>(
        AlarmModel(
            id = UUID.randomUUID().toString(),
            hour = 0,
            minute = 0,
            isOn = true,
            message = null,
            dateOfWeek = null,
            date = null
        )
    )

    val newAlarm: LiveData<AlarmModel> = _newAlarm

    fun updateTime(hour: Int, minute: Int) {
        _newAlarm.value = _newAlarm.value?.copy(hour = hour, minute = minute)
    }

    fun updateDate(date: Long) {
        _newAlarm.value = _newAlarm.value?.copy(date = date)
    }

    fun updateMessage(message: String) {
        _newAlarm.value = _newAlarm.value?.copy(message = message)
    }

    fun updateDateOfWeek(dateOfWeek: List<Int>?) {
        _newAlarm.value = _newAlarm.value?.copy(dateOfWeek = dateOfWeek)
    }

    fun updateStatus(isOn: Boolean) {
        _newAlarm.value = _newAlarm.value?.copy(isOn = isOn)
    }

    fun resetAlarm() {
        _newAlarm.value = AlarmModel(
            id = UUID.randomUUID().toString(),
            hour = 0,
            minute = 0,
            isOn = true,
            message = null,
            dateOfWeek = null,
            date = null
        )
    }

}