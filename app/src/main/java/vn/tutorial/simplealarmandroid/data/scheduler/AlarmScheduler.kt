package vn.tutorial.simplealarmandroid.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.common.helpers.AlarmHelper
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.receiver.AlarmReceiver
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // todo tạo pending intent để không bị nhầm id
    private fun getPendingIntent(alarm: AlarmModel): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
            putExtra("ALARM_MESSAGE", alarm.message)
            putExtra("ALARM_HOUR", alarm.hour)
            putExtra("ALARM_MINUTE", alarm.minute)
            putExtra("DAYS", alarm.dateOfWeek?.toIntArray())
        }
        Log.d(Tag.scheduleAlarm, " tạo pending intent trong scheduler ${alarm.id}")

        return PendingIntent.getBroadcast(
            context,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun scheduleAlarm(alarm: AlarmModel) {
        // todo schedule alarm with AlarmManager
        val triggerTime: Long = when {
            alarm.dateOfWeek != null && alarm.date == null -> {
                // Tìm ngày gần nhất
                AlarmHelper.getNextDayOfWeek(
                    alarm.hour,
                    alarm.minute,
                    alarm.dateOfWeek!!
                ).timeInMillis
            }

            alarm.date != null -> alarm.date!!
            else -> return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            getPendingIntent(alarm)
        )

        Log.d(Tag.scheduleAlarm, "đặt báo thức trong scheduler ${alarm.id} at $triggerTime")
    }

    fun cancelAlarm(alarm: AlarmModel) {
        // todo cancel alarm with AlarmManager
        alarmManager.cancel(
            getPendingIntent(alarm)
        )
    }

    fun changeAlarm(alarm: AlarmModel) {
        // todo change alarm with AlarmManager
        cancelAlarm(alarm)
        scheduleAlarm(alarm)
    }
}