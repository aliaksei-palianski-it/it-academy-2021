package com.aliakseipalianski.core

import android.content.Context
import androidx.room.Room
import com.aliakseipalianski.core.recentlySearch.db.MIGRATION_1_2


private const val APP_DATABASE = "APP_DATABASE"

object AppDatabaseProvider {
    fun createAppDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE)
            .addMigrations(MIGRATION_1_2)
            .build()
}