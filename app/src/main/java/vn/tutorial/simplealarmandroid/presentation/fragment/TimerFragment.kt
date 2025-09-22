package vn.tutorial.simplealarmandroid.presentation.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import vn.tutorial.simplealarmandroid.databinding.FragmentTimerBinding
import vn.tutorial.simplealarmandroid.R

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null
    private var timerMillis: Long = 5 * 60 * 1000 // mặc định 5 phút
    private var isRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding.root

        updateTimerText(timerMillis)
        updateResetButtonState()

        val timeButtons = mapOf(
            binding.btn5 to 5,
            binding.btn10 to 10,
            binding.btn15 to 15,
            binding.btn30 to 30,
            binding.btn45 to 45
        )

        timeButtons.forEach { (button, minutes) ->
            button.setOnClickListener {
                timerMillis = minutes * 60 * 1000L
                updateTimerText(timerMillis)
                resetTimer()
            }
        }

        binding.btnStartTimer.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
            updateResetButtonState()
        }

        binding.btnResetTimer.setOnClickListener {
            if (!isRunning) {
                timerMillis = 5 * 60 * 1000L
                updateTimerText(timerMillis)
                resetTimer()
            }
            updateResetButtonState()
        }


        return view
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timerMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                timerMillis = millisUntilFinished
                updateTimerText(timerMillis)
            }

            override fun onFinish() {
                isRunning = false
                binding.btnStartTimer.text = "START"
            }
        }.start()
        isRunning = true
        binding.btnStartTimer.text = "PAUSE"
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        binding.btnStartTimer.text = "START"
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        isRunning = false
    }

    private fun updateTimerText(millis: Long) {
        val hours = millis / 1000 / 3600
        val minutes = (millis / 1000 % 3600) / 60
        val seconds = millis / 1000 % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    private fun updateResetButtonState() {
        if (isRunning) {
            binding.btnResetTimer.isEnabled = false
            binding.btnResetTimer.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.dark_grey))
        } else {
            binding.btnResetTimer.isEnabled = true
            binding.btnResetTimer.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
