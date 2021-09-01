package com.admin.testinnsightapp.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.admin.testinnsightapp.MainActivity
import com.admin.testinnsightapp.R
import com.admin.testinnsightapp.databinding.ActivityMovieDetailScrollingBinding
import com.admin.testinnsightapp.utils.*
import com.admin.testinnsightapp.viewmodel.MovieDetailViewModel
import com.admin.testinnsightapp.viewmodel.MovieDetailViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MovieDetailScrollingActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var dataBind: ActivityMovieDetailScrollingBinding
    private lateinit var viewModel: MovieDetailViewModel
    private val factory: MovieDetailViewModelFactory by instance()
    private var movieTitle = ""
    private var moviePoster = ""
    private var movieimdbID = ""
    lateinit var toolbar: Toolbar

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_scrolling)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_scrolling)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupUI()
        handleNetworkChanges()
        setupViewModel()
        setupAPICall()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupUI() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra(AppConstant.INTENT_TITLE) && intent.getStringExtra(AppConstant.INTENT_TITLE) != null)
            movieTitle = intent.getStringExtra(AppConstant.INTENT_TITLE)!!
        if (intent.hasExtra(AppConstant.INTENT_POSTER) && intent.getStringExtra(AppConstant.INTENT_POSTER) != null)
            moviePoster = intent.getStringExtra(AppConstant.INTENT_POSTER)!!

        if (intent.hasExtra(AppConstant.INTENT_IMBDID) && intent.getStringExtra(AppConstant.INTENT_IMBDID) != null)
            movieimdbID = intent.getStringExtra(AppConstant.INTENT_IMBDID)!!


        dataBind.toolbar.title = movieTitle
        Glide.with(this).load(moviePoster)
            .centerCrop()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(dataBind.imagePoster)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun setupAPICall() {
        viewModel.movieDetailLiveData.observe(this, Observer
        { state ->
            when (state) {
                is State.Loading -> {
                    dataBind.progressBar.show()
                    dataBind.cardViewMovieDetail.hide()
                }
                is State.Success -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.show()
                    state.data.let {
                        dataBind.textReleasedDate.text = "ReleasedDate: ${it.released}"
                        dataBind.textRuntime.text = "Runtime: ${it.runtime}"
                        dataBind.textGenre.text = "Genre: ${it.genre}"
                        dataBind.textDirector.text = "Director: ${it.director}"
                        dataBind.textActors.text = "Director: ${it.actors}"
                        dataBind.textPlot.text = it.plot
                        /* if (it.ratings.isNotEmpty())
                             dataBind.textImd.text =
                                 "Internet Movie Database: ${it.ratings[0].value}"*/
                        dataBind.textImdbRating.text = "IMBD Rating: ${it.imdbrating}"
                    }
                }
                is State.Error -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.hide()
                    showToast(state.message)
                }
            }
        })
        getMoviesDetail(movieimdbID)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                dataBind.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                dataBind.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                if (viewModel.movieDetailLiveData.value is State.Error)
                    getMoviesDetail(movieimdbID)
                dataBind.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                dataBind.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(MainActivity.ANIMATION_DURATION)
                        .setDuration(MainActivity.ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    private fun getMoviesDetail(movieimdbID: String) {
        viewModel.getMovieDetail(movieimdbID)
    }
}