package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("success")
    val status: Boolean,
)

data class DataItemDetailReport(

    @field:SerializedName("amount")
    val amount: Float,

    @field:SerializedName("carbon")
    val carbon: Float,

    @field:SerializedName("category")
    val category: String
)