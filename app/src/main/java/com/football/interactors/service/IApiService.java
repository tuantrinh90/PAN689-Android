package com.football.interactors.service;

import com.football.models.BaseResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.ServiceConfig;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IApiService {
    @POST(ServiceConfig.LOGIN)
    @Multipart
    Observable<BaseResponse<UserResponse>> login(@Part("email") String email,
                                                 @Part("password") String password,
                                                 @Part("device_token") String deviceToken);

    @POST(ServiceConfig.LOGIN_SOCIAL)
    @Multipart
    Observable<BaseResponse<UserResponse>> loginSocial(@Part("provider") String provider,
                                                       @Part("access_token") String accessToken,
                                                       @Part("device_token") String deviceToken);

    @GET(ServiceConfig.LOGOUT)
    Observable<BaseResponse<UserResponse>> logout(@Header("Authorization") String token);

    @POST(ServiceConfig.REGISTER)
    @Multipart
    Observable<BaseResponse<UserResponse>> register(@Part("first_name") String firstName,
                                                    @Part("last_name") String lastName,
                                                    @Part("email") String email,
                                                    @Part("password") String password,
                                                    @Part("password_confirmation") String repassword);


}
