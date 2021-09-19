package com.ramo.movie.data.remote

import com.ramo.movie.Constant
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("movie/now_playing${Constant.fullApiKeyText}")
    suspend fun getNowPlaying(): Response<MovieResponse>

    @GET("movie/upcoming${Constant.fullApiKeyText}")
    suspend fun getUpcoming(): Response<MovieResponse>

    @GET("movie/{movie_id}${Constant.fullApiKeyText}")
    suspend fun getMovie(@Path("movie_id") movieId: Long): Response<Movie>

    @GET("movie/{movie_id}/similar${Constant.fullApiKeyText}")
    suspend fun getOtherMovie(@Path("movie_id") movieId: Long): Response<MovieResponse>

}