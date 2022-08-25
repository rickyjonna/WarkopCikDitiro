package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ProductCategoryAdapter;
import com.example.warkopcikditiro.model.ProductCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtpcatback;
    TextView tvpcatusername, tvpcatusertype;
    //leftbar
    RecyclerView rvpcatlist;
    ProductCategory productcategory;
    List<ProductCategory> pcatlist;
    ProductCategoryAdapter pcatadapter;
    Button btpcatadd;
    //rightbar
    EditText etpcatname;
    Button btpcatsave;
    TextView tvpcattitle;
    int pcategory_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        //install variable
        ///variable
        pcategory_id = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        pcatlist = new ArrayList<>();
        ///topbar
        ibtpcatback = findViewById(R.id.ibtpcatback);
        tvpcatusername = findViewById(R.id.tvpcatusername);
        tvpcatusertype = findViewById(R.id.tvpcatusertype);
        ///leftbar
        rvpcatlist = findViewById(R.id.rvpcatlist);
        btpcatadd = findViewById(R.id.btpcatadd);
        ///rightbar
        tvpcattitle = findViewById(R.id.tvpcattitle);
        etpcatname = findViewById(R.id.etpcatname);
        btpcatsave = findViewById(R.id.btpcatsave);

        //get data + fill layout
        extractapi_pcategory();
        ///topbar
        tvpcatusername.setText(sharedpref.getString("user_name", ""));
        tvpcatusertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        setrvpcatlist(pcatlist);
        ///rightbar

        //install widget
        ibtpcatback.setOnClickListener(view -> finish());
        btpcatadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btpcatsave.setOnClickListener(view -> extractapi_addpcategory());
    }

    private void extractapi_pcategory() {
        JsonObjectRequest jorpcat = new JsonObjectRequest(Request.Method.GET, ConstantUrl.PRODUCTCATEGORY, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray japcat = joresults.getJSONArray("productcategory");
                for(int i=0; i<japcat.length();i++) {
                    JSONObject jopcat = japcat.getJSONObject(i);
                    productcategory = new ProductCategory();
                    productcategory.setid(jopcat.getInt("id"));
                    productcategory.setinformation(jopcat.getString("information"));
                    pcatlist.add(productcategory);
                }
                setrvpcatlist(pcatlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorpcat);
    }

    private void setrvpcatlist(List<ProductCategory> xpcatlist) {
        pcatadapter = new ProductCategoryAdapter(this,xpcatlist);
        rvpcatlist.setLayoutManager(new LinearLayoutManager(this));
        rvpcatlist.setAdapter(pcatadapter);
        pcatadapter.setonitemclicklistener(new ProductCategoryAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                pcategory_id = xpcatlist.get(position).getid();
                tvpcattitle.setText("Edit Produk Kategori");
                etpcatname.setText(xpcatlist.get(position).getinformation());
                btpcatsave.setOnClickListener(view -> extractapi_editpcat());
            }
            @Override
            public void oneditclick(int position) {
                pcategory_id = xpcatlist.get(position).getid();
                tvpcattitle.setText("Edit Produk Kategori");
                etpcatname.setText(xpcatlist.get(position).getinformation());
                btpcatsave.setOnClickListener(view -> extractapi_editpcat());
            }
            @Override
            public void ondeleteclick(int position) {
                pcategory_id = xpcatlist.get(position).getid();
                extractapi_deletepcat();
            }
        });
    }

    private void extractapi_addpcategory() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("information",etpcatname.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddpcat = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTPRODUCTCATEGORY, joform, response -> {
            try{
                if(response.getString("message").equals("InsertProductCategory - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddpcat);
    }

    private void extractapi_editpcat() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("information",etpcatname.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditpcat = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITPRODUCTCATEGORY+String.valueOf(pcategory_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditProductCategory - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joreditpcat);
    }

    private void extractapi_deletepcat() {
        JsonObjectRequest jordeletepcat = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEPRODUCTCATEGORY+String.valueOf(pcategory_id), null, response -> {
            try{
                if(response.getString("message").equals("DeleteProductCategory - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeletepcat);
    }
}