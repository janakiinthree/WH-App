package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateGrnRequest {

    @SerializedName("grn_create_details")
    @Expose
    private CreateGrnRequest.GRNCreateRequest_details create_grn_details;

    public static class GRNCreateRequest_details {
        @SerializedName("user_id")
        @Expose
        String user_id;

        @SerializedName("purchaseorder_id")
        @Expose
        String purchaseorder_id;

        @SerializedName("invoice_number")
        @Expose
        String invoice_number;

        @SerializedName("inv_amount")
        @Expose
        String invoice_amount;

        @SerializedName("inv_tax")
        @Expose
        String invoice_tax;

        @SerializedName("inv_date")
        @Expose
        String invoice_date;


        @SerializedName("vendor_gstinno")
        @Expose
        String vgstin;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPurchaseorder_id() {
            return purchaseorder_id;
        }

        public void setPurchaseorder_id(String purchaseorder_id) {
            this.purchaseorder_id = purchaseorder_id;
        }

        public String getInvoice_number() {
            return invoice_number;
        }

        public void setInvoice_number(String invoice_number) {
            this.invoice_number = invoice_number;
        }

        public String getInvoice_amount(String invoice_amount) {
            return this.invoice_amount;
        }

        public void setInvoice_amount(String invoice_amount) {
            this.invoice_amount = invoice_amount;
        }

        public String getInvoice_tax() {
            return invoice_tax;
        }

        public void setInvoice_tax(String invoice_tax) {
            this.invoice_tax = invoice_tax;
        }

        public String getInvoice_date() {
            return invoice_date;
        }

        public void setInvoice_date(String invoice_date) {
            this.invoice_date = invoice_date;
        }

        public String getVgstin() {
            return vgstin;
        }

        public void setVgstin(String vgstin) {
            this.vgstin = vgstin;
        }


        public List<GRNResponse.ProductModel> getDelivery_products() {
            return delivery_products;
        }

        public void setDelivery_products(List<GRNResponse.ProductModel> delivery_products) {
            this.delivery_products = delivery_products;
        }

        @SerializedName("products")
        @Expose
        private List<GRNResponse.ProductModel> delivery_products;

    }
    public static class GrnProductModel {
        @SerializedName("product_id")
        @Expose
        String product_id;

        public String getOrder_qty() {
            return order_qty;
        }

        public void setOrder_qty(String order_qty) {
            this.order_qty = order_qty;
        }

        @SerializedName("qty")
        @Expose
        String order_qty;

        public String getReceived_qty() {
            return received_qty;
        }

        public void setReceived_qty(String received_qty) {
            this.received_qty = received_qty;
        }

        @SerializedName("received_qty")
        @Expose
        String received_qty;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }


        public List<Scanner_List> getScanner_list() {
            return Scanner_list;
        }

        public void setScanner_list(List<Scanner_List> scanner_list) {
            Scanner_list = scanner_list;
        }

        @SerializedName("qr_scanner_list")
        @Expose
        private List<Scanner_List> Scanner_list;



    }




}
