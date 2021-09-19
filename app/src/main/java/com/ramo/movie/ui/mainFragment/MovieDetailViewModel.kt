package com.ramo.movie.ui.mainFragment

import androidx.lifecycle.*
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _sliderResponse: MutableLiveData<NetworkResult<MovieResponse>> = MutableLiveData()
    val sliderResponse: LiveData<NetworkResult<MovieResponse>> = _sliderResponse
    fun fetchSliderResponse() = viewModelScope.launch {
        repository.getNowPlaying().collect { values ->
            _sliderResponse.value = values
        }
    }


    private val _recyclerResponse: MutableLiveData<NetworkResult<MovieResponse>> = MutableLiveData()
    val recyclerResponse: LiveData<NetworkResult<MovieResponse>> = _recyclerResponse
    fun fetchRecyclerResponse() = viewModelScope.launch {
        repository.getUpcoming().collect { values ->
            _recyclerResponse.value = values
        }
    }
}


class MainViewModelFactory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}