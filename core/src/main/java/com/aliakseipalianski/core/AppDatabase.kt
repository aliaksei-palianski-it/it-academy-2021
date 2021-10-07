package com.aliakseipalianski.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aliakseipalianski.core.recentlySearch.db.RecentlySearchedDao
import com.aliakseipalianski.core.recentlySearch.db.RecentlySearchedItem

@Database(entities = [RecentlySearchedItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecentlySearchedDao(): RecentlySearchedDao
}