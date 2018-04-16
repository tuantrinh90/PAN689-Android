package com.football.interactors.service;

import java.util.List;

import rx.Observer;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IApiService {

    Observer<List<String>> getUsers();
}
