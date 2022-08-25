package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.ActiveUser;

import java.util.List;

public class ActiveUserAdapter extends RecyclerView.Adapter<ActiveUserAdapter.ActiveUserViewHolder> {

    private Context mContext;
    private List<ActiveUser> activeuserlist;

    public ActiveUserAdapter(Context mContext, List<ActiveUser> activeuserlist) {
        this.mContext = mContext;
        this.activeuserlist = activeuserlist;
    }

    @NonNull
    @Override
    public ActiveUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_onboard_activeuser,parent,false);

        return new ActiveUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveUserViewHolder holder, int position) {
        ActiveUser activeuser = activeuserlist.get(position);
        holder.btactiveuser.setText(activeuser.getName());
        holder.btactiveuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return activeuserlist.size();
    }

    public static class ActiveUserViewHolder extends RecyclerView.ViewHolder{
        Button btactiveuser;
        public ActiveUserViewHolder(@NonNull View itemView) {
            super(itemView);
            btactiveuser = itemView.findViewById(R.id.btactiveuser);
        }
    }
}
