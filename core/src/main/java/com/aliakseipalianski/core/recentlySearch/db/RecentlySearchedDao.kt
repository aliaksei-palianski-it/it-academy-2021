package com.aliakseipalianski.core.recentlySearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RecentlySearchedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recentlySearchedItemList: RecentlySearchedItem)

    @Query("SELECT * FROM recently_searched ORDER BY timestamp DESC")
    fun getAll(): List<RecentlySearchedItem>

    @Query("DELETE FROM recently_searched")
    fun deleteAll()
}