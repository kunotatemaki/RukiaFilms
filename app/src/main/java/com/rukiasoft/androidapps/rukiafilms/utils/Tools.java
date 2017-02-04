package com.rukiasoft.androidapps.rukiafilms.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.rukiasoft.androidapps.rukiafilms.ui.ToolbarAndProgressActivity;

/**
 * Created by iRoll on 29/1/17.
 */

public class Tools {

    /**
     * set the refresh layout to be shown in the activity
     * @param activity activity having refresh layout
     * @param refreshLayout refresh layout
     */
    public void setRefreshLayout(Activity activity, SwipeRefreshLayout refreshLayout){
        if(activity instanceof ToolbarAndProgressActivity) {
            ((ToolbarAndProgressActivity) activity).setRefreshLayout(refreshLayout);
            ((ToolbarAndProgressActivity) activity).disableRefreshLayoutSwipe();
        }
    }



    /**
     * set the refresh layout to be shown in the activity
     * @param activity activity having refresh layout
     */
    public void showRefreshLayout(Activity activity){
        if(activity instanceof ToolbarAndProgressActivity) {
            ((ToolbarAndProgressActivity) activity).showRefreshLayoutSwipeProgress();
        }
    }

    /**
     * set the refresh layout to be hidden in the activity
     * @param activity activity having refresh layout
     */
    public void hideRefreshLayout(Activity activity){
        if(activity instanceof ToolbarAndProgressActivity) {
            ((ToolbarAndProgressActivity) activity).hideRefreshLayoutSwipeProgress();
        }
    }
    public void loadImageFromPath(Context mContext, ImageView imageView, String path, int defaultImage, int version) {
        Glide.with(mContext)
                .load(Uri.parse(path))
                .centerCrop()
                .signature(new MediaStoreSignature(RukiaFilmsConstants.MIME_TYPE_PICTURE, version, 0))
                .error(defaultImage)
                .into(imageView);
    }

    public void loadImageFromPath(Context mContext, BitmapImageViewTarget bitmapImageViewTarget, String path, int defaultImage, int version) {
        Glide.with(mContext)
                .load(Uri.parse(path))
                .asBitmap()
                .signature(new MediaStoreSignature(RukiaFilmsConstants.MIME_TYPE_PICTURE, version, 0))
                .centerCrop()
                .error(defaultImage)
                .into(bitmapImageViewTarget);
    }



}
