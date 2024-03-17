package com.solidecoteknologi.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataItem
import com.solidecoteknologi.databinding.FragmentRegisterBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val model : AuthViewModel by viewModels()
    private var instansi = ""
    private  var listInstansi = listOf<DataItem>()
    private  var role = ""
    private val item = listOf("user", "pic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setupTransitions()
    }
    private fun setupTransitions() {
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
        setupModel()
        setupViews()
        setupBackHandler()

    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edInstansi.windowToken, 0)
    }

    private fun setupBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = findNavController().currentDestination?.id

                if (currentDestinationId == R.id.registerFragment) {
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupViews() {

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, item)
        binding.edRole.setAdapter(adapter)

        binding.edNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    with(binding){
                        layoutNama.error = null
                    }
                } else{
                    binding.layoutNama.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    binding.layoutNama.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutNama.clearFocus()
                }
            }

        })

        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()) {
                    with(binding){
                        layoutEmail.error = null
                    }
                } else{
                    binding.layoutEmail.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    binding.layoutEmail.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutEmail.clearFocus()
                }
            }

        })

        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.length >= 6) {
                    with(binding){
                        layoutPassword.error = null
                    }
                } else{
                    binding.layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0 && p0.length <= 6) {
                    binding.layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutPassword.clearFocus()
                }
            }

        })

        binding.edPasswordValidation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.length <= 6) {
                    with(binding){
                        layoutPasswordValidation.error = null
                    }
                } else{
                    binding.layoutPasswordValidation.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0 && p0.length <= 6) {
                    binding.layoutPasswordValidation.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutPasswordValidation.clearFocus()
                }
            }

        })

        binding.edRole.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    with(binding){
                        layoutRole.error = null
                    }
                } else{
                    binding.layoutRole.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    binding.layoutRole.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutRole.clearFocus()
                }
            }

        })

        binding.edInstansi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    with(binding){
                        layoutInstansi.error = null
                    }
                } else{
                    binding.layoutInstansi.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    binding.layoutInstansi.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutInstansi.clearFocus()
                    hideKeyboard()
                }
            }

        })

    }

    private fun setupModel() {
        model.listOrganization()
    }

    private fun setupObservers() {
        model.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        model.dataRegister().observe(viewLifecycleOwner){
            if (it != null){
                findNavController().navigate(R.id.action_registerFragment_to_emailConfirmFragment)
            }
        }

        model.dataOrganization().observe(viewLifecycleOwner){
            if (it != null){
                listInstansi = it.data as MutableList<DataItem>
                val item = listInstansi.map { d -> d.name }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, item)
                binding.edInstansi.setAdapter(adapter)
            }
        }

        model.isLoading().observe(viewLifecycleOwner){
            loading(it)
        }

    }

    private fun setupListener() {

        binding.edRole.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                role = item[position]
                Log.i("TAG", "ROLE: $role")
            }

        binding.btnKembali.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.edInstansi.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                instansi = listInstansi[position].id.toString()
                binding.layoutInstansi.error = null
                hideKeyboard()
                binding.layoutInstansi.clearFocus()
                Log.i("TAG", "ROLE: $instansi")
            }


        binding.btnDaftar.setOnClickListener {
            binding.apply {
                val name = edNama.text.toString()
                val email = edEmail.text.toString()
                val pass = edPassword.text.toString()
                val passValid = edPasswordValidation.text.toString()
                val instansi = instansi
                val role = edRole.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && passValid.isNotEmpty() && role.isNotEmpty() && instansi.isNotEmpty()){
                    model.register(name, email, pass, passValid, role, instansi)
                } else {
                    layoutNama.error = getString(R.string.harap_isi_terlebih_dahulu)
                    layoutEmail.error = getString(R.string.harap_isi_terlebih_dahulu)
                    layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                    layoutPasswordValidation.error = getString(R.string.harap_isi_terlebih_dahulu)
                    layoutInstansi.error = getString(R.string.harap_isi_terlebih_dahulu)
                    layoutRole.error = getString(R.string.harap_isi_terlebih_dahulu)
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