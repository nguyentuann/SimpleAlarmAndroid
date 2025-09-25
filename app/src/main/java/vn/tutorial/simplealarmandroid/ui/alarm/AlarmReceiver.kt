package vn.tutorial.simplealarmandroid.ui.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.data.model.AlarmModel
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
        }
    }
}