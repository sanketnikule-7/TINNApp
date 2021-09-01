package com.admin.testinnsightapp.webservices

import com.admin.testinnsightapp.model.MovieDetail
import com.admin.testinnsightapp.model.SearchResults
import com.admin.testinnsightapp.utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiInterface {
    @GET("?apikey=e5311742")
    suspend fun getSearchResultData(
        @Query(value = "s") searchTitle: String,
        @Query(value = "page") pageIndex: Int
    ): Response<SearchResults>

    @GET("?apikey=e5311742")
    suspend fun getMovieDetailData(
        @Query(value = "i") title: String,
    ): Response<MovieDetail>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface {
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}