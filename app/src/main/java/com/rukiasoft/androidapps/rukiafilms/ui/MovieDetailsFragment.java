package com.rukiasoft.androidapps.rukiafilms.ui;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.rukiasoft.androidapps.rukiafilms.BuildConfig;
import com.rukiasoft.androidapps.rukiafilms.MovieConnection.MovieEndpoints;
import com.rukiasoft.androidapps.rukiafilms.R;
import com.rukiasoft.androidapps.rukiafilms.model.MovieData;
import com.rukiasoft.androidapps.rukiafilms.model.MovieParcelable;
import com.rukiasoft.androidapps.rukiafilms.model.Review;
import com.rukiasoft.androidapps.rukiafilms.model.ReviewsParent;
import com.rukiasoft.androidapps.rukiafilms.utils.LogHelper;
import com.rukiasoft.androidapps.rukiafilms.utils.RukiaFilmsConstants;
import com.rukiasoft.androidapps.rukiafilms.utils.Tools;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieDetailsFragment extends Fragment implements
        AppBarLayout.OnOffsetChangedListener{
    private static final String TAG = LogHelper.makeLogTag(MovieDetailsFragment.class);
    private static final float PERCENTAGE_TO_ELLIPSIZE_TITLE  = 0.1f;

    private static final String KEY_SAVE_MOVIE = MovieDetailsFragment.class.getPackage() + "." + MovieDetailsFragment.class.getSimpleName() + ".saverecipe";


    @BindView(R.id.image_movie_detail_poster) ImageView poster;
    @BindView(R.id.text_movie_original_title) TextView originalTitle;
    @BindView(R.id.text_movie_release_date) TextView releaseDate;
    @BindView(R.id.text_movie_vote_average) TextView voteAverage;
    @BindView(R.id.overview_body_cardview) TextView overview;
    @BindView(R.id.card_review)
    CardView cardReview;
    @BindView(R.id.movie_background) ImageView background;
    @Nullable@BindView(R.id.appbarlayout_movie_details)
    AppBarLayout mAppBarLayout;
    @Nullable@BindView(R.id.photo_container_movie_details)
    RelativeLayout photoContainer;
    @BindView(R.id.toolbar_movie_details)Toolbar toolbarMovieDetails;
    @BindView(R.id.movie_title_movie_details) TextView movieName;
    @BindView(R.id.movie_description_fab)
    FloatingActionButton movieDescriptionFAB;
    @Nullable@BindView(R.id.collapsing_toolbar_movie_details)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.listview_reviews_cardview)
    LinearLayout reviewList;
    private Unbinder unbinder;

    MovieData movie;
    @State MovieParcelable movieParcelable;
    //@State boolean movieLoaded = false;
    ActionBar actionBar;
    @State boolean land;
    @State boolean animated;
    private View viewToReveal;



    private boolean collapsed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().supportPostponeEnterTransition();
        }

    }



    private final Runnable scaleIn = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            movieDescriptionFAB.animate().setDuration(250)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .withEndAction(scaleOut);
        }
    };

    private final Runnable scaleOut = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            movieDescriptionFAB.animate().setDuration(250)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .scaleX(1.0f)
                    .scaleY(1.0f);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        land = getResources().getBoolean(R.bool.land);



        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarMovieDetails);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(!land);
        }

        if(mAppBarLayout != null){
            mAppBarLayout.addOnOffsetChangedListener(this);
        }

        if(movieDescriptionFAB != null) {

            movieDescriptionFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clickOnHeartButton();
                            }
                        }, 150);
                    }else{
                        clickOnHeartButton();
                    }
                }
            });
        }

        if(movieParcelable != null){
            movie = MovieParcelable.extract(movieParcelable);
        }

        if(movie != null){
            //movie.getReviews() == null? getReviews() : loadMovie();
            loadMovie();
        }
        if(animated){
            return mRootView;
        }
        //create de reveal effect either for landscape and portrait
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!land && mAppBarLayout != null) {
                viewToReveal = mAppBarLayout;
            } else if (land && photoContainer != null) {
                viewToReveal = photoContainer;
                collapsed = false;
            }
            viewToReveal.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    Animator animator = ViewAnimationUtils.createCircularReveal(
                            viewToReveal,
                            viewToReveal.getWidth() / 2,
                            viewToReveal.getHeight() / 2,
                            0,
                            (float) Math.hypot(viewToReveal.getWidth(), viewToReveal.getHeight()) / 2);
                    // Set a natural ease-in/ease-out interpolator.
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());

                    // make the view visible and start the animation
                    if (!collapsed) {
                        animator.start();
                        animated = true;
                    }
                }
            });

        }
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void clickOnHeartButton(){
        if (movie.isFavourite()) {
            movieDescriptionFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.ic_favorite_outline_white_24dp));
        } else {
            movieDescriptionFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.ic_favorite_white_24dp));
        }
        // TODO: 4/2/17 almacenar en la base de datos
        movie.setFavourite(!movie.isFavourite());
        movieParcelable = MovieParcelable.create(movie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            scaleIn.run();
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        if(land){
            collapsed = false;
            return;
        }
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        collapsed = percentage == 1;
        handleTitleBehavior(percentage);
        //handleToolbarTitleVisibility(percentage);

    }

    private void handleTitleBehavior(float percentage) {
        if (percentage >= PERCENTAGE_TO_ELLIPSIZE_TITLE) {
            movieName.setVisibility(View.GONE);
        }else{
            movieName.setVisibility(View.VISIBLE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                movieName.setAlpha(1 - percentage / PERCENTAGE_TO_ELLIPSIZE_TITLE);
            }
        }
    }

    public void setMovie(MovieData movie) {
        if(movieParcelable != null) return;
        this.movie = movie;
        movieParcelable = MovieParcelable.create(movie);
        getReviews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadMovie(){
        //if(movieLoaded) return;
        Tools tools = new Tools();
        movieName.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        originalTitle.setText(movie.getOriginalTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(movie.getVoteAverage().toString());

        if(actionBar != null){
            actionBar.setTitle(movie.getTitle());
        }
        if(movieDescriptionFAB != null){
            if (movie.isFavourite()) {
                movieDescriptionFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white_24dp));
            } else {
                movieDescriptionFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_outline_white_24dp));
            }
        }
        if(background != null){
            BitmapImageViewTarget bitmapImageViewTarget = new BitmapImageViewTarget(background) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                    super.onResourceReady(bitmap, anim);
                    applyPalette(bitmap);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_dish);
                    applyPalette(bitmap);
                }
            };


            Glide.with(getContext())
                    .load(RukiaFilmsConstants.IMAGE_BASE_PATH + RukiaFilmsConstants.IMAGE_DIMEN +
                            movie.getBackdropPath())
                    .asBitmap()
                    .centerCrop()
                    //.signature(new MediaStoreSignature(RukiaFilmsConstants.MIME_TYPE_PICTURE, 1, 0))
                    .error(R.drawable.splash_background)
                    .into(bitmapImageViewTarget);
        }

        Glide.with(getContext())
                .load(RukiaFilmsConstants.IMAGE_BASE_PATH + RukiaFilmsConstants.IMAGE_DIMEN +
                        movie.getPosterPath())
                .fitCenter()
                //.signature(new MediaStoreSignature(RukiaFilmsConstants.MIME_TYPE_PICTURE, 1, 0))
                .error(R.drawable.splash_background)
                .into(poster);




        //set ingredients and steps
        reviewList.removeAllViews();
        if(movie.getReviews() == null) return;
        for(Review review : movie.getReviews()){
            LayoutInflater inflater;
            inflater = (LayoutInflater) getActivity()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View reviewItem = inflater.inflate(R.layout.movie_review_item, null);
            TextView reviewTextview = (TextView) reviewItem.findViewById(R.id.movie_review_item_description);
            reviewTextview.setText(review.getContent());
            TextView authorTextview = (TextView) reviewItem.findViewById(R.id.movie_author_review_item_description);
            authorTextview.setText(review.getAuthor());

            reviewList.addView(reviewItem);
        }

    }

    private void applyPalette(Bitmap bitmap){
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if(palette == null)
                    return;
                try {
                    int mVibrantColor = palette.getVibrantColor(ContextCompat.getColor(getActivity(), R.color.ColorPrimary));
                    //int mVibrantDarkColor = palette.getDarkVibrantColor(mVibrantColor);
                    int mMutedColor = palette.getMutedColor(ContextCompat.getColor(getActivity(), R.color.ColorAccent));
                    int mMutedDarkColor = palette.getDarkMutedColor(mMutedColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getActivity().getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(mMutedDarkColor);
                    }
                    if (collapsingToolbarLayout != null) {
                        collapsingToolbarLayout.setContentScrim(new ColorDrawable(mMutedColor));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        movieDescriptionFAB.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mVibrantColor}));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(getActivity() != null) {
                        getActivity().supportStartPostponedEnterTransition();
                    }
                }

            }
        });

    }

    private void getReviews(){
        final MovieEndpoints movieEndpoints = MovieEndpoints.retrofit.create(MovieEndpoints.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("api_key", BuildConfig.API_KEY);
        final Call<ReviewsParent> call =
                movieEndpoints.GetMovieReviews(movie.getId().toString(), params);

        call.enqueue(new Callback<ReviewsParent>() {
            @Override
            public void onResponse(Call<ReviewsParent> call, Response<ReviewsParent> response) {
                if(response.body() == null) return;
                ReviewsParent resp = response.body();
                movie.setReviews(resp.getResults());
                movieParcelable = MovieParcelable.create(movie);
                loadMovie();

            }
            @Override
            public void onFailure(Call<ReviewsParent> call, Throwable t) {

                Log.d(TAG, "Something went wrong: " + t.getMessage());
            }
        });
    }

}
