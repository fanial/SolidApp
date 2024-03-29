package com.solidecoteknologi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestLogin(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String

) : Parcelable

@Parcelize
data class RequestRegister(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("password_confirmation")
    val passwordConfirmation: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("organization")
    val organization: String
) : Parcelable

@Parcelize
data class RequestStoreWaste(
    @field:SerializedName("account")
    val account: Int,

    @field:SerializedName("weight")
    val weight: Float,

    @field:SerializedName("category")
    val category: Int

) : Parcelable

@Parcelize
data class RequestMonthly(
    @field:SerializedName("start_date")
    val startDate: String,

    @field:SerializedName("end_date")
    val endDate: String

) : Parcelable

@Parcelize
data class RequestDaily(
    @field:SerializedName("date")
    val date: String,
) : Parcelable

@Parcelize
data class RequestForgetPass(
    @field:SerializedName("email")
    val email: String,
) : Parcelable

@Parcelize
data class RequestUpdate(
    @field:SerializedName("account_id")
    val accountId: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("organization")
    val organization: String
) : Parcelable
