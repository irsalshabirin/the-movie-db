package com.isl.themoviedb.api

import com.isl.themoviedb.data.GenresResponse
import com.isl.themoviedb.data.Movie
import com.isl.themoviedb.data.MovieResponse
import com.isl.themoviedb.data.MovieReviewResponse
import com.isl.themoviedb.data.MovieVideoResponse
import com.isl.themoviedb.util.Constanta
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
interface MovieDbService {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMovieDiscovers(
        @Query("with_genres") genres: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Long): Movie

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") id: Long): MovieVideoResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") id: Long,
        @Query("page") page: Int
    ): MovieReviewResponse

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        val instance: MovieDbService by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor {
                    val original: Request = it.request()
                    val originalHttpUrl: HttpUrl = original.url
                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", Constanta.MOVIE_DB_API_KEY)
                        .build()

                    val requestBuilder: Request.Builder = original.newBuilder().url(url)
                    val request: Request = requestBuilder.build()
                    it.proceed(request)
                }
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieDbService::class.java)
        }
    }
}