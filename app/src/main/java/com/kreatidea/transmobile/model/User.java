package com.kreatidea.transmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 10/23/2018.
 */
public class User {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
