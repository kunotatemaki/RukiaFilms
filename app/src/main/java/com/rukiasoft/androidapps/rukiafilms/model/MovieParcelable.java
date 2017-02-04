
package com.rukiasoft.androidapps.rukiafilms.model;


import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class MovieParcelable implements Parcelable{

    public abstract String posterPath();
    public abstract Boolean adult();
    public abstract String overview();
    public abstract String releaseDate();
    public abstract List<Integer> genreIds();
    public abstract Integer id();
    public abstract String originalTitle();
    public abstract String originalLanguage();
    public abstract String title();
    public abstract String backdropPath();
    public abstract Double popularity();
    public abstract Integer voteCount();
    public abstract Boolean video();
    public abstract Double voteAverage();
    public abstract boolean favourite();
    @Nullable public abstract List<Review> reviews();

    public static MovieParcelable create(MovieData m) {
        return new AutoValue_MovieParcelable(m.getPosterPath(), m.getAdult(), m.getOverview(),
                m.getReleaseDate(), m.getGenreIds(), m.getId(), m.getOriginalTitle(),
                m.getOriginalLanguage(), m.getTitle(), m.getBackdropPath(), m.getPopularity(),
                m.getVoteCount(), m.getVideo(), m.getVoteAverage(), m.isFavourite(), m.getReviews());
    }

    public static MovieData extract(MovieParcelable m) {
        return new MovieData(m.posterPath(), m.adult(), m.overview(),
                m.releaseDate(), m.genreIds(), m.id(), m.originalTitle(),
                m.originalLanguage(), m.title(), m.backdropPath(), m.voteAverage(),
                m.voteCount(), m.video(), m.voteAverage(), m.favourite(), m.reviews());
    }


}
