package com.rukiasoft.androidapps.rukiafilms.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rukiasoft.androidapps.rukiafilms.BuildConfig;
import com.rukiasoft.androidapps.rukiafilms.MovieConnection.MovieEndpoints;
import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.fastscroller.FastScroller;
import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.model.MovieListResponse;
import com.rukiasoft.androidapps.rukiafilms.utils.LogHelper;
import com.rukiasoft.androidapps.rukiafilms.utils.RukiaFilmsConstants;
import com.rukiasoft.androidapps.rukiafilms.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment implements MovieListRecyclerViewAdapter.OnCardClickListener{
    private final String TAG = LogHelper.makeLogTag(this.getClass());
    private final String KEY_SCROLL_POSITION = this.getClass().getPackage() + ".scrollposition";

    private List<MovieData> mPopularList;
    private List<MovieData> mTopRatedList;

    @Nullable
    @BindView(R.id.toolbar_movie_list_fragment)
    Toolbar mToolbarMovieListFragment;
    @BindView(R.id.movies_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;
    @Nullable @BindView((R.id.fastscroller))
    FastScroller fastScroller;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.movie_list_number_movies)
    TextView nMoviesInMovieList;
    @BindView(R.id.numberandtype_movies_bar)
    RelativeLayout numberAndTypeBar;
    @State
    int savedScrollPosition = 0;
    private int columnCount = 10;
    @State int order = RukiaFilmsConstants.ORDERED_BY_POPULARITY;
    @State boolean isDownloading = false;
    @State int pagePopularity = 1;
    @State int pageTopRated = 1;
    final Tools tools;

    private Unbinder unbinder;

    public MovieListActivityFragment() {
        tools = new Tools();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Set the mToolbarMovieListFragment
        if(getActivity() instanceof ToolbarAndProgressActivity){
            ((ToolbarAndProgressActivity) getActivity()).setToolbar(mToolbarMovieListFragment);
        }
        tools.setRefreshLayout(getActivity(), refreshLayout);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(0) && !isDownloading) {
                    isDownloading = true;
                    switch (order){
                        case RukiaFilmsConstants.ORDERED_BY_POPULARITY:
                            pagePopularity++;
                            getPopularListPage(pagePopularity);
                            break;
                        case RukiaFilmsConstants.ORDERED_BY_TOP_RATED:
                            pageTopRated++;
                            getTopRatedListPage(pageTopRated);
                            break;
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mRecyclerView != null) {
                if (mRecyclerView.getLayoutManager() != null) {
                    savedScrollPosition = ((GridLayoutManager) mRecyclerView.getLayoutManager())
                            .findFirstCompletelyVisibleItemPosition();
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        setMovies(order);
    }

    private void getPopularListPage(final Integer page){
        isDownloading = true;
        tools.showRefreshLayout(getActivity());
        MovieEndpoints movieEndpoints = MovieEndpoints.retrofit.create(MovieEndpoints.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", page.toString());
        params.put("api_key", BuildConfig.API_KEY);
        final Call<MovieListResponse> call =
                movieEndpoints.GetPopularMoviesPage(params);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                tools.hideRefreshLayout(getActivity());
                isDownloading = false;
                if(response.body() == null) return;
                List<MovieData> items = response.body().getResults();
                if(mPopularList.isEmpty()) {
                    setData(items);
                }else{
                    addData(items);
                }
                mPopularList.addAll(items);
            }
            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                tools.hideRefreshLayout(getActivity());
                isDownloading = false;
                Log.d(TAG, "Something went wrong: " + t.getMessage());
            }
        });
    }

    private void getTopRatedListPage(Integer page){
        isDownloading = true;
        tools.showRefreshLayout(getActivity());
        MovieEndpoints movieEndpoints = MovieEndpoints.retrofit.create(MovieEndpoints.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", page.toString());
        params.put("api_key", BuildConfig.API_KEY);
        final Call<MovieListResponse> call =
                movieEndpoints.GetTopRatedMoviesPage(params);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                tools.hideRefreshLayout(getActivity());
                isDownloading = false;
                if(response.body() == null) return;
                List<MovieData> items = response.body().getResults();
                if(mTopRatedList.isEmpty()) {
                    setData(items);
                }else{
                    addData(items);
                }
                mTopRatedList.addAll(items);

            }
            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                tools.hideRefreshLayout(getActivity());
                isDownloading = false;
                Log.d(TAG, "Something went wrong: " + t.getMessage());
            }
        });
    }

    public void setMovies(int order){
        this.order = order;
        switch (order){
            case RukiaFilmsConstants.ORDERED_BY_POPULARITY:
                if(mPopularList.isEmpty()){
                    pagePopularity = 1;
                   getPopularListPage(pagePopularity);
                }else{
                    setData(mPopularList);
                }
                break;
            case RukiaFilmsConstants.ORDERED_BY_TOP_RATED:
                if(mTopRatedList.isEmpty()){
                    pageTopRated = 1;
                   getTopRatedListPage(pageTopRated);
                }else{
                    setData(mTopRatedList);
                }
                break;
        }
    }

    private void setData(List<MovieData> mItems){

        if(isResumed()) {
            Tools tools = new Tools();
            tools.hideRefreshLayout(getActivity());
        }

        MovieListRecyclerViewAdapter adapter = new MovieListRecyclerViewAdapter(getActivity(), mItems);
        adapter.setHasStableIds(true);
        adapter.setOnCardClickListener(this);
        mRecyclerView.setAdapter(adapter);
        columnCount = getResources().getInteger(R.integer.list_column_count);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), columnCount);


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.scrollToPosition(savedScrollPosition);
        //Set the fast Scroller
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(fastScroller != null) {
                fastScroller.setRecyclerView(mRecyclerView);
            }
        }

        //set the number of recipes
        String nMovies = String.format(getResources().getString(R.string.movies), adapter.getItemCount());
        nMoviesInMovieList.setText(nMovies);


    }

    private  void addData(List<MovieData> items){
        MovieListRecyclerViewAdapter adapter = (MovieListRecyclerViewAdapter) mRecyclerView.getAdapter();
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
        String nMovies = String.format(getResources().getString(R.string.movies), adapter.getItemCount());
        nMoviesInMovieList.setText(nMovies);
    }

    @Override
    public void onCardClick(View view, MovieData movieItem) {
        /***/
    }



    public interface OnCardClickListener {
        void onCardClick(View view, MovieData movieItem);
    }


}
