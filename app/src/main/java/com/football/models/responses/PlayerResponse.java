package com.football.models.responses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.fantasy.R;
import com.football.utilities.AppUtilities;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.football.models.responses.PlayerResponse.Options.ASSISTS;
import static com.football.models.responses.PlayerResponse.Options.BALLS_RECOVERED;
import static com.football.models.responses.PlayerResponse.Options.CLEAN_SHEET;
import static com.football.models.responses.PlayerResponse.Options.DRIBBLES;
import static com.football.models.responses.PlayerResponse.Options.DUELS_THEY_WIN;
import static com.football.models.responses.PlayerResponse.Options.FOULS_COMMITTED;
import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.PASSES;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.SAVES;
import static com.football.models.responses.PlayerResponse.Options.SHOTS;
import static com.football.models.responses.PlayerResponse.Options.TURNOVERS;
import static com.football.models.responses.PlayerResponse.Options.VALUE;
import static com.football.models.responses.PlayerResponse.Options.YELLOW_CARDS;

public class PlayerResponse implements Serializable {
    /**
     * transfer_value: Value
     * point: Point
     * goals: Stat 1
     * assists: Stat 2
     * clean_sheet: Stat 3
     * duels_they_win: Stat 4
     * passes: Stat 5
     * shots: Stat 6
     * saves: Stat 7
     * yellow_cards: Stat 8
     * dribbles: Stat 9
     * turnovers: Stat 10
     * balls_recovered: Stat 11
     * fouls_committed: Stat 12
     */
    @StringDef({VALUE, POINT, GOALS, ASSISTS, CLEAN_SHEET, DUELS_THEY_WIN, PASSES, SHOTS,
            SAVES, YELLOW_CARDS, DRIBBLES, TURNOVERS, BALLS_RECOVERED, FOULS_COMMITTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Options {
        String VALUE = "value";
        String POINT = "point";
        String GOALS = "goals";
        String ASSISTS = "assists";
        String CLEAN_SHEET = "clean_sheet";
        String DUELS_THEY_WIN = "duels_they_win";
        String PASSES = "passes";
        String SHOTS = "shots";
        String SAVES = "saves";
        String YELLOW_CARDS = "yellow_cards";
        String DRIBBLES = "dribbles";
        String TURNOVERS = "turnovers";
        String BALLS_RECOVERED = "balls_recovered";
        String FOULS_COMMITTED = "fouls_committed";
    }

    public static final int POSITION_NONE = -1;
    public static final int POSITION_GOALKEEPER = 0;
    public static final int POSITION_DEFENDER = 1;
    public static final int POSITION_MIDFIELDER = 2;
    public static final int POSITION_ATTACKER = 3;

    @JsonProperty("id")
    private int id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("real_club_id")
    private int realClubId;
    @JsonProperty("real_club")
    private RealClubResponse realClub;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("is_injured")
    private Boolean isInjured;
    @JsonProperty("is_goalkeeper")
    private Boolean isGoalkeeper;
    @JsonProperty("is_defender")
    private Boolean isDefender;
    @JsonProperty("is_midfielder")
    private Boolean isMidfielder;
    @JsonProperty("is_attacker")
    private Boolean isAttacker;
    @JsonProperty("main_position")
    private int mainPosition;
    @JsonProperty("minor_position")
    private int minorPosition;
    @JsonProperty("transfer_value")
    private Long transferValue;
    @JsonProperty("point_last_round")
    private int pointLastRound;
    @JsonProperty("is_selected")
    private boolean isSelected;
    @JsonProperty("goals")
    private int goals;
    @JsonProperty("assists")
    private int assists;
    @JsonProperty("clean_sheet")
    private int cleanSheet;
    @JsonProperty("duels_they_win")
    private int duelsTheyWin;
    @JsonProperty("passes")
    private int passes;
    @JsonProperty("shots")
    private int shots;
    @JsonProperty("saves")
    private int saves;
    @JsonProperty("yellow_cards")
    private int yellowCards;
    @JsonProperty("dribbles")
    private int dribbles;
    @JsonProperty("turnovers")
    private int turnovers;
    @JsonProperty("balls_recovered")
    private int ballsRecovered;
    @JsonProperty("fouls_committed")
    private int foulsCommitted;
    @JsonProperty("point")
    private int point;
    @JsonProperty("position")
    private int position;
    @JsonProperty("order")
    private Integer order;
    @JsonProperty("is_trading")
    private boolean isTrading;

    public PlayerResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getRealClubId() {
        return realClubId;
    }

    public void setRealClubId(int realClubId) {
        this.realClubId = realClubId;
    }

    public RealClubResponse getRealClub() {
        return realClub;
    }

    public void setRealClub(RealClubResponse realClub) {
        this.realClub = realClub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getInjured() {
        return isInjured == null ? false : isInjured;
    }

    public void setInjured(Boolean injured) {
        isInjured = injured;
    }

    @JsonIgnore
    public String getInjuredText(Context context) {
        return getInjured() ? context.getString(R.string.injured) : "";
    }

    public int getMainPosition() {
        return mainPosition;
    }

    public void setMainPosition(int mainPosition) {
        this.mainPosition = mainPosition;
    }

    @JsonIgnore
    public String getMainPositionFullText() {
        return getPositionText(getMainPosition(), true);
    }

    @JsonIgnore
    public String getMainPositionText() {
        return getPositionText(getMainPosition(), false);
    }

    public int getMinorPosition() {
        return minorPosition;
    }

    public void setMinorPosition(int minorPosition) {
        this.minorPosition = minorPosition;
    }

    @JsonIgnore
    public String getMinorPositionText() {
        return getPositionText(getMinorPosition(), false);
    }

    @JsonIgnore
    public String getMinorPositionFullText() {
        return getPositionText(getMinorPosition(), true);
    }

    public Long getTransferValue() {
        return transferValue == null ? 0 : transferValue;
    }

    public void setTransferValue(Long transferValue) {
        this.transferValue = transferValue;
    }

    @JsonIgnore
    public String getTransferValueDisplay() {
        return AppUtilities.getMoney(getTransferValue());
    }

    public int getPointLastRound() {
        return pointLastRound;
    }

    public void setPointLastRound(int pointLastRound) {
        this.pointLastRound = pointLastRound;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @JsonIgnore
    public String getPositionText(int position, boolean fullText) {
        String result = "";
        switch (position) {
            case POSITION_GOALKEEPER:
                result = fullText ? "Goalkeeper" : "G";
                break;
            case POSITION_DEFENDER:
                result = fullText ? "Defender" : "D";
                break;
            case POSITION_MIDFIELDER:
                result = fullText ? "Midfielder" : "M";
                break;
            case POSITION_ATTACKER:
                result = fullText ? "Attacker" : "A";
                break;
        }

        return result;
    }

    @JsonIgnore
    public Drawable getPositionBackground(Context context, int position) {
        Drawable result = null;

        switch (position) {
            case POSITION_GOALKEEPER:
                result = ContextCompat.getDrawable(context, R.drawable.bg_green_button_radius);
                break;
            case POSITION_DEFENDER:
                result = ContextCompat.getDrawable(context, R.drawable.bg_green_radius);
                break;
            case POSITION_MIDFIELDER:
                result = ContextCompat.getDrawable(context, R.drawable.bg_yellow_radius);
                break;
            case POSITION_ATTACKER:
                result = ContextCompat.getDrawable(context, R.drawable.bg_red_radius);
                break;
        }

        return result;
    }

    public Boolean getGoalkeeper() {
        return isGoalkeeper;
    }

    public Boolean getDefender() {
        return isDefender;
    }

    public Boolean getMidfielder() {
        return isMidfielder;
    }

    public Boolean getAttacker() {
        return isAttacker;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getCleanSheet() {
        return cleanSheet;
    }

    public int getDuelsTheyWin() {
        return duelsTheyWin;
    }

    public int getPasses() {
        return passes;
    }

    public int getShots() {
        return shots;
    }

    public int getSaves() {
        return saves;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public int getDribbles() {
        return dribbles;
    }

    public int getTurnovers() {
        return turnovers;
    }

    public int getBallsRecovered() {
        return ballsRecovered;
    }

    public int getFoulsCommitted() {
        return foulsCommitted;
    }

    public int getPoint() {
        return point;
    }

    public int getPosition() {
        return position;
    }

    public Integer getOrder() {
        return order;
    }

    public boolean isTrading() {
        return isTrading;
    }

    @Override
    public String toString() {
        return "PlayerResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", realClubId=" + realClubId +
                ", realClub=" + realClub +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", isInjured=" + isInjured +
                ", isGoalkeeper=" + isGoalkeeper +
                ", isDefender=" + isDefender +
                ", isMidfielder=" + isMidfielder +
                ", isAttacker=" + isAttacker +
                ", mainPosition=" + mainPosition +
                ", minorPosition=" + minorPosition +
                ", transferValue=" + transferValue +
                ", pointLastRound=" + pointLastRound +
                ", isSelected=" + isSelected +
                ", goals=" + goals +
                ", assists=" + assists +
                ", cleanSheet=" + cleanSheet +
                ", duelsTheyWin=" + duelsTheyWin +
                ", passes=" + passes +
                ", shots=" + shots +
                ", saves=" + saves +
                ", yellowCards=" + yellowCards +
                ", dribbles=" + dribbles +
                ", turnovers=" + turnovers +
                ", ballsRecovered=" + ballsRecovered +
                ", foulsCommitted=" + foulsCommitted +
                ", order=" + order +
                ", point=" + point +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerResponse && ((PlayerResponse) obj).id == id;
    }
}
