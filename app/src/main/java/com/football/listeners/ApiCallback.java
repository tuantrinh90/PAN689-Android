package com.football.listeners;

public abstract class ApiCallback<T> {
    public void onStart() {
    }

    public void onComplete() {
    }

    public abstract void onSuccess(T t);

    public abstract void onError(String error);
}
