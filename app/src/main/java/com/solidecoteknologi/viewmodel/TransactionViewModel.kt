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
import com.solidecoteknologi.data.ResponseHistory
import com.solidecoteknologi.data.ResponseMonthlyReport
import com.solidecoteknologi.data.ResponsePresentaseError
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
            errorMessage.value = "${response.code()}: ${response.message()}"
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
                if (body.success){
                    dataCategory.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataCategory.postValue(null)
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Failed Load Response")
                }
            } else {
                dataCategory.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            errorMessage.value = "${response.code()}: ${response.message()}"
            Log.e(ContentValues.TAG, "$errorMessage")
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
                if (body.success){
                    dataDaily.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataDaily.postValue(null)
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Failed Load Response")
                }
            } else {
                dataDaily.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            errorMessage.value = "${response.code()}: ${response.message()}"
            Log.e(ContentValues.TAG, "$errorMessage")
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
                if (body.success){
                    dataMonthly.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataMonthly.postValue(null)
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Failed Load Response")
                }
            } else {
                dataMonthly.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            errorMessage.value = "${response.code()}: ${response.message()}"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }

    private val dataHistory : MutableLiveData<ResponseHistory?> = MutableLiveData()
    fun dataHistory() : MutableLiveData<ResponseHistory?> {
        return dataHistory
    }
    fun historyList(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.history(token)
                handleHistoryResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleHistoryResponse(response: Response<ResponseHistory>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.success){
                    dataHistory.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataHistory.postValue(null)
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Failed Load Response")
                }
            } else {
                dataHistory.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            errorMessage.value = "${response.code()}: ${response.message()}"
            Log.e(ContentValues.TAG, "$errorMessage")
        }
    }


    private val dataPresentaseError : MutableLiveData<ResponsePresentaseError?> = MutableLiveData()
    fun dataPresentaseError() : MutableLiveData<ResponsePresentaseError?> {
        return dataPresentaseError
    }
    fun presentaseError(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.percentageError(token)
                handlePercentageErrorResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handlePercentageErrorResponse(response: Response<ResponsePresentaseError>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.success){
                    dataPresentaseError.postValue(body)
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    dataPresentaseError.postValue(null)
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Failed Load Response")
                }
            } else {
                dataPresentaseError.postValue(null)
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            errorMessage.value = "${response.code()}: ${response.message()}"
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