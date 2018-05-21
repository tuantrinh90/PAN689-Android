package com.football.models;

import java.io.Serializable;

public class Friend implements Serializable {
    private String avatar;
    private String name;
    private boolean isFriend;

    public Friend() {
    }

    public Friend(String avatar, String name, boolean isFriend) {
        this.avatar = avatar;
        this.name = name;
        this.isFriend = isFriend;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", isFriend=" + isFriend +
                '}';
    }
}
