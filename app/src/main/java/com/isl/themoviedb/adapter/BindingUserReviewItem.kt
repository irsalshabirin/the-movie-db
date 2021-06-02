package com.isl.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.bumptech.glide.RequestManager
import com.isl.themoviedb.data.UserReview
import com.isl.themoviedb.databinding.ItemUserReviewBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class BindingUserReviewItem(
    private val glideRequest: RequestManager,
    private val item: UserReview
) : AbstractBindingItem<ItemUserReviewBinding>() {

    override val type: Int get() = javaClass.hashCode()

    override fun bindView(binding: ItemUserReviewBinding, payloads: List<Any>) {
        glideRequest.load(item.authorDetail.avatar)
            .centerCrop()
            .error("https://picsum.photos/200/300")
            .into(binding.siv)

        binding.tvUsername.text = item.author
        item.authorDetail.rating.takeIf { it > 0 }?.run {
            binding.tvRating.visibility = View.VISIBLE
            binding.tvRating.text = "☆ $this"
        } ?: kotlin.run { binding.tvRating.visibility = View.GONE }

        binding.tvContent.text =
            HtmlCompat.fromHtml(item.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemUserReviewBinding {
        return ItemUserReviewBinding.inflate(inflater, parent, false)
    }
}