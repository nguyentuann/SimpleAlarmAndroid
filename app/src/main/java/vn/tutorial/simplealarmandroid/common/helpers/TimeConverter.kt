package vn.tutorial.simplealarmandroid.common.helpers

object TimeConverter {
    fun convertTimeToString(hour: Int, minute: Int) = "%02d:%02d".format(hour, minute)
}

