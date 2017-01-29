package com.rukiasoft.androidapps.rukiafilms.utils;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

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


}
