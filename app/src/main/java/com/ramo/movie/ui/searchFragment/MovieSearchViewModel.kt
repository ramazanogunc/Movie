package com.ramo.movie.ui.searchFragment

import androidx.lifecycle.*
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieSearchViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _searchResponse: MutableLiveData<NetworkResult<MovieResponse>> = MutableLiveData()
    val movieResponse: LiveData<NetworkResult<MovieResponse>> = _searchResponse
    fun fetchMovieResponse(searchText: String) = viewModelScope.launch {
        repository.getSearchResult(searchText).collect { values ->
            _searchResponse.value = values
        }
    }
}

class MovieSearchViewModelFactory(private val movieRepository: MovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieSearchViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}