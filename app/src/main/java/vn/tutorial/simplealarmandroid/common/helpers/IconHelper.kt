package vn.tutorial.simplealarmandroid.common.helpers

import vn.tutorial.simplealarmandroid.R

object IconHelper {
    fun getIconResourceForAlarm(hour: Int): Int {
        return if (hour in 6..18) R.drawable.ic_day else R.drawable.ic_night
    }
}