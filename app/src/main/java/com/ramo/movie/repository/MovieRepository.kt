package com.ramo.movie.repository

import com.ramo.RemoteDataSource
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {
    suspend fun getNowPlaying(): Flow<NetworkResult<MovieResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getNowPlaying() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUpcoming(): Flow<NetworkResult<MovieResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUpcoming() })
        }.flowOn(Dispatchers.IO)
    }
}