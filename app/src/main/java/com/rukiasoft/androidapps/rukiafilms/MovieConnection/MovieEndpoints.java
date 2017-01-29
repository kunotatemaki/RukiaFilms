package com.rukiasoft.androidapps.rukiafilms.MovieConnection;

import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.model.MovieListResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by iRoll on 29/1/17.
 */

public interface MovieEndpoints {

    @GET("popular")
    Call<MovieListResponse> GetPopularMoviesPage(
            @QueryMap Map<String, String> params);

    @GET("top_rated")
    Call<MovieListResponse> GetTopRatedMoviesPage(
            @QueryMap Map<String, String> params);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
