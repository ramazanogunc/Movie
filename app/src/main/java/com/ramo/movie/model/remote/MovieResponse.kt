package com.ramo.movie.model.remote

import com.google.gson.annotations.SerializedName
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
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("belongs_to_collection") val collection: MovieCollection,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("genre_ids") val genreIDS: List<Long>,
    val id: Long,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("production_companies") val productCompanies: List<MovieCompanies>,
    @SerializedName("production_countries") val production_countries: List<MovieCountries>,
    @SerializedName("release_date") val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Long
) : ViewTypeListener {
    var viewHolderId: Int? = null
    override fun getRecyclerItemLayoutId() = viewHolderId!!

}


data class MovieCollection(
    val id: Long,
    val name: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String

)

data class Genre(
    val id: Long,
    val name: String
)

data class MovieCompanies(
    val id: Long,
    val name: String,
    @SerializedName("logo_path") val logoPath: String,
    @SerializedName("origin_country") val originCountry: String
)

data class MovieCountries(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String,
    val iso_639_1: String,
    val name: String,
)