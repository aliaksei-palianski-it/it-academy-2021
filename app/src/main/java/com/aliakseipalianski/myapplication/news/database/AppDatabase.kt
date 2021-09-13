package com.aliakseipalianski.myapplication.news.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentlySearchedItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecentlySearchedDao(): RecentlySearchedDao
}