package com.inthree.WH;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inthree.WH.API.Api;
import com.inthree.WH.Adapter.PoOrderRecycleListAdapter;
import com.inthree.WH.model.PoOrderRequest;
import com.inthree.WH.model.PoOrderResponse;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class POrderListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PoOrderRecycleListAdapter recyclerViewAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    List<PoOrderResponse.PoOrderModel> order_list_full = new ArrayList<>();
    List<PoOrderResponse.PoOrderModel> order_list_filter = new ArrayList<>();

    @BindView(R.id.input_search)
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.po_order_list_view);
        ButterKnife.bind(this);
        init_component();
        load_po_list();

    }

    private void init_component() {
        // Initalize the components in View
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new PoOrderRecycleListAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                processQuery(inputSearch.getText().toString());

            }
        });

    }

    void load_po_list() {
        // Call the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        PoOrderRequest request = new PoOrderRequest();
        Intent i = getIntent();
        String user_id = i.getSerializableExtra("login_user_id").toString();
        request.setUser_id(user_id);
        Call<PoOrderResponse> call = api.get_po_orderdetails(request);

        // Read the response from API and assign the details into PoOrder Model
        call.enqueue(new Callback<PoOrderResponse>() {
            @Override
            public void onResponse(Call<PoOrderResponse> call, Response<PoOrderResponse> response) {
                String po_api_status = response.body().getStatus();
                String po_api_msg = response.body().getApi_message();
                if (po_api_status.equals("success")) {
                    List<PoOrderResponse.PoOrderModel> orderResponse = response.body().getOrdermodel();
                    order_list_full = orderResponse;
                    recyclerViewAdapter.setData(orderResponse);
                } else {
                    Toast.makeText(getApplicationContext(), po_api_msg, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<PoOrderResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void processQuery(String query) {
        // Filter the query on Adapter
        List<PoOrderResponse.PoOrderModel> result = new ArrayList<>();
        order_list_filter = new ArrayList<>();
        // case insensitive search
        for (PoOrderResponse.PoOrderModel list : order_list_full) {
            if (list.getOrder_number().contains(query.toLowerCase())) {
                order_list_filter.add(list);
            }
        }
        recyclerViewAdapter.setData(order_list_filter);
    }

}
