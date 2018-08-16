package com.hoc.example.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://jsonplaceholder.typicode.com"

@Parcelize
data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable

interface ApiService {
    @GET("photos")
    fun getPhotos(
        @Query("_start") start: Int? = null,
        @Query("_limit") limit: Int? = null
    ): Call<List<Photo>>
}
