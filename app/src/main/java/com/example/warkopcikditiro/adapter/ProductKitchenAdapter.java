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

public class ProductKitchenAdapter extends RecyclerView.Adapter<ProductKitchenAdapter.ProductKitchenViewHolder> {
    Context mcontext;
    List<Product> mproductlist;
    private onitemclicklistener mlistener;

    public ProductKitchenAdapter(Context xcontext, List<Product> xproductlist) {
        this.mcontext = xcontext;
        this.mproductlist = xproductlist;
    }

    public interface onitemclicklistener{
        void ondeleteclick(int position);
        void onprocessclick(int position);
        void ondoneclick(int position);
        void onserveclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener xlistener){
        mlistener = xlistener;
    }

    @NonNull
    @Override
    public ProductKitchenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_product_kitchen,parent,false);
        return new ProductKitchenViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductKitchenViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.tvkrvpnumber.setText(String.valueOf(position+1));
        holder.tvkrvpname.setText(product.getname());
        holder.tvkrvpstatus.setText(product.getstatus());
        holder.tvkrvptotal.setText(String.valueOf(product.gettotal()));
        switch (product.getstatus_id()){
            case 1 :
                holder.ibkrvpcancel.setBackgroundResource(R.drawable.ic_cancel_50);
                holder.ibkrvpprocess.setBackgroundResource(R.drawable.ic_refreshblue_50);
                holder.ibkrvpdone.setVisibility(View.INVISIBLE);
                holder.ibkrvpserve.setVisibility(View.INVISIBLE);
                holder.ibkrvpinformation.setVisibility(View.INVISIBLE);
                break;
            case 2 :
                holder.ibkrvpcancel.setVisibility(View.INVISIBLE);
                holder.ibkrvpprocess.setVisibility(View.INVISIBLE);
                holder.ibkrvpdone.setBackgroundResource(R.drawable.ic_donegreen_50);
                holder.ibkrvpserve.setVisibility(View.INVISIBLE);
                holder.ibkrvpinformation.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.ibkrvpcancel.setVisibility(View.INVISIBLE);
                holder.ibkrvpprocess.setVisibility(View.INVISIBLE);
                holder.ibkrvpdone.setVisibility(View.INVISIBLE);
                holder.ibkrvpserve.setBackgroundResource(R.drawable.ic_serve_50);
                holder.ibkrvpinformation.setVisibility(View.INVISIBLE);
                break;
            case 4:
                holder.ibkrvpcancel.setVisibility(View.INVISIBLE);
                holder.ibkrvpprocess.setVisibility(View.INVISIBLE);
                holder.ibkrvpdone.setVisibility(View.INVISIBLE);
                holder.ibkrvpserve.setVisibility(View.INVISIBLE);
                holder.ibkrvpinformation.setBackgroundResource(R.drawable.ic_info_50);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }


    public static class ProductKitchenViewHolder extends RecyclerView.ViewHolder {
        TextView tvkrvpnumber, tvkrvpname, tvkrvpstatus, tvkrvptotal;
        ImageButton ibkrvpcancel, ibkrvpprocess, ibkrvpdone, ibkrvpserve, ibkrvpinformation;
        public ProductKitchenViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvkrvpnumber = itemView.findViewById(R.id.tvkrvpnumber);
            tvkrvpname = itemView.findViewById(R.id.tvkrvpname);
            tvkrvpstatus = itemView.findViewById(R.id.tvkrvpstatus);
            tvkrvptotal = itemView.findViewById(R.id.tvkrvptotal);
            ibkrvpcancel = itemView.findViewById(R.id.ibkrvpcancel);
            ibkrvpprocess = itemView.findViewById(R.id.ibkrvpprocess);
            ibkrvpdone = itemView.findViewById(R.id.ibkrvpdone);
            ibkrvpserve = itemView.findViewById(R.id.ibkrvpserve);
            ibkrvpinformation = itemView.findViewById(R.id.ibkrvpinformation);

            ibkrvpcancel.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.ondeleteclick(position);
                    }
                }
            });
            ibkrvpprocess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onprocessclick(position);
                        }
                    }
                }
            });
            ibkrvpdone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.ondoneclick(position);
                        }
                    }
                }
            });
            ibkrvpserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onserveclick(position);
                        }
                    }
                }
            });
        }
    }
}
