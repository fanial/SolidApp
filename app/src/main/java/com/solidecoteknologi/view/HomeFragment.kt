package com.solidecoteknologi.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
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
    private val adapter by lazy { CategoryAdapter() }
    private var isCategoryClicked = false
    private  var idCategory = ""

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
            requireActivity().finish()
            return@addCallback
        }

    }

    private fun setupViews() {
        binding.listCategory.adapter = adapter
        val layout = LinearLayoutManager(context)
        binding.listCategory.layoutManager = layout

        adapter.onClick = {
            idCategory = it.id.toString()
            binding.edKategori.text = it.name
            toggleCardVisibility()
            Log.i(TAG, "ID Category: $idCategory ${it.name}")
        }
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
            if (qty.isNotEmpty() && idCategory.isNotEmpty()){
                modelTransaction.storeWaste(token, idAccount.toInt(), qty.toInt(), idCategory.toInt())
            }
        }

        binding.edKategori.setOnClickListener {
            toggleCardVisibility()
        }


    }

    private fun toggleCardVisibility() {
        if (isCategoryClicked) {
            binding.cardCategory.visibility = View.GONE
        } else {
            binding.cardCategory.visibility = View.VISIBLE
        }
        isCategoryClicked = !isCategoryClicked
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

        modelTransaction.dataWaste().observe(viewLifecycleOwner){
            if (it != null) {
                Snackbar.make(binding.root, it.message , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        modelTransaction.isLoading().observe(viewLifecycleOwner){
            if (it == true){
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.loadingBar.visibility = View.INVISIBLE
            }
        }
    }
}