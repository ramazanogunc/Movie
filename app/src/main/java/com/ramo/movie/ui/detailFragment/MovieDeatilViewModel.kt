package com.ramo.movie.ui.detailFragment

import androidx.lifecycle.*
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _movieResponse: MutableLiveData<NetworkResult<Movie>> = MutableLiveData()
    val movieResponse: LiveData<NetworkResult<Movie>> = _movieResponse
    fun fetchMovieResponse(movieId: Long) = viewModelScope.launch {
        repository.getMovie(movieId).collect { values ->
            _movieResponse.value = values
        }
    }

    private val _otherMoviesResponse: MutableLiveData<NetworkResult<MovieResponse>> =
        MutableLiveData()
    val otherMoviesResponse: LiveData<NetworkResult<MovieResponse>> = _otherMoviesResponse
    fun fetchOtherMovieResponse(movieId: Long) = viewModelScope.launch {
        repository.getOtherMovie(movieId).collect { values ->
            _otherMoviesResponse.value = values
        }
    }

}

class MovieDetailViewModelFactory(private val movieRepository: MovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}