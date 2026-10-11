package com.example.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AladhanApiService {
  @GET("v1/timingsByCity")
  suspend fun getTimingsByCity(
    @Query("city") city: String,
    @Query("country") country: String,
    @Query("method") method: Int,
    @Query("school") school: Int
  ): AladhanResponse

  @GET("v1/timings")
  suspend fun getTimings(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("method") method: Int,
    @Query("school") school: Int
  ): AladhanResponse
}

object AladhanApiClient {
  private const val BASE_URL = "https://api.aladhan.com/"

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  val apiService: AladhanApiService = retrofit.create(AladhanApiService::class.java)
}
