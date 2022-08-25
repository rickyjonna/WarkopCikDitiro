package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;

import java.util.List;

public class ProductOrderedAdapter extends RecyclerView.Adapter<ProductOrderedAdapter.ProductOrderedViewHolder> {
    private Context mContext;
    private final List<Product> mproductlist;

    public ProductOrderedAdapter(Context xcontext, List<Product> xproductlist) {
        this.mContext = xcontext;
        this.mproductlist = xproductlist;
    }

    public static class ProductOrderedViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llorv;
        TextView tvponame, tvpototal, tvpoprice;
        public ProductOrderedViewHolder(@NonNull View itemView) {
            super(itemView);
            llorv = itemView.findViewById(R.id.llorv);
            tvponame = itemView.findViewById(R.id.tvponame);
            tvpototal = itemView.findViewById(R.id.tvpototal);
            tvpoprice = itemView.findViewById(R.id.tvpoprice);
        }
    }

    @NonNull
    @Override
    public ProductOrderedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_order_productordered,parent,false);
        return new ProductOrderedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderedViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        if(product.gettotal()==0){
            holder.llorv.setVisibility(View.GONE);
        }
        holder.tvponame.setText(product.getname());
        holder.tvpototal.setText(String.valueOf(product.gettotal()));
        holder.tvpoprice.setText(String.valueOf(product.gettotalprice()));
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }
}
