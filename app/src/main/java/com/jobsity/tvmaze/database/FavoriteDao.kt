package com.jobsity.tvmaze.database

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(favoriteList: FavoriteList?)

    @Query("select * from favorites ORDER BY name")
    suspend fun favoriteData(): List<FavoriteList?>?

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE id=:id)")
    fun isFavorite(id: Int): Int

    @Delete
    fun delete(favoriteList: FavoriteList?)
}