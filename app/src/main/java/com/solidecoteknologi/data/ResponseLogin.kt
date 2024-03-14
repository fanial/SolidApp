package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("expires_in")
	val expiresIn: Int,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("error")
	val error: String
)

data class DataLogin(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("organization_id")
	val organizationId: String,

	@field:SerializedName("organization")
	val organization: Organization,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)

data class OrganizationLogin(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
