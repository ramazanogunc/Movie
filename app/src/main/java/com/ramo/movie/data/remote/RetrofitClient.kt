package com.ramo.movie.data.remote

import com.ramo.movie.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object{

        @Volatile
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null)
                retrofit =
                    Retrofit.Builder().baseUrl(Constant.baseUrl).addConverterFactory(
                        GsonConverterFactory.create()).build()

            return retrofit as Retrofit
        }
        fun getMovieService(): MovieService {
            return getClient().create(MovieService::class.java)
        }
    }


}