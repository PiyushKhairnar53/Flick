package com.piyushkhairnar.flick.network.people;

import com.google.gson.annotations.SerializedName;
import com.piyushkhairnar.flick.network.movies.MovieBrief;

import java.util.List;

public class TrendingPerson7DaysResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<Person> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    public TrendingPerson7DaysResponse(Integer page, Integer totalResults, Integer totalPages, List<Person> results) {
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

    public List<Person> getResults() {
        return results;
    }

    public void setResults(List<Person> results) {
        this.results = results;
    }

}
