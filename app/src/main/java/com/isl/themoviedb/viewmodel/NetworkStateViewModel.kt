package com.isl.themoviedb.viewmodel

import androidx.lifecycle.LiveData
import com.isl.themoviedb.api.NetworkState

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
interface NetworkStateViewModel {
    val initialState: LiveData<NetworkState>
    val networkState: LiveData<NetworkState>
}

interface ListDataViewModel<T> {
    val data: LiveData<List<T>>
}