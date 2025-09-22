package vn.tutorial.simplealarmandroid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.presentation.fragment.HomeFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.NewAlarmFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.TimerStopWatchFragment
import vn.tutorial.simplealarmandroid.presentation.viewModel.ListAlarmViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var newAlarmFragment: NewAlarmFragment
    lateinit var timerStopWatchFragment: TimerStopWatchFragment
    private val alarmViewModel by viewModels<ListAlarmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        newAlarmFragment = NewAlarmFragment()
        timerStopWatchFragment = TimerStopWatchFragment()

        changeFragment(homeFragment)
    }

    fun addNewAlarm() {
        changeFragment(newAlarmFragment)
    }

    fun timerStopWatch() {
        changeFragment(timerStopWatchFragment)
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.home_container,
            fragment,
        ).addToBackStack(null).commit()
    }
}