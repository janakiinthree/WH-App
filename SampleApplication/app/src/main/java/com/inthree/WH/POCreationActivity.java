package com.inthree.WH;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import me.ydcool.lib.qrmodule.activity.QrScannerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.inthree.WH.API.Api;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
import com.inthree.WH.model.Login;
import com.inthree.WH.model.LoginResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class POCreationActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private ImageView mPhoto;
    private Button mInvoicePicker;
    private Button mSubmit;
    private EditText mSearchView;
    private ImageView m_search;
    private EditText mGSTNumber;
    private EditText mqty1;
    private EditText mqty2;
    private EditText mtxtinvoice;
    private LinearLayout mContentLayout;
    private ImageView mverify_icon;
    private ImageView mqr_code;
    private ImageView m_qtyscan_1;
    private ImageView m_qtyscan_2;
    private ImageView m_iv_delOcr;
    String BBID1="";
    String BBID2="";
    String invoicenumber;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_QTY_1= 1;
    private static final int REQUEST_QTY_2= 2;
    private static final int REQUEST_OCR_CODE = 333;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_creation);
        mInvoicePicker = findViewById(R.id.invoice_date_picker);
        mPhoto = findViewById(R.id.invoice_photo_icon);
        mSearchView = findViewById(R.id.search_view);
        mGSTNumber = findViewById(R.id.GSTN);
        mqty1 = findViewById(R.id.txt_qty1);
        mqty2 = findViewById(R.id.txt_qty2);
        mverify_icon = findViewById(R.id.verify_icon);
        mqr_code = findViewById(R.id.lmg_qrcode);
        m_qtyscan_1 = findViewById(R.id.lmg_qrcoderecqty);
        m_qtyscan_2 = findViewById(R.id.lmg_qrcoderecqty2);
        mtxtinvoice=findViewById(R.id.merchant);
        m_iv_delOcr=findViewById(R.id.iv_delOcr);
        m_search=findViewById(R.id.lmg_search);
        mSubmit=findViewById(R.id.submit);
        mContentLayout = findViewById(R.id.content);
        calendar = Calendar.getInstance();
        mSearchView.clearFocus();
        //mSearchView.onActionViewExpanded(); //new Added line
        //mSearchView.setIconifiedByDefault(false);
        mSearchView.setHint("Search PO number");
        year = calendar.get(Calendar.YEAR);
        hideKeyboard();
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(POCreationActivity.this, "GRN No G45637 Created Successfully ", Toast.LENGTH_LONG).show();
                mContentLayout.setVisibility(View.GONE);
            }
        });
        read_grn_details();
        mInvoicePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(POCreationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mInvoicePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        mqr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), QrScannerActivity.class),
                        QrScannerActivity.QR_REQUEST_CODE);

            }
        });

        m_qtyscan_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), QrScannerActivity.class),
                        REQUEST_QTY_1);

            }
        });
        m_qtyscan_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), QrScannerActivity.class),
                        REQUEST_QTY_2);

            }
        });

        m_iv_delOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOcrActivity();
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(POCreationActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
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
        mGSTNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() ==10) {
                    if(s.toString().equals("AGDPV5248C"))
                    {
                        mverify_icon.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(POCreationActivity.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
                        mGSTNumber.setText("") ;
                    }
                }
                else
                    mverify_icon.setVisibility(View.GONE);
               // mGSTNumber.setText("");
            }
        });
        m_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String query = mSearchView.getText().toString();
               if(query.equals(""))
               {
                   Toast.makeText(POCreationActivity.this, "Enter PO number", Toast.LENGTH_LONG).show();
               }
               else {

                   if (query.equalsIgnoreCase("12345")) {
                       mContentLayout.setVisibility(View.VISIBLE);
                   } else {
                       mContentLayout.setVisibility(View.GONE);
                       Toast.makeText(POCreationActivity.this, "Enter Valid PO number", Toast.LENGTH_LONG).show();
                   }
                   //return false;
               }

            }
        });

    }
    public void read_grn_details(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        GrnRequest request = new GrnRequest();
        //Login.Data requestData = new Login.Data();
        request.setPo_no("10000164");
       // request.setPassword(mPassword.getText().toString().trim());
        //request.setmData(requestData);


        Call<GRNResponse> call = api.get_grn_details(request);

        call.enqueue(new Callback<GRNResponse>() {
            @Override
            public void onResponse(Call<GRNResponse> call, Response<GRNResponse> response) {
                String login_status= response.body().getStatus();
                if(login_status.equals("success")){

                    // List<LoginResponse.POModel> polist = response.body().getPmodel();


                    Toast.makeText(getApplicationContext(), "Login Success!!", Toast.LENGTH_SHORT).show();
                    GRNResponse grnResponse = new GRNResponse();
                    grnResponse.getGrn_details().getProductmodel();
                    for(int i =0;i<=grnResponse.getGrn_details().getProductmodel().size();i++)
                    {


                    }

                    //  startActivity(intent);

                }

            }

            @Override
            public void onFailure(Call<GRNResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }



        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mPhoto.setImageBitmap(photo);
            mPhoto.setBackground(null);
        }

        if (requestCode == QrScannerActivity.QR_REQUEST_CODE) {
            String aadharCode = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = aadharCode;
            if (scanResult != null) {

                if (!scanResult.equalsIgnoreCase("Scanned Nothing!")) {
                    if(scanResult.equals("12345"))
                    mContentLayout.setVisibility(View.VISIBLE);
                    else
                        Toast.makeText(POCreationActivity.this, "Enter Valid PO number", Toast.LENGTH_LONG).show();

                }
            }
        }
        if (requestCode == REQUEST_QTY_1) {
            String aadharCode = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = aadharCode;
            if (scanResult != null) {

                if (!scanResult.equalsIgnoreCase("Scanned Nothing!")) {


                    if(BBID1.equals("")) {
                        BBID1 = scanResult;
                        Integer total =0;
                        if(mqty1.getText().toString().equals(""))
                        {
                            total = 1;
                        }
                        else
                        {
                            total= Integer.parseInt(mqty1.getText().toString()) + 1;
                        }
                        mqty1.setText(total.toString());
                    }
                    else {
                        String[] bbid=BBID1.split(",");
                        if (Arrays.asList(bbid).contains(scanResult)) {
                            Toast.makeText(POCreationActivity.this, "BBID already Scanned", Toast.LENGTH_LONG).show();
                        }
                        else {
                            BBID1 = BBID1 + "," + scanResult;
                            Integer total =0;
                            if(mqty1.getText().toString().equals(""))
                            {
                                total = 1;
                            }
                            else
                            {
                                total= Integer.parseInt(mqty1.getText().toString()) + 1;
                            }
                            mqty1.setText(total.toString());
                        }
                    }


                }
            }
        }
        if (requestCode == REQUEST_QTY_2) {
            String aadharCode = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = aadharCode;
            if (scanResult != null) {

                if (!scanResult.equalsIgnoreCase("Scanned Nothing!")) {


                    if(BBID2.equals("")) {
                        BBID2 = scanResult;
                        Integer total =0;
                        if(mqty2.getText().toString().equals(""))
                        {
                            total = 1;
                        }
                        else
                        {
                            total= Integer.parseInt(mqty2.getText().toString()) + 1;
                        }
                        mqty2.setText(total.toString());
                    }
                    else {
                        String[] bbid=BBID2.split(",");
                        if (Arrays.asList(bbid).contains(scanResult)) {
                            Toast.makeText(POCreationActivity.this, "BBID already Scanned", Toast.LENGTH_LONG).show();
                        }
                        else {
                            BBID2 = BBID2 + "," + scanResult;
                            Integer total =0;
                            if(mqty2.getText().toString().equals(""))
                            {
                                total = 1;
                            }
                            else
                            {
                                total= Integer.parseInt(mqty2.getText().toString()) + 1;
                            }
                            mqty2.setText(total.toString());
                        }
                    }


                }
            }
        }
        if (requestCode == REQUEST_OCR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String ocrPath = data.getStringExtra("ocrPath");
                if (ocrPath != null) {
//            Log.v("ocrPath"," - "+ ocrPath);
                    mtxtinvoice.setText(ocrPath);
                }
            }

        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void callOcrActivity() {
        Intent i = new Intent(this, VoterOcrActivity.class);
        i.putExtra("shipment_num", invoicenumber);
        startActivityForResult(i, 333);
    }

}
