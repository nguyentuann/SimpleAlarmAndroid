package vn.tutorial.simplealarmandroid.presentation.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import vn.tutorial.simplealarmandroid.common.constants.Tag
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

    val selectedDays = mutableSetOf<Int>()
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


        // todo manager create alarm
        setupToolbar()
        setupButtons()
        observeViewModel()

        // todo save alarm
        newAlarmFragment.btnSave.setOnClickListener {
            saveNewAlarm()
        }

        newAlarmFragment.icSave.setOnClickListener {
            saveNewAlarm()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _newAlarmFragment = null
    }

    private fun setupToolbar() {
        newAlarmFragment.toolBarNewAlarm.setNavigationOnClickListener {
            isChange(requireContext())
        }
    }

    private fun setupButtons() {
        newAlarmFragment.btnEdit.setOnClickListener { popUpTimePicker() }
        newAlarmFragment.btnCalendar.setOnClickListener { popUpDatePicker() }

        val buttonsWithDays = mapOf(
            newAlarmFragment.btnMon to 1,
            newAlarmFragment.btnTue to 2,
            newAlarmFragment.btnWed to 3,
            newAlarmFragment.btnThu to 4,
            newAlarmFragment.btnFri to 5,
            newAlarmFragment.btnSat to 6,
            newAlarmFragment.btnSun to 7
        )

        buttonsWithDays.forEach { (button, dayOfWeek) ->
            button.setOnClickListener {
                val isSelected = !selectedDays.contains(dayOfWeek)
                if (isSelected) selectedDays.add(dayOfWeek) else selectedDays.remove(dayOfWeek)
                chooseDate(button, isSelected)
            }
        }

        newAlarmFragment.btnSave.setOnClickListener { saveNewAlarm() }
    }

    private fun observeViewModel() {
        newAlarmViewModel.newAlarm.observe(viewLifecycleOwner) { alarm ->
            // update time
            newAlarmFragment.tvTime.text =
                TimeConverter.convertTimeToString(alarm.hour, alarm.minute)

            // update message
            if (newAlarmFragment.tfMessage.text.toString() != alarm.message) {
                newAlarmFragment.tfMessage.setText(alarm.message)
            }

            // show/hide weekly buttons
            newAlarmFragment.btnDays.visibility =
                if (alarm.date != null) View.GONE else View.VISIBLE
        }
    }

    // todo các hàm pop up time picker
    fun popUpTimePicker() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogStyle)
        val timePickerDialog = TimePickerDialog(
            wrapper,
            { _, hour, minute ->
                newAlarmViewModel.updateTime(hour, minute)
            },
            newAlarmViewModel.newAlarm.value!!.hour,
            newAlarmViewModel.newAlarm.value!!.minute,
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
                var date = TimeHelper.getCalendarFromDate(year, month, dayOfMonth).timeInMillis
                newAlarmViewModel.updateDate(date)
            },
            2024,
            5,
            20
        )

        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEUTRAL,
            "Delete"
        ) { dialog, _ ->
            // todo Xử lý khi Clear
            newAlarmViewModel.updateDate(null)
            newAlarmFragment.scheduleAlarm.setText(R.string.schedule_alarm)
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
        newAlarmViewModel.prepareAlarmBeforeSave(
            selectedDays = selectedDays,
            message = newAlarmFragment.tfMessage.text.toString()
        )

        listAlarmViewModel.saveAlarm(newAlarmViewModel.newAlarm.value!!)

        activity?.onBackPressedDispatcher?.onBackPressed() // todo quay lại HomeFragment
    }

    // todo check back
    fun isChange(context: Context) {
        if (newAlarmViewModel.isChange() || selectedDays.isNotEmpty() || newAlarmFragment.tfMessage.text.trim()
                .isNotEmpty()
        ) {
            AlertDialog.Builder(context).apply {
                setTitle("Confirm")
                setMessage("Alarm is not saved. Do you want to exit without saving?")
                setCancelable(false)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        activity?.onBackPressedDispatcher?.onBackPressed()
                    }
                    .show()
            }
        } else {
            Log.d(Tag.AlarmTag, "chưa có thay đổi gì")
            activity?.onBackPressedDispatcher?.onBackPressed()

        }
    }

}