package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseCategory(

	@field:SerializedName("data")
	val data: List<DataCategoryItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataCategoryItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
