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
import com.example.warkopcikditiro.model.Table;

import java.util.List;

public class RVTableAdapter extends RecyclerView.Adapter<RVTableAdapter.RVTableViewHolder>{
    private Context mcontext;
    private List<Table> mtablelist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public RVTableAdapter(Context xcontext, List<Table> xtablelist) {
        this.mcontext = xcontext;
        this.mtablelist = xtablelist;
    }

    @NonNull
    @Override
    public RVTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_rvtable,parent,false);
        return new RVTableViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVTableViewHolder holder, int position) {
        Table table = mtablelist.get(position);
        holder.tvtnumber.setText(String.valueOf(table.getnumber()));
        holder.tvtextend.setText(String.valueOf(table.getextend()));
        holder.tvtstatus.setText(String.valueOf(table.getstatus()));
    }

    @Override
    public int getItemCount() {
        return mtablelist.size();
    }

    public class RVTableViewHolder extends RecyclerView.ViewHolder {
        TextView tvtnumber, tvtextend, tvtstatus;
        ImageButton ibtedit, ibtdelete;
        public RVTableViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvtnumber = itemView.findViewById(R.id.tvtnumber);
            tvtextend = itemView.findViewById(R.id.tvtextend);
            tvtstatus = itemView.findViewById(R.id.tvtstatus);
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
