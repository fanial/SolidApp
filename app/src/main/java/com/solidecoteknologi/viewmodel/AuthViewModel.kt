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
import com.solidecoteknologi.data.ResponseRegister
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
    fun setToken(token: String, name: String, status: Boolean){
        viewModelScope.launch {
            store.saveDataStore(token, name, status)
        }
    }

    fun getToken() = store.getToken().asLiveData()

    fun getName() = store.getName().asLiveData()

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

    //Login
    private val dataRegister : MutableLiveData<ResponseRegister?> = MutableLiveData()
    fun dataRegister() : MutableLiveData<ResponseRegister?> {
        return dataRegister
    }
    fun register(name : String, email : String, password : String, passValidation : String, role : String, organization : Int) {
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
                dataRegister.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataRegister.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            // Handle error response
            val errorCode = response.code()
            val msg = response.message()
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