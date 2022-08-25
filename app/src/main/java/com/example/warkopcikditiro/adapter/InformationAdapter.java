package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Information;

import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.GojekOrderListViewHolder> {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    private Context mcontext;
    private List<Information> minformationlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
    }

    public void setonitemclicklistener(InformationAdapter.onitemclicklistener listener){
        mlistener = listener;
    }

    public InformationAdapter(Context xcontext, List<Information> xinformationlist) {
        this.mcontext = xcontext;
        this.minformationlist = xinformationlist;
    }

    @NonNull
    @Override
    public GojekOrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_onboard_orderlist_information,parent,false);
        return new GojekOrderListViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull GojekOrderListViewHolder holder, int position) {
        sharedpref = mcontext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        Information information = minformationlist.get(position);

        holder.btolinformation.setText(information.getorder_information());
    }

    @Override
    public int getItemCount() {
        return minformationlist.size();
    }

    public static class GojekOrderListViewHolder extends RecyclerView.ViewHolder {
        Button btolinformation;
        public GojekOrderListViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            btolinformation = itemView.findViewById(R.id.btolinformation);
            btolinformation.setOnClickListener(view -> {
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
