package com.ramo

import com.ramo.movie.data.remote.MovieService

class RemoteDataSource(private val movieService: MovieService) {
    suspend fun getNowPlaying() =
        movieService.getNowPlaying()

    suspend fun getUpcoming() = movieService.getUpcoming()
}