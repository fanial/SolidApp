package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseDailyReport(

	@field:SerializedName("total_carbon")
	val totalCarbon: Float,

	@field:SerializedName("total_volume")
	val totalVolume: Float,

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

	@field:SerializedName("carbon")
	val carbon: Float,

	@field:SerializedName("category")
	val category: String
)
