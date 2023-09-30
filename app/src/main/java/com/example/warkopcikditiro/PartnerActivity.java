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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.PartnerAdapter;
import com.example.warkopcikditiro.model.Partner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartnerActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtback;
    TextView tvusername, tvusertype, tvtitle;
    //leftbar
    RecyclerView rvptlist;
    Partner partner;
    List<Partner> partnerlist;
    PartnerAdapter partneradapter;
    Button btptadd;
    //rightbar
    EditText etptowner, etptprofit;
    Button btptsave, btptpercent, btptrp;
    TextView tvpttitle, tvptpercent, tvptrp;
    int partner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        //install variable
        ///variable
        partner_id = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        partnerlist = new ArrayList<>();
        ///topbar
        ibtback = findViewById(R.id.ibtback);
        tvusername = findViewById(R.id.namauser);
        tvusertype = findViewById(R.id.tipeuser);
        tvtitle = findViewById(R.id.namajudul);
        ///leftbar
        rvptlist = findViewById(R.id.rvptlist);
        btptadd = findViewById(R.id.btptadd);
        ///rightbar
        tvpttitle = findViewById(R.id.tvpttitle);
        etptowner = findViewById(R.id.etptowner);
        etptprofit = findViewById(R.id.etptprofit);
        btptpercent = findViewById(R.id.btptpercent);
        btptrp = findViewById(R.id.btptrp);
        tvptpercent = findViewById(R.id.tvptpercent);
        tvptrp = findViewById(R.id.tvptrp);
        btptsave = findViewById(R.id.btptsave);

        //get data + fill layout
        extractapi_partner();
        ///topbar
        tvusername.setText(sharedpref.getString("user_name", ""));
        tvusertype.setText(sharedpref.getString("user_type", ""));
        tvtitle.setText(getString(R.string.title_partner));
        ///leftbar
        setrvpartnerlist(partnerlist);
        ///rightbar
        percentstate();

        //install widget
        ibtback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, OnBoardActivity.class));
        });
        btptadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btptpercent.setOnClickListener(view -> percentstate());
        btptrp.setOnClickListener(view -> rpstate());
        btptsave.setOnClickListener(view -> extractapi_addagent());
    }

    private void extractapi_partner() {
        JsonObjectRequest jorpartner = new JsonObjectRequest(Request.Method.GET, ConstantUrl.PARTNER, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray japartner = joresults.getJSONArray("partner");
                for(int i=0; i<japartner.length();i++) {
                    JSONObject jopartner = japartner.getJSONObject(i);
                    partner = new Partner();
                    partner.setId(jopartner.getInt("id"));
                    partner.setOwner(jopartner.getString("owner"));
                    partner.setPercentage(jopartner.getInt("percentage"));
                    partnerlist.add(partner);
                }
                setrvpartnerlist(partnerlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorpartner);
    }

    private void setrvpartnerlist(List<Partner> xpartnerlist) {
        partneradapter = new PartnerAdapter(this,xpartnerlist);
        rvptlist.setLayoutManager(new LinearLayoutManager(this));
        rvptlist.setAdapter(partneradapter);
        partneradapter.setonitemclicklistener(new PartnerAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                partner_id = xpartnerlist.get(position).getId();
                tvpttitle.setText("Edit Rekan");
                etptowner.setText(xpartnerlist.get(position).getOwner());
                etptprofit.setText(String.valueOf(xpartnerlist.get(position).getPercentage()));
                btptsave.setOnClickListener(view -> extractapi_editpartner());
            }
            @Override
            public void oneditclick(int position) {
                partner_id = xpartnerlist.get(position).getId();
                tvpttitle.setText("Edit Rekan");
                etptowner.setText(xpartnerlist.get(position).getOwner());
                etptprofit.setText(String.valueOf(xpartnerlist.get(position).getPercentage()));
                btptsave.setOnClickListener(view -> extractapi_editpartner());
            }
            @Override
            public void ondeleteclick(int position) {
                partner_id = xpartnerlist.get(position).getId();
                extractapi_deletepartner();
            }
        });
    }

    private void extractapi_addagent() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("owner",etptowner.getText().toString());
            joform.put("profit",etptprofit.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddpartner = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTPARTNER, joform, response -> {
            try{
                if(response.getString("message").equals("InsertPartner - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddpartner);
    }

    private void extractapi_editpartner() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("owner",etptowner.getText().toString());
            joform.put("profit",etptprofit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditpartner = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITPARTNER+String.valueOf(partner_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditPartner - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joreditpartner);
    }

    private void extractapi_deletepartner() {
        JsonObjectRequest jordeletepartner = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEPARTNER+String.valueOf(partner_id), null, response -> {
            try{
                if(response.getString("message").equals("DeletePartner - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeletepartner);
    }

    private void percentstate(){
        btptpercent.setVisibility(View.GONE);
        tvptpercent.setVisibility(View.VISIBLE);
        btptrp.setVisibility(View.VISIBLE);
        tvptrp.setVisibility(View.GONE);
    }

    private void rpstate(){
        btptpercent.setVisibility(View.VISIBLE);
        tvptpercent.setVisibility(View.GONE);
        btptrp.setVisibility(View.GONE);
        tvptrp.setVisibility(View.VISIBLE);
    }
}