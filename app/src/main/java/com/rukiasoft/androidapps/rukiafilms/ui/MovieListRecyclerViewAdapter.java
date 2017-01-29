/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rukiasoft.androidapps.rukiafilms.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.utils.RukiaFilmsConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.MovieViewHolder>
        implements View.OnClickListener {

    private final List<MovieData> mItems;
    private OnCardClickListener onCardClickListener;
    private final Context mContext;


    public MovieListRecyclerViewAdapter(Context context, List<MovieData> items) {
        this.mItems = new ArrayList<>(items);
        this.mContext = context;
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_recycler_item, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(v);
        movieViewHolder.cardView.setOnClickListener(this);

        return movieViewHolder;
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieData item = mItems.get(position);
        holder.bindMovie(mContext, item);
        holder.itemView.setTag(item);

    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    @Override public long getItemId(int position){
        return mItems.get(position).hashCode();
    }

    @Override
    public void onClick(final View v) {

        // Give some time to the ripple to finish the effect
        if (onCardClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    onCardClickListener.onCardClick(v, (MovieData) v.getTag());
                }
            }, 200);
        }
    }


    protected static class MovieViewHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.movie_pic_cardview) ImageView movieThumbnail;
        public @BindView(R.id.cardview_movie_item)
        FrameLayout cardView;
        private Unbinder unbinder;

        public MovieViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }

        public void bindMovie(Context mContext, MovieData item) {
            Glide.with(mContext)
                    .load(RukiaFilmsConstants.IMAGE_BASE_PATH + RukiaFilmsConstants.IMAGE_DIMEN +
                        item.getPosterPath())
                    .fitCenter()
                    .signature(new MediaStoreSignature(RukiaFilmsConstants.MIME_TYPE_PICTURE, 1, 0))
                    .error(R.drawable.splash_background)
                    .into(movieThumbnail);
        }

    }

    public interface OnCardClickListener {
        void onCardClick(View view, MovieData movieItem);
    }


}
