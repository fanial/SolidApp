package com.solidecoteknologi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentHistoryBinding
import com.solidecoteknologi.databinding.FragmentHomeBinding
import com.solidecoteknologi.databinding.FragmentReportBinding
import com.solidecoteknologi.view.adapter.HistoryAdapter
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding : FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private val historyAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
        setupViews()
    }

    private fun setupViews() {
        binding.listHistory.adapter = historyAdapter
        val layout = LinearLayoutManager(context)
        binding.listHistory.layoutManager = layout
    }

    private fun setupObservers() {

        model.getToken().observe(viewLifecycleOwner){
            val token = it
            modelTransaction.historyList(token)
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

        modelTransaction.dataHistory().observe(viewLifecycleOwner){
            if (it != null) {
                historyAdapter.submitList(it.data)
            }
        }

    }

    private fun setupListener() {
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_profileFragment)
        }
        binding.report.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_reportFragment)
        }
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_homeFragment)
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