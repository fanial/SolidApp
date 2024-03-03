package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseRegister(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("account")
	val account: Account
)

data class Account(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("organization_id")
	val organizationId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)
