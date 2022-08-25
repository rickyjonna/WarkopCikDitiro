package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ProductAdapter;
import com.example.warkopcikditiro.adapter.ProductOrdered2Adapter;
import com.example.warkopcikditiro.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    ImageButton ibtolback;
    TextView tvolusername, tvolusertype, tvolinformation, tvolorderedproduct, tvolchangeorderlist;
    SearchView svolproduct;
    RecyclerView rvolproduct, rvolproductordered;
    Product product;
    List<Product> productlist, productorderedlist, productlistfilteredbytext, productlistfilteredbycategory;
    ProductAdapter productadapter;
    ProductOrdered2Adapter productordered2adapter;
    Button btolorderedproduct, btolallproduct, btolproductctg1, btolproductctg2, btolproductctg3, btolproductctg4, btolchangeorderlist;
    EditText etolnote;
    String selectedfilter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        //install variable
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ibtolback = findViewById(R.id.ibtolback);
        tvolusername = findViewById(R.id.tvolusername);
        tvolusertype = findViewById(R.id.tvolusertype);
        tvolinformation = findViewById(R.id.tvolinformation);
        svolproduct = findViewById(R.id.svolproduct);
        rvolproduct = findViewById(R.id.rvolproduct);
        tvolorderedproduct = findViewById(R.id.tvolorderedproduct);
        btolorderedproduct = findViewById(R.id.btolorderedproduct);
        btolallproduct = findViewById(R.id.btolallproduct);
        btolproductctg1 = findViewById(R.id.btolproductctg1);
        btolproductctg2 = findViewById(R.id.btolproductctg2);
        btolproductctg3 = findViewById(R.id.btolproductctg3);
        btolproductctg4 = findViewById(R.id.btolproductctg4);
        rvolproductordered = findViewById(R.id.rvolproductordered);
        etolnote = findViewById(R.id.etolnote);
        tvolchangeorderlist = findViewById(R.id.tvolchangeorderlist);
        btolchangeorderlist = findViewById(R.id.btolchangeorderlist);

        //get data + fill layout
        productlist = new ArrayList<>();
        productorderedlist = new ArrayList<>();
        extractapi_orderlistlist();
        setrvproduct(productorderedlist);
        setrvproduct2ordered(productorderedlist);
        tvolusername.setText(sharedpref.getString("user_name", ""));
        tvolusertype.setText(sharedpref.getString("user_type", ""));
        if (sharedpref.getString("order_table_id", "").equals("")) {
            tvolinformation.setText(sharedpref.getString("order_information", ""));
        } else {
            if(sharedpref.getString("order_table_extend", "").equals("0")){
                tvolinformation.setText("Meja " + sharedpref.getString("order_table_number", ""));
            }else {
                tvolinformation.setText("Meja " + sharedpref.getString("order_table_number", "") + "-" + sharedpref.getString("order_table_extend", ""));
            }
        }
        etolnote.setText(sharedpref.getString("order_note",""));

        //install widget
        ibtolback.setOnClickListener(view -> {
            editor.remove("order_table_id");
            editor.remove("order_table_number");
            editor.remove("order_table_extend");
            editor.remove("order_information");
            editor.remove("order_note");
            editor.remove("order_id");
            editor.apply();
            finish();
        });
        svolproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchquery) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchquery) {
                comparelist();
                filteractivated();
                productlistfilteredbytext = new ArrayList<>();
                for (Product product : productlist){
                    if(product.getname().toLowerCase().contains(searchquery.toLowerCase())){
                        productlistfilteredbytext.add(product);
                    }
                }
                setrvproduct(productlistfilteredbytext);
                return false;
            }
        });
        btolorderedproduct.setOnClickListener(view -> {
            tvolorderedproduct.setVisibility(View.VISIBLE);
            btolorderedproduct.setVisibility(View.GONE);
            setrvproduct(productorderedlist);
        });
        btolallproduct.setOnClickListener(view -> {
            comparelist();
            filteractivated();
            setrvproduct(productlist);
        });
        btolproductctg1.setOnClickListener(view -> {
            comparelist();
            filteractivated();
            filterlist("makanan");
        });
        btolproductctg2.setOnClickListener(view -> {
            comparelist();
            filteractivated();
            filterlist("minuman");
        });
        btolproductctg3.setOnClickListener(view -> {
            comparelist();
            filteractivated();
            filterlist("snack");
        });
        btolproductctg4.setOnClickListener(view -> {
            comparelist();
            filteractivated();
            filterlist("lainnya");
        });
        etolnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    tvolchangeorderlist.setVisibility(View.GONE);
                    btolchangeorderlist.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btolchangeorderlist.setOnClickListener(view -> updateorderlistlist());
    }

    private void extractapi_orderlistlist() {
        String urlextra_orderid = sharedpref.getString("order_id","");
        JsonObjectRequest jororderlistlist = new JsonObjectRequest(Request.Method.GET, ConstantUrl.ORDERLISTLIST+urlextra_orderid, null, response -> {
            try{
                //product list ordered
                JSONObject joresult = response.getJSONObject("results");
                JSONArray jaorderlist = joresult.getJSONArray("order_list_notdone");
                for(int i=0; i<jaorderlist.length();i++) {
                    JSONObject joproductordered = jaorderlist.getJSONObject(i);
                    product = new Product();
                    product.setid(joproductordered.getInt("product_id"));
                    product.setname(joproductordered.getString("product_name"));
                    product.settotal(joproductordered.getInt("product_total"));
                    product.settotalprice(joproductordered.getInt("product_totalprice"));
                    product.setstatus_id(joproductordered.getInt("orderlist_status_id"));
                    product.setstatus(joproductordered.getString("orderlist_status"));
                    productorderedlist.add(product);
                }
                //product list
                JSONArray japroductlist = joresult.getJSONArray("product_list");
                for(int i=0; i<japroductlist.length();i++) {
                    JSONObject joproduct = japroductlist.getJSONObject(i);
                    product = new Product();
                    product.setid(joproduct.getInt("id"));
                    product.setname(joproduct.getString("name"));
                    product.setprice(joproduct.getInt("price"));
                    product.setdiscount(joproduct.getInt("discount"));
                    product.settotalprice(joproduct.getInt("total_price"));
                    product.settotalstock(joproduct.optInt("total_stock"));
                    product.setcategory(joproduct.getString("category"));
                    product.setstatus_id(1);
                    product.setstatus("Sedang Menunggu");
                    productlist.add(product);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jororderlistlist);
    }

    private void comparelist() {
        for(Product product1 : productlist){
            for(Product product2 : productorderedlist){
                if (product1.getid()==product2.getid()){
                    product1.settotal(product2.gettotal());
                }
            }
        }
    }

    private void filteractivated(){
        tvolorderedproduct.setVisibility(View.GONE);
        btolorderedproduct.setVisibility(View.VISIBLE);
    }

    private void setrvproduct(List<Product> xproductlist) {
        rvolproduct.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        productadapter = new ProductAdapter(this,xproductlist);
        productadapter.setonitemclicklistener(new ProductAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {

            }
            @Override
            public void onplusclick(int position) {
                boolean found = false;
                for(Product product : productorderedlist){
                    if(product.getid()==xproductlist.get(position).getid()){
                        product.increasetotal();
                        product.setstatus_id(1);
                        product.setstatus("Sedang Menunggu");
                        comparelist();
                        found = true;
                    }
                }
                if(!found){
                    xproductlist.get(position).increasetotal();
                    productorderedlist.add(xproductlist.get(position));
                }
                tvolchangeorderlist.setVisibility(View.GONE);
                btolchangeorderlist.setVisibility(View.VISIBLE);
                productadapter.notifyDataSetChanged();
                productordered2adapter.notifyDataSetChanged();
            }
            @Override
            public void onminusclick(int position) {
                if(xproductlist.get(position).gettotal()>0) {
                    boolean found = false;
                    for(Product product : productorderedlist){
                        if(product.getid()==xproductlist.get(position).getid()){
                            product.decreasetotal();
                            product.setstatus_id(1);
                            product.setstatus("Sedang Menunggu");
                            comparelist();
                            found = true;
                        }
                    }
                    if(!found) {
                        xproductlist.get(position).decreasetotal();
                        productorderedlist.add(xproductlist.get(position));
                    }
                    tvolchangeorderlist.setVisibility(View.GONE);
                    btolchangeorderlist.setVisibility(View.VISIBLE);
                    productadapter.notifyDataSetChanged();
                    productordered2adapter.notifyDataSetChanged();
                }
            }
        });
        rvolproduct.setAdapter(productadapter);
    }

    private void setrvproduct2ordered(List<Product> xproductlist) {
        rvolproductordered.setLayoutManager(new LinearLayoutManager(this));
        productordered2adapter = new ProductOrdered2Adapter(this,xproductlist);
        rvolproductordered.setAdapter(productordered2adapter);
    }

    public void filterlist(String categoryfilter){
        selectedfilter = categoryfilter;
        productlistfilteredbycategory = new ArrayList<>();
        for (Product product : productlist){
            if(product.getcategory().toLowerCase().contains(categoryfilter)){
                productlistfilteredbycategory.add(product);
            }
        }
        setrvproduct(productlistfilteredbycategory);
    }

    private void updateorderlistlist() {
        JSONObject uoformjo = new JSONObject();
        try {
            uoformjo.put("token", sharedpref.getString("token", null));
            uoformjo.put("order_id", sharedpref.getString("order_id", null));
            uoformjo.put("note", etolnote.getText().toString());
            JSONArray productidjs = new JSONArray();
            JSONArray productamountjs = new JSONArray();
            JSONArray productstatusidjs = new JSONArray();
            for(int i=0; i<productorderedlist.size();i++){
                if(productorderedlist.get(i).gettotal()>0){
                    productidjs.put(String.valueOf(productorderedlist.get(i).getid()));
                    productamountjs.put(String.valueOf(productorderedlist.get(i).gettotal()));
                    productstatusidjs.put(String.valueOf(productorderedlist.get(i).getstatus_id()));
                }
            }
            uoformjo.put("product_id",productidjs);
            uoformjo.put("amount",productamountjs);
            uoformjo.put("order_list_status_id",productstatusidjs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest responsuo = new JsonObjectRequest(Request.Method.POST, ConstantUrl.UPDATEORDERLIST+sharedpref.getString("order_id",null), uoformjo, response -> {
            try {
                if (response.getString("message").equals("Update - OrderList - Success")) {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    editor.remove("order_id");
                    editor.remove("order_table_id");
                    editor.remove("order_vendor_id");
                    editor.remove("order_information");
                    editor.apply();
                    finish();
                }
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(responsuo);
    }
}