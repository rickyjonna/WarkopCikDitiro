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
import com.example.warkopcikditiro.model.Ingredient;

import java.util.List;

public class SelectedIngredientAdapter extends RecyclerView.Adapter<SelectedIngredientAdapter.SelectedIngredientViewHolder> {
    private Context mcontext;
    private List<Ingredient> mselectedingredientlist;

    public SelectedIngredientAdapter(Context mcontext, List<Ingredient> mselectedingredientlist) {
        this.mcontext = mcontext;
        this.mselectedingredientlist = mselectedingredientlist;
    }

    @NonNull
    @Override
    public SelectedIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_selectedingredient,parent,false);
        return new SelectedIngredientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SelectedIngredientViewHolder holder, int position) {
        Ingredient selectedingredient = mselectedingredientlist.get(position);
        holder.tvnumber.setText(String.valueOf(position+1)+".");
        holder.tvname.setText(selectedingredient.getName());
        holder.tvunit.setText(selectedingredient.getUnit());
        holder.textWatcher.updatePosition(position);
    }

    @Override
    public void onViewRecycled(@NonNull SelectedIngredientViewHolder holder) {
        super.onViewRecycled(holder);
        holder.etamount.removeTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return mselectedingredientlist.size();
    }

    public class SelectedIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView tvnumber, tvname, tvunit;
        EditText etamount;
        MyTextWatcher textWatcher;
        public SelectedIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvname = itemView.findViewById(R.id.tvname);
            tvunit = itemView.findViewById(R.id.tvunit);
            etamount = itemView.findViewById(R.id.etamount);
            textWatcher = new MyTextWatcher();

            etamount.addTextChangedListener(textWatcher);
        }
    }

    class MyTextWatcher implements TextWatcher {
        int position;
        public void updatePosition(int position) {
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
            mselectedingredientlist.get(position).setAmount(Integer.parseInt(editable.toString()));
        }
    }
}
