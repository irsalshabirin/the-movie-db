package com.isl.themoviedb

import androidx.lifecycle.ViewModelProvider
import com.isl.themoviedb.api.MovieDbService
import com.isl.themoviedb.viewmodel.ViewModelFactory

/**
 * Created on : March 31, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
object Injection {
    private fun provideService() = MovieDbService.instance

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideService())
    }
}