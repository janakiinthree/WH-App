package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrnRequest {
    public String getPo_no() {
        return po_no;
    }

    public void setPo_no(String po_no) {
        this.po_no = po_no;
    }

    @SerializedName("po_no")
    @Expose
    String po_no;

}
