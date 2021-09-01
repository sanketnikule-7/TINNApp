package com.admin.testinnsightapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admin.testinnsightapp.repository.MovieListRepository

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val repository: MovieListRepository) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return MovieListViewModel(repository) as T
    }
}