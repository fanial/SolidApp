package com.solidecoteknologi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.solidecoteknologi.R
import com.solidecoteknologi.databinding.FragmentEmailConfirmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailConfirmFragment : Fragment() {

    private var _binding : FragmentEmailConfirmBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentEmailConfirmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupBackHandler()
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_emailConfirmFragment_to_loginFragment)
        }
    }
    private fun setupBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = findNavController().currentDestination?.id

                if (currentDestinationId == R.id.emailConfirmFragment) {
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}