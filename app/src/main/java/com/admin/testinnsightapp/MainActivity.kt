package com.admin.testinnsightapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.admin.testinnsightapp.activity.MovieListActivity
import com.admin.testinnsightapp.databinding.ActivityMainBinding
import com.admin.testinnsightapp.utils.dismissKeyboard
import com.admin.testinnsightapp.viewmodel.HomeViewModel
import com.admin.testinnsightapp.viewmodel.HomeViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware
{
    companion object
    {
        const val ANIMATION_DURATION = 1000.toLong()
    }
    override val kodein by kodein()
    private lateinit var dataBind: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel
    private val factory: HomeViewModelFactory by instance()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        searchView = findViewById(R.id.search_view)
        setupViewModel()
        search(searchView)

    }
    private fun setupViewModel()
    {
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }
    private fun search(searchView: SearchView)
    {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String): Boolean
            {
                dismissKeyboard(searchView)
                searchView.clearFocus()
                new_activity(query);

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean
            {
                return false
            }
        })
    }

    fun new_activity(query:String)
    {
        val intent = Intent(this, MovieListActivity::class.java)

        intent.putExtra("query",query)
        startActivity(intent)
    }



}