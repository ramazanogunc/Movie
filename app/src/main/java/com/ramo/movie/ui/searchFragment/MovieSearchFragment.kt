package com.ramo.movie.ui.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ramo.RemoteDataSource
import com.ramo.movie.R
import com.ramo.movie.data.remote.RetrofitClient
import com.ramo.movie.databinding.FragmentMovieSearchBinding
import com.ramo.movie.databinding.ItemSearchBinding
import com.ramo.movie.model.remote.Movie
import com.ramo.movie.model.remote.MovieResponse
import com.ramo.movie.model.remote.NetworkResult
import com.ramo.movie.repository.MovieRepository
import com.ramo.movie.util.hide
import com.ramo.movie.util.show
import com.ramo.sweetrecycleradapter.SweetRecyclerAdapter

class MovieSearchFragment : Fragment() {

    private lateinit var binding: FragmentMovieSearchBinding
    private val resultAdapter = SweetRecyclerAdapter<Movie>()
    private val viewModel: MovieSearchViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MovieSearchViewModelFactory(
                MovieRepository(RemoteDataSource(RetrofitClient.getMovieService())),
            )
        ).get(MovieSearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.requestFocus()
        initUi()

    }

    private fun initUi() {
        resultAdapter.addHolder(R.layout.item_search) { v, item ->
            val itemBinding = ItemSearchBinding.bind(v)
            itemBinding.title.text = item.title
        }
        resultAdapter.setOnItemClickListener { v, item ->
            val bundle = bundleOf("movieId" to item.id)
            findNavController().navigate(
                R.id.action_movieSearchFragment_to_movieDetailFragment,
                bundle
            )
        }
        binding.rwResult.adapter = resultAdapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 2)
                    fetchResult(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 2)
                    fetchResult(query)
                return false
            }

        })
    }

    private fun fetchResult(queryText: String) {
        viewModel.fetchMovieResponse(queryText)
        viewModel.movieResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.rwResult.show()
                    binding.progress.hide()
                    binding.twError.hide()
                    prepareRecycler(response.data!!)
                }
                is NetworkResult.Error -> {
                    binding.twError.show()
                    binding.rwResult.hide()
                    binding.progress.hide()
                    binding.twError.text = response.message
                }
                is NetworkResult.Loading -> {
                    binding.progress.show()
                    binding.twError.hide()
                    binding.rwResult.hide()
                }
            }
        }
    }

    private fun prepareRecycler(movieResponse: MovieResponse) {
        val list = movieResponse.results
        list.map { it.viewHolderId = R.layout.item_search }
        resultAdapter.submitList(list)
    }
}