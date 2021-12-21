package com.iteach.taxi.fragment.login.base

import com.google.gson.annotations.SerializedName

data class LoginModel(
	@field:SerializedName("type_id")
	val type_id: Int? = null,

	@field:SerializedName("last_name")
	var last_name: String? = null,

	@field:SerializedName("password_reset_token")
	val password_reset_token: String? = null,

	@field:SerializedName("auth_key")
	val auth_key: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("first_name")
	var first_Name: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
