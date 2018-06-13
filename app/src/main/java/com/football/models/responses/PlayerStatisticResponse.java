package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PlayerStatisticResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("main_position")
    private Integer mainPosition;
    @JsonProperty("minor_position")
    private Integer minorPosition;
    @JsonProperty("is_injured")
    private Boolean isInjured;
    @JsonProperty("transfer_value")
    private Integer transferValue;
    @JsonProperty("total_point")
    private Integer totalPoint;
    @JsonProperty("total_round")
    private Integer totalRound;
    @JsonProperty("meta")
    private PlayerStatisticMetaResponse meta;
    @JsonProperty("metas")
    private List<PlayerStatisticMetaResponse> metas;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getMainPosition() {
        return mainPosition;
    }

    public Integer getMinorPosition() {
        return minorPosition;
    }

    public Boolean getInjured() {
        return isInjured;
    }

    public Integer getTransferValue() {
        return transferValue;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public Integer getTotalRound() {
        return totalRound;
    }

    public PlayerStatisticMetaResponse getMeta() {
        return meta;
    }

    public List<PlayerStatisticMetaResponse> getMetas() {
        return metas;
    }
}
