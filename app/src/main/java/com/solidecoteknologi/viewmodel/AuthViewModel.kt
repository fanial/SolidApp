package com.solidecoteknologi.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.solidecoteknologi.data.RequestForgetPass
import com.solidecoteknologi.data.RequestLogin
import com.solidecoteknologi.data.RequestRegister
import com.solidecoteknologi.data.ResponseLogin
import com.solidecoteknologi.data.ResponseOrganization
import com.solidecoteknologi.data.ResponseProfile
import com.solidecoteknologi.data.ResponseRefreshToken
import com.solidecoteknologi.data.ResponseRegister
import com.solidecoteknologi.data.ResponseResult
import com.solidecoteknologi.data.ResponseUpdateProfile
import com.solidecoteknologi.network.DataStoreManager
import com.solidecoteknologi.network.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
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
    fun saveRoleAccount(role : String){
        viewModelScope.launch {
            store.saveRoleAccount(role)
        }
    }

    fun  getStoredAccount() = store.getStoredValues().asLiveData()
    fun getToken() = store.getToken().asLiveData()

    fun getRoleAccount() = store.getRoleAccount().asLiveData()

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
            if (body != null && body.success){
                dataLogin.postValue(body)
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataLogin.postValue(null)
                val msg = body!!.message
                errorMessage.value = msg
                Log.e(ContentValues.TAG, "onResponse: Data Response NULL")
            }
        } else {
            val errorJson = response.errorBody()?.string()
            val errorObj = errorJson?.let { JSONObject(it) }
            val msg400 = errorObj?.getString("error")
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

    //Register
    private val dataRegister : MutableLiveData<ResponseRegister?> = MutableLiveData()
    fun dataRegister() : MutableLiveData<ResponseRegister?> {
        return dataRegister
    }
    fun register(name : String, email : String, password : String, passValidation : String, role : String, organization : String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val data = RequestRegister(name, email, password, passValidation, role, organization)
                val response = service.register(RequestRegister(name, email, password, passValidation, role, organization))
                handleRegisterResponse(response)
                Log.i("TAG", "register: $data")
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
                    errorMessage.value = msg
                }
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataRegister.postValue(null)
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

    //Organization
    private val dataOrganization : MutableLiveData<ResponseOrganization?> = MutableLiveData()
    fun dataOrganization() : MutableLiveData<ResponseOrganization?> {
        return dataOrganization
    }
    fun listOrganization() {
        viewModelScope.launch {
            try {
                val response = service.organization()
                handleListOrganizationResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }
    private fun handleListOrganizationResponse(response: Response<ResponseOrganization>) {
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.success){
                    dataOrganization.postValue(body)
                } else {
                    val msg = body.message
                    errorMessage.value = "Error: $msg"
                }
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                dataOrganization.postValue(null)
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
                if (body.status){
                    logout.value = true
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    logout.value = false
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                }
            } else {
                logout.value = false
                message.value = null
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

    //Resfresh Token
    private val refreshToken = MutableLiveData<ResponseRefreshToken?>()
    fun refreshToken(): LiveData<ResponseRefreshToken?> = refreshToken
    fun refreshToken(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.refreshToken(token)
                handleRefreshTokenResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleRefreshTokenResponse(response: Response<ResponseRefreshToken>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                refreshToken.value = body
                Log.i(ContentValues.TAG, "onResponse: Success Load Response")
            } else {
                refreshToken.value = null
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

    //Forget Password
    private val isForget = MutableLiveData<ResponseResult?>()
    fun isForget(): LiveData<ResponseResult?> = isForget
    fun forgetPassword(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.forgetPass(RequestForgetPass(email))
                handleForgetPassResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleForgetPassResponse(response: Response<ResponseResult>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.status){
                    isForget.value = body
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    isForget.value = body
                }
            } else {
                isForget.value = null
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

    //Update Profile
    private val updateProfile = MutableLiveData<ResponseUpdateProfile?>()
    fun updateProfile(): LiveData<ResponseUpdateProfile?> = updateProfile
    fun updateProfile(token: String, accountId: RequestBody, name: RequestBody?, avatar: MultipartBody.Part?, organization: RequestBody?, password: RequestBody?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.updateProfile(
                    token,
                    accountId,
                    name,
                    avatar,
                    organization,
                    password // Return if password is null
                )
                handleUpdateProfileResponse(response)
            } catch (t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleUpdateProfileResponse(response: Response<ResponseUpdateProfile>) {
        _loading.value = false
        val body = response.body()
        if (response.isSuccessful){
            if (body != null){
                if (body.success){
                    updateProfile.value = body
                    message.value = body.message
                    Log.i(ContentValues.TAG, "onResponse: Success Load Response")
                } else {
                    updateProfile.value = null
                    message.value = body.message
                }
            } else {
                isForget.value = null
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