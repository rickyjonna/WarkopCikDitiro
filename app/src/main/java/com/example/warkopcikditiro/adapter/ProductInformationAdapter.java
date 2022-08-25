package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.ProductInformation;

import java.util.List;

public class ProductInformationAdapter extends RecyclerView.Adapter<ProductInformationAdapter.ProductInformationViewHolder> {
    private Context mcontext;
    private List<ProductInformation> mproductinformationlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener xlistener){
        mlistener = xlistener;
    }

    public ProductInformationAdapter(Context xcontext, List<ProductInformation> xproductinformationlist) {
        this.mcontext = xcontext;
        this.mproductinformationlist = xproductinformationlist;
    }

    @NonNull
    @Override
    public ProductInformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_kitchen_productsame_detail,parent,false);
        return new ProductInformationViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInformationViewHolder holder, int position) {
        ProductInformation productinformation = mproductinformationlist.get(position);
        holder.tvrvdkpstotal.setText(String.valueOf(productinformation.getTotal()));
        if(productinformation.getTable_id()==0){
            holder.tvrvdkpsinformation.setText(productinformation.getInformation());
        } else {
            if(productinformation.getTable_extend()==0){
                holder.tvrvdkpsinformation.setText("Meja " + String.valueOf(productinformation.getTable_number()));
            }else{
                holder.tvrvdkpsinformation.setText("Meja " + String.valueOf(productinformation.getTable_number()) + "-" + String.valueOf(productinformation.getTable_extend()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mproductinformationlist.size();
    }

    public static class ProductInformationViewHolder extends RecyclerView.ViewHolder{
        TextView tvrvdkpstotal, tvrvdkpsinformation;
        public ProductInformationViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvrvdkpstotal = itemView.findViewById(R.id.tvrvdkpstotal);
            tvrvdkpsinformation = itemView.findViewById(R.id.tvrvdkpsinformation);
            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onitemclick(position);
                    }
                }
            });
        }
    }
}
