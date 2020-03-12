package com.inthree.WH.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.inthree.WH.GrnDetailsActivity;
import com.inthree.WH.R;
import com.inthree.WH.model.PoOrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PoOrderRecycleListAdapter extends RecyclerView.Adapter<PoOrderRecycleListAdapter.ViewHolder> implements Filterable {

    List<PoOrderResponse.PoOrderModel> order_list_records;
    List<PoOrderResponse.PoOrderModel> order_list_recordswithfilter;
    Context context;
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PoOrderResponse.PoOrderModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(order_list_records);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PoOrderResponse.PoOrderModel item : order_list_records) {
                    if (item.getSupplier_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            order_list_recordswithfilter.clear();
            order_list_recordswithfilter.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public PoOrderRecycleListAdapter(List<PoOrderResponse.PoOrderModel> order_list) {
        order_list_records = order_list;
        order_list_recordswithfilter = order_list;
    }

    public PoOrderRecycleListAdapter() {
        order_list_records = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.po_order_content_view, parent, false);

        PoOrderRecycleListAdapter.ViewHolder viewHolder = new PoOrderRecycleListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoOrderResponse.PoOrderModel pomdel = order_list_records.get(position);
        holder.order_number.setText((pomdel.getOrder_number() == null) ? "" : pomdel.getOrder_number());
        holder.vendor_name.setText((pomdel.getSupplier_name() == null) ? "" : pomdel.getSupplier_name());
        holder.order_amount.setText((pomdel.getOrder_amount() == null) ? "" : "₹  " + pomdel.getOrder_amount());
        holder.order_date.setText((pomdel.getOrder_created_date() == null) ? "" : pomdel.getOrder_created_date());
    }

    @Override
    public int getItemCount() {
        return order_list_records.size();
    }

    public void setData(List<PoOrderResponse.PoOrderModel> data) {
        this.order_list_records = data;
        this.order_list_recordswithfilter = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_number, vendor_name, order_amount, order_date;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            order_number = (TextView) view.findViewById(R.id.order_number);
            vendor_name = (TextView) view.findViewById(R.id.Vendor_name);
            order_amount = (TextView) view.findViewById(R.id.order_amount);
            order_date = (TextView) view.findViewById(R.id.order_date);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /// Toast.makeText(this.getClass(),"Click Action",Toast.LENGTH_LONG).show();
                    Toast.makeText(view.getContext(), "Click detailed view",
                            Toast.LENGTH_LONG).show();

                    // selected item


                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(view.getContext(), GrnDetailsActivity.class);
                    i.putExtra("order_number", order_number.getText().toString());
                    i.putExtra("order_date", order_date.getText().toString());
                    context.startActivity(i);


                }

            });


        }
    }
}
