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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private Context mcontext;
    private List<Product> mproductlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener xlistener){
        mlistener = xlistener;
    }

    public ProductListAdapter(Context xcontext, List<Product> xproductlist) {
        this.mcontext = xcontext;
        this.mproductlist = xproductlist;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_productlist,parent,false);
        return new ProductListViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.tvprvproductname.setText(product.getname());
        holder.tvprvproductprice.setText(String.valueOf(product.gettotalprice()));
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder{
        TextView tvprvproductname, tvprvproductprice;
        ImageButton ibtprvdelete;
        public ProductListViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvprvproductname = itemView.findViewById(R.id.tvprvproductname);
            tvprvproductprice = itemView.findViewById(R.id.tvprvproductprice);
            ibtprvdelete = itemView.findViewById(R.id.ibtprvdelete);

            ibtprvdelete.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.ondeleteclick(position);
                    }
                }
            });
        }
    }
}
