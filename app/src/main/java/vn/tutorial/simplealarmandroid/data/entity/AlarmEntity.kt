package vn.tutorial.simplealarmandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm", )
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