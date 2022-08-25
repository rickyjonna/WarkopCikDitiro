package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;

import java.util.List;

public class KitchenSameProductAdapter extends RecyclerView.Adapter<KitchenSameProductAdapter.KitchenSameProductViewHolder> {
    private Context mcontext;
    private List<Product> msameproductlist;
    private onitemclicklistener mlistener;

    public KitchenSameProductAdapter(Context xcontext, List<Product> xsameproductlist) {
        this.mcontext = xcontext;
        this.msameproductlist = xsameproductlist;
    }

    public interface onitemclicklistener{
        void onitemclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener xlistener){
        mlistener = xlistener;
    }

    @NonNull
    @Override
    public KitchenSameProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_productsame_kitchen,parent,false);
        return new KitchenSameProductViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull KitchenSameProductViewHolder holder, int position) {
        Product product = msameproductlist.get(position);
        holder.btproductsame.setText(product.getname());
        holder.tvproductsame.setText(String.valueOf(product.gettotal()));
    }

    @Override
    public int getItemCount() {
        return msameproductlist.size();
    }

    public static class KitchenSameProductViewHolder extends RecyclerView.ViewHolder {
        Button btproductsame;
        TextView tvproductsame;
        public KitchenSameProductViewHolder(@NonNull View itemView, onitemclicklistener xlistener) {
            super(itemView);
            btproductsame = itemView.findViewById(R.id.btproductsame);
            tvproductsame = itemView.findViewById(R.id.tvproductsame);
            btproductsame.setOnClickListener(view -> {
                if(xlistener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        xlistener.onitemclick(position);
                    }
                }
            });
        }
    }
}
