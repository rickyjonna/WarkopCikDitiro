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
import com.example.warkopcikditiro.adapter.IngredientAdapter;
import com.example.warkopcikditiro.model.Ingredient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtiback;
    TextView tviusername, tviusertype;
    //leftbar
    RecyclerView rvilist;
    Ingredient ingredient;
    List<Ingredient> ingredientlist;
    IngredientAdapter ingredientadapter;
    Button btiadd;
    //rightbar
    EditText etiname, etiunit, etiamount, etiminimalamount;
    Button btisave;
    TextView tvititle;
    int ingredient_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        //install variable
        ///variable
        ingredient_id = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        ingredientlist = new ArrayList<>();
        ///topbar
        ibtiback = findViewById(R.id.ibtiback);
        tviusername = findViewById(R.id.tviusername);
        tviusertype = findViewById(R.id.tviusertype);
        ///leftbar
        rvilist = findViewById(R.id.rvilist);
        btiadd = findViewById(R.id.btiadd);
        ///rightbar
        tvititle = findViewById(R.id.tvititle);
        etiname = findViewById(R.id.etiname);
        etiunit = findViewById(R.id.etiunit);
        etiamount = findViewById(R.id.etiamount);
        etiminimalamount = findViewById(R.id.etiminimalamount);
        btisave = findViewById(R.id.btisave);

        //get data + fill layout
        extractapi_ingredient();
        ///topbar
        tviusername.setText(sharedpref.getString("user_name", ""));
        tviusertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        setrvingredientlist(ingredientlist);

        //install widget
        ibtiback.setOnClickListener(view -> finish());
        btiadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btisave.setOnClickListener(view -> extractapi_addingredient());
    }

    private void extractapi_ingredient() {
        JsonObjectRequest joringredient = new JsonObjectRequest(Request.Method.GET, ConstantUrl.INGREDIENT, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jaingredient = joresults.getJSONArray("ingredient");
                for(int i=0; i<jaingredient.length();i++) {
                    JSONObject joingredient = jaingredient.getJSONObject(i);
                    ingredient = new Ingredient();
                    ingredient.setId(joingredient.getInt("id"));
                    ingredient.setName(joingredient.getString("name"));
                    ingredient.setUnit(joingredient.getString("unit"));
                    ingredient.setAmount(joingredient.getInt("amount"));
                    ingredient.setMinimalamount(joingredient.getInt("minimum_amount"));
                    ingredientlist.add(ingredient);
                }
                setrvingredientlist(ingredientlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joringredient);
    }

    private void setrvingredientlist(List<Ingredient> xingredientlist) {
        ingredientadapter = new IngredientAdapter(this,xingredientlist);
        rvilist.setLayoutManager(new LinearLayoutManager(this));
        rvilist.setAdapter(ingredientadapter);
        ingredientadapter.setonitemclicklistener(new IngredientAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                ingredient_id = xingredientlist.get(position).getId();
                tvititle.setText("Edit Bahan");
                etiname.setText(xingredientlist.get(position).getName());
                etiunit.setText(String.valueOf(xingredientlist.get(position).getUnit()));
                etiamount.setText(String.valueOf(xingredientlist.get(position).getAmount()));
                etiminimalamount.setText(String.valueOf(xingredientlist.get(position).getMinimalamount()));
                btisave.setOnClickListener(view -> extractapi_editingredient());
            }
            @Override
            public void oneditclick(int position) {
                ingredient_id = xingredientlist.get(position).getId();
                tvititle.setText("Edit Bahan");
                etiname.setText(xingredientlist.get(position).getName());
                etiunit.setText(String.valueOf(xingredientlist.get(position).getUnit()));
                etiamount.setText(String.valueOf(xingredientlist.get(position).getAmount()));
                etiminimalamount.setText(String.valueOf(xingredientlist.get(position).getMinimalamount()));
                btisave.setOnClickListener(view -> extractapi_editingredient());
            }
            @Override
            public void ondeleteclick(int position) {
                ingredient_id = xingredientlist.get(position).getId();
                extractapi_deleteingredient();
            }
        });
    }

    private void extractapi_addingredient() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("name",etiname.getText().toString());
            joform.put("unit",etiunit.getText().toString());
            joform.put("amount",etiamount.getText().toString());
            joform.put("minimum_amount",etiminimalamount.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddingredient = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTINGREDIENT, joform, response -> {
            try{
                if(response.getString("message").equals("InsertIngredient - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddingredient);
    }

    private void extractapi_editingredient() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("name",etiname.getText().toString());
            joform.put("unit",etiunit.getText().toString());
            joform.put("amount",etiamount.getText().toString());
            joform.put("minimum_amount",etiminimalamount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditingredient = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITINGREDIENT+String.valueOf(ingredient_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditIngredient - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joreditingredient);
    }

    private void extractapi_deleteingredient() {
        JsonObjectRequest jordeleteingredient = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEINGREDIENT+String.valueOf(ingredient_id), null, response -> {
            try{
                if(response.getString("message").equals("DeleteIngredient - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeleteingredient);
    }
}