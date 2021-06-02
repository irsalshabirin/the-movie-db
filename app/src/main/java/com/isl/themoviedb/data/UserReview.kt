package com.isl.themoviedb.data

import com.google.gson.annotations.SerializedName

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
data class UserReview(
    @SerializedName("author") val author: String = "",
    @SerializedName("author_details") val authorDetail: AuthorDetail = AuthorDetail(),
    @SerializedName("content") val content: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = "",
    @SerializedName("url") val url: String = ""
)

data class AuthorDetail(
    @SerializedName("name") val name: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatar_path") val avatar: String = "",
    @SerializedName("rating") val rating: Int = 0
)

data class MovieReviewResponse(
    @SerializedName("results") val results: List<UserReview>,
    @SerializedName("total_results") val totalResult: Int = 0
)