package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FriendResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("is_invited")
    private Boolean isInvited;

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

    public String getEmail() {
        return email + "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName + "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName + "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo + "";
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getInvited() {
        return isInvited == null ? false : isInvited;
    }

    public void setInvited(Boolean invited) {
        isInvited = invited;
    }

    @Override
    public String toString() {
        return "FriendResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", isInvited=" + isInvited +
                '}';
    }
}
