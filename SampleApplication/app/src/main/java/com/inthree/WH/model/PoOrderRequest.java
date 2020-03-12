package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PoOrderRequest {

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("user_id")
    @Expose
    String user_id;
}
