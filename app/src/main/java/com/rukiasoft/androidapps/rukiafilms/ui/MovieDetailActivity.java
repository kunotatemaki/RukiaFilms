package com.rukiasoft.androidapps.rukiafilms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.model.MovieParcelable;
import com.rukiasoft.androidapps.rukiafilms.utils.RukiaFilmsConstants;



import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailActivity extends ToolbarAndProgressActivity {



    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        unbinder = ButterKnife.bind(this);
        MovieParcelable movieParcelable = null;
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(RukiaFilmsConstants.KEY_MOVIE))
            movieParcelable = intent.getParcelableExtra(RukiaFilmsConstants.KEY_MOVIE);
        else{
            finish();
        }
        MovieData movieData = MovieParcelable.extract(movieParcelable);
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_movie_fragment);
        if(movieDetailsFragment != null){
            movieDetailsFragment.setMovie(movieData);
        }else{
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


}
