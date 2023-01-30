package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;

import java.util.List;

public class ProductStockNotificationAdapter extends RecyclerView.Adapter<ProductStockNotificationAdapter.ProductStockNotificatonViewHolder> {
    private Context mcontext;
    private List<Product> mproductlist;

    public ProductStockNotificationAdapter(Context mcontext, List<Product> mproductlist) {
        this.mcontext = mcontext;
        this.mproductlist = mproductlist;
    }

    @NonNull
    @Override
    public ProductStockNotificatonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_product_stocknotif,parent,false);
        return new ProductStockNotificatonViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ProductStockNotificatonViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.btstockamount.setText(String.valueOf(product.gettotalstock()));
        holder.tvproductname.setText(product.getname());
        if (mproductlist.get(position).gettotalstock() == mproductlist.get(position).getMinimumstock()) {
            holder.btstockamount.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.yellow)));
        } else if(mproductlist.get(position).gettotalstock() < mproductlist.get(position).getMinimumstock()) {
            holder.btstockamount.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.red)));
        } else if (mproductlist.get(position).gettotalstock() == 0){
            holder.btstockamount.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.black)));
        }
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public class ProductStockNotificatonViewHolder extends RecyclerView.ViewHolder {
        TextView tvproductname;
        Button btstockamount;
        public ProductStockNotificatonViewHolder(@NonNull View itemView) {
            super(itemView);
            btstockamount = itemView.findViewById(R.id.btstockamount);
            tvproductname = itemView.findViewById(R.id.tvproductname);
        }
    }
}
