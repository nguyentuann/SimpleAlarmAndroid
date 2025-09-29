package vn.tutorial.simplealarmandroid.ui.alarm

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
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
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOnLockScreen()
        enableEdgeToEdge()
        binding = ActivityAlarmReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra("ALARM_MESSAGE")
        val hour = intent.getIntExtra("ALARM_HOUR", 0)
        val minute = intent.getIntExtra("ALARM_MINUTE", 0)
        val sound = intent.getIntExtra("ALARM_SOUND", 0)

//        setupMediaPlayer(sound)

        binding.icon
            .setImageResource(IconHelper.getIconResourceForAlarm(hour))

        binding.time.text =
            TimeConverter.convertTimeToString(hour, minute)

        binding.message.text = message ?: "Alarm"


        setListener(binding.btnStop)
        setListener(binding.btnThis)
        setListener(binding.btnAlarm)

    }

    private fun showOnLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)

            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setupMediaPlayer(sound: Int) {
        try {
            mediaPlayer = if (sound != 0) {
                MediaPlayer.create(this, sound)
            } else {
                val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                MediaPlayer.create(this, ringtoneUri)
            }

            mediaPlayer?.apply {
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ImplicitSamInstance")
    fun setListener(btn: Button) {
        btn.setOnClickListener {
            btn.isEnabled = false
            stop += 1
            btn.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary))

            if (stop == 3) {
//                mediaPlayer?.stop()
//                mediaPlayer?.release()
//                mediaPlayer = null
                stopService(Intent(this, AlarmSoundService::class.java))
                finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}