package vn.tutorial.simplealarmandroid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.MainActivity
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.components.CommonComponents
import vn.tutorial.simplealarmandroid.data.model.AlarmModel
import vn.tutorial.simplealarmandroid.databinding.FragmentHomeBinding
import vn.tutorial.simplealarmandroid.ui.alarm.AlarmAdapter
import vn.tutorial.simplealarmandroid.ui.alarm.NewAlarmFragment
import vn.tutorial.simplealarmandroid.viewModel.ListAlarmViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _homeFragment: FragmentHomeBinding? = null
    private val homeFragment get() = _homeFragment!!

    private val listAlarmViewModel by activityViewModels<ListAlarmViewModel>()

    // todo giữ adapter như property để dùng lại
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        return homeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter 1 lần
        alarmAdapter = AlarmAdapter(
            { alarm -> deleteAlarm(alarm) },
            { alarm -> editAlarm(alarm) },
            { alarm -> enableAlarm(alarm) }
        )


        homeFragment.alarmList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = alarmAdapter
        }

        // click thêm mới
        homeFragment.toolBar.toolBarAdd.setOnClickListener {
            (activity as MainActivity).addNewAlarm()
        }

        homeFragment.addAlarmCard.setOnClickListener {
            (activity as MainActivity).addNewAlarm()
        }

        // chuyển sang timer stopwatch
        homeFragment.toolBar.toolBarTimer.setOnClickListener {
            (activity as MainActivity).timerStopWatch()
        }

        homeFragment.toolBar.toolBarSetting.setOnClickListener {
            (activity as MainActivity).setting()
        }

        homeFragment.toolBar.toolBarQuick.setOnClickListener {
            (activity as MainActivity).quickAlarm()
        }

        // quan sát dữ liệu
        listAlarmViewModel.alarmList.observe(viewLifecycleOwner) { alarms ->
            if (!alarms.isNullOrEmpty()) {
                alarmAdapter.submitList(alarms)

                homeFragment.alarmList.visibility = View.VISIBLE
                homeFragment.addAlarmCard.visibility = View.GONE
            } else {
                homeFragment.alarmList.visibility = View.GONE
                homeFragment.addAlarmCard.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragment = null
    }

    private fun deleteAlarm(alarm: AlarmModel) {
        CommonComponents.confirmDialog(
            requireContext(),
            getString(R.string.delete_alarm),
            getString(R.string.delete_alarm_message),
            onConfirm = {
                listAlarmViewModel.delete(alarm)
            },
        )
    }

    private fun editAlarm(alarmId: String) {
        val fragment = NewAlarmFragment.Companion.newInstance(alarmId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.home_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun enableAlarm(alarm: AlarmModel) {
        listAlarmViewModel.active(alarm, alarm.isOn)
    }
}