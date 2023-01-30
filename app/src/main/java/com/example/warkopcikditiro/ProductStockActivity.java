package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ProductStockAdapter;
import com.example.warkopcikditiro.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProductStockActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtpsback;
    TextView tvpsusername, tvpsusertype;
    //leftbar
    RecyclerView rvpstock;
    Product product;
    List<Product> productlist;
    ProductStockAdapter productstockadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_product);

        //install variable
        ///variable
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///topbar
        ibtpsback = findViewById(R.id.ibtpsback);
        tvpsusername = findViewById(R.id.tvpsusername);
        tvpsusertype = findViewById(R.id.tvpsusertype);
        ///leftbar
        rvpstock = findViewById(R.id.rvpstock);

        ///listcontainer
        productlist = new ArrayList<>();

        //get data + fill layout
        extractapi_productstock();
        ///topbar
        tvpsusername.setText(sharedpref.getString("user_name", ""));
        tvpsusertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        setrvproductstock(productlist);

        //install widget
        ibtpsback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, OnBoardActivity.class));
        });
    }

    private void extractapi_productstock() {
        JsonObjectRequest jorproductstock = new JsonObjectRequest(Request.Method.GET, ConstantUrl.PRODUCTSTOCK, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray japroductstock = joresults.getJSONArray("productstock");
                for(int i=0; i<japroductstock.length();i++) {
                    JSONObject joproductstock = japroductstock.getJSONObject(i);
                    product = new Product();
                    product.setname(joproductstock.getString("Produk"));
                    product.settotalstock(joproductstock.getInt("Jumlah"));
                    product.setMinimumstock(joproductstock.getInt("minimum_amount"));
                    product.setUnit(joproductstock.getString("unit"));
                    productlist.add(product);
                }
                setrvproductstock(productlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorproductstock);
    }

    private void setrvproductstock(List<Product> mproductlist) {
        productstockadapter = new ProductStockAdapter(this, mproductlist);
        rvpstock.setLayoutManager(new LinearLayoutManager(this));
        rvpstock.setAdapter(productstockadapter);
    }
}