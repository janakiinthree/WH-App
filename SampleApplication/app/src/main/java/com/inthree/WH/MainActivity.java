package com.inthree.WH;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.inthree.WH.API.Api;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.Login;
import com.inthree.WH.model.LoginResponse;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_password)
    EditText mPassword;

    @BindView(R.id.edt_username)
    EditText mUsername;
    @BindView(R.id.login_error)
    TextView login_error;

    @BindView(R.id.login_button)
    Button mLoginButton;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    @OnTextChanged({R.id.edt_password,R.id.edt_username})
    void text_change()
    {
        login_error.setVisibility(View.GONE);
    }
    @OnClick({R.id.login_button})
    void login(View v) {
        if (!mUsername.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
            hideKeyboard();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("ProgressDialog");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.getProgress();
            progressDialog.setCancelable(false);
            progressDialog.show();
// To dismiss the dialog

            Check_login();

        }
    }

    // Check Login
    private void Check_login() {
        hideKeyboard();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Login request = new Login();
        Login.Data requestData = new Login.Data();
        requestData.setUser_name(mUsername.getText().toString().trim());
        requestData.setPassword(mPassword.getText().toString().trim());
        request.setmData(requestData);

        Call<LoginResponse> call = api.get_authincation(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String login_status = response.body().getStatus();
                progressDialog.dismiss();
                LoginResponse loginResponse = new LoginResponse();
                String user_id = response.body().getUser_id();
                if (login_status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Login Success!!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, POrderListActivity.class);
                    i.putExtra("login_user_id",user_id);
                    loginResponse.setUser_id(user_id);
                    editor.putString("login_user_id",user_id);
                    editor.commit();

                    startActivity(i);
                }
                else{
                    progressDialog.dismiss();
                    login_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();

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
