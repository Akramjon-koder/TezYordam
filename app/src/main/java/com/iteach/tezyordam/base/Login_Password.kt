package com.iteach.taxi.fragment.login.base

import com.google.gson.annotations.SerializedName

data class Login_Password(
	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("password")
	val password: String? = null
	)
