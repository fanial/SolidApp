package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataCategoryItem
import com.solidecoteknologi.databinding.FragmentHomeBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import com.solidecoteknologi.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val modelTransaction : TransactionViewModel by viewModels()
    private val model : AuthViewModel by viewModels()
    private var token = ""
    private var idAccount = ""
    private  var idCategory = ""
    private  var listCategory = listOf<DataCategoryItem>()

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
                lifecycleScope.launch {
                    modelTransaction.storeWaste(token, idAccount.toInt(), qty.toInt(), idCategory.toInt())
                }
            }
        }

        binding.edKategori.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                idCategory = listCategory[position].id.toString()
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
                //model.refreshToken(token)
            }
        }

        modelTransaction.dataCategory().observe(viewLifecycleOwner){ listCat ->
            if (listCat != null){
                listCategory = listCat.data as MutableList<DataCategoryItem>
                val item = listCategory.map { d -> d.name }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, item)
                binding.edKategori.setAdapter(adapter)
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

        model.refreshToken().observe(viewLifecycleOwner){
            if (it != null){
                if (it.status == "Token is Expired" || it.status == "Token is Invalid"){
                    model.logout()
                    findNavController().navigate(R.id.action_homeFragment_to_onboardingFragment)
                } else {
                    model.setNewToken("${it.tokenType} ${it.token}")
                }
            }
        }
    }
}