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
import com.example.warkopcikditiro.model.ProductCategory;
import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder> {
    private Context mcontext;
    private List<ProductCategory> mpcatlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public ProductCategoryAdapter(Context mcontext, List<ProductCategory> mpcatlist) {
        this.mcontext = mcontext;
        this.mpcatlist = mpcatlist;
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_productcategory,parent,false);
        return new ProductCategoryViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position) {
        ProductCategory productcategory = mpcatlist.get(position);
        holder.tvpcatname.setText(String.valueOf(productcategory.getinformation()));
    }

    @Override
    public int getItemCount() {
        return mpcatlist.size();
    }

    public class ProductCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvpcatname;
        ImageButton ibtedit, ibtdelete;

        public ProductCategoryViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvpcatname = itemView.findViewById(R.id.tvpcatname);
            ibtedit = itemView.findViewById(R.id.ibtedit);
            ibtdelete = itemView.findViewById(R.id.ibtdelete);

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
            ibtdelete.setOnClickListener(view -> {
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
