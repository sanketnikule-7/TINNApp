package com.admin.testinnsightapp

import android.app.Application
import com.admin.testinnsightapp.repository.HomeRepository
import com.admin.testinnsightapp.repository.MovieDetailRepository
import com.admin.testinnsightapp.repository.MovieListRepository
import com.admin.testinnsightapp.utils.NetworkConnectionInterceptor
import com.admin.testinnsightapp.viewmodel.HomeViewModelFactory
import com.admin.testinnsightapp.viewmodel.MovieDetailViewModelFactory
import com.admin.testinnsightapp.viewmodel.MovieListViewModelFactory
import com.admin.testinnsightapp.webservices.ApiInterface
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TApplication : Application(), KodeinAware
{
    override val kodein = Kodein.lazy{
        import(androidXModule(this@TApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { HomeRepository(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from singleton { MovieDetailRepository(instance()) }
        bind() from provider { MovieDetailViewModelFactory(instance()) }
        bind() from singleton { MovieListRepository(instance()) }
        bind() from provider { MovieListViewModelFactory(instance()) }
    }
}