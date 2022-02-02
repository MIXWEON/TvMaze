package com.jobsity.tvmaze.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MazeEpisode(
    val id: Int,
    val name: String,
    val number: Int,
    val season: Int,
    val image: @RawValue ImageEpisodeType? = null,
    val summary: String? = null
) : Parcelable

data class ImageEpisodeType(
    val medium: String,
    val original: String
)

