package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GRNDetailsResponse {

    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("grn_details")
    @Expose
    private GRNDetailsResponse.GRN_details grn_details;


    public String getStatus() {
        return status;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public GRN_details getGrn_details() {
        return grn_details;
    }

    public void setGrn_details(GRN_details grn_details) {
        this.grn_details = grn_details;
    }

    public static class GRN_details {
        @SerializedName("user_id")
        @Expose
        int user_id;
        @SerializedName("eway")
        @Expose
        String eway;
        @SerializedName("purchase_order_id")
        @Expose
        String po_id;
        @SerializedName("inv_date")
        @Expose
        String inv_date;
        @SerializedName("inv_amount")
        @Expose
        String invoice_amount;
        @SerializedName("vendor_gstinno")
        @Expose
        String vendor_gstinno;
        @SerializedName("inv_tax")
        @Expose
        String inv_tax;
        @SerializedName("supplier_name")
        @Expose
        String supplier_name;
        @SerializedName("invoice_number")
        @Expose
        String invoice_number;


        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPo_id() {
            return po_id;
        }

        public void setPo_id(String po_id) {
            this.po_id = po_id;
        }

        public String getEway() {
            return eway;
        }

        public void setEway(String eway) {
            this.eway = eway;
        }

        public String getInv_date() {
            return inv_date;
        }

        public void setInv_date(String inv_date) {
            this.inv_date = inv_date;
        }

        public String getInvoice_number() {
            return invoice_number;
        }

        public void setInvoice_number(String invoice_number) {
            this.invoice_number = invoice_number;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getInvoice_amount() {
            return invoice_amount;
        }

        public void setInvoice_amount(String invoice_amount) {
            this.invoice_amount = invoice_amount;
        }

        public String getVendor_gstinno() {
            return vendor_gstinno;
        }

        public void setVendor_gstinno(String vendor_gstinno) {
            this.vendor_gstinno = vendor_gstinno;
        }

        public String getInv_tax() {
            return inv_tax;
        }

        public void setInv_tax(String inv_tax) {
            this.inv_tax = inv_tax;
        }




    }


}
