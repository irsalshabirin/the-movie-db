package com.isl.themoviedb.data

import com.google.gson.annotations.SerializedName

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
data class MovieVideo(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
)

data class MovieVideoResponse(
    @SerializedName("results") val result: List<MovieVideo>
)