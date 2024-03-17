package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentLupaPasswordBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LupaPasswordFragment : Fragment() {

    private var _binding : FragmentLupaPasswordBinding? = null
    private val binding get() = _binding!!

    private val model : AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLupaPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
        setupBackHandler()
    }

    private fun setupListener() {
        binding.btnKembali.setOnClickListener {
            findNavController().navigate(R.id.action_lupaPasswordFragment_to_loginFragment)
        }

        binding.btnSimpan.setOnClickListener {
            val email = binding.edEmail.text.toString()
            if (email.isNotEmpty() ){
                model.forgetPassword(email)
            }
        }
    }

    private fun setupObservers() {
        model.isForget().observe(viewLifecycleOwner){
            if (it != null) {
                if (it.status){
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_lupaPasswordFragment_to_emailConfirmFragment)
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = findNavController().currentDestination?.id

                if (currentDestinationId == R.id.lupaPasswordFragment) {
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}