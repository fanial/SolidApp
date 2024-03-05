package com.solidecoteknologi.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var doubleBackToExitPressedOnce = false

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

        model.getStatus().observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            } else {
                binding.btnStart.visibility = View.VISIBLE
            }
        }

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return@addCallback
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(requireContext(),
                getString(R.string.tekan_lagi_untuk_keluar), Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }
    companion object {

    }
}