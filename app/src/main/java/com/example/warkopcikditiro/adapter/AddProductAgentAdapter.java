package com.example.warkopcikditiro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
        return new AddProductAgentViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AddProductAgentViewHolder holder, int position) {
        Agent agent = magentlist.get(position);
        holder.tvnumber.setText(String.valueOf(position+1));
        holder.tvname.setText(agent.getName());
        holder.mytextwatcher.updatePosition2(position);
    }

    @Override
    public void onViewRecycled(@NonNull AddProductAgentViewHolder holder) {
        super.onViewRecycled(holder);
        holder.etprice.removeTextChangedListener(holder.mytextwatcher);
    }

    @Override
    public int getItemCount() {
        return magentlist.size();
    }

    public class AddProductAgentViewHolder extends RecyclerView.ViewHolder{
        TextView tvnumber, tvname;
        EditText etprice;
        MyTextWatcher2 mytextwatcher;

        public AddProductAgentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvname = itemView.findViewById(R.id.tvname);
            etprice = itemView.findViewById(R.id.etprice);
            mytextwatcher = new MyTextWatcher2();

            etprice.addTextChangedListener(mytextwatcher);
        }


    }

    class MyTextWatcher2 implements TextWatcher {
        int position;

        public void updatePosition2(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()>0) {
                magentlist.get(position).setPrice(Integer.parseInt(editable.toString()));
            }else{
                magentlist.get(position).setPrice(0);
            }
        }
    }
}
