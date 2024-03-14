package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentProfileBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val model : AuthViewModel by viewModels()
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: update profile 
        // TODO: button update pindah ke bawah 
        setupObservers()
        setupListener()
        setupViews()
        setupBackButtonHandler()
    }

    private fun setupViews() {

    }

    private fun setupListener() {
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
        binding.report.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_reportFragment)
        }
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        model.logout()
        model.logout(token)
    }

    private fun setupObservers() {
        model.getStoredAccount().observe(viewLifecycleOwner){ data ->
            if (data != null) {
                token = data.token
                model.getProfile(token)
            }
        }

        model.isLoading().observe(viewLifecycleOwner){
            if (it == true){
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.loadingBar.visibility = View.INVISIBLE
            }
        }

        model.profile().observe(viewLifecycleOwner){
            if (it != null){
                val data = it.data
                binding.edNama.setText(data.name)
                binding.edEmail.setText(data.email)
                binding.edPassword.setText(data.role)
                binding.edInstansi.setText(data.organization.name)

                if (it.status == "Token is Expired" || it.status == "Token is Invalid"){
                    model.logout()
                    findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                }
            }
        }

        model.isLogout().observe(viewLifecycleOwner){
            if (it == true){
                model.logout()
                findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
            }
        }
    }

    private fun setupBackButtonHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = findNavController().currentDestination?.id

                if (currentDestinationId == R.id.profileFragment) {
                    findNavController().navigate(R.id.homeFragment)
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

}