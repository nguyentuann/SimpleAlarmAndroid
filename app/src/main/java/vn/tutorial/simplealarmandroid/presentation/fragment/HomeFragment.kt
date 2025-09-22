package vn.tutorial.simplealarmandroid.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.MainActivity
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.common.extensions.CommonFunction
import vn.tutorial.simplealarmandroid.databinding.FragmentHomeBinding
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel
import vn.tutorial.simplealarmandroid.presentation.adapter.AlarmAdapter
import vn.tutorial.simplealarmandroid.presentation.viewModel.ListAlarmViewModel

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

        // quan sát dữ liệu
        listAlarmViewModel.alarmList.observe(viewLifecycleOwner) { alarms ->
            if (!alarms.isNullOrEmpty()) {
                Log.d(Tag.AlarmTag, "Loaded ${alarms.size} alarms into UI")
                alarmAdapter.submitList(alarms)

                homeFragment.alarmList.visibility = View.VISIBLE
                homeFragment.addAlarmCard.visibility = View.GONE
            } else {
                Log.d(Tag.AlarmTag, "No alarms found")
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
        CommonFunction.showAlertDialog(
            requireContext(),
            "Delete Alarm",
            "Are you sure to delete this alarm?",
            onConfirm = {
                listAlarmViewModel.delete(alarm)
            },
        )
    }

    private fun editAlarm(alarmId: String) {
        val fragment = NewAlarmFragment.newInstance(alarmId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.home_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun enableAlarm(alarm: AlarmModel) {
        Log.d(Tag.AlarmTag, "call enableAlarm: $alarm.isOn")
        listAlarmViewModel.active(alarm, alarm.isOn)
    }
}
