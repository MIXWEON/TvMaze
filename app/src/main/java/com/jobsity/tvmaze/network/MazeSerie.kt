package com.jobsity.tvmaze.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MazeSerie(
    val score: Double? = null,
    val show: @RawValue Show
) : Parcelable

data class Show(
    val id: Int,
    val name: String,
    val type: String,
    val language: String,
    val image: @RawValue ImageType? = null,
    val schedule: @RawValue Schedule,
    val genres: List<String>,
    val summary: String? = null
)

data class ImageType(
    val medium: String,
    val original: String
)

data class Schedule(
    val time: String,
    val days: List<String>
)