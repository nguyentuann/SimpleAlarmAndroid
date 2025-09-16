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
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.databinding.FragmentHomeBinding
import vn.tutorial.simplealarmandroid.presentation.adapter.AlarmAdapter
import vn.tutorial.simplealarmandroid.presentation.viewModel.ListAlarmViewModel

@AndroidEntryPoint
class HomeFragment(
) : Fragment() {
    private var _homeFragment: FragmentHomeBinding? = null
    private val homeFragment get() = _homeFragment!!

    val listAlarmViewModel by activityViewModels<ListAlarmViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _homeFragment = FragmentHomeBinding.inflate(inflater, container, false)


        return homeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragment.toolBar.toolBarAdd.setOnClickListener {
            (activity as MainActivity).addNewAlarm()
        }

        homeFragment.addAlarmCard.setOnClickListener {
            (activity as MainActivity).addNewAlarm()
        }

        // todo hiển thị list alarm nếu có
        if (!listAlarmViewModel.alarmList.value.isNullOrEmpty()) {

            Log.d(Tag.AlarmTag, "have alarm")
            val recyclerView = homeFragment.alarmList
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = AlarmAdapter(listAlarmViewModel.alarmList.value!!)


            homeFragment.alarmList.visibility = View.VISIBLE
            homeFragment.addAlarmCard.visibility = View.GONE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _homeFragment = null
    }
}