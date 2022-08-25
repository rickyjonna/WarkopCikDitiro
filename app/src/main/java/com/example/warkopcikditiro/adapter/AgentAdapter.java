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
import com.example.warkopcikditiro.model.Agent;

import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {
    private Context mcontext;
    private List<Agent> magentlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public AgentAdapter(Context xcontext, List<Agent> xagentlist) {
        this.mcontext = xcontext;
        this.magentlist = xagentlist;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_agent,parent,false);
        return new AgentViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        Agent agent = magentlist.get(position);
        holder.tvname.setText(agent.getName());
        holder.tvpercentage.setText(String.valueOf(agent.getPercentage()));
        holder.tvpayment.setText(agent.getPayment_information());
    }

    @Override
    public int getItemCount() {
        return magentlist.size();
    }

    public static class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvpercentage, tvpayment;
        ImageButton ibtedit, ibtdelete;
        public AgentViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvname);
            tvpercentage = itemView.findViewById(R.id.tvpercentage);
            ibtedit = itemView.findViewById(R.id.ibtedit);
            ibtdelete = itemView.findViewById(R.id.ibtdelete);
            tvpayment = itemView.findViewById(R.id.tvpayment);

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
