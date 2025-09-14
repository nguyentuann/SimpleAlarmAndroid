package vn.tutorial.simplealarmandroid.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.tutorial.simplealarmandroid.MainActivity
import vn.tutorial.simplealarmandroid.databinding.FragmentHomeBinding

class HomeFragment(
) : Fragment() {
    private var _homeFragment: FragmentHomeBinding? = null
    private val homeFragment get() = _homeFragment!!

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _homeFragment = null
    }
}