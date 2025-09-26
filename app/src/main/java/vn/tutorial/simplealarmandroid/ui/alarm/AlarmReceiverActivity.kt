package vn.tutorial.simplealarmandroid.ui.alarm

import android.content.res.ColorStateList
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.databinding.ActivityAlarmReceiverBinding
import vn.tutorial.simplealarmandroid.helpers.IconHelper
import vn.tutorial.simplealarmandroid.utils.TimeConverter

class AlarmReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmReceiverBinding
    var stop by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlarmReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
        ringtone.play()

        val message = intent.getStringExtra("ALARM_MESSAGE")
        val hour = intent.getIntExtra("ALARM_HOUR", 0)
        val minute = intent.getIntExtra("ALARM_MINUTE", 0)

        binding.icon
            .setImageResource(IconHelper.getIconResourceForAlarm(hour))

        binding.time.text =
            TimeConverter.convertTimeToString(hour, minute)

        binding.message.text = message ?: "Alarm"


        setListener(binding.btnStop, ringtone)
        setListener(binding.btnThis, ringtone)
        setListener(binding.btnAlarm, ringtone)

    }

    fun setListener(btn: Button, ringtone: Ringtone) {
        btn.setOnClickListener {
            btn.isEnabled = false
            stop += 1
            btn.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary))

            if (stop == 3) {
                ringtone.stop()
                finish()
            }
        }

    }
}