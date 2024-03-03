package com.solidecoteknologi.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.solidecoteknologi.data.RequestRegister
import com.solidecoteknologi.data.RequestStoreWaste
import com.solidecoteknologi.data.ResponseCategory
import com.solidecoteknologi.data.ResponseRegister
import com.solidecoteknologi.data.ResponseStoreWaste
import com.solidecoteknologi.network.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val service: Service): ViewModel() {

    //Live Data
    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    private val message : MutableLiveData<String?> = MutableLiveData()
    fun messageObserver(): LiveData<String?> = message

    private val errorMessage : MutableLiveData<String?> = MutableLiveData()
    fun errorMessageObserver(): LiveData<String?> = errorMessage

    //API

    //Store Waste
    private val dataWaste : MutableLiveData<ResponseStoreWaste?> = MutableLiveData()
    fun dataWaste() : MutableLiveData<ResponseStoreWaste?> {
        return dataWaste
    }
    fun storeWaste(token : String, idAcc: Int, weight : Int, category : Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.store(token, RequestStoreWaste(idAcc, weight, category))
                handleStoreWasteResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleStoreWasteResponse(response: Response<ResponseStoreWaste>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataWaste.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataWaste.postValue(null)
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


    //Category
    private val dataCategory : MutableLiveData<ResponseCategory?> = MutableLiveData()
    fun dataCategory() : MutableLiveData<ResponseCategory?> {
        return dataCategory
    }
    fun listCategory() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.category()
                handleCategoryResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleCategoryResponse(response: Response<ResponseCategory>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataCategory.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataCategory.postValue(null)
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