package vn.tutorial.simplealarmandroid.presentation.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.helpers.setAppLocale
import vn.tutorial.simplealarmandroid.databinding.FragmentSettingBinding
import java.util.Locale

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val prefsName = "app_settings"
    private val darkModeKey = "dark_mode"
    private val langKey = "app_language"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load saved settings
        val prefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)

//        val isDarkMode = prefs.getBoolean(darkModeKey, false)
//        binding.switchMode.isChecked = isDarkMode
//        applyDarkMode(isDarkMode)

        // Dark Mode toggle
//        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
//            applyDarkMode(isChecked)
//            prefs.edit { putBoolean(darkModeKey, isChecked) }
//        }

        // Language selection
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }

        binding.toolBar.setNavigationOnClickListener {
            Log.d("SettingFragment", "Back button clicked")
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

    }

//    private fun applyDarkMode(enable: Boolean) {
//        if (enable) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Tiếng Việt")
        val codes = arrayOf("en", "vi")
        val prefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val currentLang = prefs.getString(langKey, "en")
        var selectedIndex = codes.indexOf(currentLang)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setSingleChoiceItems(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_single_choice,
                    languages
                ), selectedIndex
            ) { dialog, which ->
                // Update language
                requireContext().setAppLocale(codes[which])
                prefs.edit().putString(langKey, codes[which]).apply()
                requireActivity().recreate()
                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    R.string.change_language,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
