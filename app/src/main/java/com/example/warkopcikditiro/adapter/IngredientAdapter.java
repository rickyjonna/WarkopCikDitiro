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
import com.example.warkopcikditiro.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Context mcontext;
    private List<Ingredient> mingredientlist;
    private onitemclicklistener mlistener;

    public interface onitemclicklistener{
        void onitemclick(int position);
        void oneditclick(int position);
        void ondeleteclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener listener){
        mlistener = listener;
    }

    public IngredientAdapter(Context xcontext, List<Ingredient> xingredientlist) {
        this.mcontext = xcontext;
        this.mingredientlist = xingredientlist;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.list_ingredient,parent,false);
        return new IngredientViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mingredientlist.get(position);
        holder.tvname.setText(ingredient.getName());
        holder.tvunit.setText(ingredient.getUnit());
        holder.tvamount.setText(String.valueOf(ingredient.getAmount()));
        holder.tvminimalamount.setText(String.valueOf(ingredient.getMinimalamount()));
    }

    @Override
    public int getItemCount() {
        return mingredientlist.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView tvname, tvunit, tvamount, tvminimalamount;
        ImageButton ibtedit, ibtdelete;

        public IngredientViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tvname);
            tvunit = itemView.findViewById(R.id.tvunit);
            tvamount = itemView.findViewById(R.id.tvamount);
            tvminimalamount = itemView.findViewById(R.id.tvminimalamount);
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
