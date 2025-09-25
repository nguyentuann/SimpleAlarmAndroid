package vn.tutorial.simplealarmandroid.local.db

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val LANG_KEY = "app_language"
        private const val APP_THEME = "app_theme"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var appLanguage: String
        get() = prefs.getString(LANG_KEY, "en") ?: "en"
        set(value) {
            prefs.edit { putString(LANG_KEY, value) }
        }

    var appTheme: Int
        get() = prefs.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = prefs.edit { putInt(APP_THEME, value) }

}
