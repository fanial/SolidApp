package com.solidecoteknologi.view

import android.R
import android.R.attr
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.data.DataDailyItem
import com.solidecoteknologi.databinding.FragmentDailyReportBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DailyReportFragment : Fragment() {

    private var _binding : FragmentDailyReportBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private var token = ""
    private var date = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListener()
        setupViews()

    }

    private fun setupViews() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date = currentDateTime.format(formatter)
        val day = currentDateTime.dayOfWeek
        binding.waktuReport.text = buildString {
            append(day)
            append(", ")
            append(date)
        }
    }

    private fun setupListener() {

    }

    private fun setupObservers() {
        model.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        model.getStoredAccount().observe(viewLifecycleOwner){ data ->
            if (data != null) {
                token = data.token
                modelTransaction.dailyReport(token, date)
            }
        }

        modelTransaction.dataDaily().observe(viewLifecycleOwner){
            if (it != null){
                updatePieChart(binding.pieChart, it.data)
            }
        }
    }

    private fun updatePieChart(pieChart: PieChart, data: List<DataDailyItem>) {
        // Prepare data entries for the PieChart
        val entries = ArrayList<PieEntry>()
        val colors = intArrayOf(Color.parseColor("#4DC243"), Color.parseColor("#FFB905"), Color.parseColor("#D92626"))

        data.map {
            if (it.amount.toInt() != 0){
                entries.add(
                    PieEntry(
                        it.amount,
                        it.category,
                        data.size
                    ))
            }
        }

        // Create a dataset for the PieChart
        val dataSet = PieDataSet(entries,"")

        dataSet.colors = colors.toList()

        // Create PieData object
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())

        // Set data to the PieChart
        pieChart.data = pieData

        // Customize PieChart
        pieChart.apply {
            description.isEnabled = false
            setEntryLabelColor(android.R.color.black)
            setUsePercentValues(true)
            setHoleColor(R.color.transparent)
            setTransparentCircleColor(R.color.transparent)
            setTransparentCircleAlpha(0)
            holeRadius = 0f
            transparentCircleRadius = 0f
            setDrawEntryLabels(false)
            legend.isEnabled = true
        }

        pieChart.highlightValues(null)
        // Refresh the chart
        pieChart.invalidate()
    }

}