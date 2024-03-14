package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentReportBinding
import com.solidecoteknologi.view.adapter.ReportPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter : ReportPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupViews()

    }

    private fun setupViews() {
        viewPagerAdapter = ReportPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        with(binding){
            viewPager.adapter = viewPagerAdapter

            TabLayoutMediator(tabLayout, viewPager){ tab, position ->
                when(position){
                    0 -> {
                        tab.text = "Per Hari"
                        tab.setIcon(R.drawable.day_icon) // Set icon for the tab
                    }
                    1 -> {
                        tab.text = "Per Bulan"
                        tab.setIcon(R.drawable.month_icon)
                    }
                }
            }.attach()
        }
    }

    private fun setupListener() {
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_profileFragment)
        }
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_homeFragment)
        }

    }


}