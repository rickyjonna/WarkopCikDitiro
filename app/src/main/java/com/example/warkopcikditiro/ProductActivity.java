package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ProductListAdapter;
import com.example.warkopcikditiro.adapter.ProductStockNotificationAdapter;
import com.example.warkopcikditiro.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtpback, ibproductstocknotif;
    TextView tvpusername, tvpusertype;
    Dialog dproductstocknotif;
        RecyclerView rvdpstocknotif;
            Product product;
            List<Product> productstocknotif;
            ProductStockNotificationAdapter productstocknotificationadapter;
    //main
    SearchView svproduct;
    Button btpall, btpctg1, btpctg2, btpctg3, btpctg4;
    RecyclerView rvproduct;
        List<Product> productlist,productlistfilteredbytext,productlistfilteredbycategory;
        ProductListAdapter productlistadapter;
    Dialog dconfirmdelete;
        TextView tvdpclose;
        Button btdpok;
    Button btpadd;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //install variable
        //PopUp
        popupWindow = new PopupWindow();
        ///Dialog
        dproductstocknotif = new Dialog(this);
        dproductstocknotif.setContentView(R.layout.dialog_product_stocknotif);
        dconfirmdelete = new Dialog(this);
        dconfirmdelete.setContentView(R.layout.dialog_product_confirmdelete);
        ///list container
        productstocknotif = new ArrayList<>();
        productlist = new ArrayList<>();
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///topbar
        ibtpback = findViewById(R.id.ibtpback);
        ibproductstocknotif = findViewById(R.id.ibproductstocknotif);
        tvpusername = findViewById(R.id.tvpusername);
        tvpusertype = findViewById(R.id.tvpusertype);
        ///main
        rvdpstocknotif = dproductstocknotif.findViewById(R.id.rvdpstocknotif);
        svproduct = findViewById(R.id.svproduct);
        btpall = findViewById(R.id.btpall);
        btpctg1 = findViewById(R.id.btpctg1);
        btpctg2 = findViewById(R.id.btpctg2);
        btpctg3 = findViewById(R.id.btpctg3);
        btpctg4 = findViewById(R.id.btpctg4);
        rvproduct = findViewById(R.id.rvproduct);
        btpadd = findViewById(R.id.btpadd);
        tvdpclose = dconfirmdelete.findViewById(R.id.tvdpclose);
        btdpok = dconfirmdelete.findViewById(R.id.btdpok);

        //get data + fill layout
        api_product();
        ///topbar
        tvpusername.setText(sharedpref.getString("user_name", ""));
        tvpusertype.setText(sharedpref.getString("user_type", ""));

        //install widget
        ///topbar
        ibtpback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(ProductActivity.this, OnBoardActivity.class));
        });
        ibproductstocknotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractapi_listproductalert();
                openpopupwindowstock(view);
            }
        });
        setsearchview();
        setbuttoncategory();
        btpadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProductActivity.this,AddProductActivity.class));
            }
        });
    }

    private void api_product() {
        JsonObjectRequest jorproduct = new JsonObjectRequest(Request.Method.GET, ConstantUrl.PRODUCT, null, response -> {
            try {
                if(response.getString("message").equals("Page - Product - Success")){
                    JSONArray joresults = response.getJSONArray("results");
                    for(int i=0; i<joresults.length();i++) {
                        JSONObject joproduct = joresults.getJSONObject(i);
                        product = new Product();
                        product.setid(joproduct.getInt("id"));
                        product.setname(joproduct.getString("name"));
                        product.settotalprice(joproduct.getInt("total_price"));
                        product.settotalstock(joproduct.optInt("total_stock",0));
                        product.setMinimumstock(joproduct.optInt("minimum_stock",0));
                        product.setcategory(joproduct.getString("category"));
                        productlist.add(product);
                    }
                    setrvproductlist(productlist);
                }
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(),"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorproduct);
    }

    private void setrvproductlist(List<Product> xproductlist) {
        productlistadapter = new ProductListAdapter(this, xproductlist);
        productlistadapter.setonitemclicklistener(position -> {
            dconfirmdelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dconfirmdelete.show();
            tvdpclose.setOnClickListener(view -> dconfirmdelete.dismiss());
            btdpok.setOnClickListener(view -> api_deleteproduct(xproductlist.get(position).getid()));
        });
        rvproduct.setLayoutManager(new GridLayoutManager(this,6,GridLayoutManager.VERTICAL,false));
        rvproduct.setAdapter(productlistadapter);
    }

    private void api_deleteproduct(int product_id) {
        JsonObjectRequest jordeleteproduct = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEPRODUCT+String.valueOf(product_id), null, response -> {
            try {
                if(response.getString("message").equals("DeleteProduct - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(),"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeleteproduct);
    }

    private void setsearchview() {
        svproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                productlistfilteredbytext = new ArrayList<>();
                for (Product product : productlist){
                    if(product.getname().toLowerCase().contains(s.toLowerCase())){
                        productlistfilteredbytext.add(product);
                    }
                }
                setrvproductlist(productlistfilteredbytext);
                return false;
            }
        });
    }

    private void setbuttoncategory() {
        btpall.setOnClickListener(view -> setrvproductlist(productlist));
        btpctg1.setOnClickListener(view -> filterlist("makanan",productlist));
        btpctg2.setOnClickListener(view -> filterlist("minuman",productlist));
        btpctg3.setOnClickListener(view -> filterlist("snack",productlist));
        btpctg4.setOnClickListener(view -> filterlist("lainnya",productlist));
    }

    private void filterlist(String xcategoryfilter, List<Product> xproductlist) {
        productlistfilteredbycategory = new ArrayList<>();
        for (Product product : xproductlist){
            if(product.getcategory().toLowerCase().contains(xcategoryfilter)){
                productlistfilteredbycategory.add(product);
            }
        }
        setrvproductlist(productlistfilteredbycategory);
    }

    private void extractapi_listproductalert() {
        JsonObjectRequest jorlistproductalert = new JsonObjectRequest(Request.Method.GET, ConstantUrl.LISTPRODUCTALERT, null, response -> {
            try {
                if(response.getString("message").equals("List-Product-Alert")){
                    JSONArray joresults = response.getJSONArray("results");
                    productstocknotif = new ArrayList<>();
                    for(int i=0; i<joresults.length();i++) {
                        JSONObject joproduct = joresults.getJSONObject(i);
                        product = new Product();
                        product.setid(joproduct.getInt("id"));
                        product.setname(joproduct.getString("name"));
                        product.settotalstock(joproduct.getInt("amount"));
                        product.setMinimumstock(joproduct.getInt("minimum_amount"));
                        productstocknotif.add(product);
                    }
                    setrvstocknotification(productstocknotif);
                }
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(),"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorlistproductalert);
    }

    private void setrvstocknotification(List<Product> xproductlist) {
        productstocknotificationadapter = new ProductStockNotificationAdapter(this, xproductlist);
        rvdpstocknotif.setLayoutManager(new LinearLayoutManager(this));
        rvdpstocknotif.setAdapter(productstocknotificationadapter);
    }

    private void opendialogstocknotif() {
        dproductstocknotif.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dproductstocknotif.getWindow().setGravity(Gravity.END);
        dproductstocknotif.show();
    }

    private void openpopupwindowstock(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.dialog_product_stocknotif,null);

        rvdpstocknotif = customView.findViewById(R.id.rvdpstocknotif);
        rvdpstocknotif.setLayoutManager(new LinearLayoutManager(this));
        rvdpstocknotif.setAdapter(productstocknotificationadapter);

        //Initialize a new instance of PopupWindow
        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        popupWindow.showAsDropDown(view);
    }
}