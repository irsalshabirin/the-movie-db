package com.isl.themoviedb.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.isl.themoviedb.Injection
import com.isl.themoviedb.adapter.BindingGenreItem
import com.isl.themoviedb.api.NetworkState
import com.isl.themoviedb.databinding.ActivityRecyclerViewPtrBinding
import com.isl.themoviedb.view.showContent
import com.isl.themoviedb.view.showError
import com.isl.themoviedb.view.showLoading
import com.isl.themoviedb.viewmodel.MainViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewPtrBinding

    private val itemAdapter = GenericItemAdapter()

    private val viewModel by lazy {
        val viewModelFactory = Injection.provideViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewPtrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.stateRecyclerView.stateContent.adapter = FastAdapter.with(itemAdapter)

        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            viewModel.load()
        }

        viewModel.initialState.observe(this) {
            it ?: return@observe
            when (it) {
                NetworkState.LOADING -> binding.stateRecyclerView.showLoading()
                NetworkState.FAILED -> binding.stateRecyclerView.showError { viewModel.load() }
                NetworkState.SUCCESS -> binding.stateRecyclerView.showContent()
            }
        }
        viewModel.data.observe(this) { listGenre ->
            listGenre ?: return@observe
            itemAdapter.set(
                listGenre.map { genre ->
                    BindingGenreItem(genre) {
                        val intent =
                            Intent(this@MainActivity, MovieDiscoveryActivity::class.java)
                                .apply {
                                    putExtra("genre_id", genre.id)
                                    putExtra("genre_name", genre.name)
                                }
                        startActivity(intent)
                    }
                }
            )
        }
        viewModel.load()
    }
}