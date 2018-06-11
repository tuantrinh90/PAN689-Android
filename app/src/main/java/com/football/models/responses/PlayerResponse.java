package com.football.models.responses;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.fantasy.R;

import java.io.Serializable;

public class PlayerResponse implements Serializable {
    public static final int POSITION_NONE = -1;
    public static final int POSITION_GOALKEEPER = 0;
    public static final int POSITION_DEFENDER = 1;
    public static final int POSITION_MIDFIELDER = 2;
    public static final int POSITION_ATTACKER = 3;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("real_club_id")
    private Integer realClubId;
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
    @JsonProperty("main_position")
    private Integer mainPosition;
    @JsonProperty("minor_position")
    private Integer minorPosition;
    @JsonProperty("transfer_value")
    private Long transferValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getRealClubId() {
        return realClubId == null ? 0 : realClubId;
    }

    public void setRealClubId(Integer realClubId) {
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
    public String getInjuredText(Context context){
        return getInjured() ? context.getString(R.string.injured) : "";
    }

    public Integer getMainPosition() {
        return mainPosition == null ? 0 : mainPosition;
    }

    public void setMainPosition(Integer mainPosition) {
        this.mainPosition = mainPosition;
    }

    @JsonIgnore
    public String getMainPositionText(){
        return getPositionText(getMainPosition());
    }

    public Integer getMinorPosition() {
        return minorPosition == null ? 0 : minorPosition;
    }

    public void setMinorPosition(Integer minorPosition) {
        this.minorPosition = minorPosition;
    }

    @JsonIgnore
    public String getMinorPositionText(){
        return getPositionText(getMinorPosition());
    }

    public Long getTransferValue() {
        return transferValue == null ? 0 : transferValue;
    }

    public void setTransferValue(Long transferValue) {
        this.transferValue = transferValue;
    }

    @JsonIgnore
    public String getTransferValueDisplay() {
        return String.valueOf(getTransferValue() / 1000000f);
    }

    @JsonIgnore
    public String getPositionText(int position) {
        String result = "";

        switch (position) {
            case POSITION_GOALKEEPER:
                result = "G";
                break;
            case POSITION_DEFENDER:
                result = "D";
                break;
            case POSITION_MIDFIELDER:
                result = "M";
                break;
            case POSITION_ATTACKER:
                result = "A";
                break;
        }

        return result;
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
                ", mainPosition=" + mainPosition +
                ", minorPosition=" + minorPosition +
                ", transferValue=" + transferValue +
                '}';
    }
}
