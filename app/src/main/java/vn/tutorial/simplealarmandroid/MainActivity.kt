package vn.tutorial.simplealarmandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.tutorial.simplealarmandroid.presentation.fragment.HomeFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.NewAlarmFragment

class MainActivity : AppCompatActivity() {
    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val newAlarmFragment = NewAlarmFragment()

        supportFragmentManager.beginTransaction().replace(
            R.id.home_container,
            newAlarmFragment,
        ).commit()

    }
}