package com.solidecoteknologi.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentHomeBinding
import com.solidecoteknologi.view.adapter.CategoryAdapter
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private var token = ""
    private var idAccount = ""
    private var doubleBackToExitPressedOnce = false
    private val adapter by lazy { CategoryAdapter() }
    private var isCategoryClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListener()
        setupModel()
        setupViews()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return@addCallback
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(requireContext(),"Tekan 2x untuk keluar dari aplikasi", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2)
        }

    }

    private fun setupViews() {
        binding.listCategory.adapter = adapter
        val layout = LinearLayoutManager(context)
        binding.listCategory.layoutManager = layout

    }

    private fun setupModel() {
        modelTransaction.listCategory()

    }

    private fun setupListener() {
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        binding.report.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_reportFragment)
        }

        binding.btnKirim.setOnClickListener {
            val qty = binding.edQty.text.toString()

        }

        binding.edKategori.setOnClickListener {

        }


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
                idAccount = data.idAccount
            }
        }

        modelTransaction.dataCategory().observe(viewLifecycleOwner){ listCat ->
            if (listCat != null){
                adapter.setData(listCat.data.toMutableList())
            }
        }
    }
}