package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.Constant;

import java.io.Serializable;

public class BudgetResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private Long value;
    @JsonIgnore
    private Boolean isActivated = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name + "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value == null ? 0 : value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @JsonIgnore
    public Float getValueDisplay() {
        if (value == null) return 0f;
        return value / Constant.KEY_MIO;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    @Override
    public String toString() {
        return "BudgetResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", isActivated=" + isActivated +
                '}';
    }
}
