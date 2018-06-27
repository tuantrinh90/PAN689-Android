package com.football.models.requests;

import java.io.File;

public class ProfileRequest {

    private String firstName;
    private String lastName;
    private String birthday;
    private Integer gender;
    private File photo;
    private String address;
    private String phone;
    private String description;

    private ProfileRequest(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        birthday = builder.birthday;
        gender = builder.gender;
        photo = builder.photo;
        address = builder.address;
        phone = builder.phone;
        description = builder.description;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public File getPhoto() {
        return photo;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String birthday;
        private Integer gender;
        private File photo;
        private String address;
        private String phone;
        private String description;

        public Builder() {
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder birthday(String val) {
            birthday = val;
            return this;
        }

        public Builder gender(Integer val) {
            gender = val;
            return this;
        }

        public Builder photo(File val) {
            photo = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public ProfileRequest build() {
            return new ProfileRequest(this);
        }
    }
}
