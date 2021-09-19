package com.ramo.movie.ui.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ramo.RemoteDataSource
import com.ramo.movie.Constant
import com.ramo.movie.R
import com.ramo.movie.data.remote.RetrofitClient
import com.ramo.movie.databinding.FragmentMainBinding
import com.ramo.movie.databinding.ItemMovieBinding
import com.ramo.movie.databinding.ItemSliderBinding
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import com.ramo.movie.util.hide
import com.ramo.movie.util.show
import com.ramo.movie.util.textShorting
import com.ramo.sweetrecycleradapter.SweetRecyclerAdapter

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(
                MovieRepository(RemoteDataSource(RetrofitClient.getMovieService())),
            )
        ).get(MainViewModel::class.java)
    }

    private val sliderAdapter = SweetRecyclerAdapter<Movie>()
    private val recyclerAdapter = SweetRecyclerAdapter<Movie>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        fetchSliderData()
        fetchRecyclerData()
    }

    private fun initUi() {
        binding.swipeToRefreshLayout.setOnRefreshListener {
            fetchSliderData()
            fetchRecyclerData()
            binding.swipeToRefreshLayout.isRefreshing = false
        }

        sliderAdapter.addHolder(R.layout.item_slider) { view, item ->
            val sliderBinding = ItemSliderBinding.bind(view)
            sliderBinding.title.text = item.title
            sliderBinding.description.text = textShorting(item.overview,100)
            val imageUrl = Constant.imageBaseUrl + item.backdropPath
            Glide.with(this).load(imageUrl).into(sliderBinding.backgroundImage)
        }

        recyclerAdapter.addHolder(R.layout.item_movie) { view, item ->
            val recyclerBinding = ItemMovieBinding.bind(view)
            recyclerBinding.title.text = item.title
            recyclerBinding.description.text = textShorting(item.overview,80)
            recyclerBinding.date.text = item.releaseDate
            val imageUrl = Constant.imageBaseUrl + item.posterPath
            Glide.with(this).load(imageUrl).into(recyclerBinding.movieImage)
        }

        binding.viewPagerSlider.adapter = sliderAdapter
        binding.recyclerViewMovieList.adapter = recyclerAdapter
    }

    private fun fetchSliderData() {
        mainViewModel.fetchSliderResponse()
        mainViewModel.sliderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.viewPagerSlider.show()
                    binding.sliderProgress.hide()
                    binding.twSliderError.hide()
                    prepareSlider(response.data!!)
                }
                is NetworkResult.Error -> {
                    binding.twSliderError.show()
                    binding.sliderProgress.hide()
                    binding.viewPagerSlider.hide()
                    binding.twSliderError.text = response.message
                }
                is NetworkResult.Loading -> {
                    binding.sliderProgress.show()
                    binding.twSliderError.hide()
                    binding.viewPagerSlider.hide()
                }
            }
        }
    }

    private fun prepareSlider(movieResponse: MovieResponse) {
        val list = movieResponse.results
        list.map { it.isSlider = true }
        sliderAdapter.submitList(list)
    }

    private fun fetchRecyclerData() {
        mainViewModel.fetchRecyclerResponse()
        mainViewModel.recyclerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.recyclerViewMovieList.show()
                    binding.twRecyclerError.hide()
                    binding.recyclerProgress.hide()
                    Toast.makeText(requireContext(), response.data!!.results.size.toString(), Toast.LENGTH_SHORT).show()
                    prepareRecycler(response.data!!)
                }
                is NetworkResult.Error -> {
                    binding.twRecyclerError.show()
                    binding.recyclerViewMovieList.hide()
                    binding.recyclerProgress.hide()
                    binding.twRecyclerError.text = response.message
                }
                is NetworkResult.Loading -> {
                    binding.recyclerProgress.show()
                    binding.twRecyclerError.hide()
                    binding.recyclerViewMovieList.hide()
                }
            }
        }
    }

    private fun prepareRecycler(movieResponse: MovieResponse) {
        val list = movieResponse.results
        list.map { it.isSlider = false }
        recyclerAdapter.submitList(list)
    }
}

