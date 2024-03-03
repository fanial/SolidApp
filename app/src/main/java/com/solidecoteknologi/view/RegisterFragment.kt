package com.solidecoteknologi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentLoginBinding
import com.solidecoteknologi.databinding.FragmentRegisterBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
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
    ): View{
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()

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
    }

    private fun setupListener() {
        binding.btnDaftar.setOnClickListener {
            binding.apply {
                val name = edNama.text.toString()
                val email = edEmail.text.toString()
                val pass = edPassword.text.toString()
                val passValid = edPasswordValidation.text.toString()
                val instansi = edInstansi.text.toString()
                val role = edRole.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && passValid.isNotEmpty() && role.isNotEmpty() && instansi.isNotEmpty()){
                    model.register(name, email, pass, passValid, role, instansi)
                } else {
                    edNama.error = "Harap isi terlebih dahulu"
                    edEmail.error = "Harap isi terlebih dahulu"
                    edPassword.error = "Harap isi terlebih dahulu"
                    edPasswordValidation.error = "Harap isi terlebih dahulu"
                    edInstansi.error = "Harap isi terlebih dahulu"
                    edRole.error = "Harap isi terlebih dahulu"
                }
            }
        }

    }
}