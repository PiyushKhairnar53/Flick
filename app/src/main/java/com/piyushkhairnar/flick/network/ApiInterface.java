package com.piyushkhairnar.flick.network;

import com.piyushkhairnar.flick.network.movies.Trending7DaysMoviesResponse;
import com.piyushkhairnar.flick.network.movies.TrendingTodayMoviesResponse;
import com.piyushkhairnar.flick.network.movies.GenresList;
import com.piyushkhairnar.flick.network.movies.Movie;
import com.piyushkhairnar.flick.network.movies.MovieCastsOfPersonResponse;
import com.piyushkhairnar.flick.network.movies.MovieCreditsResponse;
import com.piyushkhairnar.flick.network.movies.NowShowingMoviesResponse;
import com.piyushkhairnar.flick.network.movies.PopularMoviesResponse;
import com.piyushkhairnar.flick.network.movies.SimilarMoviesResponse;
import com.piyushkhairnar.flick.network.movies.BollywoodTopRatedMoviesResponse;
import com.piyushkhairnar.flick.network.movies.UpcomingMoviesResponse;
import com.piyushkhairnar.flick.network.people.Person;
import com.piyushkhairnar.flick.network.tvshows.AiringTodayTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.OnTheAirTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.PopularTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.SimilarTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.TVCastsOfPersonResponse;
import com.piyushkhairnar.flick.network.tvshows.TVShow;
import com.piyushkhairnar.flick.network.tvshows.TVShowCreditsResponse;
import com.piyushkhairnar.flick.network.tvshows.TopRatedTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.TrendingTodayShowsResponse;
import com.piyushkhairnar.flick.network.videos.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //MOVIES

    @GET("trending/movie/day")
    Call<TrendingTodayMoviesResponse> getTrendingMoviesDay(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("trending/movie/week")
    Call<Trending7DaysMoviesResponse> getTrendingMovies7Days(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("trending/tv/day")
    Call<TrendingTodayShowsResponse> getTrendingShowsToday(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("trending/tv/week")
    Call<TrendingTodayShowsResponse> getTrendingShows7Days(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("trending/person/week")
    Call<Trending7DaysMoviesResponse> getTrendingPeople(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("movie/now_playing")
    Call<NowShowingMoviesResponse> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/upcoming")
    Call<UpcomingMoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/top_rated")
    Call<BollywoodTopRatedMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);


    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<MovieCreditsResponse> getMovieCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<SimilarMoviesResponse> getSimilarMovies(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/movie/list")
    Call<GenresList> getMovieGenresList(@Query("api_key") String apiKey);

    //TV SHOWS

    @GET("tv/airing_today")
    Call<AiringTodayTVShowsResponse> getAiringTodayTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/on_the_air")
    Call<OnTheAirTVShowsResponse> getOnTheAirTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/popular")
    Call<PopularTVShowsResponse> getPopularTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/top_rated")
    Call<TopRatedTVShowsResponse> getTopRatedTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/{id}")
    Call<TVShow> getTVShowDetails(@Path("id") Integer tvShowId, @Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Call<VideosResponse> getTVShowVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Call<TVShowCreditsResponse> getTVShowCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/similar")
    Call<SimilarTVShowsResponse> getSimilarTVShows(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/tv/list")
    Call<com.piyushkhairnar.flick.network.tvshows.GenresList> getTVShowGenresList(@Query("api_key") String apiKey);

    //PERSON

    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastsOfPersonResponse> getMovieCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Call<TVCastsOfPersonResponse> getTVCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);

}
