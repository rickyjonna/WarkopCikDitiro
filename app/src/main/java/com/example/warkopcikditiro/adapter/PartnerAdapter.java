package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Partner;

import java.util.List;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.PartnerViewHolder> {
    private Context mcontext;
    private List<Partner> mpartnerlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public PartnerAdapter(Context xcontext, List<Partner> xpartnerlist) {
        this.mcontext = xcontext;
        this.mpartnerlist = xpartnerlist;
    }

    @NonNull
    @Override
    public PartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_partner,parent,false);
        return new PartnerViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerViewHolder holder, int position) {
        Partner partner = mpartnerlist.get(position);
        holder.tvowner.setText(partner.getOwner());
        holder.tvprofit.setText(String.valueOf(partner.getProfit()));
    }

    @Override
    public int getItemCount() {
        return mpartnerlist.size();
    }

    public class PartnerViewHolder extends RecyclerView.ViewHolder {
        TextView tvowner, tvprofit;
        ImageButton ibtedit, ibtdelete;
        public PartnerViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvowner = itemView.findViewById(R.id.tvowner);
            tvprofit = itemView.findViewById(R.id.tvprofit);
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
