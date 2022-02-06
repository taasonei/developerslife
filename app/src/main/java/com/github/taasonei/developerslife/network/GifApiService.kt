package com.github.taasonei.developerslife.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://developerslife.ru"
private const val PATH_RANDOM = "random?json=true"
private const val PATH_BY_ID = "{id}?json=true"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GifApiService {
    @GET(PATH_RANDOM)
    suspend fun getRandomGif(): GifApiModel

    @GET(PATH_BY_ID)
    suspend fun getGifById(@Path("id") id: String): GifApiModel
}

object GifApi {
    val retrofitService: GifApiService by lazy { retrofit.create(GifApiService::class.java) }
}
