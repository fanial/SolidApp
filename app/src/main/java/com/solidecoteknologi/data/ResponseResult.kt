package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("success")
    val status: Boolean,
)