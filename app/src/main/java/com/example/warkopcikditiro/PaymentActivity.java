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
import com.example.warkopcikditiro.adapter.PaymentAdapter;
import com.example.warkopcikditiro.model.Partner;
import com.example.warkopcikditiro.model.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtpayback;
    TextView tvpayusername, tvpayusertype;
    //leftbar
    RecyclerView rvpaylist;
    Payment payment;
    List<Payment> paymentlist;
    PaymentAdapter paymentadapter;
    Button btpayadd;
    //rightbar
    EditText etpayname, etpaydiscount;
    Button btpaysave, btpaypercent, btpayrp;
    TextView tvpaytitle, tvpaypercent, tvpayrp;
    int payment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //install variable
        ///variable
        payment_id = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        paymentlist = new ArrayList<>();
        ///topbar
        ibtpayback = findViewById(R.id.ibtpayback);
        tvpayusername = findViewById(R.id.tvpayusername);
        tvpayusertype = findViewById(R.id.tvpayusertype);
        ///leftbar
        rvpaylist = findViewById(R.id.rvpaylist);
        btpayadd = findViewById(R.id.btpayadd);
        ///rightbar
        tvpaytitle = findViewById(R.id.tvpaytitle);
        etpayname = findViewById(R.id.etpayname);
        etpaydiscount = findViewById(R.id.etpaydiscount);
        btpaypercent = findViewById(R.id.btpaypercent);
        btpayrp = findViewById(R.id.btpayrp);
        tvpaypercent = findViewById(R.id.tvpaypercent);
        tvpayrp = findViewById(R.id.tvpayrp);
        btpaysave = findViewById(R.id.btpaysave);

        //get data + fill layout
        extractapi_payment();
        ///topbar
        tvpayusername.setText(sharedpref.getString("user_name", ""));
        tvpayusertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        setrvpaymentlist(paymentlist);
        ///rightbar
        percentstate();

        //install widget
        ibtpayback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, OnBoardActivity.class));
        });
        btpayadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btpaypercent.setOnClickListener(view -> percentstate());
        btpayrp.setOnClickListener(view -> rpstate());
        btpaysave.setOnClickListener(view -> extractapi_addpayment());
    }

    private void extractapi_payment() {
        JsonObjectRequest jorpayment = new JsonObjectRequest(Request.Method.GET, ConstantUrl.PAYMENT, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray japayment = joresults.getJSONArray("payment");
                for(int i=0; i<japayment.length();i++) {
                    JSONObject jopartner = japayment.getJSONObject(i);
                    payment = new Payment();
                    payment.setId(jopartner.getInt("id"));
                    payment.setInformation(jopartner.getString("information"));
                    payment.setDiscount(jopartner.getInt("discount"));
                    paymentlist.add(payment);
                }
                setrvpaymentlist(paymentlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorpayment);
    }

    private void setrvpaymentlist(List<Payment> xpaymentlist) {
        paymentadapter = new PaymentAdapter(this,xpaymentlist);
        rvpaylist.setLayoutManager(new LinearLayoutManager(this));
        rvpaylist.setAdapter(paymentadapter);
        paymentadapter.setonitemclicklistener(new PaymentAdapter.onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                payment_id = xpaymentlist.get(position).getId();
                tvpaytitle.setText("Edit Tipe Pembayaran");
                etpayname.setText(xpaymentlist.get(position).getInformation());
                etpaydiscount.setText(String.valueOf(xpaymentlist.get(position).getDiscount()));
                btpaysave.setOnClickListener(view -> extractapi_editpayment());
            }
            @Override
            public void oneditclick(int position) {
                payment_id = xpaymentlist.get(position).getId();
                tvpaytitle.setText("Edit Tipe Pembayaran");
                etpayname.setText(xpaymentlist.get(position).getInformation());
                etpaydiscount.setText(String.valueOf(xpaymentlist.get(position).getDiscount()));
                btpaysave.setOnClickListener(view -> extractapi_editpayment());
            }
            @Override
            public void ondeleteclick(int position) {
                payment_id = xpaymentlist.get(position).getId();
                extractapi_deletepayment();
            }
        });
    }

    private void extractapi_addpayment() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("information",etpayname.getText().toString());
            joform.put("discount",etpaydiscount.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddpayment = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTPAYMENT, joform, response -> {
            try{
                if(response.getString("message").equals("InsertPayment - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joraddpayment);
    }

    private void extractapi_editpayment() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("information",etpayname.getText().toString());
            joform.put("discount",etpaydiscount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joreditpayment = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITPAYMENT+String.valueOf(payment_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditPayment - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joreditpayment);
    }

    private void extractapi_deletepayment() {
        JsonObjectRequest jordeletepayment = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEPAYMENT+String.valueOf(payment_id), null, response -> {
            try{
                if(response.getString("message").equals("DeletePayment - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeletepayment);
    }

    private void percentstate(){
        btpaypercent.setVisibility(View.GONE);
        tvpaypercent.setVisibility(View.VISIBLE);
        btpayrp.setVisibility(View.VISIBLE);
        tvpayrp.setVisibility(View.GONE);
    }

    private void rpstate(){
        btpaypercent.setVisibility(View.VISIBLE);
        tvpaypercent.setVisibility(View.GONE);
        btpayrp.setVisibility(View.GONE);
        tvpayrp.setVisibility(View.VISIBLE);
    }
}