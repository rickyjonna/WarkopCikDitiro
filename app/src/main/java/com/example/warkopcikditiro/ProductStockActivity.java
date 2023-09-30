package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
    ImageButton ibtback;
    TextView tvusername, tvusertype, tvtitle;
    //leftbar
    RecyclerView rvpstock;
    Product product;
    List<Product> productlist;
    ProductStockAdapter productstockadapter;
    //rightbar
    EditText etproduct, etstock, etminstock, etunit;
    LinearLayout lleditstock;
    Button btsave;
    TextView tvtitle2;
    int product_id;

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
        ibtback = findViewById(R.id.ibtback);
        tvusername = findViewById(R.id.namauser);
        tvusertype = findViewById(R.id.tipeuser);
        tvtitle = findViewById(R.id.namajudul);
        ///leftbar
        rvpstock = findViewById(R.id.rvpstock);
        ///rightbar
        lleditstock = findViewById(R.id.lleditstock);
        tvtitle2 = findViewById(R.id.tvtittle2);
        etproduct = findViewById(R.id.etproduct);
        etstock = findViewById(R.id.etstock);
        etminstock = findViewById(R.id.etminstock);
        etunit = findViewById(R.id.etunit);
        btsave = findViewById(R.id.btsave);

        ///listcontainer
        productlist = new ArrayList<>();

        //get data + fill layout
        extractapi_productstock();
        ///topbar
        tvusername.setText(sharedpref.getString("user_name", ""));
        tvusertype.setText(sharedpref.getString("user_type", ""));
        tvtitle.setText(getString(R.string.title_stock));
        ///leftbar
        setrvproductstock(productlist);

        //install widget
        ibtback.setOnClickListener(view -> {
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
                    product.setid(joproductstock.getInt("id"));
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
        productstockadapter.setonitemclicklistener(new ProductStockAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                product_id = mproductlist.get(position).getid();
                lleditstock.setVisibility(View.VISIBLE);
                etproduct.setText(String.valueOf(mproductlist.get(position).getname()));
                etstock.setText(String.valueOf(mproductlist.get(position).gettotalstock()));
                etminstock.setText(String.valueOf(mproductlist.get(position).getMinimumstock()));
                etunit.setText(String.valueOf(mproductlist.get(position).getUnit()));
                btsave.setOnClickListener(view -> extractapi_editstock());
            }

            @Override
            public void oneditclick(int position) {
                product_id = mproductlist.get(position).getid();
                lleditstock.setVisibility(View.VISIBLE);
                etproduct.setText(String.valueOf(mproductlist.get(position).getname()));
                etstock.setText(String.valueOf(mproductlist.get(position).gettotalstock()));
                etminstock.setText(String.valueOf(mproductlist.get(position).getMinimumstock()));
                etunit.setText(String.valueOf(mproductlist.get(position).getUnit()));
                btsave.setOnClickListener(view -> extractapi_editstock());
            }
        });
    }

    private void extractapi_editstock() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("amount",etstock.getText().toString());
            joform.put("minimum_amount",etminstock.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jorupdatestock = new JsonObjectRequest(Request.Method.POST, ConstantUrl.UPDATEPRODUCTSTOCK+String.valueOf(product_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditStock - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorupdatestock);
    }
}