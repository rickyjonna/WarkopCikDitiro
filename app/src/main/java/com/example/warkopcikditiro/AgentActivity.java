package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.AgentAdapter;
import com.example.warkopcikditiro.adapter.InformationAdapter;
import com.example.warkopcikditiro.adapter.ProductCashierAdapter;
import com.example.warkopcikditiro.model.Agent;
import com.example.warkopcikditiro.model.Information;
import com.example.warkopcikditiro.model.Payment;
import com.example.warkopcikditiro.model.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgentActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtaback;
    TextView tvausername, tvausertype;
    //leftbar
    RecyclerView rvalist;
    Agent agent;
    List<Agent> agentlist;
    AgentAdapter agentadapter;
    Button btaadd;
    //rightbar
    EditText etaname, etapercentage;
    Button btasave, btapercent, btarp;
    TextView tvatitle, tvapercent, tvarp;
    Spinner spapayment;
    Payment payment;
    ArrayAdapter<Payment> paymentadapter;
    List<Payment> paymentlist;
    int id_agent, payment_id, percentage_agent;
    String name_agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        //install variable
        ///variable
        id_agent = 0;
        payment_id = 0;
        percentage_agent = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        agentlist = new ArrayList<>();
        paymentlist = new ArrayList<>();
        ///topbar
        ibtaback = findViewById(R.id.ibtaback);
        tvausername = findViewById(R.id.tvausername);
        tvausertype = findViewById(R.id.tvausertype);
        ///leftbar
        rvalist = findViewById(R.id.rvalist);
        btaadd = findViewById(R.id.btaadd);
        ///rightbar
        tvatitle = findViewById(R.id.tvatitle);
        etaname = findViewById(R.id.etaname);
        etapercentage = findViewById(R.id.etapercentage);
        btapercent = findViewById(R.id.btapercent);
        btarp = findViewById(R.id.btarp);
        tvapercent = findViewById(R.id.tvapercent);
        tvarp = findViewById(R.id.tvarp);
        spapayment = findViewById(R.id.spapayment);
        btasave = findViewById(R.id.btasave);

        //get data + fill layout
        extractapi_agent();
        ///topbar
        tvausername.setText(sharedpref.getString("user_name", ""));
        tvausertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        setrvagentlist(agentlist);
        ///rightbar
        percentstate();

        //install widget
        ibtaback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, OnBoardActivity.class));
        });
        btaadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btapercent.setOnClickListener(view -> percentstate());
        btarp.setOnClickListener(view -> rpstate());
        btasave.setOnClickListener(view -> extractapi_addagent());
    }

    private void extractapi_agent() {
        JsonObjectRequest joragent = new JsonObjectRequest(Request.Method.GET, ConstantUrl.AGENT, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jaagent = joresults.getJSONArray("agent");
                for(int i=0; i<jaagent.length();i++) {
                    JSONObject joagent = jaagent.getJSONObject(i);
                    agent = new Agent();
                    agent.setId(joagent.getInt("id"));
                    agent.setPayment_id(joagent.getInt("payment_id"));
                    agent.setPayment_information(joagent.getString("payment_information"));
                    agent.setName(joagent.getString("name"));
                    agent.setPercentage(joagent.getInt("percentage"));
                    agentlist.add(agent);
                }
                setrvagentlist(agentlist);
                JSONArray japayment = joresults.getJSONArray("payment");
                for(int i=0; i<japayment.length();i++) {
                    JSONObject jopayment = japayment.getJSONObject(i);
                    payment = new Payment();
                    payment.setId(jopayment.getInt("id"));
                    payment.setInformation(jopayment.getString("information"));
                    payment.setDiscount(jopayment.getInt("discount"));
                    paymentlist.add(payment);
                }
                setspinnerpaymentlist(paymentlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joragent);
    }

    private void setrvagentlist(List<Agent> xagentlist) {
        agentadapter = new AgentAdapter(this,xagentlist);
        rvalist.setLayoutManager(new LinearLayoutManager(this));
        rvalist.setAdapter(agentadapter);
        agentadapter.setonitemclicklistener(new AgentAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                id_agent = xagentlist.get(position).getId();
                payment_id = xagentlist.get(position).getPayment_id();
                name_agent = xagentlist.get(position).getName();
                percentage_agent = xagentlist.get(position).getPercentage();

                spapayment.setSelection(getIndex(spapayment, xagentlist.get(position).getPayment_information()));
                tvatitle.setText("Edit Agen");
                etaname.setText(xagentlist.get(position).getName());
                etapercentage.setText(String.valueOf(percentage_agent));
                btasave.setOnClickListener(view -> extractapi_editagent());
            }
            @Override
            public void oneditclick(int position) {
                id_agent = xagentlist.get(position).getId();
                payment_id = xagentlist.get(position).getPayment_id();
                name_agent = xagentlist.get(position).getName();
                percentage_agent = xagentlist.get(position).getPercentage();

                spapayment.setSelection(getIndex(spapayment, xagentlist.get(position).getPayment_information()));
                tvatitle.setText("Edit Agen");
                etaname.setText(xagentlist.get(position).getName());
                etapercentage.setText(String.valueOf(percentage_agent));
                btasave.setOnClickListener(view -> extractapi_editagent());
            }
            @Override
            public void ondeleteclick(int position) {
                id_agent = xagentlist.get(position).getId();
                extractapi_deleteagent();
            }
        });
    }

    private void extractapi_deleteagent() {
        JsonObjectRequest jordeleteagent = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEAGENT+String.valueOf(id_agent), null, response -> {
            try{
                if(response.getString("message").equals("DeleteAgent - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeleteagent);
    }

    private void setspinnerpaymentlist(List<Payment> xpaymentlist) {
        ArrayAdapter<Payment> paymentadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, xpaymentlist);
        paymentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spapayment.setAdapter(paymentadapter);
        spapayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                payment_id = paymentadapter.getItem(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private int getIndex(Spinner xspinner, String xpaymentname){
        for (int i=0;i<xspinner.getCount();i++){
            if (xspinner.getItemAtPosition(i).toString().equalsIgnoreCase(xpaymentname)){
                return i;
            }
        }
        return 0;
    }

    private void extractapi_addagent() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("payment_id",String.valueOf(payment_id));
            joform.put("name",etaname.getText().toString());
            joform.put("percentage",etapercentage.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddagent = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTAGENT, joform, response -> {
            try{
                if(response.getString("message").equals("InsertAgent - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddagent);
    }

    private void extractapi_editagent() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("payment_id",String.valueOf(payment_id));
            joform.put("name",etaname.getText().toString());
            joform.put("percentage",etapercentage.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditagent = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITAGENT+String.valueOf(id_agent), joform, response -> {
            try{
                if(response.getString("message").equals("EditAgent - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joreditagent);
    }

    private void percentstate(){
        btapercent.setVisibility(View.GONE);
        tvapercent.setVisibility(View.VISIBLE);
        btarp.setVisibility(View.VISIBLE);
        tvarp.setVisibility(View.GONE);
    }

    private void rpstate(){
        btapercent.setVisibility(View.VISIBLE);
        tvapercent.setVisibility(View.GONE);
        btarp.setVisibility(View.GONE);
        tvarp.setVisibility(View.VISIBLE);
    }
}