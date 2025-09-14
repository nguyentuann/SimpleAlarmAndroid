package vn.tutorial.simplealarmandroid.presentation.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.databinding.FragmentNewAlarmBinding
import java.util.Calendar

class NewAlarmFragment : Fragment() {
    private var _newAlarmFragment: FragmentNewAlarmBinding? = null
    private val newAlarmFragment get() = _newAlarmFragment!!
    val selectedButtons = mutableSetOf<Button>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _newAlarmFragment = FragmentNewAlarmBinding.inflate(inflater, container, false)
        return newAlarmFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo quay lại HomeFragment
        newAlarmFragment.toolBarNewAlarm.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        //todo pop up time picker
        newAlarmFragment.btnEdit.setOnClickListener {
            popUpTimePicker()
        }

        // todo pop up datepicker
        newAlarmFragment.btnCalendar.setOnClickListener {
            popUpDatePicker()
        }

        // todo select date of week

        val buttons = listOf(
            newAlarmFragment.btnMon,
            newAlarmFragment.btnTue,
            newAlarmFragment.btnWed,
            newAlarmFragment.btnThu,
            newAlarmFragment.btnFri,
            newAlarmFragment.btnSat,
            newAlarmFragment.btnSun
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                if (selectedButtons.contains(button)) {
                    // todo Nếu button đã được chọn, bỏ chọn nó
                    selectedButtons.remove(button)
                    chooseDate(button, false)
                } else {
                    // todo Nếu button chưa được chọn, chọn nó
                    selectedButtons.add(button)
                    chooseDate(button, true)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _newAlarmFragment = null
    }

    // todo các hàm pop up chọn time và date
    fun popUpTimePicker() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogStyle)
        val timePickerDialog = TimePickerDialog(
            wrapper,
            { _, hour, minute ->
                println("$hour : $minute")
            },
            0,
            0,
            false // 12h format,

        )
        timePickerDialog.show()
    }

    fun popUpDatePicker() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogStyle)
        val datePickerDialog = DatePickerDialog(
            wrapper,
            { _, year, month, dayOfMonth ->
                println("$dayOfMonth / $month / $year")
            },
            2024,
            5,
            20
        )

        val calendar = Calendar.getInstance()
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEUTRAL,
            "Delete"
        ) { dialog, _ ->
            // todo Xử lý khi Clear
            dialog.dismiss()
        }
        datePickerDialog.show()
    }

    fun chooseDate(button: Button, isChosen: Boolean = false) {
        val color = if (isChosen) R.color.light_blue else R.color.dark_blue
        button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))

        button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

}