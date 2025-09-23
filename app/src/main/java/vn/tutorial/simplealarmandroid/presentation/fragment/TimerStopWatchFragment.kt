package vn.tutorial.simplealarmandroid.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.simplealarmandroid.databinding.FragmentTimerStopWatchBinding
import vn.tutorial.simplealarmandroid.presentation.adapter.TimerStopWatchAdapter
import vn.tutorial.simplealarmandroid.R
@AndroidEntryPoint
class TimerStopWatchFragment : Fragment() {

    private var _binding: FragmentTimerStopWatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerStopWatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs = listOf(
            getString(R.string.timer),
            getString(R.string.stopwatch)
        )

        binding.pager.adapter = TimerStopWatchAdapter(requireActivity(), tabs)

        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        binding.toolBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
