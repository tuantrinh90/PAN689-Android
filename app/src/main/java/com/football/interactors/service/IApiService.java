package com.football.interactors.service;

import com.football.models.BaseResponse;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.DraftCountdownResponse;
import com.football.models.responses.FormResponse;
import com.football.models.responses.FriendResponse;
import com.football.models.responses.InviteResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.MatchResponse;
import com.football.models.responses.NewsResponse;
import com.football.models.responses.NotificationResponse;
import com.football.models.responses.PickHistoryResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PlayerStatisticResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.models.responses.RankingResponse;
import com.football.models.responses.RealClubResponse;
import com.football.models.responses.RealMatchResponse;
import com.football.models.responses.SeasonResponse;
import com.football.models.responses.SettingsResponse;
import com.football.models.responses.StopResponse;
import com.football.models.responses.TeamPitchViewResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TeamSquadResponse;
import com.football.models.responses.TeamStatisticResponse;
import com.football.models.responses.TeamTransferringResponse;
import com.football.models.responses.TradeResponse;
import com.football.models.responses.TransferHistoryResponse;
import com.football.models.responses.UploadResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.ServiceConfig;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST(ServiceConfig.LOGOUT)
    Observable<BaseResponse<Object>> logout(@Body RequestBody body);

    @POST(ServiceConfig.FORGOT_PASSWORD)
    Observable<BaseResponse<Object>> forgotPassword(@Body RequestBody body);

    @POST(ServiceConfig.REGISTER)
    Observable<BaseResponse<UserResponse>> register(@Body RequestBody body);

    @GET(ServiceConfig.PENDING_INVITATIONS)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getPendingInvitations(@Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @POST(ServiceConfig.INVITATION_DECISION)
    Observable<BaseResponse<Object>> invitationDecision(@Path(ServiceConfig.KEY_ID) int id, @Body RequestBody body);

    @GET(ServiceConfig.HOME_NEWS)
    Observable<BaseResponse<PagingResponse<NewsResponse>>> getHomeNews(@Query(Constant.KEY_PAGE) int page, @Query(Constant.KEY_PER_PAGE) int perPage);

    @GET(ServiceConfig.REAL_MATCHES)
    Observable<BaseResponse<PagingResponse<RealMatchResponse>>> getRealMatches(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.TEAM_LIST)
    Observable<BaseResponse<PagingResponse<TeamResponse>>> getTeams(@Query(ServiceConfig.KEY_LEAGUE_ID) int leagueId);

    @GET(ServiceConfig.TEAM_DETAIL)
    Observable<BaseResponse<TeamResponse>> getTeamDetails(@Path(ServiceConfig.KEY_ID) int teamId);

    @GET(ServiceConfig.TEAM_SQUAD)
    Observable<BaseResponse<TeamSquadResponse>> getTeamSquad(@Path(ServiceConfig.KEY_ID) int teamId, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.TEAM_STATISTIC)
    Observable<BaseResponse<TeamStatisticResponse>> getTeamStatistic(@Path(ServiceConfig.KEY_ID) int teamId);

    @GET(ServiceConfig.TEAM_PITCH_VIEW)
    Observable<BaseResponse<TeamPitchViewResponse>> getPitchView(@Path(ServiceConfig.KEY_ID) int teamId, @QueryMap Map<String, String> queries);

    @POST(ServiceConfig.TEAM_PITCH_VIEW)
    Observable<BaseResponse<TeamPitchViewResponse>> updatePitchView(@Path(ServiceConfig.KEY_ID) int playerId, @Body RequestBody requestBody, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.TRADE_REQUESTS)
    Observable<BaseResponse<PagingResponse<TradeResponse>>> getTradeRequests(@QueryMap Map<String, String> queries);

    @POST(ServiceConfig.TRADE_REQUESTS)
    Observable<BaseResponse<TradeResponse>> submitTradeRequests(@Body RequestBody requestBody);

    @GET(ServiceConfig.TRADE_REVIEW)
    Observable<BaseResponse<PagingResponse<TradeResponse>>> getTradeReviews(@QueryMap Map<String, String> queries);

    @POST(ServiceConfig.TRADE_DECISION)
    Observable<BaseResponse<TradeResponse>> submitTradeDecision(@Path(ServiceConfig.KEY_ID) int requestId, @Body RequestBody requestBody);

    @POST(ServiceConfig.TRADE_CANCEL)
    Observable<BaseResponse<TradeResponse>> cancelTradeDecision(@Path(ServiceConfig.KEY_ID) int requestId, @Body RequestBody requestBody);


    @POST(ServiceConfig.CHANGE_TEAM_FORMATION)
    Observable<BaseResponse<TeamPitchViewResponse>> changeTeamFormation(@Path(ServiceConfig.KEY_ID) int teamId, @Body RequestBody requestBody);

    @POST(ServiceConfig.REMOVE_TEAM)
    Observable<BaseResponse<Object>> removeTeam(@Path(ServiceConfig.KEY_ID) int leagueId, @Path(ServiceConfig.KEY_TEAM_ID) int teamId);

    @POST(ServiceConfig.START_LEAGUE)
    Observable<BaseResponse<LeagueResponse>> startLeague(@Path(ServiceConfig.KEY_ID) int leagueId);

    @GET(ServiceConfig.SEARCH_FRIEND)
    Observable<BaseResponse<PagingResponse<FriendResponse>>> getInviteFriends(@QueryMap Map<String, String> queries);

    @POST(ServiceConfig.INVITE_FRIEND)
    Observable<BaseResponse<InviteResponse>> inviteFriends(@Body RequestBody body);

    @POST(ServiceConfig.LEAVE_LEAGUES)
    Observable<BaseResponse<Object>> leaveLeague(@Path(ServiceConfig.KEY_ID) int leagueId, @Body RequestBody requestBody);

    @POST(ServiceConfig.LEAVE_LEAGUES)
    Observable<BaseResponse<Object>> leaveLeague(@Path(ServiceConfig.KEY_ID) int leagueId);

    @DELETE(ServiceConfig.STOP_LEAGUE)
    Observable<BaseResponse<StopResponse>> stopLeague(@Path(ServiceConfig.KEY_ID) int leagueId);

    @GET(ServiceConfig.MY_LEAGUES)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getMyLeagues(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.MY_MATCH_RESULTS)
    Observable<BaseResponse<PagingResponse<MatchResponse>>> getMyMatchResults(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.MATCH_RESULTS)
    Observable<BaseResponse<PagingResponse<MatchResponse>>> getMatchResults(@Path(ServiceConfig.KEY_ID) int leagueId, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.TEAM_RESULTS)
    Observable<BaseResponse<PagingResponse<RankingResponse>>> getTeamResults(@Path(ServiceConfig.KEY_ID) int leagueId, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.PICK_HISTORIES)
    Observable<BaseResponse<PagingResponse<PickHistoryResponse>>> getPickHistories(@Path(ServiceConfig.KEY_ID) int leagueId, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.END_COUNT_DOWN)
    Observable<BaseResponse<DraftCountdownResponse>> endCountdown(@Path(ServiceConfig.KEY_ID) int leagueId);

    @GET(ServiceConfig.JOIN_DRAFT_PICK)
    Observable<BaseResponse<Object>> joinDraftPick(@Path(ServiceConfig.KEY_ID) int leagueId);

    @GET(ServiceConfig.LEAGUE_DETAIL)
    Observable<BaseResponse<LeagueResponse>> getLeagueDetail(@Path(ServiceConfig.KEY_ID) Integer leagueId);

    @GET(ServiceConfig.OPEN_LEAGUES)
    Observable<BaseResponse<PagingResponse<LeagueResponse>>> getOpenLeagues(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.JOINT)
    Observable<BaseResponse<LeagueResponse>> join(@Path(ServiceConfig.KEY_ID) Integer leagueId);

    @GET(ServiceConfig.PLAYER_LIST)
    Observable<BaseResponse<ExtPagingResponse<PlayerResponse>>> getPlayerList(@QueryMap Map<String, String> queries);

    @GET(ServiceConfig.REAL_CLUB)
    Observable<BaseResponse<PagingResponse<RealClubResponse>>> getRealClubs();


    @GET(ServiceConfig.SEASONS)
    Observable<BaseResponse<PagingResponse<SeasonResponse>>> getSeasons();

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

    @POST(ServiceConfig.UPDATE_TEAM)
    Observable<BaseResponse<TeamResponse>> updateTeam(@Path(ServiceConfig.KEY_ID) int teamId, @Body RequestBody body);

    @GET(ServiceConfig.TEAM_LINEUP)
    Observable<BaseResponse<LineupResponse>> getLineup(@Path(ServiceConfig.KEY_ID) int teamId);

    @POST(ServiceConfig.COMPLETE_LINEUP)
    Observable<BaseResponse<Object>> completeLineup(@Path(ServiceConfig.KEY_ID) int teamId);

    @POST(ServiceConfig.ADD_PLAYER)
    Observable<BaseResponse<PropsPlayerResponse>> addPlayer(@Body RequestBody body);

    @POST(ServiceConfig.REMOVE_PLAYER)
    Observable<BaseResponse<PropsPlayerResponse>> removePlayer(@Body RequestBody body);

    @GET(ServiceConfig.TEAM_TRANSFERRING)
    Observable<BaseResponse<TeamTransferringResponse>> getTeamTransferring(@Path(ServiceConfig.KEY_ID) int teamId,
                                                                           @QueryMap Map<String, String> queries);

    @POST(ServiceConfig.TRANSFER_PLAYER)
    Observable<BaseResponse<Object>> transferPlayer(@Path(ServiceConfig.KEY_ID) int teamId,
                                                    @Body RequestBody body);

    @GET(ServiceConfig.TRANSFER_HISTORIES)
    Observable<BaseResponse<PagingResponse<TransferHistoryResponse>>> getTransferHistories(@Path(ServiceConfig.KEY_ID) int teamId,
                                                                                           @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.PLAYER_DETAIL)
    Observable<BaseResponse<PlayerResponse>> getPlayerDetail(@Path(ServiceConfig.KEY_ID) int playerId);

    @GET(ServiceConfig.PLAYER_STATISTIC)
    Observable<BaseResponse<PlayerStatisticResponse>> getPlayerStatistic(@Path(ServiceConfig.KEY_ID) Integer playerId, @QueryMap Map<String, String> queries);

    @GET(ServiceConfig.PLAYER_STATISTIC_WITH_TEAM)
    Observable<BaseResponse<PlayerStatisticResponse>> getPlayerStatisticWithTeam(@Path(ServiceConfig.KEY_PLAYER_ID) int playerId,
                                                                                 @Path(ServiceConfig.KEY_TEAM_ID) int teamId,
                                                                                 @QueryMap Map<String, String> queries);

    /* user */
    @GET(ServiceConfig.GET_PROFILE)
    Observable<BaseResponse<UserResponse>> getProfile();

    @POST(ServiceConfig.UPDATE_PROFILE)
    Observable<BaseResponse<UserResponse>> updateProfile(@Body RequestBody body);

    @POST(ServiceConfig.CHANGE_PASSWORD)
    Observable<BaseResponse<Object>> changePassword(@Body RequestBody body);

    @GET(ServiceConfig.SETTINGS)
    Observable<BaseResponse<SettingsResponse>> getSettings();

    @POST(ServiceConfig.SETTINGS)
    Observable<BaseResponse<SettingsResponse>> changeSettings(@Body RequestBody body);

    @POST(ServiceConfig.DEVICE_OF_USER)
    Observable<BaseResponse<Object>> updateDeviceOfUser(@Body RequestBody body);

    @GET(ServiceConfig.NOTIFICATIONS)
    Observable<BaseResponse<PagingResponse<NotificationResponse>>> getNotifications(@QueryMap Map<String, String> queries);
}
