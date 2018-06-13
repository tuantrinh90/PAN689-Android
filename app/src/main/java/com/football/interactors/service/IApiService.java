package com.football.interactors.service;

import com.football.models.BaseResponse;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.FormResponse;
import com.football.models.responses.FriendResponse;
import com.football.models.responses.InviteResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.NewsResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PlayerStatisticResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.models.responses.StopResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.UploadResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.ServiceConfig;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    @POST(ServiceConfig.FORGOT_PASSWORD)
    Observable<BaseResponse<Object>> forgotPassword(@Body RequestBody body);

    @POST(ServiceConfig.REGISTER)
    Observable<BaseResponse<UserResponse>> register(@Body RequestBody body);

    @POST(ServiceConfig.RECOVER_PASSWORD)
    Observable<BaseResponse<Object>> recoverPassword(@Body RequestBody body);

    @GET(ServiceConfig.PENDING_INVITATIONS)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getPendingInvitations(@Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @POST(ServiceConfig.INVITATION_DECISION)
    Observable<BaseResponse<Object>> invitationDecision(@Path("id") int id, @Body RequestBody body);

    @GET(ServiceConfig.HOME_NEWS)
    Observable<BaseResponse<PagingResponse<NewsResponse>>> getHomeNews(@Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @GET(ServiceConfig.LEAGUE)
    Observable<BaseResponse<LeagueResponse>> getLeagueDetail(@Path(ServiceConfig.KEY_ID) int leagueId);

    @GET(ServiceConfig.TEAMS)
    Observable<BaseResponse<PagingResponse<TeamResponse>>> getTeams(@Query(ServiceConfig.KEY_LEAGUE_ID) int leagueId);

    @POST(ServiceConfig.REMOVE_TEAM)
    Observable<BaseResponse<Object>> removeTeam(@Path(ServiceConfig.KEY_ID) int leagueId, @Path(ServiceConfig.KEY_TEAM_ID) int teamId);

    @GET(ServiceConfig.SEARCH_FRIEND)
    Observable<BaseResponse<PagingResponse<FriendResponse>>> getInviteFriends(@Query(ServiceConfig.KEY_ID) int leagueId, @Query(Constant.KEY_WORD) String keyword,
                                                                              @Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @POST(ServiceConfig.INVITE_FRIEND)
    Observable<BaseResponse<InviteResponse>> inviteFriends(@Body RequestBody body);

    @POST(ServiceConfig.LEAVE_LEAGUES)
    Observable<BaseResponse<Object>> leaveLeagues(@Path("id") int leagueId, @Body RequestBody requestBody);

    @POST(ServiceConfig.STOP_LEAGUE)
    Observable<BaseResponse<StopResponse>> stopLeague(@Path(ServiceConfig.KEY_ID) int leagueId, @Body RequestBody body);

    @GET(ServiceConfig.MY_LEAGUES)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getMyLeagues(@Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @GET(ServiceConfig.OPEN_LEAGUES)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getOpenLeagues(@Query(Constant.KEY_ORDER_BY) String orderBy, @Query(Constant.KEY_PAGE) int page,
                                                                            @Query(Constant.KEY_PER_PAGE) int perPage,
                                                                            @Query(Constant.KEY_WORD) String query);

    @GET(ServiceConfig.PLAYER_LIST)
    Observable<BaseResponse<ExtPagingResponse<PlayerResponse>>> getPlayerList(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.FORM_OPTIONS)
    Observable<BaseResponse<FormResponse>> getFormOption(@Path(ServiceConfig.KEY_ID) int leagueId);

    @POST(ServiceConfig.UPLOAD)
    Observable<BaseResponse<UploadResponse>> upload(@Body RequestBody body);

    @POST(ServiceConfig.CREATE_LEAGUE)
    Observable<BaseResponse<LeagueResponse>> createLeague(@Body RequestBody body);

    @POST(ServiceConfig.UPDATE_LEAGUE)
    Observable<BaseResponse<LeagueResponse>> updateLeague(@Path(ServiceConfig.KEY_ID) int leagueId, @Body RequestBody body);

    @POST(ServiceConfig.CREATE_TEAM)
    Observable<BaseResponse<TeamResponse>> createTeam(@Body RequestBody body);

    @GET(ServiceConfig.LINEUP)
    Observable<BaseResponse<LineupResponse>> getLineup(@Path(ServiceConfig.KEY_ID) int leagueId);

    @POST(ServiceConfig.ADD_PLAYER)
    Observable<BaseResponse<PropsPlayerResponse>> addPlayer(@Body RequestBody body);

    @POST(ServiceConfig.REMOVE_PLAYER)
    Observable<BaseResponse<PropsPlayerResponse>> removePlayer(@Body RequestBody body);

    @GET(ServiceConfig.PLAYERS_STATISTIC)
    Observable<BaseResponse<PlayerStatisticResponse>> getPlayerStatistic(@Path(ServiceConfig.KEY_ID) Integer playerId, @Query("filter") String filter);
}
