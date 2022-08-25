package com.example.warkopcikditiro.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warkopcikditiro.R;
import com.example.warkopcikditiro.model.Product;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context mContext;
    private List<Product> mproductlist;
    private onitemclicklistener mlistener;

    public ProductAdapter(Context xcontext, List<Product> xproductlist) {
        this.mContext = xcontext;
        this.mproductlist = xproductlist;
    }

    public interface onitemclicklistener{
        void onitemclick(int position);
        void onplusclick(int position);
        void onminusclick(int position);
    }

    public void setonitemclicklistener(onitemclicklistener xlistener){
        mlistener = xlistener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_product,parent,false);
        return new ProductViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mproductlist.get(position);
        holder.tvorvproductname.setText(product.getname());
        holder.tvorvproductprice.setText(String.valueOf(product.gettotalprice()));
        holder.tvorvcounter.setText(String.valueOf(product.gettotal()));
    }

    @Override
    public int getItemCount() {
        return mproductlist.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvorvproductname, tvorvproductprice, tvorvcounter;
        Button btorvplus, btorvminus;

        public ProductViewHolder(@NonNull View itemView, onitemclicklistener listener) {
            super(itemView);

            tvorvproductname = itemView.findViewById(R.id.tvorvproductname);
            tvorvproductprice = itemView.findViewById(R.id.tvorvproductprice);
            btorvplus = itemView.findViewById(R.id.btorvplus);
            btorvminus = itemView.findViewById(R.id.btorvminus);
            tvorvcounter = itemView.findViewById(R.id.tvorvcounter);

            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onitemclick(position);
                    }
                }
            });
            btorvplus.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onplusclick(position);
                    }
                }
            });
            btorvminus.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onminusclick(position);
                    }
                }
            });
        }
    }
}
