package com.piyushkhairnar.flick.network.tvshows;

import com.google.gson.annotations.SerializedName;
import com.piyushkhairnar.flick.network.movies.MovieBrief;

import java.util.List;

public class TrendingTodayShowsResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<TVShowBrief> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    public TrendingTodayShowsResponse(Integer page, Integer totalResults, Integer totalPages, List<TVShowBrief> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<TVShowBrief> getResults() {
        return results;
    }

    public void setResults(List<TVShowBrief> results) {
        this.results = results;
    }

}
