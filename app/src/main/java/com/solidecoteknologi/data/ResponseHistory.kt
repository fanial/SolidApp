package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseHistory(

	@field:SerializedName("data")
	val data: List<DataHistoryItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Category(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)

data class DataHistoryItem(

	@field:SerializedName("account_id")
	val accountId: String,

	@field:SerializedName("category_id")
	val categoryId: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("organization_id")
	val organizationId: String,

	@field:SerializedName("weight")
	val weight: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: Category
)
