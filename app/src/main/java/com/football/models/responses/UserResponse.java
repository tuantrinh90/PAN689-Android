package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserResponse implements Serializable {

    public static final int UNDISCLOSED = -1;
    public static final int GENDER_FEMALE = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_OTHER = 2;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("role_id")
    private Integer roleId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_admin")
    private Integer isAdmin;
    @JsonProperty("api_token")
    private String apiToken;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("deleted_at")
    private String deletedAt;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("active")
    private Integer active;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("gender")
    private Integer gender;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("address")
    private String address;
    @JsonProperty("fb_id")
    private String fbId;
    @JsonProperty("fb_token")
    private String fbToken;
    @JsonProperty("gg_id")
    private String ggId;
    @JsonProperty("gg_token")
    private String ggToken;
    @JsonProperty("tw_id")
    private String twId;
    @JsonProperty("tw_token")
    private String twToken;
    @JsonProperty("locale")
    private String locale;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsAdmin() {
        return isAdmin == null ? 0 : isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getGgId() {
        return ggId;
    }

    public void setGgId(String ggId) {
        this.ggId = ggId;
    }

    public String getGgToken() {
        return ggToken;
    }

    public void setGgToken(String ggToken) {
        this.ggToken = ggToken;
    }

    public String getTwId() {
        return twId;
    }

    public void setTwId(String twId) {
        this.twId = twId;
    }

    public String getTwToken() {
        return twToken;
    }

    public void setTwToken(String twToken) {
        this.twToken = twToken;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", isAdmin=" + isAdmin +
                ", apiToken='" + apiToken + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ", phone='" + phone + '\'' +
                ", active=" + active +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender=" + gender +
                ", photo='" + photo + '\'' +
                ", address='" + address + '\'' +
                ", fbId='" + fbId + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", ggId='" + ggId + '\'' +
                ", ggToken='" + ggToken + '\'' +
                ", twId='" + twId + '\'' +
                ", twToken='" + twToken + '\'' +
                '}';
    }
}
