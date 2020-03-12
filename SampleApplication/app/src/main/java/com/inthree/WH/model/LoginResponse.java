package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LoginResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("user_id")
    @Expose
    String user_id;

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("po_details")
    @Expose
    private List<POModel> pmodel;

    public List<POModel> getPmodel() {
        return pmodel;
    }

    public void setPmodel(List<POModel> pmodel) {
        this.pmodel = pmodel;
    }

    public class POModel {
        @SerializedName("po_no")
        public String po_number;
        @SerializedName("po_created_at")
        public String po_created_at;

        public String getPo_number() {
            return po_number;
        }

        public void setPo_number(String po_number) {
            this.po_number = po_number;
        }

        public String getPo_created_at() {
            return po_created_at;
        }

        public void setPo_created_at(String po_created_at) {
            this.po_created_at = po_created_at;
        }

        public String getValid_upto() {
            return valid_upto;
        }

        public void setValid_upto(String valid_upto) {
            this.valid_upto = valid_upto;
        }

        public String getBill_name() {
            return bill_name;
        }

        public void setBill_name(String bill_name) {
            this.bill_name = bill_name;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getPo_status() {
            return po_status;
        }

        public void setPo_status(String po_status) {
            this.po_status = po_status;
        }

        public String getPo_paid_date() {
            return po_paid_date;
        }

        public void setPo_paid_date(String po_paid_date) {
            this.po_paid_date = po_paid_date;
        }

        @SerializedName("valid_upto")
        public String valid_upto;
        @SerializedName("bill_name")
        public String bill_name;
        @SerializedName("supplier_name")
        public String supplier_name;
        @SerializedName("po_status")
        public String po_status;
        @SerializedName("po_paid_date")
        public String po_paid_date;

    }
}
