package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.ProductStockActivity;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;
import com.example.warkopcikditiro.model.Table;
import java.util.List;

public class ProductStockAdapter extends RecyclerView.Adapter<ProductStockAdapter.ProductStockViewHolder> {
    private Context mcontext;
    private List<Product> mproductlist;

    public ProductStockAdapter(Context mcontext, List<Product> mproductlist) {
        this.mcontext = mcontext;
        this.mproductlist = mproductlist;
    }

    @NonNull
    @Override
    public ProductStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_productstock,parent,false);
        return new ProductStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductStockViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        for (int i=0; i<mproductlist.size(); i++){
            holder.tvrvpsnumber.setText(String.valueOf(i));
        }
        holder.tvrvpsproduct_name.setText(product.getname());
        holder.tvrvpstotal_amount.setText(String.valueOf(product.gettotalstock()));
        holder.tvrvpsminimum_amount.setText(String.valueOf(product.getMinimumstock()));
        holder.tvrvpsunit.setText(product.getUnit());
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public class ProductStockViewHolder extends RecyclerView.ViewHolder {
        TextView tvrvpsnumber, tvrvpsproduct_name, tvrvpstotal_amount, tvrvpsminimum_amount, tvrvpsunit;

        public ProductStockViewHolder(@NonNull View itemView) {
            super(itemView);
            tvrvpsnumber = itemView.findViewById(R.id.tvrvpsnumber);
            tvrvpsproduct_name = itemView.findViewById(R.id.tvrvpsproduct_name);
            tvrvpstotal_amount = itemView.findViewById(R.id.tvrvpstotal_amount);
            tvrvpsminimum_amount = itemView.findViewById(R.id.tvrvpsminimum_amount);
            tvrvpsunit = itemView.findViewById(R.id.tvrvpsunit);
        }
    }
}
