package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseMessage(

	@field:SerializedName("message")
	val message: String
)

data class ResponseRefreshToken(

	@field:SerializedName("status")
	val status: String
)