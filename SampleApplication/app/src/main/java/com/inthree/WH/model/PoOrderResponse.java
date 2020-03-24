package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PoOrderResponse {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @SerializedName("status")
    @Expose
    String status;


    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    @SerializedName("message")
    @Expose
    String api_message;

    public List<PoOrderModel> getOrdermodel() {
        return ordermodel;
    }

    public void setOrdermodel(List<PoOrderModel> ordermodel) {
        this.ordermodel = ordermodel;
    }

    @SerializedName("po_details")
    @Expose
    private List<PoOrderModel> ordermodel;


    public static class PoOrderModel {

        @SerializedName("po_no")
        @Expose
        String order_number;

        @SerializedName("po_created_at")
        @Expose
        String order_created_date;


        @SerializedName("bill_name")
        @Expose
        String bill_name;

        @SerializedName("supplier_name")
        @Expose
        String supplier_name;


        @SerializedName("po_status")
        @Expose
        String order_status;

        @SerializedName("po_paid_date")
        @Expose
        String order_paid_date;

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        @SerializedName("po_amount")
        @Expose
        String order_amount;


        @SerializedName("po_qty_received")
        @Expose
        String order_received_qty;

        @SerializedName("po_qty")
        @Expose
        String ordered_qty;

        public String getValid_upto() {
            return valid_upto;
        }

        public void setValid_upto(String valid_upto) {

            this.valid_upto = valid_upto;
        }

        @SerializedName("valid_upto")
        @Expose
        String valid_upto;

       public String getRequired_qty_for_order() {
            return required_qty_for_order;
        }

        public void setRequired_qty_for_order(String required_qty_for_order) {
            this.required_qty_for_order = required_qty_for_order;
        }

        @SerializedName("req_qty")
        @Expose
        String required_qty_for_order;

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getOrder_created_date() {
            return order_created_date;
        }

        public void setOrder_created_date(String order_created_date) {
            this.order_created_date = order_created_date;
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

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getOrder_paid_date() {
            return order_paid_date;
        }

        public void setOrder_paid_date(String order_paid_date) {
            this.order_paid_date = order_paid_date;
        }

        public String getOrder_received_qty() {
            return order_received_qty;
        }

        public void setOrder_received_qty(String order_received_qty) {
            this.order_received_qty = order_received_qty;
        }

        public String getOrdered_qty() {
            return ordered_qty;
        }

        public void setOrdered_qty(String ordered_qty) {
            this.ordered_qty = ordered_qty;
        }


    }
}






