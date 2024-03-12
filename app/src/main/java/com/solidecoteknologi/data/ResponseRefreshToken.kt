package com.solidecoteknologi.data

import com.google.gson.annotations.SerializedName

data class ResponseRefreshToken(

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("data")
	val data: DataRefreshTokenItem,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("expires_in")
	val expiresIn: Int,

	@field:SerializedName("token")
	val token: String
)

data class DataRefreshTokenItem(

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
