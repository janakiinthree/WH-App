package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGrnResponse {

    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("grn_id")
    @Expose
    String grn_no;
    @SerializedName("status")
    @Expose
    private String status;

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrn_no() {
        return grn_no;
    }

    public void setGrn_no(String grn_no) {
        this.grn_no = grn_no;
    }


}
