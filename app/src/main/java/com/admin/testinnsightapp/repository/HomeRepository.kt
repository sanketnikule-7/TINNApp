package com.admin.testinnsightapp.repository

import com.admin.testinnsightapp.model.SearchResults
import com.admin.testinnsightapp.webservices.ApiInterface
import com.admin.testinnsightapp.webservices.SafeApiRequest


class HomeRepository(private val api: ApiInterface) : SafeApiRequest() {
    suspend fun getMovies(searchTitle: String, pageIndex: Int): SearchResults {
        return apiRequest { api.getSearchResultData(searchTitle, pageIndex) }
    }
}