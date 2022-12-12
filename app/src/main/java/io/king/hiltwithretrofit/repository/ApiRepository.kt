package io.king.hiltwithretrofit.repository

import dagger.hilt.android.scopes.ActivityScoped
import io.king.hiltwithretrofit.api.ApiService
import javax.inject.Inject

@ActivityScoped
class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getPopularMoviesList(page: Int) = apiService.getPopularMoviesList(page)

    fun getMovieDetails(movie_id: Int) = apiService.getMovieDetails(movie_id)
}