package com.ramo.movie.model.remote

import com.google.gson.annotations.SerializedName
import com.ramo.movie.R
import com.ramo.sweetrecycleradapter.ViewTypeListener


data class MovieResponse(
    val dates: Dates,
    val page: Long,
    val results: List<Movie>,

    @SerializedName("total_pages")
    val totalPages: Long,

    @SerializedName("total_results")
    val totalResults: Long,
)

data class Dates(
    val maximum: String,
    val minimum: String
)

data class Movie(
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("genre_ids")
    val genreIDS: List<Long>,

    val id: Long,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    val overview: String,
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("release_date")
    val releaseDate: String,

    val title: String,
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Long,
    var isSlider: Boolean = false
) : ViewTypeListener {
    override fun getRecyclerItemLayoutId(): Int {
        return if (isSlider) R.layout.item_slider
        else R.layout.item_movie
    }

}