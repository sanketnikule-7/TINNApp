package com.admin.testinnsightapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admin.testinnsightapp.repository.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return HomeViewModel(repository) as T
    }
}