package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;

import java.util.List;

public class ProductCashierAdapter extends RecyclerView.Adapter<ProductCashierAdapter.ProductCashierViewHolder> {
    Context mcontext;
    List<Product> mproductlist;

    public ProductCashierAdapter(Context mcontext, List<Product> mproductlist) {
        this.mcontext = mcontext;
        this.mproductlist = mproductlist;
    }

    @NonNull
    @Override
    public ProductCashierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_product_cashier,parent,false);
        return new ProductCashierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCashierViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.tvcrvpnumber.setText(String.valueOf(position+1));
        holder.tvcrvpname.setText(product.getname());
        holder.tvcrvpstatus.setText(product.getstatus());
        holder.tvcrvptotal.setText(String.valueOf(product.gettotal()));
        int finalprice = product.gettotalprice() * product.gettotal();
        holder.tvcrvpprice.setText(String.valueOf(finalprice));
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public static class ProductCashierViewHolder extends RecyclerView.ViewHolder{
        TextView tvcrvpnumber, tvcrvpname, tvcrvpstatus, tvcrvptotal, tvcrvpprice;
        public ProductCashierViewHolder(@NonNull View itemView) {
            super(itemView);

            tvcrvpnumber = itemView.findViewById(R.id.tvcrvpnumber);
            tvcrvpname = itemView.findViewById(R.id.tvcrvpname);
            tvcrvpstatus = itemView.findViewById(R.id.tvcrvpstatus);
            tvcrvptotal = itemView.findViewById(R.id.tvcrvptotal);
            tvcrvpprice = itemView.findViewById(R.id.tvcrvpprice);
        }
    }
}
