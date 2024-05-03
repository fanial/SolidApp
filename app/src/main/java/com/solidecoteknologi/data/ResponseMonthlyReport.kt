package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseMonthlyReport(

	@field:SerializedName("total_volume")
	val totalAmount: Float,

	@field:SerializedName("total_carbon")
	val totalCarbon: Float,

	@field:SerializedName("data")
	val data: List<DataMonthlyItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataMonthlyItem(

	@field:SerializedName("amount")
	val amount: Float,

	@field:SerializedName("carbon")
	val carbon: Float,

	@field:SerializedName("category")
	val category: String
)
