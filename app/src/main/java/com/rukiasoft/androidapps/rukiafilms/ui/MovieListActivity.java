package com.rukiasoft.androidapps.rukiafilms.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.utils.RukiaFilmsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListActivity extends ToolbarAndProgressActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Unbinder unbinder;
    @BindView(R.id.navigation_view) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        unbinder = ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_popularity:
                showOrderedList(RukiaFilmsConstants.ORDERED_BY_POPULARITY);
                break;
            case R.id.navigation_top_rated:
                showOrderedList(RukiaFilmsConstants.ORDERED_BY_TOP_RATED);
                break;
            default:
                return false;
        }
        return true;
    }

    private void showOrderedList(int order){
        MovieListActivityFragment fragment = (MovieListActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if(fragment != null){
            fragment.setMovies(order);
        }
    }


}
