package com.inthree.WH;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inthree.WH.API.Api;
import com.inthree.WH.Adapter.PoOrderRecycleListAdapter;;
import com.inthree.WH.model.PoOrderRequest;
import com.inthree.WH.model.PoOrderResponse;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class POrderListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PoOrderRecycleListAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.po_order_list_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new PoOrderRecycleListAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

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

            call.enqueue(new Callback<PoOrderResponse>() {
                @Override
                public void onResponse(Call<PoOrderResponse> call, Response<PoOrderResponse> response) {
                    String po_api_status= response.body().getStatus();
                    String po_api_msg= response.body().getApi_message();
                    if(po_api_status.equals("success")){
                        List<PoOrderResponse.PoOrderModel> orderResponse = response.body().getOrdermodel();
                        recyclerViewAdapter.setData(orderResponse);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), po_api_msg, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<PoOrderResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setMenuItem(item);
        return true;
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                recyclerViewAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });

    }



}
