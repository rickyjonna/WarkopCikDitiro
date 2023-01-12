package com.example.warkopcikditiro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Agent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddProductAgentAdapter extends RecyclerView.Adapter<AddProductAgentAdapter.AddProductAgentViewHolder> {
    private Context mContext;
    private List<Agent> magentlist;
    ArrayList<String> agent_price = new ArrayList<>();

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
        return new AddProductAgentViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AddProductAgentViewHolder holder, int position) {
        Agent agent = magentlist.get(position);
        holder.tvnumber.setText(String.valueOf(position+1));
        holder.tvname.setText(agent.getName());
        int agent_id = magentlist.get(position).getId();

        holder.etprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                for(int i=0; i<=position; i++){
                    agent_price.add("0");
                }
                if(editable.length()==0||editable.charAt(0)==' '){
                    agent_price.set(position,"0");
                } else {
                    agent_price.set(position,String.valueOf(agent_id) + "=" + editable.toString());
                }
                Log.d("vendor_price",agent_price.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return magentlist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
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
