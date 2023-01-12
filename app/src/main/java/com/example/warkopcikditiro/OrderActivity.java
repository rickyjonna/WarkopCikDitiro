package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ProductAdapter;
import com.example.warkopcikditiro.adapter.ProductOrderedAdapter;
import com.example.warkopcikditiro.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity{

    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    ImageButton ibtoback;
    TextView tvousername, tvousertype, tvotablenumber,tvototalprice;
    SearchView svoproduct;
    String selectedfilter;
    RecyclerView rvoproduct, rvoproductordered;
    Product product;
    List<Product> productlist,productlistfilteredbytext,productlistfilteredbycategory, productlistordered;
    ProductAdapter productadapter;
    ProductOrderedAdapter productorderedadapter;
    Button btoallproduct, btoproductctg1, btoproductctg2, btoproductctg3, btoproductctg4, btoinsertorder;
    EditText etonote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //install widget
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ibtoback = findViewById(R.id.ibtoback);
        tvousername = findViewById(R.id.tvousername);
        tvousertype = findViewById(R.id.tvousertype);
        tvotablenumber = findViewById(R.id.tvotablenumber);
        svoproduct = findViewById(R.id.svoproduct);
        rvoproduct = findViewById(R.id.rvoproduct);
        rvoproductordered = findViewById(R.id.rvoproductordered);
        btoallproduct = findViewById(R.id.btoallproduct);
        btoproductctg1 = findViewById(R.id.btoproductctg1);
        btoproductctg2 = findViewById(R.id.btoproductctg2);
        btoproductctg3 = findViewById(R.id.btoproductctg3);
        btoproductctg4 = findViewById(R.id.btoproductctg4);
        tvototalprice = findViewById(R.id.tvototalprice);
        btoinsertorder = findViewById(R.id.btoinsertorder);
        etonote = findViewById(R.id.etonote);

        //Pasang rv
        productlist = new ArrayList<>();
        productadapter = new ProductAdapter(this, productlist);
        rvoproduct.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        rvoproduct.setAdapter(productadapter);

        productlistordered = new ArrayList<>();
        productorderedadapter = new ProductOrderedAdapter(this,productlist);
        rvoproductordered.setLayoutManager(new LinearLayoutManager(this));
        rvoproductordered.setAdapter(productorderedadapter);

        //ambil data
        tvousername.setText(sharedpref.getString("user_name",""));
        tvousertype.setText(sharedpref.getString("user_type",""));
        if (sharedpref.getString("order_table_number", "").equals("")){
            tvotablenumber.setText(sharedpref.getString("order_information",""));
        } else {
            if(sharedpref.getString("order_table_extend", "").equals("0")){
                tvotablenumber.setText("Meja " + sharedpref.getString("order_table_number", ""));
            }else {
                tvotablenumber.setText("Meja " + sharedpref.getString("order_table_number", "") + "-" + sharedpref.getString("order_table_extend", ""));
            }
        }
        extractapi();

        //widget
        FilterWidgets(); //set edittext for search product
        btoinsertorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertorder();
            }
        });
        //some button automethod from layout
    }

    public void extractapi() {
        StringRequest responsproduct = new StringRequest(Request.Method.GET, ConstantUrl.PRODUCTLIST, response ->
        {
            try{
                JSONObject objectproductjs = new JSONObject(response);
                JSONArray resultproductjs = objectproductjs.getJSONArray("result");
                //get product list from json response
                productlist = new ArrayList<>();
                for(int i=0; i<resultproductjs.length();i++) {
                    JSONObject productjs = resultproductjs.getJSONObject(i);
                    product = new Product();
                    product.setid(productjs.getInt("id"));
                    product.setname(productjs.getString("name"));
                    product.setprice(productjs.getInt("price"));
                    product.setdiscount(productjs.getInt("discount"));
                    product.settotalprice(productjs.getInt("total_price"));
                    product.settotalstock(productjs.optInt("total_stock"));
                    product.setcategory(productjs.getString("category"));
                    productlist.add(product);
                }
                setrecyclerviewproduct(productlist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> Toast.makeText(getApplicationContext(),"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(responsproduct);
    }

    public void setrecyclerviewproduct(List<Product> xproductlist) {
        productadapter = new ProductAdapter(this, xproductlist);
        productadapter.setonitemclicklistener(new ProductAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                //show photo and information in new dialog later
            }
            @Override
            public void onplusclick(int position) {
                xproductlist.get(position).increasetotal();
                int price = xproductlist.get(position).gettotalprice();
                int lasttotalprice = Integer.parseInt(tvototalprice.getText().toString());
                int totalorderprice = lasttotalprice + price;
                tvototalprice.setText(String.valueOf(totalorderprice));
                productlistordered = new ArrayList<>();
                for(Product product: productlist)
                    if(product.gettotal()!=0){
                        productlistordered.add(product);
                    }
                setrecyclerviewproductordered(productlistordered);
                productadapter.notifyDataSetChanged();
            }
            @Override
            public void onminusclick(int position) {
                if(xproductlist.get(position).gettotal()>0) {
                    xproductlist.get(position).decreasetotal();
                    int price = xproductlist.get(position).gettotalprice();
                    int lasttotalprice = Integer.parseInt(tvototalprice.getText().toString());
                    int totalorderprice = lasttotalprice - price;
                    tvototalprice.setText(String.valueOf(totalorderprice));
                    productlistordered = new ArrayList<>();
                    for(Product product: productlist)
                        if(product.gettotal()!=0){
                            productlistordered.add(product);
                        }
                    setrecyclerviewproductordered(productlistordered);
                    productadapter.notifyDataSetChanged();
                }
            }
        });
        rvoproduct.setAdapter(productadapter);
    }

    public void FilterWidgets(){
        svoproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchquery) {
                productlistfilteredbytext = new ArrayList<>();
                for (Product product : productlist){
                    if(product.getname().toLowerCase().contains(searchquery.toLowerCase())){
                        productlistfilteredbytext.add(product);
                    }
                }
                setrecyclerviewproduct(productlistfilteredbytext);
                return false;
            }
        });
    }

    public void allproducttapped(View view){
        setrecyclerviewproduct(productlist);
    }
    public void foodtapped(View view){
        filterlist("makanan",productlist);
    }
    public void drinktapped(View view){
        filterlist("minuman",productlist);
    }
    public void snacktapped(View view){
        filterlist("snack",productlist);
    }
    public void othertapped(View view){
        filterlist("lainnya",productlist);
    }

    public void filterlist(String categoryfilter, List<Product> xproductlist){
        selectedfilter = categoryfilter;
        productlistfilteredbycategory = new ArrayList<>();
        for (Product product : xproductlist){
            if(product.getcategory().toLowerCase().contains(categoryfilter)){
                productlistfilteredbycategory.add(product);
            }
        }
        setrecyclerviewproduct(productlistfilteredbycategory);
    }

    public void setrecyclerviewproductordered(List<Product> xproductlist) {
        productorderedadapter = new ProductOrderedAdapter(this, xproductlist);
        rvoproductordered.setAdapter(productorderedadapter);
    }

    public void goback(View view){
        editor.remove("order_table_id");
        editor.remove("order_agent_id");
        editor.remove("order_information");
        editor.apply();
        finish();
    }

    public void insertorder(){
        JSONObject orderformjo = new JSONObject();
        try {
            orderformjo.put("token",sharedpref.getString("token", null));
            orderformjo.put("merchant_id",sharedpref.getString("merchant_id", null));
            orderformjo.put("table_id",sharedpref.getString("order_table_id", null));
            orderformjo.put("user_id",sharedpref.getString("user_id", null));
            orderformjo.put("agent_id",sharedpref.getString("order_agent_id",null));
            orderformjo.put("information",sharedpref.getString("order_information",null));
            orderformjo.put("note", etonote.getText().toString());
            JSONArray productidjs = new JSONArray();
            JSONArray productamountjs = new JSONArray();
            for(int i=0; i<productlistordered.size();i++){
                productidjs.put(String.valueOf(productlistordered.get(i).getid()));
                productamountjs.put(String.valueOf(productlistordered.get(i).gettotal()));
            }
            orderformjo.put("product_id",productidjs);
            orderformjo.put("amount",productamountjs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest responsinsertorder = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTORDER, orderformjo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("message").equals("Order Telah Dibuat")){
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        editor.remove("order_table_id");
                        editor.remove("order_agent_id");
                        editor.remove("order_information");
                        editor.apply();
                        OrderActivity.this.finish();
                    }
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(getApplicationContext(),"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(responsinsertorder);
    }
}