package com.solidecoteknologi.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentDailyReportBinding
import com.solidecoteknologi.databinding.FragmentMonthlyReportBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MonthlyReportFragment : Fragment() {


    private var _binding : FragmentMonthlyReportBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private var token = ""
    private var startDate = ""
    private var endDate = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = LocalDate.now()
        startDate = currentDate.withDayOfMonth(1).toString()
        endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).toString()

        model.getToken().observe(viewLifecycleOwner){
            val token = it
            modelTransaction.monthlyReport(token, startDate, endDate)
        }

        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()

        modelTransaction.dataMonthly().observe(viewLifecycleOwner){
            if (it != null){
                // Create entries for the pie chart
                val entries = ArrayList<BarEntry>()

                val categories = it.data

                for ((index, data) in categories.withIndex()) {
                    if (data.amount.toInt() != 0){
                        entries.add(BarEntry(index.toFloat(), data.amount))
                    }

                    when (data.category) {
                        "Anorganik" -> {
                            colors.add(Color.parseColor("#FFB905"))
                            categoryLabels.add("Anorganik")
                        }
                        "B3" -> {
                            colors.add(Color.parseColor("#D92626"))
                            categoryLabels.add("B3")
                        }
                        "Organik" -> {
                            colors.add(Color.parseColor("#4DC243"))
                            categoryLabels.add("Organik")
                        }
                    }
                }

                // Create a dataset for the line chart
                val dataSet = BarDataSet(entries, "Monthly Report")
                dataSet.colors = colors // Set bar colors
                dataSet.stackLabels = categoryLabels.toTypedArray()

                // Create LineData object from the dataset
                val barData = BarData(dataSet)

                binding.lineChart.apply {
                    description.isEnabled = false // Disable description
                    axisLeft.textColor = Color.BLACK // Set left axis text color
                    axisRight.isEnabled = false // Disable right axis
                    xAxis.textColor = Color.BLACK // Set x-axis text color
                    legend.isEnabled = false

                    val xAxis = xAxis
                    xAxis.valueFormatter = IndexAxisValueFormatter(categoryLabels)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f
                    xAxis.isGranularityEnabled = true

                    // Set data to the line chart
                    data = barData

                    // Refresh the chart
                    invalidate()
                }
            }

        }
    }

}