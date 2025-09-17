package vn.tutorial.simplealarmandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import vn.tutorial.simplealarmandroid.common.helpers.TimeConverter
import vn.tutorial.simplealarmandroid.common.helpers.TimeHelper
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel

@Entity(tableName = "alarm")
class AlarmEntity(
    @PrimaryKey
    val id: String,
    val hour: Int,
    val minute: Int,
    val isOn: Boolean,
    val message: String? = null,
    val dateOfWeek: Int? = null,
    val date: Long? = null
)

fun AlarmEntity.toAlarmModel() : AlarmModel {
    return AlarmModel(
        id = id,
        hour = hour,
        minute = minute,
        isOn = isOn,
        message = message,
        dateOfWeek = if (dateOfWeek == null) null else TimeHelper.fromBitToList(dateOfWeek),
        date = date
    )
}

fun AlarmModel.toAlarmEntity() : AlarmEntity {
    return AlarmEntity(
        id = id,
        hour = hour,
        minute = minute,
        isOn = isOn,
        message = message,
        dateOfWeek = if (dateOfWeek == null) null else TimeHelper.fromListToBit(dateOfWeek!!),
        date = date
    )
}

fun AlarmEntity.toDataString() : String {
    return "AlarmEntity(id=$id, hour=$hour, minute=$minute, isOn=$isOn, message=$message, dateOfWeek=$dateOfWeek, date=$date)"
}