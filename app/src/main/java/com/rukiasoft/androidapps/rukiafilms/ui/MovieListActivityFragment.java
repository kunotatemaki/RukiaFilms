package com.rukiasoft.androidapps.rukiafilms.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rukiasoft.androidapps.rukiafilms.BuildConfig;
import com.rukiasoft.androidapps.rukiafilms.MovieConnection.MovieEndpoints;
import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.model.MovieListResponse;
import com.rukiasoft.androidapps.rukiafilms.utils.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment {
    private final String TAG = LogHelper.makeLogTag(this.getClass());
    private List<MovieData> mPopularList;
    private List<MovieData> mTopRatedList;

    public MovieListActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mPopularList == null){
            mPopularList = new ArrayList<>();
        }
        if(mTopRatedList == null){
            mTopRatedList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPopularList.isEmpty()){
            getPopularListPage(2);
        }
    }

    private void getPopularListPage(Integer page){
        MovieEndpoints movieEndpoints = MovieEndpoints.retrofit.create(MovieEndpoints.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", page.toString());
        params.put("api_key", BuildConfig.API_KEY);
        final Call<MovieListResponse> call =
                movieEndpoints.GetPopularMoviesPage(params);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                mPopularList.addAll(response.body().getResults());
            }
            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Log.d(TAG, "Something went wrong: " + t.getMessage());
            }
        });
    }

    private void getTopRatedListPage(Integer page){

        MovieEndpoints movieEndpoints = MovieEndpoints.retrofit.create(MovieEndpoints.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", page.toString());
        params.put("api_key", BuildConfig.API_KEY);
        final Call<MovieListResponse> call =
                movieEndpoints.GetTopRatedMoviesPage(params);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                mTopRatedList.addAll(response.body().getResults());
            }
            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Log.d(TAG, "Something went wrong: " + t.getMessage());
            }
        });
    }


}
