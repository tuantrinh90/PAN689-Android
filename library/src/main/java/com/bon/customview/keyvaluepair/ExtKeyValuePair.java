package com.bon.customview.keyvaluepair;

import android.graphics.Color;

import com.bon.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Dang Pham Phu on 2/2/2017.
 */

public class ExtKeyValuePair implements Serializable {
    String key;
    String value;
    @JsonIgnore
    boolean selected;
    @JsonIgnore
    int textColor = Color.BLACK;

    public ExtKeyValuePair() {
    }

    public ExtKeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ExtKeyValuePair(String key, String value, int textColor) {
        this.key = key;
        this.value = value;
        this.textColor = textColor;
    }

    public ExtKeyValuePair(String key, String value, boolean selected) {
        this.key = key;
        this.value = value;
        this.selected = selected;
    }

    public ExtKeyValuePair(String key, String value, boolean selected, int textColor) {
        this.key = key;
        this.value = value;
        this.selected = selected;
        this.textColor = textColor;
    }

    public String getKey() {
        return StringUtils.isEmpty(key) ? "" : key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return StringUtils.isEmpty(value) ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "ExtKeyValuePair{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", selected=" + selected +
                ", textColor=" + textColor +
                '}';
    }
}
