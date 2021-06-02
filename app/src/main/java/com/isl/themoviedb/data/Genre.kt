package com.isl.themoviedb.data

import com.google.gson.annotations.SerializedName

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
data class Genre(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)

data class GenresResponse(
    @SerializedName("genres") val genres: List<Genre>
)