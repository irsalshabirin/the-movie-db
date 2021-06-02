package com.isl.themoviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isl.themoviedb.api.MovieDbService

/**
 * Created on : March 31, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class ViewModelFactory(private val service: MovieDbService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(service) as T
            }
            modelClass.isAssignableFrom(MovieDiscoveryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MovieDiscoveryViewModel(service) as T
            }
            modelClass.isAssignableFrom(MovieReviewViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MovieReviewViewModel(service) as T
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(service) as T
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}