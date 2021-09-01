package com.admin.testinnsightapp.repository

import com.admin.testinnsightapp.model.MovieDetail
import com.admin.testinnsightapp.webservices.ApiInterface
import com.admin.testinnsightapp.webservices.SafeApiRequest


class MovieDetailRepository(private val api: ApiInterface) : SafeApiRequest() {
    suspend fun getMovieDetail(movieimdbID: String): MovieDetail {
        return apiRequest { api.getMovieDetailData(movieimdbID) }
    }
}