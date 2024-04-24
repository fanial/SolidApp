package com.solidecoteknologi.view

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.databinding.FragmentDailyReportBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


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
        setRetainInstance(true)
        setupObservers()
        setupViews()

    }

    private fun setupViews() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date = currentDateTime.format(formatter)
        val day = currentDateTime.dayOfWeek
        val dayOfWeekInIndonesian = day.getDisplayName(TextStyle.FULL, Locale("id"))
        binding.waktuReport.text = buildString {
            append(dayOfWeekInIndonesian)
            append(", ")
            append(date)
        }
    }

    private fun setupObservers() {
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

        model.isLoading().observe(viewLifecycleOwner){
            loading(it)
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


        model.getStoredAccount().observe(viewLifecycleOwner){ data ->
            if (data != null) {
                token = data.token
                modelTransaction.dailyReport(token, date)
            }
        }

        modelTransaction.dataDaily().observe(viewLifecycleOwner){ res ->
            if (res != null){
                val data = res.data
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
                binding.pieChart.data = pieData

                // Customize PieChart
                binding.pieChart.apply {
                    if (entries.isEmpty()) {
                        setNoDataText("No data available")
                        setNoDataTextColor(resources.getColor(com.solidecoteknologi.R.color.md_theme_primary))
                        setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
                    } else {
                        setUsePercentValues(true)
                        setDrawSliceText(false)
                        setMinAngleForSlices(10f)
                        description.isEnabled = false
                        setEntryLabelColor(R.color.black)
                        setHoleColor(R.color.transparent)
                        setTransparentCircleColor(R.color.transparent)
                        setTransparentCircleAlpha(0)
                        holeRadius = 0f
                        transparentCircleRadius = 0f
                        setDrawEntryLabels(false)
                        legend.isEnabled = true
                        highlightValues(null)
                        // Refresh the chart
                        invalidate()
                    }

                }

            }
        }
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