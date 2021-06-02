package com.isl.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.isl.themoviedb.data.Movie
import com.isl.themoviedb.databinding.ItemMovieBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class BindingMovieItem(
    private val glideRequest: RequestManager,
    private val item: Movie,
    private val onClickListener: () -> Unit
) : AbstractBindingItem<ItemMovieBinding>() {

    override val type: Int get() = javaClass.hashCode()

    override fun bindView(binding: ItemMovieBinding, payloads: List<Any>) {
        glideRequest.load(item.imagePoster)
            .centerCrop()
            .into(binding.iv)

        binding.tvTitle.text = item.title
        binding.tvVoteAvg.text = item.voteAverage.toString()
        binding.root.setOnClickListener { onClickListener() }
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemMovieBinding {
        return ItemMovieBinding.inflate(inflater, parent, false)
    }
}