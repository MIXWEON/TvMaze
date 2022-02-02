package com.jobsity.tvmaze.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MazePeople(
    val score: Double? = null,
    val person: @RawValue Person
) : Parcelable

data class Person(
    val id: Int,
    val name: String,
    val genre: String? = null,
    val image: @RawValue ImagePersonType? = null
)

data class ImagePersonType(
    val medium: String,
    val original: String
)