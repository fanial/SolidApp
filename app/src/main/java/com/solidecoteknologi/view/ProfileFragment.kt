package com.solidecoteknologi.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataItem
import com.solidecoteknologi.databinding.FragmentProfileBinding
import com.solidecoteknologi.utils.UriToFile
import com.solidecoteknologi.utils.bitmapToUri
import com.solidecoteknologi.utils.loadImage
import com.solidecoteknologi.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val model : AuthViewModel by viewModels()
    private var token = ""
    private var photoProfile: File? = null
    private var instansi = ""
    private var idAccount = ""
    private  var listInstansi = listOf<DataItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListener()
        setupViews()
        setupBackButtonHandler()
    }

    private fun setupViews() {
        model.listOrganization()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupListener() {

        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
        binding.report.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_reportFragment)
        }
        binding.btnLogout.setOnClickListener {
            model.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
        binding.history.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_historyFragment)
        }

        binding.cardProfile.setOnClickListener {
            checkAndRequestPermission()
        }

        binding.edInstansi.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                instansi = listInstansi[position].id.toString()
                binding.layoutInstansi.error = null
                hideKeyboard()
                binding.layoutInstansi.clearFocus()
                Log.i("TAG", "ROLE: $instansi")
            }

        binding.btnUpdateProfile.setOnClickListener {
            val name = binding.edNama.text.toString()
            val organization = instansi
            val password = binding.edPassword.text.toString()
            if (photoProfile ==  null){
                model.updateProfile(
                    token,
                    idAccount.toRequestBody("text/plain".toMediaTypeOrNull()),
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    null,
                    organization.toRequestBody("text/plain".toMediaTypeOrNull()),
                    password.toRequestBody("text/plain".toMediaTypeOrNull())
                )
            } else if (password.isEmpty()){
                val avatar = photoProfile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
                    "avatar",
                    photoProfile?.name,
                    avatar
                )

                model.updateProfile(
                    token,
                    idAccount.toRequestBody("text/plain".toMediaTypeOrNull()),
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    imageMultipart,
                    organization.toRequestBody("text/plain".toMediaTypeOrNull()),
                    null
                )
            } else {
                val avatar = photoProfile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
                    "avatar",
                    photoProfile?.name,
                    avatar
                )

                model.updateProfile(
                    token,
                    idAccount.toRequestBody("text/plain".toMediaTypeOrNull()),
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    imageMultipart,
                    organization.toRequestBody("text/plain".toMediaTypeOrNull()),
                    password.toRequestBody("text/plain".toMediaTypeOrNull())
                )

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES
                ),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            pickImageFromGalleryOrCamera()
        }
    }

    private fun logout() {
        model.logout(token)
    }

    private fun setupObservers() {

        model.isLoading().observe(viewLifecycleOwner){
            loading(it)
        }

        model.messageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        model.errorMessageObserver().observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                Snackbar.make(binding.root,msg , Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        model.updateProfile().observe(viewLifecycleOwner){
            if (it != null){
                if (it.success){
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
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

        model.getStoredAccount().observe(viewLifecycleOwner){ data ->
            if (data != null) {
                token = data.token
                idAccount = data.idAccount
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
                if (it.success){
                    val data = it.data
                    binding.edNama.setText(data.name)
                    binding.edEmail.setText(data.email)
                    binding.edRole.setText(data.role)
                    binding.edInstansi.setText(data.organization.name)
                    instansi = data.organization.id.toString()
                    if (data.avatar != null){
                        loadImage(requireContext(), data.avatar, binding.photoProfile)
                    }
                    if (data.emailVerifiedAt == null){
                        binding.emailStatus.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun pickImageFromGalleryOrCamera() {
        val options = arrayOf<CharSequence>("Dari Kamera", "Pilih dari Galeri", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Dari Kamera" -> {
                    startCameraIntent()
                }
                options[item] == "Pilih dari Galeri" -> {
                    startGalleryIntent()
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent,
            REQUEST_IMAGE_CAPTURE)
    }

    private fun startGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)

        galleryIntent.type = "image/*"

        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                when (requestCode) {
                    REQUEST_IMAGE_CAPTURE -> {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        binding.photoProfile.setImageBitmap(imageBitmap)
                        val uri = bitmapToUri(requireContext(), imageBitmap)
                        photoProfile = UriToFile(requireContext()).getImageBody(uri!!)
                        Log.i(ContentValues.TAG, "URI CAMERA: $uri")
                    }

                    REQUEST_PICK_IMAGE -> {
                        val selectedImageUri = data?.data
                        if (selectedImageUri != null) {
                            binding.photoProfile.setImageURI(selectedImageUri)
                            lifecycleScope.launch {
                                photoProfile = UriToFile(requireContext()).getImageBody(selectedImageUri)
                                Log.i("TAG", "FOTO: $photoProfile")
                            }
                        }
                    }
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



    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edInstansi.windowToken, 0)
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS  = 101
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_PICK_IMAGE = 2
    }

}