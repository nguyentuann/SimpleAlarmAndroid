package vn.tutorial.simplealarmandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vn.tutorial.simplealarmandroid.presentation.fragment.HomeFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.NewAlarmFragment

class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var newAlarmFragment: NewAlarmFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        newAlarmFragment = NewAlarmFragment()

        changeFragment(homeFragment)
    }

    fun addNewAlarm() {
        changeFragment(newAlarmFragment)
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.home_container,
            fragment,
        ).addToBackStack(null).commit()
    }
}