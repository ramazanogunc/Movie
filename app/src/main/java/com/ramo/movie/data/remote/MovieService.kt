package com.ramo.movie.data.remote

import com.ramo.movie.Constant
import com.ramo.movie.model.remote.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {
    @GET("movie/now_playing${Constant.fullApiKeyText}")
    suspend fun getNowPlaying(): Response<MovieResponse>

    @GET("movie/upcoming${Constant.fullApiKeyText}")
    suspend fun getUpcoming(): Response<MovieResponse>
}