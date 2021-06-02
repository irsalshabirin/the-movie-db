package com.isl.themoviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isl.themoviedb.api.MovieDbService
import com.isl.themoviedb.data.Movie
import com.isl.themoviedb.data.UserReview
import kotlinx.coroutines.launch

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieDetailViewModel(
    private val service: MovieDbService
) : ViewModel() {

    private val _movie = MutableLiveData(Movie())
    private val _userReviews = MutableLiveData<List<UserReview>>(listOf())
    private val _videoKey = MutableLiveData("")
    val movie: LiveData<Movie> get() = _movie
    val userReviews: LiveData<List<UserReview>> get() = _userReviews
    val videoKey: LiveData<String> get() = _videoKey

    fun load(movieId: Long) = viewModelScope.launch {
        try {
            val result = service.getMovie(movieId)
            _movie.postValue(result)
        } catch (e: Exception) {
        }

    }

    fun loadUseReview(movieId: Long) = viewModelScope.launch {
        try {
            val result = service.getMovieReviews(movieId, 1)
            _userReviews.postValue(result.results)
        } catch (e: Exception) {

        }
    }

    fun loadVideo(movieId: Long) = viewModelScope.launch {
        val videos = service.getMovieVideos(movieId)
        val youtubeList = videos.result.filter {
            it.type.toLowerCase().contains("trailer") && it.site.toLowerCase() == "youtube"
        }
        _videoKey.postValue(youtubeList.firstOrNull()?.key ?: "")
    }
}