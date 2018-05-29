package com.football.models;

public class ErrorBody extends Throwable {
    private int status;
    private String message;

    public ErrorBody() {
    }

    public ErrorBody(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorBody{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
