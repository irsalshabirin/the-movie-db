package com.isl.themoviedb.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.isl.themoviedb.Injection
import com.isl.themoviedb.R
import com.isl.themoviedb.adapter.BindingUserReviewItem
import com.isl.themoviedb.databinding.ActivityMovieDetailBinding
import com.isl.themoviedb.util.Constanta
import com.isl.themoviedb.viewmodel.MovieDetailViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter

/**
 * Created on : June 02, 2021
 * Author     : irsalshabirin
 * Name       : Irsal Shabirin
 * GitHub     : https://github.com/irsalshabirin
 */
class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding

    private var youTubePlayer: YouTubePlayer? = null
    private val glideManager by lazy { Glide.with(this) }
    private val movieId by lazy { intent.getLongExtra("movie_id", 0) }
    private val movieTitle by lazy { intent.getStringExtra("movie_title") }

    private val viewModel by lazy {
        val viewModelFactory = Injection.provideViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[MovieDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = movieTitle

        viewModel.load(movieId)
        initMovie()

        binding.mbLoadReviewMore.setOnClickListener {
            val intent = Intent(this, MovieReviewActivity::class.java).apply {
                putExtra("movie_id", movieId)
                putExtra("movie_title", movieTitle)
            }
            startActivity(intent)
        }

        initializeYoutubePlayer()

        viewModel.loadUseReview(movieId)
        initUserReview()

        viewModel.loadVideo(movieId)
        initVideo()
    }

    private fun initMovie() {
        viewModel.movie.observe(this) {
            val movie = it ?: return@observe
            glideManager
                .load(movie.imagePoster)
                .into(binding.ivPoster)

            binding.tvOverview.text = movie.overview
            binding.mcvOverview.visibility =
                if (movie.overview.isBlank()) View.GONE else View.VISIBLE

            binding.mcvDetail.visibility =
                if (movie.releaseDate.isBlank()) View.GONE else View.VISIBLE
            binding.tvVoteAverage.text = movie.voteAverage.toString()
            binding.tvVoteCount.text = movie.voteCount.toString()

            binding.tvGenre.text = movie.genres.joinToString { it.name }
            binding.tvReleaseDate.text = movie.releaseDate

            binding.llBudget.visibility =
                if (movie.budget ?: 0 > 0L) View.VISIBLE else View.GONE
            binding.tvBudget.text = movie.budget?.toString()
            binding.llRevenue.visibility =
                if (movie.revenue ?: 0 > 0L) View.VISIBLE else View.GONE
            binding.tvRevenue.text = movie.revenue?.toString()

            binding.tvCompany.text =
                movie.productionCompanies?.map { it.name }?.joinToString()
            binding.tvCountry.text =
                movie.productionCountries?.map { it.name }?.joinToString()
        }
    }

    private fun initUserReview() {
        viewModel.userReviews.observe(this) {
            val userReviews = it ?: return@observe
            if (userReviews.isEmpty()) {
                binding.llUserReviews.visibility = View.GONE
            } else {
                binding.mbLoadReviewMore.visibility =
                    if (userReviews.size > 1) View.VISIBLE else View.GONE

                binding.llUserReviews.visibility = View.VISIBLE
                binding.tvUserReview.text = "Review (${userReviews.size})"
                val itemAdapter = GenericItemAdapter()
                itemAdapter.add(BindingUserReviewItem(glideManager, userReviews.first()))
                binding.rvUserReviews.adapter = FastAdapter.with(itemAdapter)
                binding.rvUserReviews.isNestedScrollingEnabled = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeYoutubePlayer() {
        val youTubePlayerFragment = supportFragmentManager
            .findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragment?
            ?: return

        youTubePlayerFragment.initialize(
            Constanta.YOUTUBE_DEVELOPER_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    player: YouTubePlayer,
                    wasRestored: Boolean
                ) {
                    if (!wasRestored) {
                        youTubePlayer = player

                        //set the player style default
                        youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        initVideo()
                    }
                }

                override fun onInitializationFailure(
                    arg0: YouTubePlayer.Provider,
                    arg1: YouTubeInitializationResult
                ) {
                }
            })
    }

    private fun initVideo() {
        viewModel.videoKey.observe(this) { videoKey ->
            binding.llVideo.visibility = if (videoKey.isBlank()) View.GONE else View.VISIBLE
            binding.ivPoster.visibility = if (videoKey.isBlank()) View.VISIBLE else View.GONE
            youTubePlayer?.cueVideo(videoKey)
        }
    }
}