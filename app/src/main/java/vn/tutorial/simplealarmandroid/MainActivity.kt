package vn.tutorial.simplealarmandroid

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.common.helpers.setAppLocale
import vn.tutorial.simplealarmandroid.presentation.fragment.HomeFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.NewAlarmFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.SettingFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.TimerStopWatchFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var newAlarmFragment: NewAlarmFragment
    lateinit var timerStopWatchFragment: TimerStopWatchFragment
    lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_language", "en") ?: "en"
        setAppLocale(lang)

        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        newAlarmFragment = NewAlarmFragment()
        timerStopWatchFragment = TimerStopWatchFragment()
        settingFragment = SettingFragment()

        changeFragment(homeFragment)
    }

    fun addNewAlarm() {
        changeFragment(newAlarmFragment)
    }

    fun timerStopWatch() {
        changeFragment(timerStopWatchFragment)
    }

    fun setting() {
        changeFragment(settingFragment)
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.home_container,
            fragment,
        ).addToBackStack(null).commit()
    }
}