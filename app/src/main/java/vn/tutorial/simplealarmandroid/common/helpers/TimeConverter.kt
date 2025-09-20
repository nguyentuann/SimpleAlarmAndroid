package vn.tutorial.simplealarmandroid.common.helpers

import java.util.Calendar

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
        val selectedDayNames = days.map { e -> dayNames[(e - 1) % 7] }
        return selectedDayNames.joinToString(", ")
    }

    // todo lấy ra thứ tiếp theo có báo thức
    fun nameDateOfWeek(calendar: Calendar): String {
        val dateOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dateOfWeek) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Unknown"
        }
    }

    fun dayMonthYearFormat(day: Int, month: Int, year: Int): String {
        return "$day/${month + 1}/$year"
    }

    fun dayMonthYearFormatFromCalendar(millis: Long): String {
        val cal = Calendar.getInstance().apply {
            timeInMillis = millis
        }
        return dayMonthYearFormat(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    }

}

