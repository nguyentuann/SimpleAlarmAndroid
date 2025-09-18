package vn.tutorial.simplealarmandroid.presentation.fragment

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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.common.extensions.CommonFunction
import vn.tutorial.simplealarmandroid.common.helpers.AlarmHelper
import vn.tutorial.simplealarmandroid.common.helpers.TimeConverter
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


        // todo manager create alarm
        setupToolbar()
        setupButtons()
        observeViewModel()

        newAlarmFragment.tfMessage.setText(newAlarmViewModel.newAlarm.value?.message ?: "")

        newAlarmFragment.tfMessage.doOnTextChanged { text, _, _, _ ->
            // todo update message
            newAlarmViewModel.updateMessage(text.toString())
        }


        // todo save alarm
        newAlarmFragment.btnSave.setOnClickListener {
            saveNewAlarm()
        }

        newAlarmFragment.icSave.setOnClickListener {
            saveNewAlarm()
        }

    }

    override fun onStop() {
        super.onStop()
        newAlarmViewModel.resetAlarm()
        selectedDays.clear()
        newAlarmFragment.tfMessage.setText("")
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
            newAlarmFragment.btnSun to 1,
            newAlarmFragment.btnMon to 2,
            newAlarmFragment.btnTue to 3,
            newAlarmFragment.btnWed to 4,
            newAlarmFragment.btnThu to 5,
            newAlarmFragment.btnFri to 6,
            newAlarmFragment.btnSat to 7,
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
                newAlarmFragment.scheduleAlarm.text = getString(
                    R.string.schedule_for,
                    TimeConverter.dayMonthYearFormat(dayOfMonth, month, year)
                )

                var date = AlarmHelper.getCalendarFromDate(year, month, dayOfMonth)
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
            CommonFunction.showAlertDialog(
                context,
                "Discard changes?",
                "You have unsaved changes. Are you sure you want to discard them?",
                onConfirm = { activity?.onBackPressedDispatcher?.onBackPressed() }
            )
        } else {
            Log.d(Tag.AlarmTag, "chưa có thay đổi gì")
            activity?.onBackPressedDispatcher?.onBackPressed()

        }
    }

}