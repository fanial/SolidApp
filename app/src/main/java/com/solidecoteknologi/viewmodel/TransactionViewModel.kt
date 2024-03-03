package com.solidecoteknologi.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solidecoteknologi.network.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

@HiltViewModel
class TransactionViewModel(private val service: Service): ViewModel() {

    //Live Data
    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    private val message : MutableLiveData<String?> = MutableLiveData()
    fun messageObserver(): LiveData<String?> = message

    private val errorMessage : MutableLiveData<String?> = MutableLiveData()
    fun errorMessageObserver(): LiveData<String?> = errorMessage

    // TODO: Setup View Model for endpoint transaction
    //API
    /*//ketentuan
    private val ketentuan : MutableLiveData<ResponseKetentuan?> = MutableLiveData()
    fun ketentuan() : MutableLiveData<ResponseKetentuan?> {
        return ketentuan
    }
    fun ketentuan(token: String, ketentuan : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.ketentuan(token, ketentuan)
                handleKetentuanResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleKetentuanResponse(response: Response<ResponseKetentuan>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if ((body != null) && body.result){
                message.value = body.message
                ketentuan.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Ketentuan")
            } else {
                ketentuan.postValue(null)
                val error = body?.message
                message.value = error
                Log.e(ContentValues.TAG, "onResponse: Data Ketentuan NULL")
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
            is SocketTimeoutException -> "Connection timeout"
            else -> "Kesalahan Server"
        }
        _loading.value = false
        Log.e("Response Error", "onFailure: ${t.message}")
    }
*/
}