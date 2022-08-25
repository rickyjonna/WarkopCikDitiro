package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;

import java.util.List;

public class ProductOrdered2Adapter extends RecyclerView.Adapter<ProductOrdered2Adapter.ProductOrdered2ViewHolder> {
    private Context mcontext;
    private List<Product> mproductlist;

    public ProductOrdered2Adapter(Context xcontext, List<Product> xproductlist) {
        this.mcontext = xcontext;
        this.mproductlist = xproductlist;
    }

    @NonNull
    @Override
    public ProductOrdered2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_orderlist_productordered,parent,false);
        return new ProductOrdered2ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ProductOrdered2ViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        switch(product.getstatus_id()){
            case 1:
                holder.btolrvstatus.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.green)));
                break;
            case 2:
                holder.btolrvstatus.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.yellow)));
                break;
            case 3:
                holder.btolrvstatus.setBackgroundTintList(ColorStateList.valueOf(mcontext.getResources().getColor(R.color.red)));
                break;
        }
        holder.tvolrvname.setText(product.getname());
        holder.tvolrvstatus.setText(product.getstatus());
        holder.tvolrvtotal.setText(String.valueOf(product.gettotal()));
        holder.tvolrvprice.setText(String.valueOf(product.gettotalprice()));
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public static class ProductOrdered2ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llolrv;
        Button btolrvstatus;
        TextView tvolrvname, tvolrvstatus, tvolrvtotal, tvolrvprice;
        public ProductOrdered2ViewHolder(@NonNull View itemView) {
            super(itemView);
            llolrv = itemView.findViewById(R.id.llolrv);
            btolrvstatus = itemView.findViewById(R.id.btolrvstatus);
            tvolrvstatus = itemView.findViewById(R.id.tvolrvstatus);
            tvolrvname = itemView.findViewById(R.id.tvolrvname);
            tvolrvtotal = itemView.findViewById(R.id.tvolrvtotal);
            tvolrvprice = itemView.findViewById(R.id.tvolrvprice);
        }
    }
}
