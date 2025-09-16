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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.helpers.TimeConverter
import vn.tutorial.simplealarmandroid.common.helpers.TimeHelper
import vn.tutorial.simplealarmandroid.databinding.FragmentNewAlarmBinding
import vn.tutorial.simplealarmandroid.presentation.viewModel.ListAlarmViewModel
import vn.tutorial.simplealarmandroid.presentation.viewModel.NewAlarmViewModel
import java.util.Calendar

@AndroidEntryPoint
class NewAlarmFragment : Fragment() {
    private var _newAlarmFragment: FragmentNewAlarmBinding? = null
    private val newAlarmFragment get() = _newAlarmFragment!!

    val selectedDays = mutableListOf<Int>()
    val newAlarmViewModel by viewModels<NewAlarmViewModel>()
    val listAlarmViewModel by activityViewModels<ListAlarmViewModel>()

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

        // todo reset alarm
        newAlarmViewModel.resetAlarm()
        selectedDays.clear()

        // todo quay lại HomeFragment
        newAlarmFragment.toolBarNewAlarm.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        //todo pop up time picker
        newAlarmFragment.btnEdit.setOnClickListener {
            popUpTimePicker()
        }

        // todo pop up date picker
        newAlarmFragment.btnCalendar.setOnClickListener {
            popUpDatePicker()
        }

        // todo select date of week
        val buttonsWithDays = mapOf(
            newAlarmFragment.btnSun to 1,
            newAlarmFragment.btnMon to 2,
            newAlarmFragment.btnTue to 3,
            newAlarmFragment.btnWed to 4,
            newAlarmFragment.btnThu to 5,
            newAlarmFragment.btnFri to 6,
            newAlarmFragment.btnSat to 7,
        )

        buttonsWithDays.forEach { button, dayOfWeek ->
            button.setOnClickListener {
                if (selectedDays.contains(dayOfWeek)) {
                    // todo Nếu button đã được chọn, bỏ chọn nó
                    selectedDays.remove(dayOfWeek)
                    chooseDate(button, false)
                } else {
                    // todo Nếu button chưa được chọn, chọn nó
                    selectedDays.add(dayOfWeek)
                    chooseDate(button, true)
                }
            }
        }

        // todo theo dõi ngày trong tương lai
        newAlarmViewModel.newAlarm.observe(viewLifecycleOwner) { alarm ->
            if (alarm.date != null) {
                newAlarmFragment.btnDays.visibility = View.GONE
            }
        }

        // todo theo dõi time
        newAlarmViewModel.newAlarm.observe(viewLifecycleOwner) { alarm ->
            newAlarmFragment.tvTime.text =
                TimeConverter.convertTimeToString(alarm.hour, alarm.minute)
        }

        // todo theo dõi message
        newAlarmFragment.tfMessage.setText(newAlarmViewModel.newAlarm.value!!.message)

        // todo lưu alarm
        newAlarmFragment.btnSave.setOnClickListener {
            saveNewAlarm()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _newAlarmFragment = null
    }

    // todo các hàm pop up time picker
    fun popUpTimePicker() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogStyle)
        val timePickerDialog = TimePickerDialog(
            wrapper,
            { _, hour, minute ->
                newAlarmViewModel.updateTime(hour, minute)
            },
            0,
            0,
            true
        )
        timePickerDialog.show()
    }

    // todo hàm pop up date picker
    fun popUpDatePicker() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogStyle)
        val datePickerDialog = DatePickerDialog(
            wrapper,
            { _, year, month, dayOfMonth ->
                newAlarmFragment.scheduleAlarm.text =
                    getString(R.string.schedule_for, "$dayOfMonth/${month + 1}/$year")
                4
                var date = TimeHelper.getCalendarFromDate(year, month, dayOfMonth).timeInMillis
                newAlarmViewModel.updateDate(date)
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

    // todo chọn ngày trong tuần
    fun chooseDate(button: Button, isChosen: Boolean = false) {
        val color = if (isChosen) R.color.light_blue else R.color.dark_blue
        button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))

        button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    // todo lưu alarm
    fun saveNewAlarm() {

        if (newAlarmViewModel.newAlarm.value!!.date != null) {
            newAlarmViewModel.updateDateOfWeek(null)
        } else {
            val sortedDays = selectedDays.sorted()
            newAlarmViewModel.updateDateOfWeek(sortedDays)
        }
        newAlarmViewModel.updateMessage(newAlarmFragment.tfMessage.text.toString())

        listAlarmViewModel.saveAlarm(newAlarmViewModel.newAlarm.value!!)

        activity?.onBackPressedDispatcher?.onBackPressed() // todo quay lại HomeFragment
    }

}