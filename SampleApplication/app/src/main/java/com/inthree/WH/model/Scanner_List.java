package com.inthree.WH.model;

import android.widget.ScrollView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scanner_List {


    @SerializedName("imei_scan_value")
    @Expose
    String imei_scan_value;
    @SerializedName("bbid_value")
    @Expose
    String bbid_value;
    @SerializedName("serial_number")
    @Expose
    String serial_number;

    Scanner_List(){

    }
    public Scanner_List(String bbid, String imei_no, String serial_no){
        this.bbid_value = bbid;
        this.imei_scan_value=imei_no;
        this.serial_number=serial_no;
    }

    public String getImei_scan_value() {
        return imei_scan_value;
    }

    public void setImei_scan_value(String imei_scan_value) {
        this.imei_scan_value = imei_scan_value;
    }


    public boolean equals(Object obj){
        System.out.println("In equals");
        boolean check_bbi =false;
        boolean check_imei=false;
        boolean check_serial=false;
        if (obj instanceof Scanner_List) {
            Scanner_List pp = (Scanner_List) obj;
            if(bbid_value!="" && bbid_value!="null" && !bbid_value.equals("Scanned Nothing!"))
                check_bbi = true;
            if(imei_scan_value!="" && imei_scan_value!="null" && !imei_scan_value.equals("Scanned Nothing!"))
                check_imei = true;
            if(serial_number!="" && serial_number!="null" && !serial_number.equals("Scanned Nothing!"))
                check_serial = true;

                return (pp.bbid_value.equals(this.bbid_value) && pp.imei_scan_value.equals(this.imei_scan_value) && pp.serial_number.equals(this.serial_number));
        } else {
            return false;
        }
    }
    public String getBbid_value() {
        return bbid_value;
    }

    public void setBbid_value(String bbid_value) {
        this.bbid_value = bbid_value;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }


}
