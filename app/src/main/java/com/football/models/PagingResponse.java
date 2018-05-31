package com.football.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PagingResponse<T> implements Serializable {
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("per_page")
    private Integer perPage;
    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("last_page")
    private Integer lastPage;
    @JsonProperty("next_page_url")
    private String nextPageUrl;
    @JsonProperty("prev_page_url")
    private String prevPageUrl;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("data")
    private List<T> data;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PagingResponse{" +
                "total=" + total +
                ", perPage=" + perPage +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", prevPageUrl='" + prevPageUrl + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", data=" + data +
                '}';
    }
}
