package com.isl.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.isl.themoviedb.databinding.ItemProgressBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class BindingProgressItem : AbstractBindingItem<ItemProgressBinding>() {

    override val type: Int get() = javaClass.hashCode()

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemProgressBinding {
        return ItemProgressBinding.inflate(inflater, parent, false)
    }
}