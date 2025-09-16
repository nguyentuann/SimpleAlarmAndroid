package vn.tutorial.simplealarmandroid.common.helpers

object TimeConverter {
    fun convertTimeToString(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val hour12 = when {
            hour > 12 -> hour - 12
            else -> hour
        }
        return "%02d:%02d %s".format(hour12, minute, amPm)
    }

    fun convertListDateToString(days: List<Int>?): String {
        if (days == null || days.isEmpty()) return "Once"
        if (days.size == 7) return "Everyday"
        val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val selectedDayNames = days.map { e -> dayNames[(e-1) % 7] }
        return selectedDayNames.joinToString(", ")

    }
}

