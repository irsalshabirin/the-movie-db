package com.isl.themoviedb.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.isl.themoviedb.Injection
import com.isl.themoviedb.adapter.BindingButtonItem
import com.isl.themoviedb.adapter.BindingProgressItem
import com.isl.themoviedb.adapter.BindingUserReviewItem
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.databinding.ActivityRecyclerViewPtrBinding
import com.isl.themoviedb.view.showContent
import com.isl.themoviedb.view.showError
import com.isl.themoviedb.view.showLoading
import com.isl.themoviedb.viewmodel.MovieReviewViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewPtrBinding

    private val itemAdapter = GenericItemAdapter()
    private val footerAdapter = GenericItemAdapter()

    private val glideRequest by lazy { Glide.with(this) }
    private val movieId by lazy { intent.getLongExtra("movie_id", 0) }
    private val movieTitle by lazy { intent.getStringExtra("movie_title") }

    private val endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener(footerAdapter) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.loadMore(movieId, currentPage + 1)
            }
        }
    }

    private val viewModel by lazy {
        val viewModelFactory = Injection.provideViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[MovieReviewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewPtrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Review - $movieTitle"

        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            endlessRecyclerOnScrollListener.resetPageCount(0)
        }

        binding.stateRecyclerView.stateContent.adapter =
            FastAdapter.with(itemAdapter).addAdapter(1, footerAdapter)
        binding.stateRecyclerView.stateContent.addOnScrollListener(endlessRecyclerOnScrollListener)

        viewModel.initialState.observe(this) {
            it ?: return@observe
            when (it) {
                NetworkState.FAILED -> binding.stateRecyclerView.showError {
                    viewModel.loadMore(movieId, 1)
                }
                NetworkState.LOADING -> binding.stateRecyclerView.showLoading()
                NetworkState.SUCCESS -> binding.stateRecyclerView.showContent()
            }
        }

        viewModel.networkState.observe(this) {
            it ?: return@observe
            when (it) {
                NetworkState.FAILED -> {
                    footerAdapter.set(
                        listOf(BindingButtonItem {
                            val currentPage = endlessRecyclerOnScrollListener.currentPage + 1
                            viewModel.loadMore(movieId, currentPage)
                        })
                    )
                }
                NetworkState.LOADING -> {
                    val progressItem = BindingProgressItem()
                    footerAdapter.set(listOf(progressItem))
                }
                NetworkState.SUCCESS -> footerAdapter.clear()
            }
        }

        viewModel.data.observe(this) { movies ->
            movies ?: return@observe
            val items = movies.map {
                BindingUserReviewItem(glideRequest, it)
            }
            itemAdapter.set(items)
        }

        viewModel.loadMore(movieId, 1)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().run { true }
        else -> super.onOptionsItemSelected(item)
    }
}