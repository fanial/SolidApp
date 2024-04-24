package com.solidecoteknologi

import com.google.gson.annotations.SerializedName

data class ResponseMonthlyReport(

	@field:SerializedName("total_carbon")
	val totalCarbon: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("carbon")
	val carbon: Int,

	@field:SerializedName("category")
	val category: String
)
