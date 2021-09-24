package com.aliakseipalianski.myapplication.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedItem

@Database(entities = [RecentlySearchedItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecentlySearchedDao(): RecentlySearchedDao
}