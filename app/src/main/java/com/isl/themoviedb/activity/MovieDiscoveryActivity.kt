package com.isl.themoviedb.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.isl.themoviedb.Injection
import com.isl.themoviedb.adapter.BindingButtonItem
import com.isl.themoviedb.adapter.BindingMovieItem
import com.isl.themoviedb.adapter.BindingProgressItem
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.databinding.ActivityRecyclerViewPtrBinding
import com.isl.themoviedb.view.showContent
import com.isl.themoviedb.view.showError
import com.isl.themoviedb.view.showLoading
import com.isl.themoviedb.viewmodel.MovieDiscoveryViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieDiscoveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewPtrBinding

    private val itemAdapter = GenericItemAdapter()
    private val footerAdapter = GenericItemAdapter()

    private val endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener(footerAdapter) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.loadMore(genreId, currentPage + 1)
            }
        }
    }

    private val viewModel by lazy {
        val viewModelFactory = Injection.provideViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[MovieDiscoveryViewModel::class.java]
    }

    private val glideRequest by lazy { Glide.with(this) }
    private val genreId by lazy { intent.getLongExtra("genre_id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewPtrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = intent.getStringExtra("genre_name")

        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            endlessRecyclerOnScrollListener.resetPageCount(0)
        }

        binding.stateRecyclerView.stateContent.adapter =
            FastAdapter.with(itemAdapter).addAdapter(1, footerAdapter)
        binding.stateRecyclerView.stateContent.layoutManager =
            GridLayoutManager(applicationContext, 3)
        binding.stateRecyclerView.stateContent.addOnScrollListener(endlessRecyclerOnScrollListener)

        viewModel.initialState.observe(this) {
            it ?: return@observe
            when (it) {
                NetworkState.FAILED -> binding.stateRecyclerView.showError {
                    viewModel.loadMore(genreId, 1)
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
                            viewModel.loadMore(genreId, currentPage)
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
                BindingMovieItem(glideRequest, it) {
                    val intent = Intent(
                        this@MovieDiscoveryActivity,
                        MovieDetailActivity::class.java
                    ).apply {
                        putExtra("movie_id", it.id)
                        putExtra("movie_title", it.title)
                    }
                    startActivity(intent)
                }
            }
            itemAdapter.set(items)
        }

        viewModel.loadMore(genreId, 1)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().run { true }
        else -> super.onOptionsItemSelected(item)
    }
}