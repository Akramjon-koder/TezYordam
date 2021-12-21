package com.iteach.tezyordam.base

data class BaseResponse<T> (
    val success :Boolean,
    val message :String,
    val data :T)