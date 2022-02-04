package com.github.taasonei.developerslife

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://developerslife.ru"
private const val PATH = "random?json=true"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GifApiService {
    @GET(PATH)
    suspend fun getRandomGif(): GifModel
}

object GifApi {
    val retrofitService: GifApiService by lazy { retrofit.create(GifApiService::class.java) }
}