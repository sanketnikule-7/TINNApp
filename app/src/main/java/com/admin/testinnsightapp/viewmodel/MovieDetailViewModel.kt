package com.admin.testinnsightapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.testinnsightapp.model.MovieDetail
import com.admin.testinnsightapp.repository.MovieDetailRepository
import com.admin.testinnsightapp.utils.ApiException
import com.admin.testinnsightapp.utils.NoInternetException
import com.admin.testinnsightapp.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailViewModel(private val repository: MovieDetailRepository) : ViewModel() {
    private val _movieDetailLiveData = MutableLiveData<State<MovieDetail>>()
    val movieDetailLiveData: LiveData<State<MovieDetail>> get() = _movieDetailLiveData
    private lateinit var movieDetailResponse: MovieDetail

    fun getMovieDetail(movieimdbID: String) {
        _movieDetailLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO)
        {
            try {
                movieDetailResponse = repository.getMovieDetail(movieimdbID)
                withContext(Dispatchers.Main)
                {
                    _movieDetailLiveData.postValue(State.success(movieDetailResponse))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main)
                {
                    _movieDetailLiveData.postValue(State.error(e.message!!))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main)
                {
                    _movieDetailLiveData.postValue(State.error(e.message!!))
                }
            }
        }
    }
}