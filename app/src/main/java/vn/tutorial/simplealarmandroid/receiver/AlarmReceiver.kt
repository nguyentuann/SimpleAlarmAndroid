package vn.tutorial.simplealarmandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.data.scheduler.AlarmScheduler
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.presentation.activity.AlarmReceiverActivity
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver() : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("ALARM_ID")
        val message = intent.getStringExtra("ALARM_MESSAGE")
        val hour = intent.getIntExtra("ALARM_HOUR", 0)
        val minute = intent.getIntExtra("ALARM_MINUTE", 0)
        val days = intent.getIntArrayExtra("DAYS")?.toList()

        val activityIntent = Intent(context, AlarmReceiverActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtras(intent)
        }

        context.startActivity(activityIntent)
        Log.d(Tag.AlarmTag, "Alarm received in receiver: id=$id, message=$message, hour=$hour, minute=$minute, days=$days")

        if (!days.isNullOrEmpty()) {
            // todo lên lịch cho lần tiếp theo
            val nextAlarm = AlarmModel(
                id!!,
                hour,
                minute,
                true,
                message,
                days,
            )
            alarmScheduler.scheduleAlarm(nextAlarm)
            Log.d(Tag.scheduleAlarm, "reschedule alarm from receiver $nextAlarm")
        }
    }
}