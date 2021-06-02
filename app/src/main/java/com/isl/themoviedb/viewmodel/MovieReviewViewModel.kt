package com.isl.themoviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isl.themoviedb.api.MovieDbService
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.data.UserReview
import kotlinx.coroutines.launch

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieReviewViewModel(
    private val service: MovieDbService
) : ViewModel(), NetworkStateViewModel, ListDataViewModel<UserReview> {

    private val _initialState = MutableLiveData<NetworkState>()
    private val _networkState = MutableLiveData<NetworkState>()
    private val _data = MutableLiveData<List<UserReview>>(listOf())
    override val initialState: LiveData<NetworkState> get() = _initialState
    override val networkState: LiveData<NetworkState> get() = _networkState
    override val data: LiveData<List<UserReview>> get() = _data

    fun loadMore(movieId: Long, page: Int) {
        load(movieId, page)
    }

    private fun load(
        movieId: Long,
        page: Int,
        liveData: MutableLiveData<NetworkState> = if (page == 1) _initialState else _networkState
    ) = viewModelScope.launch {
        liveData.postValue(NetworkState.LOADING)
        try {
            val reviewResponse = service.getMovieReviews(movieId, page)
            liveData.postValue(NetworkState.SUCCESS)
            if (page == 1) {
                _data.postValue(reviewResponse.results)
            } else if (reviewResponse.results.isNotEmpty()) {
                _data.postValue(data.value?.plus(reviewResponse.results))
            }
        } catch (e: Exception) {
            liveData.postValue(NetworkState.FAILED)
        }
    }
}