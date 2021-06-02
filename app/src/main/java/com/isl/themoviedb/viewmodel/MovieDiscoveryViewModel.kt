package com.isl.themoviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isl.themoviedb.api.MovieDbService
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.data.Movie
import kotlinx.coroutines.launch

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieDiscoveryViewModel(
    private val service: MovieDbService
) : ViewModel(), NetworkStateViewModel, ListDataViewModel<Movie> {

    private val _initialState = MutableLiveData<NetworkState>()
    private val _networkState = MutableLiveData<NetworkState>()
    private val _data = MutableLiveData<List<Movie>>(listOf())
    override val initialState: LiveData<NetworkState> get() = _initialState
    override val networkState: LiveData<NetworkState> get() = _networkState
    override val data: LiveData<List<Movie>> get() = _data

    fun loadMore(genreId: Long, page: Int) {
        load(genreId, page)
    }

    private fun load(
        genreId: Long,
        page: Int,
        liveData: MutableLiveData<NetworkState> = if (page == 1) _initialState else _networkState
    ) = viewModelScope.launch {
        liveData.postValue(NetworkState.LOADING)
        try {
            val movieResult = service.getMovieDiscovers(genreId.toString(), page)
            liveData.postValue(NetworkState.SUCCESS)
            if (page == 1) {
                _data.postValue(movieResult.results)
            } else if (movieResult.results.isNotEmpty()) {
                _data.postValue(data.value?.plus(movieResult.results))
            }
        } catch (e: Exception) {
            liveData.postValue(NetworkState.FAILED)
        }
    }
}