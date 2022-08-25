package com.example.warkopcikditiro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder>{

    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    private final Context mContext;
    private final List<Table> mtablelist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public TableAdapter(Context mContext, List<Table> xtablelist) {
        this.mContext = mContext;
        this.mtablelist = xtablelist;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_table,parent,false);
        return new TableViewHolder(view, mlistener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        //inisiasi objek
        sharedpref = mContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        Table table = mtablelist.get(position);

        //method
        if(table.getextend() == 0)
        {
            holder.bttable.setText(String.valueOf(table.getnumber()));
        } else {
            holder.bttable.setText(table.getnumber()+"-"+table.getextend());
        }
        if(table.getstatus().equals("Available")){
            holder.bttable.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.brown)));
        }else{
            holder.bttable.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey)));
        }
    }

    @Override
    public int getItemCount() {
        return mtablelist.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder{
        Button bttable;
        public TableViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            bttable = itemView.findViewById(R.id.bttable);
            bttable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onitemclick(position);
                        }
                    }
                }
            });
        }
    }
}
