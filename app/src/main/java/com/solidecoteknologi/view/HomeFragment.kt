package com.solidecoteknologi.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataCategoryItem
import com.solidecoteknologi.data.DataItem
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
        binding.edQty.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                hideKeyboardQty()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.edQty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.layoutQty.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    binding.layoutQty.error = null
                } else {
                    binding.layoutQty.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.linearLayout.visibility = View.VISIBLE
                if (p0?.length == 0) {
                    binding.layoutQty.clearFocus()
                }
            }

        })

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
        binding.history.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

        binding.btnKirim.setOnClickListener {
            val qty = binding.edQty.text.toString()
            if (qty.isNotEmpty() && idCategory.isNotEmpty()){
                lifecycleScope.launch {
                    modelTransaction.storeWaste(token, idAccount.toInt(), qty.toFloat(), idCategory.toInt())
                }
            } else {
                binding.layoutQty.error = getString(R.string.harap_isi_terlebih_dahulu)
            }
        }

        binding.edKategori.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                idCategory = listCategory[position].id.toString()
                binding.edKategori.clearFocus()
            }


    }
    private fun hideKeyboardQty() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edQty.windowToken, 0)
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
                idAccount = data.idAccount
                model.getProfile(token)
            }
        }

        model.profile().observe(viewLifecycleOwner) {
            if (it != null) {
                if (!it.success) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
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
                if (it.success){
                    binding.layoutSuccess.visibility = View.VISIBLE
                    binding.msgSuccess.text = it.message
                    binding.edQty.text = null
                    binding.edKategori.text = null
                    binding.edKategori.clearFocus()
                    binding.edQty.clearFocus()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.layoutSuccess.visibility = View.GONE
                    }, 1300)
                } else {
                    Snackbar.make(binding.root, it.message , Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}