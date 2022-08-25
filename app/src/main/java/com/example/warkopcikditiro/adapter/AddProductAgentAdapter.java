package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Agent;

import java.util.List;

public class AddProductAgentAdapter extends RecyclerView.Adapter<AddProductAgentAdapter.AddProductAgentViewHolder> {
    private Context mContext;
    private List<Agent> magentlist;

    public AddProductAgentAdapter(Context mContext, List<Agent> magentlist) {
        this.mContext = mContext;
        this.magentlist = magentlist;
    }

    @NonNull
    @Override
    public AddProductAgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_addproduct_agentlist,parent,false);
        return new AddProductAgentAdapter.AddProductAgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductAgentViewHolder holder, int position) {
        Agent agent = magentlist.get(position);
        holder.tvnumber.setText(String.valueOf(position+1));
        holder.tvname.setText(agent.getName());
    }

    @Override
    public int getItemCount() {
        return magentlist.size();
    }

    public class AddProductAgentViewHolder extends RecyclerView.ViewHolder{
        TextView tvnumber, tvname;
        EditText etprice;
        public AddProductAgentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvname = itemView.findViewById(R.id.tvname);
            etprice = itemView.findViewById(R.id.etprice);
        }
    }
}
