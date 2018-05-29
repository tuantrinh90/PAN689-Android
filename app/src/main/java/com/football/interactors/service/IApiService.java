package com.football.interactors.service;

import com.football.models.BaseResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.ServiceConfig;

import io.reactivex.Observable;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IApiService {
    @POST(ServiceConfig.LOGIN)
    @Multipart
    Observable<BaseResponse<UserResponse>> loginService(@Part("email") String email, @Part("password") String password, @Part("device_token") String deviceToken);
}
