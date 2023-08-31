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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.RVTableAdapter;
import com.example.warkopcikditiro.model.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibttback;
    TextView tvtusername, tvtusertype;
    //leftbar
    RecyclerView rvtlist;
    Table table;
    List<Table> tablelist;
    RVTableAdapter rvtableAdapter;
    Button bttadd;
    //rightbar
    EditText ettnumber, ettextend;
    RadioGroup rgtstatus;
    Button bttsave;
    TextView tvttitle;
    int table_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        //install variable
        ///variable
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        tablelist = new ArrayList<>();
        ///topbar
        ibttback = findViewById(R.id.ibttback);
        tvtusername = findViewById(R.id.tvtusername);
        tvtusertype = findViewById(R.id.tvtusertype);
        ///leftbar
        rvtlist = findViewById(R.id.rvtlist);
        bttadd = findViewById(R.id.bttadd);
        ///rightbar
        tvttitle = findViewById(R.id.tvttitle);
        ettnumber = findViewById(R.id.ettnumber);
        ettextend = findViewById(R.id.ettextend);
        rgtstatus = findViewById(R.id.rgtstatus);
        bttsave = findViewById(R.id.bttsave);

        //get data + fill layout
        extractapi_agent();
        ///topbar
        tvtusername.setText(sharedpref.getString("user_name", ""));
        tvtusertype.setText(sharedpref.getString("user_type", ""));

        //install widget
        ibttback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, OnBoardActivity.class));
        });
        bttadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        bttsave.setOnClickListener(view -> extractapi_addtable());
    }

    private void extractapi_agent() {
        JsonObjectRequest jortable = new JsonObjectRequest(Request.Method.GET, ConstantUrl.TABLE, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jatable = joresults.getJSONArray("table");
                for(int i=0; i<jatable.length();i++) {
                    JSONObject jotable = jatable.getJSONObject(i);
                    table = new Table();
                    table.setid(jotable.getInt("id"));
                    table.setnumber(jotable.getInt("number"));
                    table.setextend(jotable.getInt("extend"));
                    table.setstatus(jotable.getString("status"));
                    tablelist.add(table);
                }
                setrvagentlist(tablelist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jortable);
    }

    private void setrvagentlist(List<Table> xtablelist) {
        rvtableAdapter = new RVTableAdapter(this,xtablelist);
        rvtlist.setLayoutManager(new LinearLayoutManager(this));
        rvtlist.setAdapter(rvtableAdapter);
        rvtableAdapter.setonitemclicklistener(new RVTableAdapter.onitemclicklistener(){
            @Override
            public void onitemclick(int position) {
                table_id = xtablelist.get(position).getid();
                tvttitle.setText("Edit Meja");
                ettnumber.setText(String.valueOf(xtablelist.get(position).getnumber()));
                ettextend.setText(String.valueOf(xtablelist.get(position).getextend()));
                bttsave.setOnClickListener(view -> extractapi_edittable());
            }
            @Override
            public void oneditclick(int position) {
                table_id = xtablelist.get(position).getid();
                tvttitle.setText("Edit Meja");
                ettnumber.setText(String.valueOf(xtablelist.get(position).getnumber()));
                ettextend.setText(String.valueOf(xtablelist.get(position).getextend()));
                bttsave.setOnClickListener(view -> extractapi_edittable());
            }
            @Override
            public void ondeleteclick(int position) {
                table_id = xtablelist.get(position).getid();
                extractapi_deletetable();
            }
        });
    }

    private void extractapi_addtable() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("number",ettnumber.getText().toString());
            joform.put("extend",ettextend.getText().toString());
            if(rgtstatus.getCheckedRadioButtonId() == R.id.rbtavailable) {
                joform.put("status","Available");
            } else {
                joform.put("status","Not Available");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddtable = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTTABLE, joform, response -> {
            try{
                if(response.getString("message").equals("InsertTable - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddtable);
    }

    private void extractapi_edittable() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("number",ettnumber.getText().toString());
            joform.put("extend",ettextend.getText().toString());
            if(rgtstatus.getCheckedRadioButtonId() == R.id.rbtavailable) {
                joform.put("status","Available");
            } else {
                joform.put("status","Not Available");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditpartner = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITTABLE+String.valueOf(table_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditTable - Success")){
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

    private void extractapi_deletetable() {
        JsonObjectRequest jordeletetable = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETETABLE+String.valueOf(table_id), null, response -> {
            try{
                if(response.getString("message").equals("DeleteTable - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeletetable);
    }
}