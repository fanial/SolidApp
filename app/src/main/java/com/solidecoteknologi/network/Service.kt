package com.solidecoteknologi.network

import com.solidecoteknologi.data.RequestDaily
import com.solidecoteknologi.data.RequestForgetPass
import com.solidecoteknologi.data.RequestLogin
import com.solidecoteknologi.data.RequestMonthly
import com.solidecoteknologi.data.RequestRegister
import com.solidecoteknologi.data.RequestStoreWaste
import com.solidecoteknologi.data.ResponseCategory
import com.solidecoteknologi.data.ResponseDailyReport
import com.solidecoteknologi.data.ResponseHistory
import com.solidecoteknologi.data.ResponseLogin
import com.solidecoteknologi.data.ResponseMonthlyReport
import com.solidecoteknologi.data.ResponseOrganization
import com.solidecoteknologi.data.ResponsePresentaseError
import com.solidecoteknologi.data.ResponseProfile
import com.solidecoteknologi.data.ResponseRefreshToken
import com.solidecoteknologi.data.ResponseRegister
import com.solidecoteknologi.data.ResponseResult
import com.solidecoteknologi.data.ResponseStoreWaste
import com.solidecoteknologi.data.ResponseUpdateProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Service {
    @POST("login")
    suspend fun login(
        @Body dataLogin: RequestLogin
    ): Response<ResponseLogin>

    @POST("register")
    suspend fun register(
        @Body dataRegister: RequestRegister
    ): Response<ResponseRegister>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ResponseResult>

    @POST("refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<ResponseRefreshToken>


    @GET("account-profile")
    suspend fun profile(
        @Header("Authorization") token: String
    ): Response<ResponseProfile>

    @GET("category")
    suspend fun category(): Response<ResponseCategory>

    @GET("organization")
    suspend fun organization(
    ): Response<ResponseOrganization>

    @POST("transaction/report_monthly")
    suspend fun monthly(
        @Header("Authorization") token: String,
        @Body dataMonthly: RequestMonthly
    ): Response<ResponseMonthlyReport>

    @POST("transaction/report_daily")
    suspend fun daily(
        @Header("Authorization") token: String,
        @Body date: RequestDaily
    ): Response<ResponseDailyReport>

    //    Transaction End Point
    @POST("transaction/store")
    suspend fun store(
        @Header("Authorization") token: String,
        @Body dataStoreWaste: RequestStoreWaste
    ): Response<ResponseStoreWaste>

    @POST("forgot-password")
    suspend fun forgetPass(
        @Body emailForgetPass: RequestForgetPass
    ): Response<ResponseResult>

    @POST("update-profile")
    @Multipart
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("account_id") accId: RequestBody,
        @Part("name") name: RequestBody?,
        @Part avatar: MultipartBody.Part?,
        @Part("organization") organization: RequestBody?,
        @Part("password") passwordBaru: RequestBody?,
    ): Response<ResponseUpdateProfile>

    @GET("transaction/list")
    suspend fun history(
        @Header("Authorization") token: String
    ): Response<ResponseHistory>

    @GET("transaction/percentage_error")
    suspend fun percentageError(
        @Header("Authorization") token: String
    ): Response<ResponsePresentaseError>

}

