package vn.tutorial.simplealarmandroid.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.tutorial.simplealarmandroid.helpers.AlarmHelper
import vn.tutorial.simplealarmandroid.data.model.AlarmModel
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

    fun updateDate(date: Long?) {
        _newAlarm.value = _newAlarm.value?.copy(date = date)
    }


    fun updateMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newAlarm.postValue(_newAlarm.value?.copy(message = text))
        }
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

    fun prepareAlarmBeforeSave(selectedDays: Set<Int>, message: String) {
        val alarm = _newAlarm.value ?: return

        when {
            alarm.date != null -> updateDateOfWeek(null)
            selectedDays.isEmpty() -> updateDate(AlarmHelper.getTomorrowDate())
            else -> updateDateOfWeek(selectedDays.sorted())
        }

        updateMessage(message)
    }

    fun isChange(): Boolean {
        val initial = AlarmModel(
            id = _newAlarm.value!!.id,
            hour = 0,
            minute = 0,
            isOn = true,
            message = null,
            dateOfWeek = null,
            date = null
        )
        return initial != _newAlarm.value
    }

    fun setAlarm(alarm: AlarmModel) {
        _newAlarm.value = alarm
    }

}