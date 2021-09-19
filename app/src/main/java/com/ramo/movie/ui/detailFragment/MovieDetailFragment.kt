package com.ramo.movie.ui.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ramo.RemoteDataSource
import com.ramo.movie.Constant
import com.ramo.movie.R
import com.ramo.movie.data.remote.RetrofitClient
import com.ramo.movie.databinding.FragmentMovieDetailBinding
import com.ramo.movie.databinding.ItemOtherMoviesBinding
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import com.ramo.movie.util.hide
import com.ramo.movie.util.show
import com.ramo.sweetrecycleradapter.SweetRecyclerAdapter

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private var movieId: Long = 0
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MovieDetailViewModelFactory(
                MovieRepository(RemoteDataSource(RetrofitClient.getMovieService())),
            )
        ).get(MovieDetailViewModel::class.java)
    }
    private val otherMoviesAdapter = SweetRecyclerAdapter<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            movieId = requireArguments().getLong("movieId")
        }
        initUi()
        fetchMovie()
        fetchOtherMovies()
    }

    private fun fetchOtherMovies() {
        viewModel.fetchOtherMovieResponse(movieId)
        viewModel.otherMoviesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.rwOtherMovies.show()
                    binding.otherMoviesError.hide()
                    binding.otherMoviesProgress.hide()
                    prepareRecycler(response.data!!)
                }
                is NetworkResult.Error -> {
                    binding.otherMoviesError.show()
                    binding.rwOtherMovies.hide()
                    binding.otherMoviesProgress.hide()
                }
                is NetworkResult.Loading -> {
                    binding.otherMoviesProgress.show()
                    binding.otherMoviesError.hide()
                    binding.rwOtherMovies.hide()
                }
            }
        }
    }

    private fun prepareRecycler(movieResponse: MovieResponse) {
        val list = movieResponse.results
        list.map { it.viewHolderId = R.layout.item_other_movies }
        otherMoviesAdapter.submitList(list)

    }

    private fun fetchMovie() {
        viewModel.fetchMovieResponse(movieId)
        viewModel.movieResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.movieDetails.show()
                    binding.detailProgress.hide()
                    binding.twDetailError.hide()
                    setDataToUi(response.data!!)
                }
                is NetworkResult.Error -> {
                    binding.twDetailError.show()
                    binding.movieDetails.hide()
                    binding.detailProgress.hide()
                }
                is NetworkResult.Loading -> {
                    binding.detailProgress.show()
                    binding.twDetailError.hide()
                    binding.movieDetails.hide()
                }
            }
        }
    }

    private fun setDataToUi(movie: Movie) {
        val imageUrl = Constant.imageBaseUrl + movie.backdropPath
        Glide.with(this).load(imageUrl).into(binding.movieImage)

        val imdbIconUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/IMDB_Logo_2016.svg/300px-IMDB_Logo_2016.svg.png"
        Glide.with(this).load(imdbIconUrl).into(binding.imageImdb)

        binding.title.text = movie.title
        binding.description.text = movie.overview
        binding.rate.text = movie.voteAverage.toString()
        binding.date.text = movie.releaseDate
    }

    private fun initUi() {
        otherMoviesAdapter.addHolder(R.layout.item_other_movies) { view, item ->
            val itemBinding = ItemOtherMoviesBinding.bind(view)
            val imageUrl = Constant.imageBaseUrl + item.posterPath
            Glide.with(this).load(imageUrl).into(itemBinding.movieImage)
            itemBinding.title.text = item.title
        }
        otherMoviesAdapter.setOnItemClickListener { _, item ->
            val bundle = bundleOf("movieId" to item.id)
            findNavController().navigate(R.id.action_movieDetailFragment_self, bundle)
        }
        binding.rwOtherMovies.adapter = otherMoviesAdapter
    }
}