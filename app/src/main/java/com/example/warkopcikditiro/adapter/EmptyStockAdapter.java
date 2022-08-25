package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.EmptyStock;
import java.util.List;

public class EmptyStockAdapter extends RecyclerView.Adapter<EmptyStockAdapter.EmptyStockViewHolder> {
    private final Context mContext;
    private final List<EmptyStock> emptystocklist;

    public EmptyStockAdapter(Context mContext, List<EmptyStock> emptystocklist) {
        this.mContext = mContext;
        this.emptystocklist = emptystocklist;
    }

    @NonNull
    @Override
    public EmptyStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_empty_stock,parent,false);
        return new EmptyStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyStockViewHolder holder, int position) {
        EmptyStock emptystock = emptystocklist.get(position);
        holder.btemptystock.setText(emptystock.getName());
        if(emptystock.getQuantity()>0)
        {
            holder.btemptystock.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else{
            holder.btemptystock.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.btemptystock.setOnClickListener(view -> {
            // pergi ke stok halaman atur stok
        });
    }

    @Override
    public int getItemCount() {
        return emptystocklist.size();
    }


    public static class EmptyStockViewHolder extends RecyclerView.ViewHolder{
        Button btemptystock;
        public EmptyStockViewHolder(@NonNull View itemView) {
            super(itemView);

            btemptystock = itemView.findViewById(R.id.btemptystock);
        }
    }
}
