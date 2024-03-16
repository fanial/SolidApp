package com.solidecoteknologi.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solidecoteknologi.data.RequestDaily
import com.solidecoteknologi.data.RequestMonthly
import com.solidecoteknologi.data.RequestStoreWaste
import com.solidecoteknologi.data.ResponseCategory
import com.solidecoteknologi.data.ResponseDailyReport
import com.solidecoteknologi.data.ResponseMonthlyReport
import com.solidecoteknologi.data.ResponseStoreWaste
import com.solidecoteknologi.network.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.Date
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
    fun storeWaste(token : String, idAcc: Int, weight : Float, category : Int) {
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
                if (body.success){
                    dataWaste.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataWaste.postValue(null)
                    message.postValue(body.message)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                }

            } else {
                dataWaste.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            val errorJson = response.errorBody()?.string()
            val errorObj = errorJson?.let { JSONObject(it) }
            val msg400 = errorObj?.getString("message")
            val msg500 = errorObj?.getString("error")
            val errorCode = response.code()
            Log.e(ContentValues.TAG, "$errorMessage")
            when (response.code()) {
                in 400..499 -> errorMessage.value = "$errorCode: $msg400"
                in 500..599 -> errorMessage.value = "$errorCode: $msg500"
                else -> errorMessage.value = "Error: Unknown error occurred"

            }
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
            val errorJson = response.errorBody()?.string()
            val errorObj = errorJson?.let { JSONObject(it) }
            val msg400 = errorObj?.getString("message")
            val msg500 = errorObj?.getString("error")
            val errorCode = response.code()
            Log.e(ContentValues.TAG, "$errorMessage")
            when (response.code()) {
                in 400..499 -> errorMessage.value = "$errorCode: $msg400"
                in 500..599 -> errorMessage.value = "$errorCode: $msg500"
                else -> errorMessage.value = "Error: Unknown error occurred"

            }
        }
    }


    //Daily
    private val dataDaily : MutableLiveData<ResponseDailyReport?> = MutableLiveData()
    fun dataDaily() : MutableLiveData<ResponseDailyReport?> {
        return dataDaily
    }
    fun dailyReport(token: String, date : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.daily(token, RequestDaily(date))
                handleDailyResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleDailyResponse(response: Response<ResponseDailyReport>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataDaily.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataDaily.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            val errorJson = response.errorBody()?.string()
            val errorObj = errorJson?.let { JSONObject(it) }
            val msg400 = errorObj?.getString("message")
            val msg500 = errorObj?.getString("error")
            val errorCode = response.code()
            Log.e(ContentValues.TAG, "$errorMessage")
            when (response.code()) {
                in 400..499 -> errorMessage.value = "$errorCode: $msg400"
                in 500..599 -> errorMessage.value = "$errorCode: $msg500"
                else -> errorMessage.value = "Error: Unknown error occurred"

            }
        }
    }

    //Monthly
    private val dataMonthly : MutableLiveData<ResponseMonthlyReport?> = MutableLiveData()
    fun dataMonthly() : MutableLiveData<ResponseMonthlyReport?> {
        return dataMonthly
    }
    fun monthlyReport(token: String, startDate : String, endDate : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.monthly(token, RequestMonthly(startDate, endDate))
                handleMonthlyResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleMonthlyResponse(response: Response<ResponseMonthlyReport>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                dataMonthly.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataMonthly.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            val errorJson = response.errorBody()?.string()
            val errorObj = errorJson?.let { JSONObject(it) }
            val msg400 = errorObj?.getString("message")
            val msg500 = errorObj?.getString("error")
            val errorCode = response.code()
            Log.e(ContentValues.TAG, "$errorMessage")
            when (response.code()) {
                in 400..499 -> errorMessage.value = "$errorCode: $msg400"
                in 500..599 -> errorMessage.value = "$errorCode: $msg500"
                else -> errorMessage.value = "Error: Unknown error occurred"

            }
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