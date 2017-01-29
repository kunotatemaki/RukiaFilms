package com.rukiasoft.androidapps.rukiafilms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by iRoll on 29/1/17.
 */

public class MovieListResponse {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("results")
    @Expose
    private List<MovieData> results;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<MovieData> getResults() {
        return results;
    }

    public void setResults(List<MovieData> results) {
        this.results = results;
    }
}
