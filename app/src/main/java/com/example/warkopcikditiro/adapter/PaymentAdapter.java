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
import com.example.warkopcikditiro.model.Payment;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private Context mcontext;
    private List<Payment> mpaymentlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public PaymentAdapter(Context xcontext, List<Payment> xpaymentlist) {
        this.mcontext = xcontext;
        this.mpaymentlist = xpaymentlist;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_payment,parent,false);
        return new PaymentViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = mpaymentlist.get(position);
        holder.tvname.setText(payment.getInformation());
        holder.tvdiscount.setText(String.valueOf(payment.getDiscount()));
    }

    @Override
    public int getItemCount() {
        return mpaymentlist.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvdiscount;
        ImageButton ibtedit, ibtdelete;
        public PaymentViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvname);
            tvdiscount = itemView.findViewById(R.id.tvdiscount);
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

