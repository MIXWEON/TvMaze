package com.jobsity.tvmaze.database

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.jobsity.tvmaze.network.ImageType
import com.jobsity.tvmaze.network.Schedule


@Entity(tableName = "favorites")
class FavoriteList {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "language")
    var language: String? = null

    @ColumnInfo(name = "imageMedium")
    var imageMedium: String? = null

    @ColumnInfo(name = "imageOriginal")
    var imageOriginal: String? = null

    @ColumnInfo(name = "scheduleTime")
    var scheduleTime: String? = null

    @ColumnInfo(name = "scheduleDays")
    var scheduleDays: List<String>? = null

    @ColumnInfo(name = "genres")
    var genres: List<String>? = null

    @ColumnInfo(name = "summary")
    var summary: String? = null
}