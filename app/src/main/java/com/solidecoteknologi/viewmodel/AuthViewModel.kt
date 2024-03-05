package com.solidecoteknologi.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.solidecoteknologi.data.RequestLogin
import com.solidecoteknologi.data.RequestRegister
import com.solidecoteknologi.data.ResponseLogin
import com.solidecoteknologi.data.ResponseOrganization
import com.solidecoteknologi.data.ResponseProfile
import com.solidecoteknologi.data.ResponseRegister
import com.solidecoteknologi.data.ResponseResult
import com.solidecoteknologi.network.DataStoreManager
import com.solidecoteknologi.network.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val store: DataStoreManager, private val service: Service): ViewModel() {

    //Data Store
    fun setToken(token: String, idAcc: String, status: Boolean){
        viewModelScope.launch {
            store.saveDataStore(token, idAcc, status)
        }
    }

    fun  getStoredAccount() = store.getStoredValues().asLiveData()
    fun getToken() = store.getToken().asLiveData()

    fun getName() = store.getIdAccount().asLiveData()

    fun getStatus()= store.getStatus().asLiveData()
    fun logout(){
        viewModelScope.launch { store.delete() }
    }

    //Live Data

    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    private val message : MutableLiveData<String?> = MutableLiveData()
    fun messageObserver(): LiveData<String?> = message

    private val errorMessage : MutableLiveData<String?> = MutableLiveData()
    fun errorMessageObserver(): LiveData<String?> = errorMessage

    //API

    //Login
    private val dataLogin : MutableLiveData<ResponseLogin?> = MutableLiveData()
    fun dataLogin() : MutableLiveData<ResponseLogin?> {
        return dataLogin
    }
    fun login(email : String, password : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.login(RequestLogin(email, password))
                handleLoginResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleLoginResponse(response: Response<ResponseLogin>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataLogin.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataLogin.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            dataLogin.postValue(null)
            val errorCode = response.code()
            val msg = "Opsss! Something Wrong"
            errorMessage.value = "$errorCode: $msg"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    //Register
    private val dataRegister : MutableLiveData<ResponseRegister?> = MutableLiveData()
    fun dataRegister() : MutableLiveData<ResponseRegister?> {
        return dataRegister
    }
    fun register(name : String, email : String, password : String, passValidation : String, role : String, organization : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.register(RequestRegister(name, email, password, passValidation, role, organization))
                handleRegisterResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleRegisterResponse(response: Response<ResponseRegister>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.success){
                    dataRegister.postValue(body)
                } else {
                    val msg = body.message
                    errorMessage.value = "Error: $msg"
                }
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataRegister.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            val errorCode = response.code()
            val msg = "Opsss! Something Wrong"
            errorMessage.value = "Error $errorCode : $msg"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    //Organization
    private val dataOrganization : MutableLiveData<ResponseOrganization?> = MutableLiveData()
    fun dataOrganization() : MutableLiveData<ResponseOrganization?> {
        return dataOrganization
    }
    fun listOrganization() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.organization()
                handleListOrganizationResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleListOrganizationResponse(response: Response<ResponseOrganization>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataOrganization.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataOrganization.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            val errorCode = response.code()
            val msg = "Opsss! Something Wrong"
            errorMessage.value = "Error $errorCode: $msg"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    //Logout
    private val logout = MutableLiveData<Boolean>()
    fun isLogout(): LiveData<Boolean> = logout
    fun logout(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.logout(token)
                handleLogoutResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleLogoutResponse(response: Response<ResponseResult>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                logout.value = true
                message.value = body.message
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                logout.value = false
                message.value = null
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            val errorCode = response.code()
            val msg = "Opsss! Something Wrong"
            errorMessage.value = "Error $errorCode: $msg"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    //Profile
    private val profile = MutableLiveData<ResponseProfile?>()
    fun profile(): LiveData<ResponseProfile?> = profile
    fun getProfile(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.profile(token)
                handleProfileResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleProfileResponse(response: Response<ResponseProfile>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                profile.value = body
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                profile.value = null
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            val errorCode = response.code()
            val msg = "Opsss! Something Wrong"
            errorMessage.value = "Error $errorCode: $msg"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    //Handle Failure
    private fun handleFailure(t: Throwable) {
        message.value = when (t) {
            is SocketTimeoutException -> "Connection Timeout"
            else -> "Failure Connect to Server"
        }
        _loading.value = false
        Log.e("Response Error", "onFailure: ${t.message}")
    }

}