package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseMessage(

	@field:SerializedName("message")
	val message: String
)
