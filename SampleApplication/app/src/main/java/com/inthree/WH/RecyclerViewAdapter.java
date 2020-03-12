package com.inthree.WH;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.inthree.WH.model.Login;
import com.inthree.WH.model.LoginResponse;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<LoginResponse.POModel> po_list;
    Context context;


    public RecyclerViewAdapter() {
        po_list = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.po_order_content_view, parent, false);

        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        LoginResponse.POModel pomdel = po_list.get(position);
        String order_name = "PO0000"+position;
        holder.txtCoin.setText(order_name);
//        holder.txtMarket.setText(pomdel.market);
//        holder.txtPrice.setText("$" + String.format("%.2f", Double.parseDouble(pomdel.price)));
//        if (pomdel.coinName.equalsIgnoreCase("eth")) {
//            holder.cardView.setCardBackgroundColor(Color.WHITE);
//        } else {
//            holder.cardView.setCardBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public int getItemCount() {
        return po_list.size();
    }

    public void setData(List<LoginResponse.POModel> data) {
        this.po_list.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCoin;
        public TextView txtMarket;
        public TextView txtPrice;
        public CardView cardView;
        public BreakIterator edit_txt;

        public ViewHolder(View view) {
            super(view);

//            txtCoin = view.findViewById(R.id.txtCoin);
//            txtMarket = view.findViewById(R.id.txtMarket);
//            txtPrice = view.findViewById(R.id.txtPrice);
//            cardView = view.findViewById(R.id.cardView);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   /// Toast.makeText(this.getClass(),"Click Action",Toast.LENGTH_LONG).show();
//                    Toast.makeText(view.getContext(), "Click detailed view",
//                            Toast.LENGTH_LONG).show();
//
//                    // selected item
//
//
//                    // Launching new Activity on selecting single List Item
//                    Intent i = new Intent(view.getContext(), POCreationActivity.class);
//                    // sending data to new activity
//                    i.putExtra("order_number", txtCoin.getText().toString());
//                    i.putExtra("vendor", txtMarket.getText().toString());
//                    i.putExtra("order_amount", txtPrice.getText().toString());
////                    String order_number_val = i.getStringExtra("order_number");
////                    String vendor_val = i.getStringExtra("vendor");
////                    String order_amount_val = i.getStringExtra("order_amount");
////                    //context.bindService()startActivity(i);
//                   // context.startActivity(i);
//
//
//
//                }
//
//            });
        }
    }
}
