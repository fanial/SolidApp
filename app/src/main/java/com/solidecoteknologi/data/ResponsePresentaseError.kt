package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponsePresentaseError(

	@field:SerializedName("data")
	val data: Any,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
