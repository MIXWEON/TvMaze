package com.jobsity.tvmaze.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.tvmaze.com/"
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface MazeApiService {
    @GET("search/shows")
    suspend fun getProperties(@Query("q") type: String):List<MazeSerie>
    @GET("shows")
    suspend fun getShows():List<Show>
    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path("id") type: String):List<MazeEpisode>
    @GET("search/people")
    suspend fun getPeople(@Query("q") type: String):List<MazePeople>
}

object MazeApi{
    val retrofitService : MazeApiService by lazy{
        retrofit.create(MazeApiService::class.java)
    }
}