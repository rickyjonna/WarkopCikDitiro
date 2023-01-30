package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.AddProductAgentAdapter;
import com.example.warkopcikditiro.adapter.AddProductFormulaIngredientAdapter;
import com.example.warkopcikditiro.adapter.SelectedIngredientAdapter;
import com.example.warkopcikditiro.model.Agent;
import com.example.warkopcikditiro.model.Ingredient;
import com.example.warkopcikditiro.model.Partner;
import com.example.warkopcikditiro.model.ProductCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtapback;
    TextView tvapusername, tvapusertype;
    //mainbar
    EditText etapname;
    Spinner spapcategory, spappartner;
    ProductCategory productcategory;
    List<ProductCategory> productcategorylist;
    EditText etapprice;
    RecyclerView rvapagentlist;
        Agent agent;
        List<Agent> agentlist;
        AddProductAgentAdapter addproductagentadapter;
    Partner partner;
    List<Partner> partnerlist;
    EditText etapstock, etapminstock, etapunitstock;
    EditText etapdiscount;
    Button  btappercent, btaprp;
    TextView tvappercent, tvaprp;
    boolean isdiscountonpercent = true;
    CheckBox cbapstock;
    boolean isonstock = false;
    boolean isonformula = false;
    CheckBox cbapformula;
    RecyclerView rvdapilist;
        Ingredient ingredient;
        List<Ingredient> ingredientlist;
        AddProductFormulaIngredientAdapter addproductformulaingredientadapter;
    Dialog dialog_formula;
    RecyclerView rvapselectedingredient;
        List<Ingredient> selectedingredientlist;
        SelectedIngredientAdapter selectedingredientadapter;
    Button btdapfok;
    EditText etapinformation;
    Button btapsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //install variable
        ///dialog
        dialog_formula = new Dialog(this);
        dialog_formula.setContentView(R.layout.dialog_addproduct_formula);
        dialog_formula.setCanceledOnTouchOutside(false);
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        productcategorylist = new ArrayList<>();
        agentlist = new ArrayList<>();
        partnerlist = new ArrayList<>();
        ingredientlist = new ArrayList<>();
        selectedingredientlist = new ArrayList<>();
        ///topbar
        ibtapback = findViewById(R.id.ibtapback);
        tvapusername = findViewById(R.id.tvapusername);
        tvapusertype = findViewById(R.id.tvapusertype);
        ///mainbar
        etapname = findViewById(R.id.etapname);
        etapprice = findViewById(R.id.etapprice);
        rvapagentlist = findViewById(R.id.rvapagentlist);
        spapcategory = findViewById(R.id.spapcategory);
        spappartner = findViewById(R.id.spappartner);
        etapstock = findViewById(R.id.etapstock);
        etapminstock = findViewById(R.id.etapminstock);
        etapunitstock = findViewById(R.id.etapunitstock);
        cbapstock = findViewById(R.id.cbapstock);
        etapdiscount = findViewById(R.id.etapdiscount);
        btappercent = findViewById(R.id.btappercent);
        tvappercent = findViewById(R.id.tvappercent);
        btaprp = findViewById(R.id.btaprp);
        tvaprp = findViewById(R.id.tvaprp);
        cbapformula = findViewById(R.id.cbapformula);
        rvdapilist = dialog_formula.findViewById(R.id.rvdapilist);
        btdapfok = dialog_formula.findViewById(R.id.btdapfok);
        rvapselectedingredient = findViewById(R.id.rvapselectedingredient);
        etapinformation = findViewById(R.id.etapinformation);
        btapsave = findViewById(R.id.btapsave);

        //get data + fill layout
        extractapi_addproduct();
        ///topbar
        tvapusername.setText(sharedpref.getString("user_name", ""));
        tvapusertype.setText(sharedpref.getString("user_type", ""));
        //mainbar
        rvapagentlist.setLayoutManager(new LinearLayoutManager(this));
        setrvagentlist(agentlist);
        rvdapilist.setLayoutManager(new LinearLayoutManager(this));
        setrvingredientlist(ingredientlist);
        rvapselectedingredient.setLayoutManager(new LinearLayoutManager(this));
        setrvselectedingredientlist(selectedingredientlist);

        //install widget
        ibtapback.setOnClickListener(view -> finish());
        etapstock.setEnabled(false);
        etapminstock.setEnabled(false);
        cbapstock.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
            {
                hasstock();
            } else
                {
                    etapstock.setText("");
                    etapminstock.setText("");
                    etapunitstock.setText("");
                    hasnostock();
                }
        });
        discountpercent();
        cbapformula.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
            {
                hasformula();
                extractapi_ingredient();
                opendialogformula();
            } else
            {
                selectedingredientlist = new ArrayList<>();
                setrvselectedingredientlist(selectedingredientlist);
            }
        });
        tvappercent.setOnClickListener(view -> discountpercent());
        tvaprp.setOnClickListener(view -> discountrp());
        btapsave.setOnClickListener(view -> save_product());
    }

    private void extractapi_addproduct() {
        JsonObjectRequest joraddproduct = new JsonObjectRequest(Request.Method.GET, ConstantUrl.ADDPRODUCT, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jacategory = joresults.getJSONArray("category");
                for(int i=0; i<jacategory.length();i++) {
                    JSONObject jocategory = jacategory.getJSONObject(i);
                    productcategory = new ProductCategory();
                    productcategory.setid(jocategory.getInt("id"));
                    productcategory.setinformation(jocategory.getString("information"));
                    productcategorylist.add(productcategory);
                }
                setspinnercategory(productcategorylist);
                JSONArray jaagent = joresults.getJSONArray("agent");
                for(int i=1; i<jaagent.length();i++) {
                    JSONObject joagent = jaagent.getJSONObject(i);
                    agent = new Agent();
                    agent.setId(joagent.getInt("id"));
                    agent.setName(joagent.getString("name"));
                    agentlist.add(agent);
                }
                setrvagentlist(agentlist);
                JSONArray japartner = joresults.getJSONArray("partner");
                for(int i=0; i<japartner.length();i++) {
                    JSONObject jopartner = japartner.getJSONObject(i);
                    partner = new Partner();
                    partner.setId(jopartner.getInt("id"));
                    partner.setOwner(jopartner.getString("owner"));
                    partner.setPercentage(jopartner.getInt("percentage"));
                    partnerlist.add(partner);
                }
                setspinnerpartner(partnerlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddproduct);
    }

    private void setspinnercategory(List<ProductCategory> xproductcategorylist) {
        ArrayAdapter<ProductCategory> productcategoryadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, xproductcategorylist);
        productcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spapcategory.setAdapter(productcategoryadapter);
        spapcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                productcategory = productcategoryadapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setrvagentlist(List<Agent> xagentlist) {
        addproductagentadapter = new AddProductAgentAdapter(this,xagentlist);
        rvapagentlist.setAdapter(addproductagentadapter);
    }

    private void setspinnerpartner(List<Partner> xpartnerlist) {
        ArrayAdapter<Partner> partneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, xpartnerlist);
        partneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spappartner.setAdapter(partneradapter);
        spappartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                partner = partneradapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void extractapi_ingredient() {
        JsonObjectRequest joringredient = new JsonObjectRequest(Request.Method.GET, ConstantUrl.INGREDIENT, null, response -> {
            try{
                ingredientlist =  new ArrayList<>();
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
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joringredient);
    }

    private void opendialogformula() {
        dialog_formula.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_formula.show();
        dialog_formula.setCancelable(false);
        dialog_formula.setCanceledOnTouchOutside(false);
        btdapfok.setVisibility(View.INVISIBLE);
        btdapfok.setOnClickListener(view -> dialog_formula.dismiss());
    }

    private void setrvingredientlist(List<Ingredient> xingredientlist) {
        addproductformulaingredientadapter = new AddProductFormulaIngredientAdapter(this,xingredientlist);
        rvdapilist.setAdapter(addproductformulaingredientadapter);
        addproductformulaingredientadapter.setonitemclicklistener(new AddProductFormulaIngredientAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                selectedingredientlist.add(xingredientlist.get(position));
                setrvselectedingredientlist(selectedingredientlist);
                Toast.makeText(getApplicationContext(), "Bahan dimasukkan ke list", Toast.LENGTH_LONG).show();
                btdapfok.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setrvselectedingredientlist(List<Ingredient> xselectedingredientlist) {
        selectedingredientadapter = new SelectedIngredientAdapter(this,xselectedingredientlist);
        rvapselectedingredient.setAdapter(selectedingredientadapter);
    }

    private void hasstock() {
        isonstock = true;
        etapstock.setEnabled(true);
        etapminstock.setEnabled(true);
        etapunitstock.setEnabled(true);
    }

    private void hasnostock() {
        isonstock = false;
        etapstock.setEnabled(false);
        etapminstock.setEnabled(false);
        etapunitstock.setEnabled(false);
    }

    private void hasformula() {
        isonformula = true;
    }

    private void discountpercent() {
        isdiscountonpercent = true;
        btappercent.setVisibility(View.VISIBLE);
        tvappercent.setVisibility(View.GONE);
        btaprp.setVisibility(View.GONE);
        tvaprp.setVisibility(View.VISIBLE);
    }

    private void discountrp() {
        isdiscountonpercent = false;
        btappercent.setVisibility(View.GONE);
        tvappercent.setVisibility(View.VISIBLE);
        btaprp.setVisibility(View.VISIBLE);
        tvaprp.setVisibility(View.GONE);
    }


    private void save_product() {
        JSONObject joform = new JSONObject();
        JSONArray jsonarraystock = new JSONArray();
        JSONArray jsonarrayagentprice = new JSONArray();
        try{
            int price,discount;
            if (etapprice.getText().toString().matches("")){
                price = 0;
            }else{
                price = Integer.parseInt(etapprice.getText().toString());
            }
            if (etapdiscount.getText().toString().matches("")){
                discount = 0;
            }else{
                discount = Integer.parseInt(etapdiscount.getText().toString());
            }
            joform.put("merchant_id",1);
            joform.put("partner_id",partner.getId());
            joform.put("product_category_id",productcategory.getid());
            joform.put("name",etapname.getText().toString());
            joform.put("price",price);
            if (isdiscountonpercent){
                joform.put("discount",price*discount/100);
            }else{
                joform.put("discount",discount);
            }
            if (isonstock){
                joform.put("hasstock",1);
                if (etapstock.getText().toString().trim().equalsIgnoreCase("")) {
                    etapstock.setError("This field can not be blank");
                }else{
                    joform.put("amount", Integer.parseInt(etapstock.getText().toString()));
                }
                if (etapminstock.getText().toString().trim().equalsIgnoreCase("")) {
                    etapminstock.setError("This field can not be blank");
                }else{
                    joform.put("minimum_amount", Integer.parseInt(etapminstock.getText().toString()));
                }
                if (etapunitstock.getText().toString().trim().equalsIgnoreCase("")) {
                    etapunitstock.setError("This field can not be blank");
                }else{
                    joform.put("unit", etapunitstock.getText().toString());
                }
            }else{
                joform.put("hasstock",0);
            }
            if (isonformula){
                joform.put("isformula",1);
                for (int i=0; i<selectedingredientlist.size(); i++){
                    jsonarraystock.put(2*i+0,selectedingredientlist.get(i).getId());
                    jsonarraystock.put(2*i+1,selectedingredientlist.get(i).getAmount());
                }
                joform.put("ingredient",jsonarraystock);
            }else{
                joform.put("isformula",0);
            }
            joform.put("information",etapinformation.getText().toString());
            for (int i=0; i<agentlist.size(); i++){
                if(agentlist.get(i).getPrice() == 0){
                    continue;
                }else {
                    jsonarrayagentprice.put(agentlist.get(i).getId());
                    jsonarrayagentprice.put(agentlist.get(i).getPrice());
                }
            }
            joform.put("agent_price",jsonarrayagentprice);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddagent = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTPRODUCT, joform, response -> {
            try{
                if(response.getString("message").equals("InsertProduct - Success")){
                    finish();
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddagent);
    }
}