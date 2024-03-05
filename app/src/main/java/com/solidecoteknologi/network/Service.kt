package com.solidecoteknologi.network

import com.solidecoteknologi.data.RequestLogin
import com.solidecoteknologi.data.RequestRegister
import com.solidecoteknologi.data.RequestStoreWaste
import com.solidecoteknologi.data.ResponseCategory
import com.solidecoteknologi.data.ResponseLogin
import com.solidecoteknologi.data.ResponseOrganization
import com.solidecoteknologi.data.ResponseProfile
import com.solidecoteknologi.data.ResponseRegister
import com.solidecoteknologi.data.ResponseResult
import com.solidecoteknologi.data.ResponseStoreWaste
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Service {
    @POST("login")
    suspend fun login(
        @Body dataLogin : RequestLogin
    ) : Response<ResponseLogin>

    @POST("register")
    suspend fun register(
        @Body dataRegister : RequestRegister
    ) : Response<ResponseRegister>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token : String
    ) : Response<ResponseResult>

    @GET("account-profile")
    suspend fun profile(
        @Header("Authorization") token : String
    ) : Response<ResponseProfile>

    @GET("category")
    suspend fun category() : Response<ResponseCategory>

    @GET("organization")
    suspend fun organization(
    ) : Response<ResponseOrganization>

//    Transaction End Point
    @POST("transaction/store")
    suspend fun store(
        @Header("Authorization") token : String,
        @Body dataStoreWaste : RequestStoreWaste
    ) : Response<ResponseStoreWaste>
}

