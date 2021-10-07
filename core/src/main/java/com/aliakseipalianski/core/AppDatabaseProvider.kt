package com.aliakseipalianski.core

import android.content.Context
import androidx.room.Room


private const val APP_DATABASE = "APP_DATABASE"

object AppDatabaseProvider {
    fun createAppDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE).build()
}