package com.aliakseipalianski.myapplication.news.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "recently_searched")
data class RecentlySearchedItem(
    @PrimaryKey @ColumnInfo(name = "_id") val id: String,
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "timestamp") val timestamp: Int
)

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE new_recently_searched (
                    _id TEXT PRIMARY KEY NOT NULL,
                    query TEXT NOT NULL,
                    timestamp INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """.trimIndent()
        )
        database.execSQL(
            """
                INSERT INTO new_recently_searched (_id, query)
                SELECT CAST(_id AS TEXT), query FROM recently_searched
                """.trimIndent()
        )
        database.execSQL("DROP TABLE recently_searched")
        database.execSQL("ALTER TABLE new_recently_searched RENAME TO recently_searched")
    }
}
