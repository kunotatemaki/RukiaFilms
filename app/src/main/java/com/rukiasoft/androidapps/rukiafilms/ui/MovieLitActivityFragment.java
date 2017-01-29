package com.rukiasoft.androidapps.rukiafilms.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rukiasoft.androidapps.rukiafilms.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieLitActivityFragment extends Fragment {

    public MovieLitActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_lit, container, false);
    }
}
