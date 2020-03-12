package com.inthree.WH;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inthree.WH.API.Api;
import com.inthree.WH.Adapter.GrnRecycleListAdapter;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
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
import me.ydcool.lib.qrmodule.activity.QrScannerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrnDetailsActivity2 extends AppCompatActivity {


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
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_QTY = 2;
    RecyclerView grn_list_view;
    String order_number;
    String order_date;
    private int year, month, day;
    private Calendar calendar;
    Map<Integer, List<String>> qr_code_list = new HashMap<Integer, List<String>>();
    String VendorGSTno = "";
    Integer Selected_product_id;
    List<GRNResponse.ProductModel> Product_list;
    GRNResponse.ProductModel Selected_pmodel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_details_view);
        initComponents();
        ButterKnife.bind(this);
        hideKeyboard();



        read_grn_details();

    }

    @OnClick({R.id.submit})
    void Submitaction() {

        System.out.println("qr_cod_");
        System.out.println("qr_cod_ lisst" + qr_code_list.size());
        System.out.println("VAUES");
    }


    private void initComponents() {

        calendar = Calendar.getInstance();
        grn_list_view = findViewById(R.id.grn_list);
        recyclerViewAdapter = new GrnRecycleListAdapter();
        grn_list_view.setLayoutManager(new LinearLayoutManager(this));
        grn_list_view.setAdapter(recyclerViewAdapter);

        // get the current date
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        recyclerViewAdapter.setOnScannerClick(new GrnRecycleListAdapter.onScannerClick() {
            @Override
            public void onClick(String product_id) {
                Intent intent = new Intent(getApplicationContext(), QrScannerActivity.class);
                intent.putExtra("product_id", product_id);
                Selected_product_id = Integer.parseInt(product_id);
                setResult(REQUEST_QTY, intent);
                startActivityForResult(intent, REQUEST_QTY);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(GrnDetailsActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                invoice_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                Calendar c = Calendar.getInstance();
                String dtStart = order_date.toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date order_date_val = format.parse(dtStart);
                    datePickerDialog.getDatePicker().setMinDate(order_date_val.getTime());
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                    System.out.println("date of  is " + order_date_val.getDate() + "Month:" + order_date_val.getMonth() + "Year:" + order_date_val.getYear());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
            }
        });


        invoice_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(GrnDetailsActivity2.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
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
                if (s.length() == 15) {
                    hideKeyboard();
                    if (s.toString().equals(VendorGSTno)) {
                        GSTVerifyIcon.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(GrnDetailsActivity2.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
                        GSTN_number.setText("");
                    }
                } else
                    GSTVerifyIcon.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            invoice_attachment.setImageBitmap(photo);
            invoice_attachment.setBackground(null);
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
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "You cannot Scan ore than Ordered qty!", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    recyclerViewAdapter.setData(Product_list);
                }
            }

        }

    }

    public void read_grn_details() {
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
                    GRNResponse.GRN_details grnResponse = new GRNResponse.GRN_details();
                    grnResponse = response.body().getGrn_details();
                    // GSTN_number.setText((grnResponse.getVendor_gstinno() == null) ? " " : grnResponse.getVendor_gstinno());
                    VendorGSTno = (grnResponse.getVendor_gstinno() == null) ? " " : grnResponse.getVendor_gstinno();
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