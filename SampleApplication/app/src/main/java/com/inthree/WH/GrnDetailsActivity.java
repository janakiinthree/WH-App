package com.inthree.WH;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.inthree.WH.API.Api;
import com.inthree.WH.Adapter.GrnRecycleListAdapter;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
import com.inthree.WH.model.Scanner_List;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.SchedulerRunnableIntrospection;
import me.ydcool.lib.qrmodule.activity.QrScannerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrnDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_QTY_BY_Serail = 2;
    private static final int REQUEST_QTY_BY_IMEI = 3;
    private static final int REQUEST_QTY_BY_BBID = 4;
    private static final int REQUEST_QTY = 5;
    @BindView(R.id.merchant_name)
    TextView merchant_name;
    @BindView(R.id.invoice_number)
    EditText invoice_number;
    @BindView(R.id.invoice_amount)
    EditText invoice_amount;
    @BindView(R.id.invoice_tax_amt)
    EditText invoice_tax_amt;
    @BindView(R.id.GSTN_number)
    EditText GSTN_number;
    @BindView(R.id.po_order_date)
    TextView po_order_date_;
    @BindView(R.id.invoice_date)
    Button invoice_date;
    Context context;
    @BindView(R.id.invoice_photo_icon)
    ImageView invoice_attachment;
    @BindView(R.id.verify_icon)
    ImageView GSTVerifyIcon;
    @BindView(R.id.submit)
    Button btn_submit;
    GrnRecycleListAdapter recyclerViewAdapter;
    RecyclerView grn_list_view;
    String order_number;
    String order_date;
    Map<Integer, List<String>> qr_code_list = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<Scanner_List>> qr_scanner_list = new HashMap<Integer, List<Scanner_List>>();
    String VendorGSTno = "";
    Integer Selected_product_id;
    List<GRNResponse.ProductModel> Product_list;
    Uri invoice_image_path = null;
    GRNResponse.GRN_details grnResponse;
    PopupWindow mpopup;
    String serial_no, imei_no, bbid;
    List<Scanner_List> list_ids;
    View popUpView;
    private int year, month, day;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_details_view);
        ButterKnife.bind(this);
        initComponents();
        hideKeyboard();
        read_grn_details();

    }

    private Boolean validateGSTNumber(String s) {

        if (s.length() == 15) {
            if (s.toString().equals(VendorGSTno)) {
                return true;
            } else {
                return false;

            }
        } else {
            return false;
        }
    }

    @OnClick({R.id.submit})
    void Submitaction() {
        System.out.println("qr_cod_");
        System.out.println("qr_cod_ lisst" + qr_code_list.size());
        System.out.println("VAUES");
        // String filePath = getFilePath(invoice_image_path);
        // if(Product_list
        if (grnResponse != null) {
            //System.out.println("GGG"+grnResponse.toString());
            grnResponse.setInvoice_number(invoice_number.getText().toString());
            grnResponse.setInv_date(invoice_date.getText().toString());

        }

        Gson gson = new Gson();

        String json = gson.toJson(grnResponse);
        System.out.println("JOSN::" + json);
        System.out.println("AVU::");

    }

    String getFilePath(Uri selectedImage) {
        if (selectedImage == null) {
            return null;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor == null) {
            return null;
        }
        String file_path = null;

        boolean moveToFirst = cursor.moveToFirst();
        if (moveToFirst) {

            // Get columns name by uri type.
            String columnName = MediaStore.Images.Media.DATA;

            if (selectedImage == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Images.Media.DATA;
            } else if (selectedImage == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Audio.Media.DATA;
            } else if (selectedImage == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Video.Media.DATA;
            }

            // Get column index.
            int imageColumnIndex = cursor.getColumnIndex(columnName);

            // Get column value which is the uri related file local path.
            // Get column index.

            // Get column value which is the uri related file local path.
            file_path = cursor.getString(imageColumnIndex);

        }

        return file_path;
    }


    private void initComponents() {
        //  Initialize Components
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        grn_list_view = findViewById(R.id.grn_list);
        recyclerViewAdapter = new GrnRecycleListAdapter();
        grn_list_view.setLayoutManager(new LinearLayoutManager(this));
        grn_list_view.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnScannerClick(new GrnRecycleListAdapter.onScannerClick() {
            @Override
            public void onClick(String product_id, boolean display_bbid, boolean display_imei_no, boolean display_serial_no) {

                Boolean is_valid = true;
                //  AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //validateGSTNumber(GSTN_number.getText().toString());
//                popUpView = getLayoutInflater().inflate(R.layout.read_parameters,
//                        null); // inflating popup layout
//                mpopup = new PopupWindow(popUpView, RecyclerView.LayoutParams.FILL_PARENT,
//                        RecyclerView.LayoutParams.WRAP_CONTENT, true); // Creation of popup
//               // mpopup.setAnimationStyle(android.R.style.);
//                mpopup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                mpopup.setOutsideTouchable(true);
//
//                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying popup
                Dialog dialog = new Dialog(GrnDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popUpView = getLayoutInflater().inflate(R.layout.read_parameters,
                        null);
                dialog.setContentView(popUpView);
                dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, 400);
                dialog.show();

                Selected_product_id = Integer.parseInt(product_id);
                ImageView scan_serial = (ImageView) popUpView.findViewById(R.id.serial_number_scan);
                ImageView scan_bbid = (ImageView) popUpView.findViewById(R.id.bbid_number_scan);
                ImageView scan_imei = (ImageView) popUpView.findViewById(R.id.imei_scan);

                EditText serial_txt_no = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
                EditText bbid_no = (EditText) popUpView.findViewById(R.id.bbid_number_scan_value);
                EditText imei_txt_no = (EditText) popUpView.findViewById(R.id.imei_scan_value);

                if (!display_bbid) {
                    //bbid_no.setVisibility(View.GONE);
                    popUpView.findViewById(R.id.scan_bbid_layout).setVisibility(View.GONE);
                }
                if (!display_imei_no) {
                    //  imei_txt_no.setVisibility(View.GONE);
                    popUpView.findViewById(R.id.scan_imei_layout).setVisibility(View.GONE);
                }
                if (!display_serial_no) {
                    // serial_txt_no.setVisibility(View.GONE);
                    popUpView.findViewById(R.id.scan_serial_layout).setVisibility(View.GONE);
                }
                Button ok_btn = (Button) popUpView.findViewById(R.id.update);
                Button ok_cancel = (Button) popUpView.findViewById(R.id.cancel);
                list_ids = new ArrayList<>();


                scan_serial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScannerAction(product_id, REQUEST_QTY_BY_Serail, list_ids);
                    }
                });
                scan_imei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScannerAction(product_id, REQUEST_QTY_BY_IMEI, list_ids);
                    }
                });
                scan_bbid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScannerAction(product_id, REQUEST_QTY_BY_BBID, list_ids);
                    }
                });

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //list_ids.add(new Scanner_List(bbid_no.getText().toString(),imei_no.getText().toString(),serial_no.getText().toString()));
                        String bbid = (display_bbid) ? bbid_no.getText().toString() : " ";
                        String imei_no = (display_imei_no) ? imei_txt_no.getText().toString() : " ";
                        String serial_no = (display_serial_no) ? serial_txt_no.getText().toString() : "";
                        update_product_qty(bbid, imei_no, serial_no);
                        dialog.dismiss();
                    }
                });

                ok_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list_ids.clear();
                        dialog.dismiss();
                    }
                });

//                if (is_valid) {
//                    Intent intent = new Intent(getApplicationContext(), QrScannerActivity.class);
//                    intent.putExtra("product_id", product_id);
//                    Selected_product_id = Integer.parseInt(product_id);
//                    setResult(REQUEST_QTY, intent);
//                    startActivityForResult(intent, REQUEST_QTY);
//                } else {
//                    Toast.makeText(GrnDetailsActivity.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
//                }
            }
        });

        Intent i = getIntent();
        order_number = i.getSerializableExtra("order_number").toString();
        order_date = i.getSerializableExtra("order_date").toString();
        po_order_date_.setText(order_date);

        invoice_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(GrnDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                invoice_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                Calendar c = Calendar.getInstance();
                String dtStart = order_date.toString();
                android.icu.text.SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date order_date_val = format.parse(dtStart);
                    datePickerDialog.getDatePicker().setMinDate(order_date_val.getTime());
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
            }
        });

        invoice_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(GrnDetailsActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (takePictureIntent.resolveActivity(Objects.requireNonNull(getPackageManager())) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
            }
        });


        GSTN_number.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Boolean is_valid = validateGSTNumber(GSTN_number.getText().toString());
                if (s.length() > 0) {
                    if (is_valid) {
                        GSTVerifyIcon.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(GrnDetailsActivity.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
                        // GSTN_number.setText("");
                        GSTVerifyIcon.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void update_product_qty(String bbid, String imei_no, String serial_no) {

        if (qr_scanner_list.containsKey(Selected_product_id)) {
            List<Scanner_List> already_added_list = qr_scanner_list.get(Selected_product_id);
            // if (!already_added_list.contains(scanResult)) {
            already_added_list.add(new Scanner_List(bbid, imei_no, serial_no));
//            } else {
//                Toast.makeText(getApplicationContext(), "Scanned Already!", Toast.LENGTH_LONG).show();
//            }
        } else {
            List<Scanner_List> new_added_list = new ArrayList<>();
            new_added_list.add(new Scanner_List(bbid, imei_no, serial_no));
            qr_scanner_list.put(Selected_product_id, new_added_list);
        }

        for (int i = 0; i < Product_list.size(); i++) {
            if (Product_list.get(i).getProduct_id().equals(Selected_product_id.toString())) {
                if (Product_list.get(i).getRecevied_qty() <= qr_scanner_list.get(Selected_product_id).size()) {
                    int total_qty = (qr_scanner_list.get(Selected_product_id).size());

                    Product_list.get(i).setRecevied_qty(total_qty);
                    Product_list.get(i).setBarcode_value("");
                    Product_list.get(i).setScanner_list(qr_scanner_list.get(Selected_product_id));
                    break;
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot Scan more than Ordered qty!", Toast.LENGTH_LONG).show();
                }

            }
        }
        recyclerViewAdapter.setData(Product_list);

    }

    void ScannerAction(String product_id, int request_code, List<Scanner_List> list_ids) {
        Intent intent = new Intent(getApplicationContext(), QrScannerActivity.class);
        intent.putExtra("product_id", product_id);
        Selected_product_id = Integer.parseInt(product_id);
        setResult(request_code, intent);
        startActivityForResult(intent, request_code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            invoice_image_path = data.getData();
            invoice_attachment.setImageBitmap(photo);
            invoice_attachment.setBackground(null);

        }
        if (requestCode == REQUEST_QTY_BY_BBID || requestCode == REQUEST_QTY_BY_IMEI || requestCode == REQUEST_QTY_BY_Serail) {
            String scaned_code = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = scaned_code;
//            EditText serial_txt = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
//            serial_txt.setText(scaned_code);
            if (requestCode == REQUEST_QTY_BY_BBID) {
                EditText bbid_no = (EditText) popUpView.findViewById(R.id.bbid_number_scan_value);
                bbid_no.setText(scaned_code);
            }

            if (requestCode == REQUEST_QTY_BY_IMEI) {
                EditText imei_no = (EditText) popUpView.findViewById(R.id.imei_scan_value);
                imei_no.setText(scaned_code);
            }
            if (requestCode == REQUEST_QTY_BY_Serail) {
                EditText serial_txt = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
                serial_txt.setText(scaned_code);
            }

        }


        if (requestCode == REQUEST_QTY) {
            String scaned_code = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = scaned_code;
            if (scanResult != null) {
                if (!scanResult.equalsIgnoreCase("Scanned Nothing!")) {
                    if (qr_code_list.containsKey(Selected_product_id)) {
                        List<String> already_added_list = qr_code_list.get(Selected_product_id);
                        if (!already_added_list.contains(scanResult)) {
                            already_added_list.add(scanResult);
                        } else {
                            Toast.makeText(getApplicationContext(), "Scanned Already!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        List<String> new_added_list = new ArrayList<>();
                        new_added_list.add(scanResult);
                        qr_code_list.put(Selected_product_id, new_added_list);
                    }

                    for (int i = 0; i < Product_list.size(); i++) {
                        if (Product_list.get(i).getProduct_id().equals(Selected_product_id.toString())) {
                            if (Product_list.get(i).getRecevied_qty() <= qr_code_list.get(Selected_product_id).size()) {
                                int total_qty = (qr_code_list.get(Selected_product_id).size());
                                Product_list.get(i).setRecevied_qty(total_qty);
                                Product_list.get(i).setBarcode_value(scanResult);
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "You cannot Scan more than Ordered qty!", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    recyclerViewAdapter.setData(Product_list);
                }
            }

        }

    }

    public void read_grn_details() {
        // Call the API and load the details view of PO order
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        GrnRequest request = new GrnRequest();
        request.setPo_no(order_number);

        Call<GRNResponse> call = api.get_grn_details(request);

        call.enqueue(new Callback<GRNResponse>() {
            @Override
            public void onResponse(Call<GRNResponse> call, Response<GRNResponse> response) {
                String response_status = response.body().getStatus();

                response.body().getGrn_details().getProductmodel();
                if (response_status.equals("success")) {
                    grnResponse = new GRNResponse.GRN_details();
                    grnResponse = response.body().getGrn_details();
                    // GSTN_number.setText((grnResponse.getVendor_gstinno() == null) ? " " : grnResponse.getVendor_gstinno());
                    VendorGSTno = (grnResponse.getVendor_gstinno() == null) ? "18AABCT3518Q1ZV" : grnResponse.getVendor_gstinno();
                    invoice_tax_amt.setText((grnResponse.getInv_tax() == null) ? " " : grnResponse.getInv_tax());
                    invoice_amount.setText((grnResponse.getInvoice_amount() == null) ? " " : grnResponse.getInvoice_amount());
                    merchant_name.setText((grnResponse.getSupplier_name() == null) ? " " : grnResponse.getSupplier_name());
                    Product_list = grnResponse.getProductmodel();
                    recyclerViewAdapter.setData(grnResponse.getProductmodel());
                    hideKeyboard();

                }

            }

            @Override
            public void onFailure(Call<GRNResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}