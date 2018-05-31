package com.football.interactors.service;

import com.football.models.BaseResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.ServiceConfig;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IApiService {
    @POST(ServiceConfig.LOGIN)
    Observable<BaseResponse<UserResponse>> loginService(@Body RequestBody body);

    @POST(ServiceConfig.LOGIN_SOCIAL)
    Observable<BaseResponse<UserResponse>> loginSocial(@Body RequestBody body);

    @GET(ServiceConfig.LOGOUT)
    Observable<BaseResponse<UserResponse>> logout();

    @POST(ServiceConfig.REGISTER)
    Observable<BaseResponse<UserResponse>> register(@Body RequestBody body);

    @GET(ServiceConfig.PENDING_INVITATIONS)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getPendingInvitations(@Query("per_page") int perPage);

    @GET(ServiceConfig.LEAGUE)
    Observable<BaseResponse<LeagueResponse>> getLeagueDetail(@Path("league_id") int leagueId);
}
