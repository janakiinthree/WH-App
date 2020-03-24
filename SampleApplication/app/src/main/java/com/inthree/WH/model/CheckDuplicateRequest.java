package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckDuplicateRequest {

    @SerializedName("bb_id")
    @Expose
    String bb_id;

    @SerializedName("imei_no")
    @Expose
    String imei_no;
    @SerializedName("serial_no")
    @Expose
    String serial_no;


    public String getBb_id() {
        return bb_id;
    }

    public void setBb_id(String bb_id) {
        this.bb_id = bb_id;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }
}
