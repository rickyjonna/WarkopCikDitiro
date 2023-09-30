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

public class ProductStockAdapter extends RecyclerView.Adapter<ProductStockAdapter.ProductStockViewHolder> {
    private final Context mcontext;
    private final List<Product> mproductlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

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
        return new ProductStockViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductStockViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.tvrvpsproduct_name.setText(product.getname());
        holder.tvrvpstotal_amount.setText(String.valueOf(product.gettotalstock()));
        holder.tvrvpsminimum_amount.setText(String.valueOf(product.getMinimumstock()));
        holder.tvrvpsunit.setText(product.getUnit());
        holder.tvrvpsunit2.setText(product.getUnit());
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public static class ProductStockViewHolder extends RecyclerView.ViewHolder {
        TextView tvrvpsproduct_name, tvrvpstotal_amount, tvrvpsminimum_amount, tvrvpsunit, tvrvpsunit2;
        ImageButton ibtedit;

        public ProductStockViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvrvpsproduct_name = itemView.findViewById(R.id.tvrvpsproduct_name);
            tvrvpstotal_amount = itemView.findViewById(R.id.tvrvpstotal_amount);
            tvrvpsminimum_amount = itemView.findViewById(R.id.tvrvpsminimum_amount);
            tvrvpsunit = itemView.findViewById(R.id.tvrvpsunit);
            tvrvpsunit2 = itemView.findViewById(R.id.tvrvpsunit2);
            ibtedit = itemView.findViewById(R.id.ibtedit);

            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onitemclick(position);
                    }
                }
            });
            ibtedit.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.oneditclick(position);
                    }
                }
            });
        }
    }
}
