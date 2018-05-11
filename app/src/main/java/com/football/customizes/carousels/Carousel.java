package com.football.customizes.carousels;

import java.io.Serializable;

public class Carousel implements Serializable {
    private String content;
    private boolean isActive;

    public Carousel() {
    }

    public Carousel(String content, boolean isActive) {
        this.content = content;
        this.isActive = isActive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Carousel{" +
                "content='" + content + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
