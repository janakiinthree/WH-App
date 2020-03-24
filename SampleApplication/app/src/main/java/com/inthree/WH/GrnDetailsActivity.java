package com.inthree.WH;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inthree.WH.API.Api;
import com.inthree.WH.Adapter.GrnRecycleListAdapter;
import com.inthree.WH.model.CheckDuplicateRequest;
import com.inthree.WH.model.CheckDuplicateResponse;
import com.inthree.WH.model.CreateGrnResponse;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
import com.inthree.WH.model.LoginResponse;
import com.inthree.WH.model.Scanner_List;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.ydcool.lib.qrmodule.activity.QrScannerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static androidx.constraintlayout.widget.Constraints.TAG;

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
    EditText invoice_date;
    @BindView(R.id.eway_txt)
    EditText eway_txt;
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
    String Vendorinvoice_amount = "";
    String Vendorinv_tax_amt = "";
    Integer Selected_product_id;
    List<GRNResponse.ProductModel> Product_list;
    Uri invoice_image_path = null;
    GRNResponse.GRN_details grnResponse;
    PopupWindow mpopup;
    String serial_no, imei_no, bbid;
    List<Scanner_List> list_ids;
    View popUpView;
    String currentPhotoPath;
    boolean is_duplicate = false;
    boolean is_bbid_duplicate, is_imei_no_duplicate, is_serial_no_duplicate = false;
    EditText serial_txt_no, bbid_no, imei_txt_no;
    Dialog dialog;
    File upload_file;
    boolean is_invoice_amt_match = false;
    boolean is_invoice_tax_amt_match = false;
    boolean is_gst_no_match = false;
    boolean add_min_qty = false;
    ProgressDialog progressDialog;
    private int year, month, day;
    private Calendar calendar;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.resss);
        setContentView(R.layout.grn_details_view);
        // setContentView(R.layout.sdsds);
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
        if (grnResponse != null) {
            boolean allow_proceed = form_validation();
            if (allow_proceed) {
                if (add_min_qty) {
                    //System.out.println("GGG"+grnResponse.toString());
                    grnResponse.setInvoice_number(invoice_number.getText().toString());
                    grnResponse.setInv_date(invoice_date.getText().toString());
                    grnResponse.setEway(eway_txt.getText().toString());
                    SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
                    String user_id = prefs.getString("login_user_id", "");
                    grnResponse.setUser_id(Integer.parseInt(user_id));
                    grnResponse.setPo_id(order_number);
                    grnResponse.setInvoice_amount(Vendorinvoice_amount);
                    grnResponse.setInv_tax(Vendorinv_tax_amt);
                    grnResponse.setEnterted_inv_tax(invoice_tax_amt.getText().toString());
                    grnResponse.setEnterted_invoice_amount(invoice_amount.getText().toString());

                    // Gson gson = new Gson();
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    String json = gson.toJson(grnResponse);
                    System.out.println("JOSN::" + json);
                    progressDialog = new ProgressDialog(GrnDetailsActivity.this);
                    progressDialog.setTitle("ProgressDialog");
                    progressDialog.setMessage("Loading...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMax(100);
                    progressDialog.getProgress();
                    progressDialog.setCancelable(false);
                    saveaction(json);
                } else {
                    ViewDialog ss = new ViewDialog();
                    ss.displayError("Min 1 qty required to create GRN ");
                }
            }
        }

    }

    void saveaction(String json) {
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), upload_file);
        // RequestBody requestFiledd = RequestBody.create("text/plain", "df",gson.toJson(grnResponse).toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), json);
        // RequestBody requestFilea = RequestBody.create("values", grnResponse);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("invoice_file", upload_file.getName(), requestFile);
        //  MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", upload_file.getName(), requestFile);

        Call<CreateGrnResponse> call = api.create_grn(description, multipartBody);

        call.enqueue(new Callback<CreateGrnResponse>() {
            @Override
            public void onResponse(Call<CreateGrnResponse> call, Response<CreateGrnResponse> response) {
                String response_status = response.body().getStatus();
                progressDialog.dismiss();
                String Grn_no = response.body().getGrn_no();
                String api_msg = response.body().getApi_message();
                if (response_status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "GRN Created" + Grn_no, Toast.LENGTH_SHORT).show();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(GrnDetailsActivity.this, "Sucesss", grnResponse.getPo_id(), Grn_no);

                } else {
                    Toast.makeText(getApplicationContext(), api_msg, Toast.LENGTH_SHORT).show();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(GrnDetailsActivity.this, "Failure", "", "");
                }

            }

            @Override
            public void onFailure(Call<CreateGrnResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            Bitmap bmp = BitmapFactory.decodeStream((InputStream)url.getContent());
            return bmp;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private boolean compare_to(String fromstr, String tostr, String type) {
        boolean is_match = false;
        if (type.equals("string")) {
            if (fromstr.equals(tostr)) {
                is_match = true;
            }
        } else if (type.equals("float")) {
            if (Float.parseFloat(fromstr) == Float.parseFloat(tostr)) {
                is_match = true;
            }
        } else if (type.equals("int")) {
            if (Integer.parseInt(fromstr) == Integer.parseInt(tostr)) {
                is_match = true;
            }
        }
        return is_match;
    }

    private boolean form_validation() {
        boolean allow_proceed = true;
        String msg = "";
        String invoice_date_value = invoice_date.getText().toString();
        String invoice_no = invoice_number.getText().toString();
        String invoice_amt = invoice_amount.getText().toString();
        String gstn_number = GSTN_number.getText().toString();
        //  String eway_txt = eway_txt.getText().toString();
//        if (is_invoice_amt_match && is_invoice_tax_amt_match && is_gst_no_match && invoice_no.length() > 0) {
//            allow_proceed = true;
//        }
//        else{
//            msg ="Please fill the details";
//        }
        if (!is_invoice_amt_match) {
            allow_proceed = false;
            msg = "Please fill the details";
            invoice_amount.setError("Invoice amount does't match");
        }
        if (!is_gst_no_match) {
            allow_proceed = false;
            msg = "Please fill the details";
            GSTN_number.setError("Invalid GSTN number");
        }
        if (invoice_no.length() == 0) {
            allow_proceed = false;
            msg = "Please fill the details";
            invoice_number.setError("Enter Invoice number");
        }
        if (!is_invoice_tax_amt_match) {
            allow_proceed = false;
            msg = "Please fill the details";
            invoice_tax_amt.setError("Invoice tax amount does't match");
        }
        if (invoice_date_value.equals("Choose date")) {
            allow_proceed = false;
            msg = msg.concat("\n \n Choose Invoice date");
            //ViewDialog d = new ViewDialog();
            //d.displayError("Choose Invoice date \n \n -Kindly fill all details");
        }
        if (upload_file == null) {
            allow_proceed = false;
            msg = msg.concat("\n \n Upload Invoice image.");

        }

        if (!allow_proceed) {
            ViewDialog d = new ViewDialog();
            d.displayError(msg);
        }

        return allow_proceed;
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

                // Boolean is_valid = true;
                Boolean is_valid = validateGSTNumber(GSTN_number.getText().toString());
                if (is_valid) {
                    display_scanner_view(product_id, display_bbid, display_imei_no, display_serial_no);
                } else {
                    Toast.makeText(GrnDetailsActivity.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
                    ViewDialog alert = new ViewDialog();
                    String errormsg = "Enter Valid GSTN No";
                    alert.displayError(errormsg);

                }
            }

            @Override
            public void Check_receviec_qty(GRNResponse.ProductModel pomodel, int entered_qty, int required_qty) {
                if (entered_qty > required_qty) {
                    Toast.makeText(getApplicationContext(), "You are not allowed to entered more than required qty", Toast.LENGTH_SHORT).show();
                    String error = "You are not allowed to entered more than required qty";
                    ViewDialog alert = new ViewDialog();
                    alert.displayError(error);
                } else {
                    pomodel.setRecevied_qty(entered_qty);
                }
                if (entered_qty > 0) {
                    add_min_qty = true;
                }
            }
        });

        Intent i = getIntent();
        order_number = i.getSerializableExtra("order_number").toString();
        order_date = i.getSerializableExtra("order_date").toString();
        po_order_date_.setText(order_date);
        invoice_date.setFocusable(false);


        invoice_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(GrnDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   invoice_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                invoice_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

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
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = null;
                        try {
                            file = createImageFile();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fileUri = FileProvider.getUriForFile(GrnDetailsActivity.this, "com.inthree.WH.fileprovider", file);

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

        invoice_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (invoice_amount.getText().toString().length() > 0) {
                    boolean is_match = compare_to(Vendorinvoice_amount, invoice_amount.getText().toString(), "float");
                    if (is_match) {
                        invoice_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                        is_invoice_amt_match = true;
                    } else {
                        invoice_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_notdone_black_24dp, 0);
                        is_invoice_amt_match = false;
                    }
                } else {
                    invoice_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }
            }
        });

        invoice_tax_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (invoice_tax_amt.getText().toString().length() > 0) {
                    boolean is_match = compare_to(Vendorinv_tax_amt, invoice_tax_amt.getText().toString(), "float");
                    if (is_match) {
                        invoice_tax_amt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                        is_invoice_tax_amt_match = true;
                    } else {
                        invoice_tax_amt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_notdone_black_24dp, 0);
                        is_invoice_tax_amt_match = false;
                    }
                } else {
                    invoice_tax_amt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }
            }
        });


        GSTN_number.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (GSTN_number.getText().toString().length() > 0) {
                    boolean is_match = compare_to(VendorGSTno, GSTN_number.getText().toString(), "string");
                    if (is_match) {
                        GSTN_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                        is_gst_no_match = true;
                    } else {
                        GSTN_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_notdone_black_24dp, 0);
                        is_gst_no_match = false;
                    }
                } else {
                    GSTN_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                boolean is_valid = validateGSTNumber(GSTN_number.getText().toString());
//                if (s.length() > 0) {
//                    if (is_valid) {
//                        GSTVerifyIcon.setVisibility(View.VISIBLE);
//                    } else {
//                        Toast.makeText(GrnDetailsActivity.this, "Enter Valid GSTN No", Toast.LENGTH_LONG).show();
//                        // GSTN_number.setText("");
//                        GSTVerifyIcon.setVisibility(View.GONE);
//                    }
//                }
            }
        });
    }

    void display_scanner_view(String product_id, boolean display_bbid, boolean display_imei_no, boolean display_serial_no) {
        dialog = new Dialog(GrnDetailsActivity.this);
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

        serial_txt_no = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
        bbid_no = (EditText) popUpView.findViewById(R.id.bbid_number_scan_value);
        imei_txt_no = (EditText) popUpView.findViewById(R.id.imei_scan_value);

        if (!display_bbid) {
            popUpView.findViewById(R.id.scan_bbid_layout).setVisibility(View.GONE);
        }
        if (!display_imei_no) {
            popUpView.findViewById(R.id.scan_imei_layout).setVisibility(View.GONE);
        }
        if (!display_serial_no) {
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
                String bbid = (display_bbid) ? bbid_no.getText().toString() : "";
                String imei_no = (display_imei_no) ? imei_txt_no.getText().toString() : "";
                String serial_no = (display_serial_no) ? serial_txt_no.getText().toString() : "";
                boolean process_req = true;
                if (display_bbid) {
                    if (bbid.length() == 0) {
                        bbid_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (display_serial_no) {
                    if (serial_txt_no.length() == 0) {
                        serial_txt_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (display_imei_no) {
                    if (imei_txt_no.length() == 0) {
                        imei_txt_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (process_req) {
                    check_duplicate_serial_no(bbid, imei_no, serial_no, display_bbid, display_serial_no, display_imei_no, bbid_no, imei_txt_no, serial_txt_no, dialog);
                }

            }
        });

        ok_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_ids.clear();
                dialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private File getOutputMediaFile(int type) {
        String IMAGE_DIRECTORY_NAME = "imageuploadtest";
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                mediaStorageDir.mkdirs();

            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private synchronized void check_duplicate_serial_no(String bbid, String imei_no, String serial_no, boolean display_bbid, boolean display_serial_no, boolean display_imei_no, EditText edit_bbid, EditText edit_imei, EditText edit_serial, Dialog dialog) {
        String message = "";
        // Call the API and load the details view of PO order
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        CheckDuplicateRequest request = new CheckDuplicateRequest();
        request.setBb_id(bbid);
        request.setImei_no(imei_no);
        request.setSerial_no(serial_no);

        Call<CheckDuplicateResponse> call = api.check_duplicate_ids(request);

        call.enqueue(new Callback<CheckDuplicateResponse>() {
            @Override
            public void onResponse(Call<CheckDuplicateResponse> call, Response<CheckDuplicateResponse> response) {
                String response_status = response.body().getStatus();
                if (response_status.equals("success")) {
                    is_bbid_duplicate = response.body().getDuplicateList().isIs_bb_id_duplicate();
                    is_imei_no_duplicate = response.body().getDuplicateList().isIs_imei_duplicate();
                    is_serial_no_duplicate = response.body().getDuplicateList().isIs_serial_no_duplicate();
                    if (response.body().getDuplicateList().isIs_bb_id_duplicate() ||
                            response.body().getDuplicateList().isIs_imei_duplicate() ||
                            response.body().getDuplicateList().isIs_serial_no_duplicate()) {

                        if (is_bbid_duplicate && display_bbid) {
                            is_duplicate = true;
                            bbid_no.setError("Already exist");
                        }
                        if (is_imei_no_duplicate && display_imei_no) {
                            is_duplicate = true;
                            imei_txt_no.setError("Already exist");
                        }
                        if (is_serial_no_duplicate && display_serial_no) {
                            is_duplicate = true;
                            serial_txt_no.setError("Already exist");
                        }
                        if (!is_duplicate) {
                            is_duplicate = false;
                            boolean show_hide = update_product_qty(bbid, imei_no, serial_no, edit_bbid, edit_imei, edit_serial, dialog);
                            if (!show_hide)
                                dialog.dismiss();
                        }

                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    } else {
                        is_duplicate = false;
                        boolean show_hide = update_product_qty(bbid, imei_no, serial_no, edit_bbid, edit_imei, edit_serial, dialog);
                        if (!show_hide)
                            dialog.dismiss();
                    }

                }

            }

            @Override
            public void onFailure(Call<CheckDuplicateResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
        //return new boolean[]{is_duplicate, is_bbid_duplicate, is_imei_no_duplicate, is_serial_no_duplicate};


    }

    private boolean update_product_qty(String bbid, String imei_no, String serial_no, EditText edit_bbid, EditText edit_imei, EditText edit_serial, Dialog dialog) {
        Scanner_List p = new Scanner_List(bbid, imei_no, serial_no);
        boolean show_dialog = false;
        if (qr_scanner_list.containsKey(Selected_product_id)) {
            List<Scanner_List> already_added_list = qr_scanner_list.get(Selected_product_id);
            if (!already_added_list.contains(p)) {
                already_added_list.add(p);
                show_dialog = false;

            } else {
                Toast.makeText(getApplicationContext(), "Scanned Already!", Toast.LENGTH_LONG).show();
                edit_bbid.setError("Already scanned");
                edit_imei.setError("Already scanned");
                edit_serial.setError("Already scanned");
                show_dialog = true;
            }
        } else {
            List<Scanner_List> new_added_list = new ArrayList<>();
            new_added_list.add(p);
            qr_scanner_list.put(Selected_product_id, new_added_list);
            show_dialog = false;
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
        return show_dialog;
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
            File file = new File(currentPhotoPath);

            uploadImage(file);

        }
        if (requestCode == REQUEST_QTY_BY_BBID || requestCode == REQUEST_QTY_BY_IMEI || requestCode == REQUEST_QTY_BY_Serail) {
            String scaned_code = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = scaned_code;
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

    private void uploadImage(File file) {
//        String path_value = path.toString();
//        File file = new File(path_value);
        //  uploadFile(file, "");
        upload_file = file;
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
//        LoginResponse r = new LoginResponse();
//        String u_id = r.getUser_id();


        Call<GRNResponse> call = api.get_grn_details(request);

        call.enqueue(new Callback<GRNResponse>() {
            @Override
            public void onResponse(Call<GRNResponse> call, Response<GRNResponse> response) {
                String response_status = response.body().getStatus();

                response.body().getGrn_details().getProductmodel();
                if (response_status.equals("success")) {
                    //grnResponse = new GRNResponse.GRN_details();
                    grnResponse = response.body().getGrn_details();
                    // GSTN_number.setText((grnResponse.getVendor_gstinno() == null) ? " " : grnResponse.getVendor_gstinno());
                    VendorGSTno = (grnResponse.getVendor_gstinno() == null) ? "18AABCT3518Q1ZV" : grnResponse.getVendor_gstinno();
                    Vendorinv_tax_amt = (grnResponse.getInv_tax() == null) ? " " : grnResponse.getInv_tax();
                    Vendorinvoice_amount = (grnResponse.getInvoice_amount() == null) ? " " : grnResponse.getInvoice_amount();
                    merchant_name.setText((grnResponse.getSupplier_name() == null) ? " " : grnResponse.getSupplier_name());
                    Product_list = grnResponse.getProductmodel();
                    if (grnResponse.getProductmodel() != null)
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

    private void uploadFile(File file, String desc) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.1.34/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        // Call<MyResponse> call = api.upload(multipartBody);
        Call<ResponseBody> call = api.upload(multipartBody);


        //finally performing the call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("RES+++" + response);
//                if (response) {
//                    Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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

    public class ViewDialog {

        public void displayError(String errmsg) {

            AlertDialog alertDialog = new AlertDialog.Builder(GrnDetailsActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(errmsg);
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dail, int w) {
                    // getActivity().finish();
                    alertDialog.hide();
                }
            });
            alertDialog.show();
        }

        public void showDialog(Activity activity, String msg, String Po_no, String grn_no) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            if (msg.equals("Failure")) {
                dialog.setContentView(R.layout.failure_page);

            } else {
                dialog.setContentView(R.layout.success_page);
                TextView po_text = (TextView) dialog.findViewById(R.id.Po_text);
                TextView grn_text = (TextView) dialog.findViewById(R.id.text);
                po_text.setText("Purchase Order -" + Po_no);
                grn_text.setText("GRN number -" + grn_no);
            }
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (!msg.equals("Failure")) {
                        Intent i = new Intent(GrnDetailsActivity.this, POrderListActivity.class);
                        i.putExtra("login_user_id", grnResponse.getUser_id());
                        startActivity(i);
                    }
                }
            });
            dialog.show();

        }
    }


}