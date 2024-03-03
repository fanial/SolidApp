package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseOrganization(

	@field:SerializedName("ResponseOrganization")
	val responseOrganization: List<ResponseOrganizationItem>
)

data class ResponseOrganizationItem(

	@field:SerializedName("address")
	val address: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
