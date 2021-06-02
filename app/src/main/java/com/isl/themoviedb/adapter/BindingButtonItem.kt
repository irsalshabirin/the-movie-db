package com.isl.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.isl.themoviedb.databinding.ItemButtonBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class BindingButtonItem(private val onClickListener: () -> Unit) :
    AbstractBindingItem<ItemButtonBinding>() {

    override val type: Int get() = javaClass.hashCode()

    override fun bindView(binding: ItemButtonBinding, payloads: List<Any>) {
        binding.mbRetry.setOnClickListener { onClickListener() }
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemButtonBinding {
        return ItemButtonBinding.inflate(inflater, parent, false)
    }
}