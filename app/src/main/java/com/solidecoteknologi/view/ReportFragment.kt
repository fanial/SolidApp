package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentReportBinding
import com.solidecoteknologi.view.adapter.ReportPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListener()
        setupModel()
        setupViews()
    }

    private fun setupViews() {
        val adapter = ReportPagerAdapter(requireActivity().supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.addFragment(DailyReportFragment(), "Per Hari")
        adapter.addFragment(MonthlyReportFragment(), "Per Bulan")

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupModel() {

    }

    private fun setupListener() {
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_profileFragment)
        }
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_homeFragment)
        }

    }

    private fun setupObservers() {

    }

}