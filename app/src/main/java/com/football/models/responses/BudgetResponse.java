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
    private Float value;
    @JsonIgnore
    private Boolean isActived = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @JsonIgnore
    public Float getValueDisplay() {
        return value / Constant.KEY_MIO;
    }

    public Boolean getActived() {
        return isActived;
    }

    public void setActived(Boolean isActived) {
        this.isActived = isActived;
    }

    @Override
    public String toString() {
        return "BudgetResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", isActived=" + isActived +
                '}';
    }
}
