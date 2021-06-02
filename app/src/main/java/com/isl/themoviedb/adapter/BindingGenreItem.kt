package com.isl.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.isl.themoviedb.data.Genre
import com.isl.themoviedb.databinding.ItemGenreBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class BindingGenreItem(
    private val item: Genre,
    private val onClickListener: () -> Unit
) : AbstractBindingItem<ItemGenreBinding>() {

    override val type: Int get() = javaClass.hashCode()

    override fun bindView(binding: ItemGenreBinding, payloads: List<Any>) {
        binding.tv.text = item.name
        binding.root.setOnClickListener { onClickListener() }
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemGenreBinding {
        return ItemGenreBinding.inflate(inflater, parent, false)
    }
}