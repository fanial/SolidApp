package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseDailyReport(

	@field:SerializedName("data")
	val data: List<DataDailyItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataDailyItem(

	@field:SerializedName("amount")
	val amount: Float,

	@field:SerializedName("category")
	val category: String
)
