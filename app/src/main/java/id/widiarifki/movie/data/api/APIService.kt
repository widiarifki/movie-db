package id.widiarifki.movie.data.api

import id.widiarifki.movie.data.api.wrapper.ResponseGenre
import id.widiarifki.movie.data.api.wrapper.ResponseList
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.data.model.Video
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("genre/movie/list")
    suspend fun getGenre() : ResponseGenre

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int
    ): ResponseList<Movie>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): Response<Movie>

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): ResponseList<Video>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @Query("page") page: Int
    ): ResponseList<Review>
}