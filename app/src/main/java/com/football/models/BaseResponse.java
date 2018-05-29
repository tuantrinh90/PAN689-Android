package com.football.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse<T> {
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("response")
    private T response;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return meta != null && meta.getSuccess();
    }

    @JsonIgnore
    public String getMessage() {
        return meta != null ? meta.getMessage() : "";
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "meta=" + meta +
                ", response=" + response +
                '}';
    }
}
