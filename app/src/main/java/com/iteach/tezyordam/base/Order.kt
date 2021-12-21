package com.iteach.tezyordam.base

data class Order(
    val complaint: String = "",
    val phone: String = "",
    val date: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val condition: Int = 0,
)