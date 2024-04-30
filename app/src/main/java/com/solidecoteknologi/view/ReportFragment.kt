package com.solidecoteknologi.view

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.data.ResponseDailyReport
import com.solidecoteknologi.data.ResponseMonthlyReport
import com.solidecoteknologi.databinding.FragmentReportBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private var startDate = ""
    private var endDate = ""
    private var date = ""

    private lateinit var flipInAnimator: ObjectAnimator
    private lateinit var flipOutAnimator: ObjectAnimator

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
        setupObservers()
    }

    private fun setupObservers() {

        val currentDate = LocalDate.now()
        startDate = currentDate.withDayOfMonth(1).toString()
        endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).toString()

        model.getToken().observe(viewLifecycleOwner){
            val token = it
            modelTransaction.monthlyReport(token, startDate, endDate)
            modelTransaction.dailyReport(token, date)
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

        //Daily Report
        modelTransaction.dataDaily().observe(viewLifecycleOwner){ res ->
            if (res != null){
                setDataChartAmount(res)
                setDataChartCarbon(res)
                binding.bannerThankyou.text = "Terima kasih anda telah membantu mengurangi emisi sebesar ${res.totalCarbon} C02e per hari ini"
            }
        }

        //Monthly Report
        modelTransaction.dataMonthly().observe(viewLifecycleOwner){ res ->
            if (res != null){
                setDataChartMonthlyAmount(res)
                setDataChartMonthlyCarbon(res)
            }
        }

    }

    private fun setDataChartMonthlyCarbon(res: ResponseMonthlyReport) {
        val data = res.data
        // Prepare data entries for the PieChart
        val entries = ArrayList<PieEntry>()
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()

        data.map {
            entries.add(
                PieEntry(
                    it.carbon,
                    it.category,
                    data.size
                ))
            when (it.category) {
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

                else -> {
                    colors.add(Color.parseColor("#002201"))
                    categoryLabels.add("Other")
                }
            }
        }


        // Create a dataset for the PieChart
        val dataSet = PieDataSet(entries,"")

        dataSet.colors = colors.toList()

        // Create PieData object
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())

        // Set data to the PieChart
        binding.carbonChartBulanan.data = pieData

        // Customize PieChart
        binding.carbonChartBulanan.apply {
            setUsePercentValues(true)
            setDrawSliceText(false)
            setMinAngleForSlices(10f)
            description.isEnabled = false
            setEntryLabelColor(android.R.color.black)
            setHoleColor(android.R.color.transparent)
            setTransparentCircleColor(android.R.color.transparent)
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

    private fun setDataChartMonthlyAmount(res: ResponseMonthlyReport) {
        val data = res.data
        // Prepare data entries for the PieChart
        val entries = ArrayList<PieEntry>()
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()

        data.map {
            entries.add(
                PieEntry(
                    it.amount,
                    it.category,
                    data.size
                ))

            when (it.category) {
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

                else -> {
                    colors.add(Color.parseColor("#002201"))
                    categoryLabels.add("Other")
                }
            }
        }

        // Create a dataset for the PieChart
        val dataSet = PieDataSet(entries,"")
        dataSet.colors = colors

        // Create PieData object
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())

        // Set data to the PieChart
        binding.amountChartBulanan.data = pieData

        // Customize PieChart
        binding.amountChartBulanan.apply {
            setUsePercentValues(true)
            setDrawSliceText(false)
            setMinAngleForSlices(10f)
            description.isEnabled = false
            setEntryLabelColor(android.R.color.black)
            setHoleColor(android.R.color.transparent)
            setTransparentCircleColor(android.R.color.transparent)
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

    private fun setDataChartCarbon(amount: ResponseDailyReport) {
        val data = amount.data
        // Prepare data entries for the PieChart
        val entries = ArrayList<PieEntry>()
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()

        data.map {
            entries.add(
                PieEntry(
                    it.carbon,
                    it.category,
                    data.size
                ))
            when (it.category) {
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

                else -> {
                    colors.add(Color.parseColor("#002201"))
                    categoryLabels.add("Other")
                }
            }
        }


        // Create a dataset for the PieChart
        val dataSet = PieDataSet(entries,"")

        dataSet.colors = colors.toList()

        // Create PieData object
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())

        // Set data to the PieChart
        binding.carbonChartHarian.data = pieData

        // Customize PieChart
        binding.carbonChartHarian.apply {
            setUsePercentValues(true)
            setDrawSliceText(false)
            setMinAngleForSlices(10f)
            description.isEnabled = false
            setEntryLabelColor(android.R.color.black)
            setHoleColor(android.R.color.transparent)
            setTransparentCircleColor(android.R.color.transparent)
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

    private fun setDataChartAmount(amount: ResponseDailyReport) {
        val data = amount.data
        // Prepare data entries for the PieChart
        val entries = ArrayList<PieEntry>()
        val colors = mutableListOf<Int>()
        val categoryLabels = mutableListOf<String>()

        data.map {
            entries.add(
                PieEntry(
                    it.amount,
                    it.category,
                    data.size
                ))

            when (it.category) {
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

                else -> {
                    colors.add(Color.parseColor("#002201"))
                    categoryLabels.add("Other")
                }
            }
        }


        // Create a dataset for the PieChart
        val dataSet = PieDataSet(entries,"")
        dataSet.colors = colors

        // Create PieData object
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())

        // Set data to the PieChart
        binding.amountChartHarian.data = pieData

        // Customize PieChart
        binding.amountChartHarian.apply {
            setUsePercentValues(true)
            setDrawSliceText(false)
            setMinAngleForSlices(10f)
            description.isEnabled = false
            setEntryLabelColor(android.R.color.black)
            setHoleColor(android.R.color.transparent)
            setTransparentCircleColor(android.R.color.transparent)
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

    private fun setupViews() {

        flipInAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_in) as ObjectAnimator
        flipOutAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_out) as ObjectAnimator

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date = currentDateTime.format(formatter)

        if (model.getRoleAccount().value != "PIC"){
            binding.bannerThankyou.visibility = View.GONE
        }
    }

    private fun setupListener() {

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_profileFragment)
        }
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_homeFragment)
        }
        binding.history.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_historyFragment)
        }
        binding.frameHarian.setOnClickListener {
            flipCardHarian()
        }
        binding.frameBulanan.setOnClickListener {
            flipBulanan()
        }
    }

    private fun flipBulanan() {
        val frontVisible = binding.cardAmountBulanan.isVisible

        if (frontVisible) {
            flipOutAnimator.setTarget(binding.cardAmountBulanan)
            flipInAnimator.setTarget(binding.cardCarbonBulanan)
        } else {
            flipOutAnimator.setTarget(binding.cardCarbonBulanan)
            flipInAnimator.setTarget(binding.cardAmountBulanan)
        }

        flipOutAnimator.start()
        flipInAnimator.start()

        binding.cardAmountBulanan.isVisible = !frontVisible
        binding.cardCarbonBulanan.isVisible = frontVisible
    }

    private fun flipCardHarian() {
        val frontVisible = binding.cardAmountHarian.isVisible

        if (frontVisible) {
            flipOutAnimator.setTarget(binding.cardAmountHarian)
            flipInAnimator.setTarget(binding.cardCarbonHarian)
        } else {
            flipOutAnimator.setTarget(binding.cardCarbonHarian)
            flipInAnimator.setTarget(binding.cardAmountHarian)
        }

        flipOutAnimator.start()
        flipInAnimator.start()

        binding.cardAmountHarian.isVisible = !frontVisible
        binding.cardCarbonHarian.isVisible = frontVisible
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