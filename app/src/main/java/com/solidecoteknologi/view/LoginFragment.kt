package com.solidecoteknologi.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentLoginBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val model : AuthViewModel by viewModels()

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
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
        setupViews()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
            return@addCallback
        }

        model.getStoredAccount().observe(viewLifecycleOwner){
            Log.i("TAG", "onViewCreated: $it")
        }
    }

    private fun setupViews() {
        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()) {
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
                        binding.btnMasuk.isEnabled = true
                    }
                } else{
                    binding.layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0 && p0.length <= 6) {
                    binding.layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.btnMasuk.isEnabled = false
                }
            }

        })
    }

    private fun setupObservers() {
        model.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }

        }

        model.dataLogin().observe(viewLifecycleOwner){
            if (it != null){
                val data = it.data
                model.setToken("${it.tokenType} ${it.token}", data.id.toString(), true)
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }, 500)
            }
        }

        model.isLoading().observe(viewLifecycleOwner){
            if (it == true){
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.loadingBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupListener() {
        binding.btnMasuk.setOnClickListener {
            binding.apply {
                val email = edEmail.text.toString()
                val pass = edPassword.text.toString()
                if (email.isNotEmpty() && pass.isNotEmpty()){
                    binding.layoutNama.error = null
                    binding.layoutPassword.error = null
                    model.login(email, pass)
                } else {
                    binding.layoutNama.error = getString(R.string.harap_isi_terlebih_dahulu)
                    binding.layoutPassword.error = getString(R.string.harap_isi_terlebih_dahulu)
                }
            }
        }

        binding.btnLupaPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_lupaPasswordFragment)
        }

        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}