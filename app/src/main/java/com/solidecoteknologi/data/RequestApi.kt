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
    val weight: Int,

    @field:SerializedName("category")
    val category: Int

) : Parcelable