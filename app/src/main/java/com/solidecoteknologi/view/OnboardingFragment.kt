package com.solidecoteknologi.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentOnboardingBinding
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding : FragmentOnboardingBinding? = null
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
        _binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.getStatus().observe(viewLifecycleOwner){
            if (it == false) {
                model.getToken().observe(viewLifecycleOwner){ token ->
                    if (token != null && token.isNotBlank()){
                        model.getProfile(token)
                    } else {
                        findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                    }
                    Log.i("TAG", "TOKEN: $token")
                }
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            }
        }

        model.profile().observe(viewLifecycleOwner){ profile ->
            if (profile != null){
                if (!profile.success){
                    findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                }
            }
        }



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
            return@addCallback
        }
    }
}