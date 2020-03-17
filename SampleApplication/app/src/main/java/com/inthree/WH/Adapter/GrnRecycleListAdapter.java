package com.inthree.WH.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.inthree.WH.R;
import com.inthree.WH.model.GRNResponse;
import java.util.ArrayList;
import java.util.List;

public class GrnRecycleListAdapter extends RecyclerView.Adapter<GrnRecycleListAdapter.ViewHolder> {

    List<GRNResponse.ProductModel> product_list_records;
    Context context;
    private onScannerClick mScannerClick;


    public GrnRecycleListAdapter(List<GRNResponse.ProductModel> product_list) {
        product_list_records = product_list;
    }

    public GrnRecycleListAdapter() {
        product_list_records = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_product_list_view, parent, false);

        GrnRecycleListAdapter.ViewHolder viewHolder = new GrnRecycleListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GRNResponse.ProductModel pomdel = product_list_records.get(position);
        holder.product_name.setText((pomdel.getProduct_name() == null) ? "" : pomdel.getProduct_name());
        holder.item_code.setText((pomdel.getProduct_sku() == null) ? "" : pomdel.getProduct_sku());
        holder.ordered_qty.setText((pomdel.getQty() == null) ? "" : pomdel.getQty());
        holder.received_qty.setText(String.valueOf(pomdel.getRecevied_qty()));
        holder.barcodevalue.setText((pomdel.getBarcode_value() == null) ? "" : pomdel.getBarcode_value());

        holder.qrcode_recevied_cqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mScannerClick!=null){
                    mScannerClick.onClick(pomdel.getProduct_id());
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return this.product_list_records.size();
    }

    public void setData(List<GRNResponse.ProductModel> data) {
        this.product_list_records = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name, item_code, ordered_qty,barcodevalue;
        EditText received_qty;
        public ImageView qrcode_recevied_cqty;

        public ViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            item_code = (TextView) view.findViewById(R.id.item_code);
            ordered_qty = (TextView) view.findViewById(R.id.ordered_qty);
            received_qty = (EditText) view.findViewById(R.id.entered_qty);
            qrcode_recevied_cqty = (ImageView) view.findViewById(R.id.qrcode_recevied_cqty);
            barcodevalue = (TextView)view.findViewById(R.id.barcodevalue);

        }
    }

    public void setOnScannerClick(onScannerClick scannerClick){
        mScannerClick = scannerClick;
    }

    public interface onScannerClick{
        void onClick(String product_id);
    }
}
