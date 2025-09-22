package vn.tutorial.simplealarmandroid.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.tutorial.simplealarmandroid.presentation.fragment.StopwatchFragment
import vn.tutorial.simplealarmandroid.presentation.fragment.TimerFragment

class TimerStopWatchAdapter(
    fragment: FragmentActivity,
    private val tabs: List<String>

) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TimerFragment()
            1 -> StopwatchFragment()
            else -> TimerFragment()
        }
    }

    override fun getItemCount(): Int {
        return tabs.size
    }
}