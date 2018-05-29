package com.football.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("code")
    private Integer code;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
