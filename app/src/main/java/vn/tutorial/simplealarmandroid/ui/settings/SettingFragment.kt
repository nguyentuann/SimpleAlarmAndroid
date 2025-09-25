package vn.tutorial.simplealarmandroid.ui.settings

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.components.CommonComponents
import vn.tutorial.simplealarmandroid.databinding.FragmentSettingBinding
import vn.tutorial.simplealarmandroid.helpers.setAppLocale
import vn.tutorial.simplealarmandroid.local.db.AppPreferences
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var appPrefs: AppPreferences

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

        // Language selection
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }

        binding.btnTheme.setOnClickListener {
            showThemeDialog()
        }

        binding.toolBar.setNavigationOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Tiếng Việt")
        val codes = arrayOf("en", "vi")
        val currentLang = appPrefs.appLanguage
        val selectedIndex = codes.indexOf(currentLang)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.language))
            .setSingleChoiceItems(
                ArrayAdapter(
                    requireContext(),
                    R.layout.simple_list_item_single_choice,
                    languages
                ), selectedIndex
            ) { dialog, which ->
                requireContext().setAppLocale(codes[which])
                appPrefs.appLanguage = codes[which]
                requireActivity().recreate()
                dialog.dismiss()
                CommonComponents.toastText(
                    context = requireContext(),
                    message = getString(R.string.change_language)
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showThemeDialog() {
        val themes = arrayOf(
            getString(R.string.light),
            getString(R.string.dark),
        )
        val codes = arrayOf(
            AppCompatDelegate.MODE_NIGHT_NO,        // Light
            AppCompatDelegate.MODE_NIGHT_YES,       // Dark
        )

        val currentTheme = appPrefs.appTheme
        val selectedIndex = codes.indexOf(currentTheme).takeIf { it >= 0 } ?: 2

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.theme))
            .setSingleChoiceItems(
                ArrayAdapter(
                    requireContext(),
                    R.layout.simple_list_item_single_choice,
                    themes
                ), selectedIndex
            ) { dialog, which ->
                val newTheme = codes[which]
                appPrefs.appTheme = newTheme
                AppCompatDelegate.setDefaultNightMode(newTheme)
                requireActivity().recreate()
                dialog.dismiss()

                CommonComponents.toastText(
                    context = requireContext(),
                    message = getString(R.string.change_theme)
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}