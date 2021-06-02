package com.isl.themoviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isl.themoviedb.api.MovieDbService
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.data.Genre
import kotlinx.coroutines.launch

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MainViewModel(
    private val service: MovieDbService
) : ViewModel(), NetworkStateViewModel, ListDataViewModel<Genre> {
    private val _initialState = MutableLiveData<NetworkState>()
    private val _networkState = MutableLiveData<NetworkState>()
    private val _data = MutableLiveData<List<Genre>>(listOf())
    override val initialState: LiveData<NetworkState> get() = _initialState
    override val networkState: LiveData<NetworkState> get() = _networkState
    override val data: LiveData<List<Genre>> get() = _data

    fun load() = viewModelScope.launch {
        _initialState.postValue(NetworkState.LOADING)
        try {
            val result = service.getGenres()
            _data.postValue(result.genres)
            _initialState.postValue(NetworkState.SUCCESS)
        } catch (e: Exception) {
            _initialState.postValue(NetworkState.FAILED)
        }

    }
}