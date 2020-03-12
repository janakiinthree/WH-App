package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GRNResponse {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    @SerializedName("message")
    @Expose
    String api_message;

    @SerializedName("grn_details")
    @Expose
    private GRNResponse.GRN_details grn_details;

    public GRN_details getGrn_details() {
        return grn_details;
    }

    public void setGrn_details(GRN_details grn_details) {
        this.grn_details = grn_details;
    }

    public static class GRN_details {
        @SerializedName("inv_amount")
        @Expose
        String invoice_amount;

        @SerializedName("vendor_gstinno")
        @Expose
        String vendor_gstinno;
        @SerializedName("inv_tax")
        @Expose
        String inv_tax;

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        @SerializedName("supplier_name")
        @Expose
        String supplier_name;

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

        public List<ProductModel> getProductmodel() {
            return productmodel;
        }

        public void setProductmodel(List<ProductModel> productmodel) {
            this.productmodel = productmodel;
        }

        @SerializedName("products")
        @Expose
        private List<GRNResponse.ProductModel> productmodel;


    }


    public class ProductModel {
        @SerializedName("product_id")
        @Expose
        String product_id;

        @SerializedName("product_name")
        @Expose
        String product_name;

        @SerializedName("product_sku")
        @Expose
        String product_sku;

        @SerializedName("qty")
        @Expose
        String qty;

        @SerializedName("cost")
        @Expose
        String cost;

        @SerializedName("supplier_name")
        @Expose
        String supplier_name;

        @SerializedName("warehouse_name")
        @Expose
        String warehouse_name;

        public int getRecevied_qty() {
            return recevied_qty;
        }

        public void setRecevied_qty(int recevied_qty) {
            this.recevied_qty = recevied_qty;
        }

        @SerializedName("recevied_qty")
        @Expose
        int recevied_qty;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_sku() {
            return product_sku;
        }

        public void setProduct_sku(String product_sku) {
            this.product_sku = product_sku;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }
    }
}
