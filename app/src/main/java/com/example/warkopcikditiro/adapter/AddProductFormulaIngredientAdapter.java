package com.example.warkopcikditiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Ingredient;

import java.util.List;

public class AddProductFormulaIngredientAdapter extends RecyclerView.Adapter<AddProductFormulaIngredientAdapter.AddProductFormulaIngredientViewHolder> {
    private Context mcontext;
    private List<Ingredient> mingredientlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public AddProductFormulaIngredientAdapter(Context xcontext, List<Ingredient> xingredientlist) {
        this.mcontext = xcontext;
        this.mingredientlist = xingredientlist;
    }

    @NonNull
    @Override
    public AddProductFormulaIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_addproduct_ingredient,parent,false);
        return new AddProductFormulaIngredientViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductFormulaIngredientViewHolder holder, int position) {
        Ingredient ingredient = mingredientlist.get(position);
        holder.tvingredientname.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return mingredientlist.size();
    }

    public class AddProductFormulaIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView tvingredientname;
        public AddProductFormulaIngredientViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvingredientname = itemView.findViewById(R.id.tvingredientname);

            itemView.setOnClickListener(view -> {
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
