package com.isl.themoviedb.view

import android.view.View
import com.isl.themoviedb.databinding.StateRecyclerViewBinding

/**
 * Created on : March 29, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
fun StateRecyclerViewBinding.showEmpty() {
    stateEmpty.root.visibility = View.VISIBLE
    stateLoading.root.visibility = View.GONE
    stateContent.visibility = View.GONE
    stateError.root.visibility = View.GONE
}

fun StateRecyclerViewBinding.showLoading() {
    stateEmpty.root.visibility = View.GONE
    stateLoading.root.visibility = View.VISIBLE
    stateContent.visibility = View.GONE
    stateError.root.visibility = View.GONE
}

fun StateRecyclerViewBinding.showError(retryClickListener: (View) -> Unit) {
    stateEmpty.root.visibility = View.GONE
    stateLoading.root.visibility = View.GONE
    stateContent.visibility = View.GONE
    stateError.also {
        it.root.visibility = View.VISIBLE
        it.btnRetry.setOnClickListener(retryClickListener)
    }
}

fun StateRecyclerViewBinding.showContent() {
    stateEmpty.root.visibility = View.GONE
    stateLoading.root.visibility = View.GONE
    stateContent.visibility = View.VISIBLE
    stateError.root.visibility = View.GONE
}