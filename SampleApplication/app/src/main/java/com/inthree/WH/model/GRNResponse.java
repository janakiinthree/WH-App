package com.inthree.WH.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.SkipCallbackExecutor;

public class GRNResponse {

    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("grn_details")
    @Expose
    private GRNResponse.GRN_details grn_details;

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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getEway() {
            return eway;
        }

        public void setEway(String eway) {
            this.eway = eway;
        }

        public String getPo_id() {
            return po_id;
        }

        public void setPo_id(String po_id) {
            this.po_id = po_id;
        }

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

        public String getEnterted_invoice_amount() {
            return enterted_invoice_amount;
        }

        public void setEnterted_invoice_amount(String enterted_invoice_amount) {
            this.enterted_invoice_amount = enterted_invoice_amount;
        }

        @SerializedName("entered_inv_amount")
        @Expose
        String enterted_invoice_amount;
        @SerializedName("vendor_gstinno")
        @Expose
        String vendor_gstinno;
        @SerializedName("inv_tax")
        @Expose
        String inv_tax;



        @SerializedName("enterted_inv_tax")
        @Expose
        String enterted_inv_tax;

        public String getEnterted_inv_tax() {
            return enterted_inv_tax;
        }

        public void setEnterted_inv_tax(String enterted_inv_tax) {
            this.enterted_inv_tax = enterted_inv_tax;
        }

        @SerializedName("supplier_name")
        @Expose
        String supplier_name;
        @SerializedName("invoice_number")
        @Expose


        String invoice_number;
        @SerializedName("products")
        @Expose
        private List<GRNResponse.ProductModel> productmodel;

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

        public List<ProductModel> getProductmodel() {

            return productmodel;
        }

        public void setProductmodel(List<ProductModel> productmodel) {
            this.productmodel = productmodel;
        }


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
        @SerializedName("recevied_qty")
        @Expose
        int recevied_qty;
        @SerializedName("Barcodevalue")
        @Expose
        String barcode_value;
        @SerializedName("capture_bbid")
        @Expose
        boolean capture_bbid;
        @SerializedName("capture_serial_no")
        @Expose
        boolean capture_serial_no;
        @SerializedName("capture_imei")
        @Expose
        boolean capture_imei;

        public boolean isDisplay_scanner() {
            return display_scanner;
        }

        public void setDisplay_scanner(boolean display_scanner) {
            this.display_scanner = display_scanner;
        }

        @SerializedName("capture")
        @Expose
        boolean display_scanner;
        @SerializedName("qr_scanner_list")
        @Expose
        private List<Scanner_List> Scanner_list;

        public int getReq_qty() {
            return req_qty;
        }

        public void setReq_qty(int req_qty) {
            this.req_qty = req_qty;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        @SerializedName("image_details")
        @Expose
        String image_url;

        @SerializedName("req_qty")
        @Expose
        int req_qty;

        public boolean getCapture_bbid() {
            return capture_bbid;
        }

        public boolean getCapture_serial_no() {
            return capture_serial_no;
        }

        public boolean getCapture_imei() {
            return capture_imei;
        }

        public boolean isCapture_bbid() {
            return capture_bbid;
        }

        public void setCapture_bbid(boolean capture_bbid) {
            this.capture_bbid = capture_bbid;
        }

        public boolean isCapture_serial_no() {
            return capture_serial_no;
        }

        public void setCapture_serial_no(boolean capture_serial_no) {
            this.capture_serial_no = capture_serial_no;
        }

        public boolean isCapture_imei() {
            return capture_imei;
        }

        public void setCapture_imei(boolean capture_imei) {
            this.capture_imei = capture_imei;
        }



        public List<Scanner_List> getScanner_list() {
            return Scanner_list;
        }

        public void setScanner_list(List<Scanner_List> scanner_list) {
            Scanner_list = scanner_list;
        }


        public String getBarcode_value() {
            return barcode_value;
        }

        public void setBarcode_value(String barcode_value) {
            this.barcode_value = barcode_value;
        }

        public int getRecevied_qty() {
            return recevied_qty;
        }

        public void setRecevied_qty(int recevied_qty) {
            this.recevied_qty = recevied_qty;
        }

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