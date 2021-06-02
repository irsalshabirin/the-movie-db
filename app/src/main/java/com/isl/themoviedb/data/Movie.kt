package com.isl.themoviedb.data

import com.google.gson.annotations.SerializedName

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
data class Movie(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("adult") val isAdult: Boolean = false,
    @SerializedName("title") val title: String = "",
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("vote_average") val voteAverage: Float = 0f,
    @SerializedName("vote_count") val voteCount: Long = 0,
    @SerializedName("release_date") val releaseDate: String = "",

    @SerializedName("budget") val budget: Long? = null,
    @SerializedName("revenue") val revenue: Long? = null,
    @SerializedName("genres") val genres: List<Genre> = listOf(),
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanies>? = null,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountries>? = null
) {
    val imagePoster get() = "https://image.tmdb.org/t/p/original$posterPath"
}

data class ProductionCompanies(
    @SerializedName("id") val id: Long,
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("origin_country") val originCountry: String? = null
)

data class ProductionCountries(
    @SerializedName("name") val name: String? = null
)

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)