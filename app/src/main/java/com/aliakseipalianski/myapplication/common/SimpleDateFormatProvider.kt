package com.aliakseipalianski.myapplication.common

import java.text.SimpleDateFormat

object DateFormatProvider {
    fun createYYYYMMDD() = SimpleDateFormat("yyyy.MM.dd")
}