package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.InformationAdapter;
import com.example.warkopcikditiro.adapter.ProductCashierAdapter;
import com.example.warkopcikditiro.adapter.TableOrderListAdapter;
import com.example.warkopcikditiro.model.Information;
import com.example.warkopcikditiro.model.Payment;
import com.example.warkopcikditiro.model.Product;
import com.example.warkopcikditiro.model.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CashierActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtcback;
    TextView tvcusername, tvcusertype;
    //leftbar
    RecyclerView rvcorderlisttable, rvcolgoinformation, rvcolgrinformation, rvcolinformation;
    Table table;
    Information informationgo, informationgr, information;
    List<Table> tablelist;
    List<Information> informationgolist, informationgrlist, informationlist;
    TableOrderListAdapter tableorderlistadapter;
    InformationAdapter informationgoadapter, informationgradapter, informationadapter;
    //rightbar
    TextView tvcorderinformation, tvctotalpriceordered;
    RecyclerView rvcproductlist;
    Product product;
    List<Product> productlist;
    ProductCashierAdapter productcashieradapter;
    int finalprice;
    RadioGroup rgcdiscount, rgctax;
    int discount, tax, payment_id, payment_discount;
    EditText etcdiscount, etctax, etcphonenumber, etcemail;
    Spinner spcpayment;
    Payment payment;
    List<Payment> paymentlist;
    Button btcprint, btcdone;
    TextView tvcprint, tvcdone;
    Dialog dialogcheckout;
        TextView tvdcoclose, tvdcototalprice;
        EditText etdcoinformation;
        Button btdcodone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        //install variable
        ///variable
        finalprice = 0;
        discount = 0;
        tax = 0;
        payment_id = 1;
        payment_discount = 0;
        ///dialog
        dialogcheckout = new Dialog(this);
        dialogcheckout.setContentView(R.layout.dialog_checkout);
        ///list container
        tablelist = new ArrayList<>();
        productlist = new ArrayList<>();
        informationgolist = new ArrayList<>();
        informationgrlist = new ArrayList<>();
        informationlist = new ArrayList<>();
        paymentlist = new ArrayList<>();
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///topbar
        ibtcback = findViewById(R.id.ibtcback);
        tvcusername = findViewById(R.id.tvcusername);
        tvcusertype = findViewById(R.id.tvcusertype);
        //leftbar
        rvcorderlisttable = findViewById(R.id.rvcorderlisttable);
        rvcolgoinformation = findViewById(R.id.rvcolgoinformation);
        rvcolgrinformation = findViewById(R.id.rvcolgrinformation);
        rvcolinformation = findViewById(R.id.rvcolinformation);
        //rightbar
        tvcorderinformation = findViewById(R.id.tvcorderinformation);
        rvcproductlist = findViewById(R.id.rvcproductlist);
        etcdiscount = findViewById(R.id.etcdiscount);
        rgcdiscount = findViewById(R.id.rgcdiscount);
        rgctax = findViewById(R.id.rgctax);
        etctax = findViewById(R.id.etctax);
        etcphonenumber = findViewById(R.id.etcphonenumber);
        etcemail = findViewById(R.id.etcemail);
        spcpayment = findViewById(R.id.spcpayment);
        tvctotalpriceordered = findViewById(R.id.tvctotalpriceordered);
        btcprint = findViewById(R.id.btcprint);
        btcdone = findViewById(R.id.btcdone);
        tvcprint = findViewById(R.id.tvcprint);
        tvcdone = findViewById(R.id.tvcdone);
        tvdcoclose = dialogcheckout.findViewById(R.id.tvdcoclose);
        tvdcototalprice = dialogcheckout.findViewById(R.id.tvdcototalprice);
        etdcoinformation = dialogcheckout.findViewById(R.id.etdcoinformation);
        btdcodone = dialogcheckout.findViewById(R.id.btdcodone);

        //get data + fill layout
        extractapi_cashier();
        ///topbar
        tvcusername.setText(sharedpref.getString("user_name", ""));
        tvcusertype.setText(sharedpref.getString("user_type", ""));
        ///leftbar
        rvcorderlisttable.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        setrvorderlisttable(tablelist);
        rvcolgoinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistgo(informationgolist);
        rvcolgrinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistgr(informationgrlist);
        rvcolinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistta(informationlist);
        //rightbar
        rvcproductlist.setLayoutManager(new LinearLayoutManager(this));
        setrvproductlist(productlist);
        setspinnerpaymentlist(paymentlist);
        notselected();

        //install widget
        ibtcback.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this,OnBoardActivity.class));
        });
        etcdiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length()!=0){
                    if(rgcdiscount.getCheckedRadioButtonId() == R.id.rbcdiscountpercent) {
                        discount = Integer.parseInt(etcdiscount.getText().toString()) * finalprice / 100;
                    } else {
                        discount = Integer.parseInt(etcdiscount.getText().toString());
                    }
                } else {
                    discount = 0;
                }
                notprinted();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etctax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length()!=0){
                    if(rgctax.getCheckedRadioButtonId() == R.id.rbctaxpercent) {
                        tax = Integer.parseInt(etctax.getText().toString()) * finalprice / 100;
                    } else {
                        tax = Integer.parseInt(etctax.getText().toString());
                    }
                } else {
                    tax = 0;
                }
                notprinted();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etcphonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notprinted();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etcemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notprinted();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btcprint.setOnClickListener(view -> extractapi_insertinvoice());
        btcdone.setOnClickListener(view -> opendialogcheckout());
    }

    private void extractapi_cashier() {
        JsonObjectRequest jorcashier = new JsonObjectRequest(Request.Method.GET, ConstantUrl.CASHIER, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jatablelist = joresults.getJSONArray("table");
                for(int i=0; i<jatablelist.length();i++) {
                    JSONObject jotable = jatablelist.getJSONObject(i);
                    table = new Table();
                    table.setid(jotable.getInt("table_id"));
                    table.setnumber(jotable.getInt("table_number"));
                    table.setextend(jotable.getInt("table_extend"));
                    table.setorder_id(jotable.getInt("order_id"));
                    tablelist.add(table);
                }
                setrvorderlisttable(tablelist);
                JSONArray jainformationgolist = joresults.getJSONArray("gojek");
                for(int i=0; i<jainformationgolist.length();i++) {
                    JSONObject jogoinformation = jainformationgolist.getJSONObject(i);
                    informationgo = new Information();
                    informationgo.setorder_information(jogoinformation.getString("information"));
                    informationgo.setorder_id(jogoinformation.getInt("order_id"));
                    informationgolist.add(informationgo);
                }
                setrvorderlistgo(informationgolist);
                JSONArray jainformationgrlist = joresults.getJSONArray("grab");
                for(int i=0; i<jainformationgrlist.length();i++) {
                    JSONObject jogrinformation = jainformationgrlist.getJSONObject(i);
                    informationgr = new Information();
                    informationgr.setorder_information(jogrinformation.getString("information"));
                    informationgr.setorder_id(jogrinformation.getInt("order_id"));
                    informationgrlist.add(informationgr);
                }
                setrvorderlistgr(informationgrlist);
                JSONArray jainformationlist = joresults.getJSONArray("takeaway");
                for(int i=0; i<jainformationlist.length();i++) {
                    JSONObject joinformation = jainformationlist.getJSONObject(i);
                    information = new Information();
                    information.setorder_information(joinformation.getString("information"));
                    information.setorder_id(joinformation.getInt("order_id"));
                    informationlist.add(information);
                }
                setrvorderlistta(informationlist);
                JSONArray japaymentlist = joresults.getJSONArray("payment");
                for(int i=0; i<japaymentlist.length();i++) {
                    JSONObject jopayment = japaymentlist.getJSONObject(i);
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
        Volley.newRequestQueue(this).add(jorcashier);
    }

    @SuppressLint("SetTextI18n")
    private void setrvorderlisttable(List<Table> xtablelist) {
        tableorderlistadapter = new TableOrderListAdapter(this,xtablelist);
        tableorderlistadapter.setonitemclicklistener(position -> {
            cleartype();
            notprinted();
            editor.putString("order_id",String.valueOf(xtablelist.get(position).getorder_id()));
            editor.apply();
            String order_id = String.valueOf(xtablelist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvcorderinformation.setText("Meja " + String.valueOf(xtablelist.get(position).getnumber()));
        });
        rvcorderlisttable.setAdapter(tableorderlistadapter);
    }

    private void setrvorderlistgo(List<Information> xinformationgolist) {
        informationgoadapter = new InformationAdapter(this, xinformationgolist);
        informationgoadapter.setonitemclicklistener(position -> {
            cleartype();
            notprinted();
            editor.putString("order_id",String.valueOf(xinformationgolist.get(position).getorder_id()));
            editor.apply();
            String order_id = String.valueOf(xinformationgolist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvcorderinformation.setText(xinformationgolist.get(position).getorder_information());
        });
        rvcolgoinformation.setAdapter(informationgoadapter);
    }

    private void setrvorderlistgr(List<Information> xinformationgrlist) {
        informationgradapter = new InformationAdapter(this, xinformationgrlist);
        informationgradapter.setonitemclicklistener(position -> {
            cleartype();
            notprinted();
            editor.putString("order_id",String.valueOf(xinformationgrlist.get(position).getorder_id()));
            editor.apply();
            String order_id = String.valueOf(xinformationgrlist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvcorderinformation.setText(xinformationgrlist.get(position).getorder_information());
        });
        rvcolgrinformation.setAdapter(informationgradapter);
    }

    private void setrvorderlistta(List<Information> xinformationlist) {
        informationadapter = new InformationAdapter(this, xinformationlist);
        informationadapter.setonitemclicklistener(position -> {
            cleartype();
            notprinted();
            editor.putString("order_id",String.valueOf(xinformationlist.get(position).getorder_id()));
            editor.apply();
            String order_id = String.valueOf(xinformationlist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvcorderinformation.setText(xinformationlist.get(position).getorder_information());
        });
        rvcolinformation.setAdapter(informationadapter);
    }

    private void setspinnerpaymentlist(List<Payment> xpaymentlist) {
        ArrayAdapter<Payment> paymentadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, xpaymentlist);
        paymentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcpayment.setAdapter(paymentadapter);
        int lastIndex = paymentlist.size() - 1;
        spcpayment.setSelection(lastIndex);
        spcpayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                payment = paymentadapter.getItem(position);
                payment_id = payment.getId();
                payment_discount = payment.getDiscount();
                notprinted();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void extractapi_orderlistlist(String order_id) {
        JsonObjectRequest jororderlistlist = new JsonObjectRequest(Request.Method.GET, ConstantUrl.ORDERLISTLIST+order_id, null, response -> {
            try{
                //product list ordered
                finalprice = 0;
                productlist = new ArrayList<>();
                JSONObject joresult = response.getJSONObject("results");
                JSONArray jaorderdonelist = joresult.getJSONArray("order_list_all");
                for(int i=0; i<jaorderdonelist.length();i++) {
                    JSONObject joproductordered = jaorderdonelist.getJSONObject(i);
                    product = new Product();
                    product.setid(joproductordered.getInt("product_id"));
                    product.setname(joproductordered.getString("product_name"));
                    product.settotal(joproductordered.getInt("product_total"));
                    product.settotalprice(joproductordered.getInt("product_totalprice"));
                    product.setstatus_id(joproductordered.getInt("orderlist_status_id"));
                    product.setstatus(joproductordered.getString("orderlist_status"));
                    product.setorderlist_id(joproductordered.getInt("orderlist_id"));
                    productlist.add(product);
                }
                setrvproductlist(productlist);
                finalprice = countfinalprice(productlist);
                tvctotalpriceordered.setText(String.valueOf(finalprice));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jororderlistlist);
    }

    private void setrvproductlist(List<Product> xproductlist) {
        productcashieradapter = new ProductCashierAdapter(this,xproductlist);
        rvcproductlist.setAdapter(productcashieradapter);
    }

    public int countfinalprice(List<Product> xproductlist){
        for (Product product : xproductlist){
            finalprice += product.gettotalprice() * product.gettotal();
        }
        return finalprice;
    }

    private void notprinted(){
        btcprint.setVisibility(View.VISIBLE);
        tvcprint.setVisibility(View.GONE);
        tvcdone.setVisibility(View.VISIBLE);
        btcdone.setVisibility(View.GONE);
    }

    private void notselected() {
        btcprint.setVisibility(View.GONE);
        tvcprint.setVisibility(View.VISIBLE);
        tvcdone.setVisibility(View.VISIBLE);
        btcdone.setVisibility(View.GONE);
    }

    private void printed(){
        btcprint.setVisibility(View.GONE);
        tvcprint.setVisibility(View.VISIBLE);
        tvcdone.setVisibility(View.GONE);
        btcdone.setVisibility(View.VISIBLE);
    }

    private void cleartype(){
        etcdiscount.getText().clear();
        etctax.getText().clear();
        etcphonenumber.getText().clear();
        etcemail.getText().clear();
    }

    private void extractapi_insertinvoice() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("merchant_id","1");
            joform.put("order_id",sharedpref.getString("order_id", null));
            joform.put("user_id",sharedpref.getString("user_id", null));
            joform.put("payment_id",payment_id);
            joform.put("discount",discount);
            joform.put("tax",tax);

            if(!etcphonenumber.getText().toString().equals("")){
                joform.put("phone_number",etcphonenumber.getText().toString());
            }
            if(!etcemail.getText().toString().equals("")){
                joform.put("email",etcemail.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jorinsertinvoice = new JsonObjectRequest(Request.Method.POST, ConstantUrl.INSERTINVOICE, joform, response -> {
            try{
                if(response.getString("message").equals("Invoice Berhasil Dibuat")){
                    JSONObject joresult = response.getJSONObject("results");
                    editor.putString("invoice_id",joresult.getString("invoice_id"));
                    editor.apply();
                    printed();
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorinsertinvoice);
    }

    private void opendialogcheckout() {
        //belum masuk discount payment
        //belum masuk dicount basic product
        //bill masih sementara dari android aplikasi
        dialogcheckout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogcheckout.show();
        tvdcoclose.setOnClickListener(view -> dialogcheckout.dismiss());
        int bill = finalprice + tax - discount;
        tvdcototalprice.setText(String.valueOf(bill));
        btdcodone.setOnClickListener(view -> extractapi_checkout());
    }

    private void extractapi_checkout() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("information",etdcoinformation.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jorinsertinvoice = new JsonObjectRequest(Request.Method.POST, ConstantUrl.CHECKOUT+sharedpref.getString("invoice_id",null), joform, response -> {
            try{
                if(response.getString("message").equals("Checkout - Success")){
                    editor.remove("invoice_id");
                    editor.apply();
                    finish();
                    startActivity(getIntent());
                    printed();
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorinsertinvoice);
    }
}