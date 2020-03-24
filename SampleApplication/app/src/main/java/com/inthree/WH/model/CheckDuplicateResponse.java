package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckDuplicateResponse {
    @SerializedName("message")
    @Expose
    String api_message;
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

    public DuplicateList getDuplicateList() {
        return duplicateList;
    }

    public void setDuplicateList(DuplicateList duplicateList) {
        this.duplicateList = duplicateList;
    }

    @SerializedName("dup_result")
    @Expose
    private CheckDuplicateResponse.DuplicateList duplicateList;

    public class DuplicateList {

        @SerializedName("imei_no")
        @Expose
        boolean is_imei_duplicate;
        @SerializedName("serial_no")
        @Expose
         boolean is_serial_no_duplicate;


        @SerializedName("bb_id")
        @Expose
         boolean is_bb_id_duplicate;


        public boolean isIs_imei_duplicate() {
            return is_imei_duplicate;
        }

        public void setIs_imei_duplicate(boolean is_imei_duplicate) {
            this.is_imei_duplicate = is_imei_duplicate;
        }

        public boolean isIs_serial_no_duplicate() {
            return is_serial_no_duplicate;
        }

        public void setIs_serial_no_duplicate(boolean is_serial_no_duplicate) {
            this.is_serial_no_duplicate = is_serial_no_duplicate;
        }

        public boolean isIs_bb_id_duplicate() {
            return is_bb_id_duplicate;
        }

        public void setIs_bb_id_duplicate(boolean is_bb_id_duplicate) {
            this.is_bb_id_duplicate = is_bb_id_duplicate;
        }
    }

}
