package com.solidecoteknologi.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.data.ResponseMonthlyReport
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
    private var startDate = ""
    private var endDate = ""

    private lateinit var flipInAnimator: ObjectAnimator
    private lateinit var flipOutAnimator: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRetainInstance(true)

        val currentDate = LocalDate.now()
        startDate = currentDate.withDayOfMonth(1).toString()
        endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).toString()

        model.getToken().observe(viewLifecycleOwner){
            val token = it
            modelTransaction.monthlyReport(token, startDate, endDate)
        }

        flipInAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_in) as ObjectAnimator
        flipOutAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_out) as ObjectAnimator

        // Set click listener to flip the card
        binding.flipContainer.setOnClickListener {
            flipCard()
        }

        model.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        model.messageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        modelTransaction.isLoading().observe(viewLifecycleOwner){
            loading(it)
        }

        modelTransaction.messageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        modelTransaction.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        modelTransaction.dataMonthly().observe(viewLifecycleOwner){
            if (it != null){
                setAmountChart(it)
                setCarbonChart(it)
                binding.emisiCarbon.text = buildString {
                    append("Bulan ini anda telah mengurangi emisi karbon sebesar ")
                    append(it.totalCarbon)
                }
            }

        }
    }

    private fun setCarbonChart(carbonReport: ResponseMonthlyReport) {
        // Create entries for the pie chart
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()
        val entries = ArrayList<BarEntry>()

        val categories = carbonReport.data

        for ((index, data) in categories.withIndex()) {
            if (data.amount.toInt() != 0){
                entries.add(BarEntry(index.toFloat(), data.carbon))
            }

            when (data.category) {
                "Anorganik" -> {
                    colors.add(Color.parseColor("#ffC107"))
                    categoryLabels.add("Anorganik")
                }
                "E-Waste" -> {
                    colors.add(Color.parseColor("#DC3545"))
                    categoryLabels.add("E-Waste")
                }
                "Organik" -> {
                    colors.add(Color.parseColor("#28A745"))
                    categoryLabels.add("Organik")
                }
            }
        }

        val dataSet = BarDataSet(entries, "Monthly Carbon Report")
        dataSet.colors = colors // Set bar colors
        dataSet.stackLabels = categoryLabels.toTypedArray()
        val barData = BarData(dataSet)

        binding.carbonChart.apply {
            if (entries.isEmpty()) {
                setNoDataText("No data available")
                setNoDataTextColor(resources.getColor(com.solidecoteknologi.R.color.md_theme_primary))
                setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            } else {

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

                val leftAxis: YAxis = axisLeft
                leftAxis.setAxisMinimum(0f);

                val rightAxis: YAxis = axisRight
                rightAxis.setAxisMinimum(0f);

                // Set data to the line chart
                data = barData
                // Refresh the chart
                invalidate()
            }

        }
    }

    private fun setAmountChart(amountReport: ResponseMonthlyReport) {
        // Create entries for the pie chart
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()
        val entries = ArrayList<BarEntry>()

        val categories = amountReport.data

        for ((index, data) in categories.withIndex()) {
            if (data.amount.toInt() != 0){
                entries.add(BarEntry(index.toFloat(), data.amount))
            }

            when (data.category) {
                "Anorganik" -> {
                    colors.add(Color.parseColor("#ffC107"))
                    categoryLabels.add("Anorganik")
                }
                "E-Waste" -> {
                    colors.add(Color.parseColor("#DC3545"))
                    categoryLabels.add("E-Waste")
                }
                "Organik" -> {
                    colors.add(Color.parseColor("#28A745"))
                    categoryLabels.add("Organik")
                }
            }
        }

        val dataSet = BarDataSet(entries, "Monthly Amount Report")
        dataSet.colors = colors // Set bar colors
        dataSet.stackLabels = categoryLabels.toTypedArray()
        val barData = BarData(dataSet)

        binding.amountChart.apply {
            if (entries.isEmpty()) {
                setNoDataText("No data available")
                setNoDataTextColor(resources.getColor(com.solidecoteknologi.R.color.md_theme_primary))
                setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            } else {

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

                val leftAxis: YAxis = axisLeft
                leftAxis.setAxisMinimum(0f);

                val rightAxis: YAxis = axisRight
                rightAxis.setAxisMinimum(0f);

                // Set data to the line chart
                data = barData
                // Refresh the chart
                invalidate()
            }

        }
    }

    private fun flipCard() {

        val frontVisible = binding.frontView.isVisible

        if (frontVisible) {
            flipOutAnimator.setTarget(binding.frontView)
            flipInAnimator.setTarget(binding.backView)
        } else {
            flipOutAnimator.setTarget(binding.backView)
            flipInAnimator.setTarget(binding.frontView)
        }

        flipOutAnimator.start()
        flipInAnimator.start()

        binding.frontView.isVisible = !frontVisible
        binding.backView.isVisible = frontVisible
    }

    private fun loading(status: Boolean) {
        when(status){
            true -> {
                binding.loadingBar.visibility = View.VISIBLE
            }
            false -> {
                binding.loadingBar.visibility = View.INVISIBLE
            }
        }
    }

}