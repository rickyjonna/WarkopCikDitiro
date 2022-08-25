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
import com.example.warkopcikditiro.model.User;

import java.util.List;

public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.RVUserViewHolder> {
    private Context mcontext;
    private List<User> muserlist;
    private onitemclicklistener mlistener;

    public RVUserAdapter(Context xcontext, List<User> xuserlist) {
        this.mcontext = xcontext;
        this.muserlist = xuserlist;
    }

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    @NonNull
    @Override
    public RVUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_user,parent,false);
        return new RVUserViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVUserViewHolder holder, int position) {
        User user = muserlist.get(position);
        holder.tvuname.setText(user.getName());
        holder.tvuaddress.setText(user.getAddress());
        holder.tvuphone_number.setText(user.getPhone_number());
        holder.tvutype.setText(user.getUser_type());
    }

    @Override
    public int getItemCount() {
        return muserlist.size();
    }

    public class RVUserViewHolder extends RecyclerView.ViewHolder{
        TextView tvuname, tvuaddress, tvuphone_number, tvutype;
        ImageButton ibtedit, ibtdelete;
        public RVUserViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvuname = itemView.findViewById(R.id.tvuname);
            tvuaddress = itemView.findViewById(R.id.tvuaddress);
            tvuphone_number = itemView.findViewById(R.id.tvuphone_number);
            tvutype = itemView.findViewById(R.id.tvutype);
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
