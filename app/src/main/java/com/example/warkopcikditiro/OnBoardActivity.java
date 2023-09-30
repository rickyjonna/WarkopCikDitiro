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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.ActiveUserAdapter;
import com.example.warkopcikditiro.adapter.EmptyStockAdapter;
import com.example.warkopcikditiro.adapter.InformationAdapter;
import com.example.warkopcikditiro.adapter.TableAdapter;
import com.example.warkopcikditiro.adapter.TableOrderListAdapter;
import com.example.warkopcikditiro.model.ActiveUser;
import com.example.warkopcikditiro.model.EmptyStock;
import com.example.warkopcikditiro.model.Information;
import com.example.warkopcikditiro.model.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnBoardActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    //inisiasi variabel
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    CircleImageView civfotouser;
    PopupMenu popupfotouser;
    MenuItem itemchangepassword;
    TextView tvtotalpenjualan, tvnamauser,tvtipeuser, tvnamajudul, tvdotableclose, tvdogoclose, tvdogrclose, tvdotaclose;
    ImageButton tvdcpclose, tvdoclose, ibkeluar, iborder, iborderlist, ibkitchen, ibtcashier;
    LinearLayout viewgroupadmin, viewgroupadmin2;
    ActiveUser activeuser;
    EmptyStock emptystockp, emptystocki;
    Table table;
    Information gojekinformation, grabinformation, takeawayinformation;
    RecyclerView rvactiveuser, rvemptystockp, rvemptystocki, rvdotable, rvdoltable, rvdolinformationgo, rvdolinformationgr, rvdolinformation;
        List<ActiveUser> activeuserlist = new ArrayList<>();
        List<EmptyStock> emptystocklistp = new ArrayList<>();
        List<EmptyStock> emptystocklisti = new ArrayList<>();
        List<Table> tablelist;
        List<Table> tableorderlist;
        List<Information> gojekinformationlist, grabinformationlist, takeawayinformationlist;
    EmptyStockAdapter esproductadapter, esingredientadapter;
    TableAdapter tableadapter;
    TableOrderListAdapter tableorderlistadapter;
    InformationAdapter gojekorderlistadapter, graborderlistadapter, takeawayorderlistadapter;
    Dialog dialogchangepassword, dialogorder, dialogorderbytable, dialogorderbygojek, dialogorderbygrab, dialogorderbytakeaway, dialogorderlist;
    EditText etdcpoldpw, etdcpnewpw, etdogoinformation, etdogrinformation, etdotainformation;
    Button btdcpsave, btdomeja, btdogojek, btdogoconfirm, btdograb, btdogrconfirm, btdotakeaway, btdotaconfirm;
    RequestQueue queue;
    //Manajemen
    Button btdproduct, btdproductcategory, btdagent, btdingredient, btdformula, btdpartner, btdtable, btdpstock, btduser, btdpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        //inisiasi objek
        dialogchangepassword = new Dialog(this);
        dialogchangepassword.setContentView(R.layout.dialog_changepassword);
        dialogorder = new Dialog(this);
        dialogorder.setContentView(R.layout.dialog_order);
        dialogorderbytable = new Dialog(this);
        dialogorderbytable.setContentView(R.layout.dialog_orderbytable);
        dialogorderbygojek = new Dialog(this);
        dialogorderbygojek.setContentView(R.layout.dialog_orderbygojek);
        dialogorderbygrab = new Dialog(this);
        dialogorderbygrab.setContentView(R.layout.dialog_orderbygrab);
        dialogorderbytakeaway = new Dialog(this);
        dialogorderbytakeaway.setContentView(R.layout.dialog_orderbytakeaway);
        dialogorderlist = new Dialog(this);
        dialogorderlist.setContentView(R.layout.dialog_order_list);

        //sambungkan widget dengan id di layoutnya
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        tvnamauser = findViewById(R.id.namauser);
        tvtipeuser = findViewById(R.id.tipeuser);
        civfotouser = findViewById(R.id.civfotouser);
        tvnamajudul = findViewById(R.id.namajudul);
        itemchangepassword = findViewById(R.id.itemchangepassword);
        viewgroupadmin = findViewById(R.id.viewgroupadmin);
        viewgroupadmin2 = findViewById(R.id.viewgroupadmin2);
        tvtotalpenjualan = findViewById(R.id.tvtotalpenjualan);
        ibkeluar = findViewById(R.id.ibkeluar);
        rvactiveuser = findViewById(R.id.rvactiveuser);
        rvemptystockp = findViewById(R.id.rvemptystockp);
        rvemptystocki = findViewById(R.id.rvemptystocki);
        iborder = findViewById(R.id.iborder);
        iborderlist = findViewById(R.id.iborderlist);
        ibkitchen = findViewById(R.id.ibkitchen);
        ibtcashier = findViewById(R.id.ibtcashier);
        tvdcpclose = dialogchangepassword.findViewById(R.id.tvdcpclose);
        etdcpoldpw = dialogchangepassword.findViewById(R.id.etdcpoldpw);
        etdcpnewpw = dialogchangepassword.findViewById(R.id.etdcpnewpw);
        btdcpsave = dialogchangepassword.findViewById(R.id.btdcpsave);
        tvdoclose = dialogorder.findViewById(R.id.tvdoclose);
        btdomeja = dialogorder.findViewById(R.id.btdomeja);
        btdogojek = dialogorder.findViewById(R.id.btdogojek);
        btdograb = dialogorder.findViewById(R.id.btdograb);
        btdotakeaway = dialogorder.findViewById(R.id.btdotakeaway);
        tvdotableclose = dialogorderbytable.findViewById(R.id.tvdotableclose);
        rvdotable = dialogorderbytable.findViewById(R.id.rvdotable);
        tvdogoclose = dialogorderbygojek.findViewById(R.id.tvdogoclose);
        etdogoinformation = dialogorderbygojek.findViewById(R.id.etdogoinformation);
        btdogoconfirm = dialogorderbygojek.findViewById(R.id.btdogoconfirm);
        tvdogrclose = dialogorderbygrab.findViewById(R.id.tvdogrclose);
        etdogrinformation = dialogorderbygrab.findViewById(R.id.etdogrinformation);
        btdogrconfirm = dialogorderbygrab.findViewById(R.id.btdogrconfirm);
        tvdotaclose = dialogorderbytakeaway.findViewById(R.id.tvdotaclose);
        etdotainformation = dialogorderbytakeaway.findViewById(R.id.etdotainformation);
        btdotaconfirm = dialogorderbytakeaway.findViewById(R.id.btdotaconfirm);
        rvdoltable = dialogorderlist.findViewById(R.id.rvdoltable);
        rvdolinformationgo = dialogorderlist.findViewById(R.id.rvdolinformationgo);
        rvdolinformationgr = dialogorderlist.findViewById(R.id.rvdolinformationgr);
        rvdolinformation = dialogorderlist.findViewById(R.id.rvdolinformation);
        //Manajemen
        btdproduct = findViewById(R.id.btdproduct);
        btdproductcategory = findViewById(R.id.btdproductcategory);
        btdagent = findViewById(R.id.btdagent);
        btdingredient = findViewById(R.id.btdingredient);
        btdformula = findViewById(R.id.btdformula);
        btdpartner = findViewById(R.id.btdpartner);
        btdtable = findViewById(R.id.btdtable);
        btdpstock = findViewById(R.id.btdpstock);
        btduser = findViewById(R.id.btduser);
        btdpayment = findViewById(R.id.btdpayment);

        //Pasang RV (tes tanpa variabel, karena ga dipake lagi)
        activeuserlist = new ArrayList<>();
        rvactiveuser.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        rvactiveuser.setAdapter(new ActiveUserAdapter(this,activeuserlist));

        emptystocklistp = new ArrayList<>();
        esproductadapter = new EmptyStockAdapter(this, emptystocklistp);
        rvemptystockp.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        rvemptystockp.setAdapter(esproductadapter);

        emptystocklisti = new ArrayList<>();
        esingredientadapter = new EmptyStockAdapter(this, emptystocklisti);
        rvemptystocki.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        rvemptystocki.setAdapter(esingredientadapter);

        tablelist = new ArrayList<>();
        tableadapter = new TableAdapter(this,tablelist);
        rvdotable.setLayoutManager(new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false));
        rvdotable.setAdapter(tableadapter);

        tableorderlist = new ArrayList<>();
        tableorderlistadapter = new TableOrderListAdapter(this,tableorderlist);
        rvdoltable.setLayoutManager(new GridLayoutManager(this,6,GridLayoutManager.VERTICAL,false));
        rvdoltable.setAdapter(tableorderlistadapter);

        gojekinformationlist = new ArrayList<>();
        gojekorderlistadapter = new InformationAdapter(this,gojekinformationlist);
        rvdolinformationgo.setLayoutManager(new LinearLayoutManager(this));
        rvdolinformationgo.setAdapter(gojekorderlistadapter);

        grabinformationlist = new ArrayList<>();
        graborderlistadapter = new InformationAdapter(this,grabinformationlist);
        rvdolinformationgr.setLayoutManager(new LinearLayoutManager(this));
        rvdolinformationgr.setAdapter(graborderlistadapter);

        takeawayinformationlist = new ArrayList<>();
        takeawayorderlistadapter = new InformationAdapter(this,takeawayinformationlist);
        rvdolinformation.setLayoutManager(new LinearLayoutManager(this));
        rvdolinformation.setAdapter(takeawayorderlistadapter);

        //ambil data
        extractapi();

        //install widget
        //1.Foto
        civfotouser.setOnClickListener(view -> showpopup());
        //2.Keluar
        ibkeluar.setOnClickListener(view -> logout());
        //3.Order
        iborder.setOnClickListener(view -> opendialogorder());
        //4.Orderlist
        iborderlist.setOnClickListener(view -> opendialogorderlist());
        //5.Kitchen
        ibkitchen.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, KitchenActivity.class));
        });
        //6.Cashier
        ibtcashier.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this,CashierActivity.class));
        });
        //Management
        //7.Product
        btdproduct.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(OnBoardActivity.this, ProductActivity.class));
        });
        //8.Product Category
//        btdproductcategory.setOnClickListener(view -> {
//            finish();
//            startActivity(new Intent(this, ProductCategoryActivity.class));
//        });
        btdproductcategory.setVisibility(View.GONE);
        //9.Agent
        btdagent.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, AgentActivity.class));
        });
        //10.Ingredient
        btdingredient.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, IngredientActivity.class));
        });
        //11.Formula
        btdformula.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, ProductCategoryActivity.class));
        });
        //12.Partner
        btdpartner.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, PartnerActivity.class));
        });
        //13.Table
        btdtable.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, TableActivity.class));
        });
        //14.Stock
        btdpstock.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, ProductStockActivity.class));
        });
        //15.User
        btduser.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, UserActivity.class));
        });
        //16.Payment
        btdpayment.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, PaymentActivity.class));
        });
        //17.Title
        tvnamajudul.setText(getString(R.string.nama_toko));
    }

    //Fungsi
    private void extractapi(){
        StringRequest responsdashboard = new StringRequest(Request.Method.GET, ConstantUrl.DASHBORD+sharedpref.getString("token",""), response ->
        {
            try{
                JSONObject objectdashboard = new JSONObject(response);
                JSONObject objectresult = objectdashboard.getJSONObject("result");
                //pasang nama user & simpan ke pref
                JSONArray userjs = objectresult.getJSONArray("user");
                JSONObject userobject = userjs.getJSONObject(0);
                tvnamauser.setText(userobject.getString("user_name"));
                tvtipeuser.setText(userobject.getString("user_type"));
                editor.putString("user_id",userobject.getString("user_id"));
                editor.putString("user_type_id",userobject.getString("user_type_id"));
                editor.putString("user_name",userobject.getString("user_name"));
                editor.putString("user_type",userobject.getString("user_type"));
                editor.apply();
                if(userobject.getString("user_type_id").equals("1")){
                    viewgroupadmin.setVisibility(View.VISIBLE);
                    viewgroupadmin2.setVisibility(View.VISIBLE);
                    //pasang total penjualan ke textview
                    tvtotalpenjualan.setText(objectresult.getString("today_income"));
                    //pasang data useraktif ke recycleview
                    activeuserlist = new ArrayList<>();
                    JSONArray activeuserjs = objectresult.getJSONArray("user_active");
                    for(int i=0; i<activeuserjs.length();i++){
                        JSONObject activeuserobject = activeuserjs.getJSONObject(i);
                        activeuser = new ActiveUser();
                        activeuser.setName(activeuserobject.getString("name"));
                        activeuserlist.add(activeuser);
                    }
                    rvactiveuser.setAdapter(new ActiveUserAdapter(this,activeuserlist));
                } else if (userobject.getString("user_type_id").equals("2") || (userobject.getString("user_type_id").equals("3"))) {
                    viewgroupadmin2.setVisibility(View.VISIBLE);
                } else {

                }
                //pasang data produk stok habis ke rv
                JSONArray emptyproductjs = objectresult.getJSONArray("product");
                for(int i=0; i<emptyproductjs.length();i++) {
                    JSONObject emptyproductobject = emptyproductjs.getJSONObject(i);
                    emptystockp = new EmptyStock();
                    emptystockp.setId(emptyproductobject.getInt("id"));
                    emptystockp.setName(emptyproductobject.getString("name"));
                    emptystockp.setQuantity(emptyproductobject.getInt("quantity"));
                    emptystocklistp.add(emptystockp);
                }
                esproductadapter = new EmptyStockAdapter(this, emptystocklistp);
                rvemptystockp.setAdapter(esproductadapter);
                //pasang data produk bahan habis ke rv
                JSONArray emptyingredientjs = objectresult.getJSONArray("ingredient");
                for(int i=0; i<emptyingredientjs.length();i++) {
                    JSONObject emptyingredientobject = emptyingredientjs.getJSONObject(i);
                    emptystocki = new EmptyStock();
                    emptystocki.setId(emptyingredientobject.getInt("id"));
                    emptystocki.setName(emptyingredientobject.getString("name"));
                    emptystocki.setQuantity(emptyingredientobject.getInt("quantity"));
                    emptystocklisti.add(emptystocki);
                }
                esingredientadapter = new EmptyStockAdapter(this, emptystocklisti);
                rvemptystocki.setAdapter(esingredientadapter);
                Toast.makeText(getApplicationContext(), objectdashboard.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        queue = Volley.newRequestQueue(this);
        queue.add(responsdashboard);
    }

    //Popupmenu foto
    private void showpopup(){
        popupfotouser = new PopupMenu(this,civfotouser);
        popupfotouser.setOnMenuItemClickListener(this);
        popupfotouser.inflate(R.menu.popup_fotouser);
        popupfotouser.show();

    }
    //method untuk popupmenu
    public boolean onMenuItemClick(MenuItem menuitem) {
        if (menuitem.getItemId() == R.id.itemchangepassword) {
            tvdcpclose.setOnClickListener(view -> dialogchangepassword.dismiss());
            btdcpsave.setOnClickListener(view -> {
                StringRequest responschangepw = new StringRequest(Request.Method.POST, ConstantUrl.CHANGEPASSWORD, response ->
                {
                    try {
                        JSONObject objectchangepw = new JSONObject(response);
                        Toast.makeText(this,objectchangepw.getString("message"),Toast.LENGTH_SHORT).show();
                        if (objectchangepw.getString("message").equals("Ubah Password Berhasil")) {
                            dialogchangepassword.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show()){
                    @Override
                    protected Map<String, String> getParams() {
                        //masukkan token ke map lalu direturn
                        Map<String, String> form = new HashMap<>();
                        form.put("token", sharedpref.getString("token", ""));
                        form.put("password", etdcpoldpw.getText().toString());
                        form.put("newpassword", etdcpnewpw.getText().toString());
                        return form;
                    }
                };
                queue = Volley.newRequestQueue(this);
                queue.add(responschangepw);
            });
            dialogchangepassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogchangepassword.show();
        }
        return false;
    }

    private void logout() {
        StringRequest responslogout = new StringRequest(Request.Method.POST, ConstantUrl.LOGOUT, response ->
        {
            try {
                JSONObject objectlogout = new JSONObject(response);
                Toast.makeText(this,objectlogout.getString("message"),Toast.LENGTH_SHORT).show();
                editor.clear();
                editor.apply();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> form = new HashMap<>();
                form.put("token", sharedpref.getString("token", ""));
                return form;
            }
        };
        queue = Volley.newRequestQueue(this);
        queue.add(responslogout);
    }

    private void opendialogorder() {
        dialogorder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorder.show();
        tvdoclose.setOnClickListener(view -> dialogorder.dismiss());
        btdomeja.setOnClickListener(view -> {
            dialogorder.dismiss();
            opendialogorderbytable();
        });
        btdogojek.setOnClickListener(view -> {
            dialogorder.dismiss();
            opendialogorderbygojek();
        });
        btdograb.setOnClickListener(view -> {
            dialogorder.dismiss();
            opendialogorderbygrab();
        });
        btdotakeaway.setOnClickListener(view -> {
            dialogorder.dismiss();
            opendialogorderbytakeaway();
        });
    }

    private void opendialogorderbytable() {
        dialogorderbytable.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorderbytable.show();
        tvdotableclose.setOnClickListener(view -> dialogorderbytable.dismiss());
        StringRequest responsetablelist = new StringRequest(Request.Method.GET, ConstantUrl.TABLELIST, response ->
        {
            try{
                JSONObject jsonobjecttable = new JSONObject(response);
                JSONArray resulttablejs = jsonobjecttable.getJSONArray("results");
                //pasang data table list ke rv
                tablelist = new ArrayList<>();
                for(int i=0; i<resulttablejs.length();i++) {
                    JSONObject tableobject = resulttablejs.getJSONObject(i);
                    table = new Table();
                    table.setid(tableobject.getInt("id"));
                    table.setnumber(tableobject.getInt("number"));
                    table.setextend(tableobject.getInt("extend"));
                    table.setstatus(tableobject.getString("status"));
                    tablelist.add(table);
                }
                tableadapter = new TableAdapter(this,tablelist);
                tableadapter.setonitemclicklistener(position -> {
                    if(tablelist.get(position).getstatus().equals("Available")){
                        editor.putString("order_table_id",String.valueOf(tablelist.get(position).getid()));
                        editor.putString("order_table_number",String.valueOf(tablelist.get(position).getnumber()));
                        editor.putString("order_table_extend",String.valueOf(tablelist.get(position).getextend()));
                        editor.putString("order_table_status",tablelist.get(position).getstatus());
                        editor.putString("order_agent_id","1");
                        editor.remove("order_information");
                        editor.apply();
                        dialogorderbytable.dismiss();
                        startActivity(new Intent(OnBoardActivity.this, OrderActivity.class));
                    }
                });
                rvdotable.setAdapter(tableadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        queue = Volley.newRequestQueue(this);
        queue.add(responsetablelist);
    }

    private void opendialogorderbygojek() {
        dialogorderbygojek.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorderbygojek.show();
        tvdogoclose.setOnClickListener(view -> dialogorderbygojek.dismiss());
        btdogoconfirm.setOnClickListener(view -> {
            editor.putString("order_agent_id","2");
            editor.putString("order_information",etdogoinformation.getText().toString());
            editor.remove("order_table_id");
            editor.remove("order_table_number");
            editor.remove("order_table_extend");
            editor.remove("order_table_status");
            editor.apply();
            dialogorderbygojek.dismiss();
            startActivity(new Intent(this, OrderActivity.class));
        });
    }

    private void opendialogorderbygrab() {
        dialogorderbygrab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorderbygrab.show();
        tvdogrclose.setOnClickListener(view -> dialogorderbygrab.dismiss());
        btdogrconfirm.setOnClickListener(view -> {
            editor.putString("order_agent_id","3");
            editor.putString("order_information",etdogrinformation.getText().toString());
            editor.remove("order_table_id");
            editor.remove("order_table_number");
            editor.remove("order_table_extend");
            editor.remove("order_table_status");
            editor.apply();
            dialogorderbygrab.dismiss();
            startActivity(new Intent(this, OrderActivity.class));
        });
    }

    private void opendialogorderbytakeaway() {
        dialogorderbytakeaway.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorderbytakeaway.show();
        tvdotaclose.setOnClickListener(view -> dialogorderbytakeaway.dismiss());
        btdotaconfirm.setOnClickListener(view -> {
            editor.putString("order_information",etdotainformation.getText().toString());
            editor.putString("order_agent_id","1");
            editor.remove("order_table_id");
            editor.remove("order_table_number");
            editor.remove("order_table_extend");
            editor.remove("order_table_status");
            editor.apply(); // in case error while sharedpref stuck
            dialogorderbytakeaway.dismiss();
            startActivity(new Intent(this, OrderActivity.class));
        });
    }

    private void opendialogorderlist() {
        StringRequest requestorderlist = new StringRequest(Request.Method.GET, ConstantUrl.ORDERLIST, response ->
        {
            try{
                JSONObject orderlistobject = new JSONObject(response);
                //pasang data table list ke rv
                JSONArray oltablelistjs = orderlistobject.getJSONArray("table");
                tableorderlist = new ArrayList<>();
                for(int i=0; i<oltablelistjs.length();i++) {
                    JSONObject tableobject = oltablelistjs.getJSONObject(i);
                    table = new Table();
                    table.setid(tableobject.getInt("id"));
                    table.setnumber(tableobject.getInt("number"));
                    table.setextend(tableobject.getInt("extend"));
                    table.setorder_id(tableobject.getInt("order_id"));
                    table.setorder_note(tableobject.getString("order_note"));
                    tableorderlist.add(table);
                }
                tableorderlistadapter = new TableOrderListAdapter(this,tableorderlist);
                rvdoltable.setAdapter(tableorderlistadapter);
                tableorderlistadapter.setonitemclicklistener(position -> {
                    editor.putString("order_table_id",String.valueOf(tableorderlist.get(position).getid()));
                    editor.putString("order_table_number",String.valueOf(tableorderlist.get(position).getnumber()));
                    editor.putString("order_table_extend",String.valueOf(tableorderlist.get(position).getextend()));
                    editor.putString("order_id",String.valueOf(tableorderlist.get(position).getorder_id()));
                    editor.putString("order_note",tableorderlist.get(position).getorder_note());
                    editor.remove("order_agent_id");
                    editor.remove("order_information");
                    editor.apply();
                    dialogorderlist.dismiss();
                    startActivity(new Intent(OnBoardActivity.this, OrderListActivity.class));
                });
                //pasang data gojek information list ke rv
                JSONArray goinformationjs = orderlistobject.getJSONArray("gojek");
                gojekinformationlist = new ArrayList<>();
                for(int i=0; i<goinformationjs.length();i++) {
                    JSONObject goinformationobject = goinformationjs.getJSONObject(i);
                    gojekinformation = new Information();
                    gojekinformation.setorder_information(goinformationobject.getString("information"));
                    gojekinformation.setorder_id(goinformationobject.getInt("order_id"));
                    gojekinformation.setorder_note(goinformationobject.getString("order_note"));
                    gojekinformationlist.add(gojekinformation);
                }
                gojekorderlistadapter = new InformationAdapter(this,gojekinformationlist);
                rvdolinformationgo.setAdapter(gojekorderlistadapter);
                gojekorderlistadapter.setonitemclicklistener(position -> {
                    editor.putString("order_information",gojekinformationlist.get(position).getorder_information());
                    editor.putString("order_id",String.valueOf(gojekinformationlist.get(position).getorder_id()));
                    editor.putString("order_note",gojekinformationlist.get(position).getorder_note());
                    editor.remove("order_table_id");
                    editor.remove("order_table_number");
                    editor.remove("order_table_extend");
                    editor.apply();
                    dialogorderlist.dismiss();
                    startActivity(new Intent(OnBoardActivity.this, OrderListActivity.class));
                });
                //pasang data grab information list ke rv
                JSONArray grinformationjs = orderlistobject.getJSONArray("grab");
                grabinformationlist = new ArrayList<>();
                for(int i=0; i<grinformationjs.length();i++) {
                    JSONObject grinformationobject = grinformationjs.getJSONObject(i);
                    grabinformation = new Information();
                    grabinformation.setorder_information(grinformationobject.getString("information"));
                    grabinformation.setorder_id(grinformationobject.getInt("order_id"));
                    grabinformation.setorder_note(grinformationobject.getString("order_note"));
                    grabinformationlist.add(grabinformation);
                }
                graborderlistadapter = new InformationAdapter(this,grabinformationlist);
                graborderlistadapter.setonitemclicklistener(position -> dialogorderlist.dismiss());
                rvdolinformationgr.setAdapter(graborderlistadapter);
                graborderlistadapter.setonitemclicklistener(position -> {
                    editor.putString("order_information",grabinformationlist.get(position).getorder_information());
                    editor.putString("order_id",String.valueOf(grabinformationlist.get(position).getorder_id()));
                    editor.putString("order_note",grabinformationlist.get(position).getorder_note());
                    editor.remove("order_table_id");
                    editor.remove("order_table_number");
                    editor.remove("order_table_extend");
                    editor.apply();
                    dialogorderlist.dismiss();
                    startActivity(new Intent(OnBoardActivity.this, OrderListActivity.class));
                });
                //pasang data takeaway information list ke rv
                JSONArray tainformationjs = orderlistobject.getJSONArray("take_away");
                takeawayinformationlist = new ArrayList<>();
                for(int i=0; i<tainformationjs.length();i++) {
                    JSONObject tainformationobject = tainformationjs.getJSONObject(i);
                    takeawayinformation = new Information();
                    takeawayinformation.setorder_information(tainformationobject.getString("information"));
                    takeawayinformation.setorder_id(tainformationobject.getInt("order_id"));
                    takeawayinformation.setorder_note(tainformationobject.getString("order_note"));
                    takeawayinformationlist.add(takeawayinformation);
                }
                takeawayorderlistadapter = new InformationAdapter(this,takeawayinformationlist);
                takeawayorderlistadapter.setonitemclicklistener(position -> dialogorderlist.dismiss());
                rvdolinformation.setAdapter(takeawayorderlistadapter);
                takeawayorderlistadapter.setonitemclicklistener(position -> {
                    editor.putString("order_information",takeawayinformationlist.get(position).getorder_information());
                    editor.putString("order_id",String.valueOf(takeawayinformationlist.get(position).getorder_id()));
                    editor.putString("order_note",takeawayinformationlist.get(position).getorder_note());
                    editor.remove("order_table_id");
                    editor.remove("order_table_number");
                    editor.remove("order_table_extend");
                    editor.apply();
                    dialogorderlist.dismiss();
                    startActivity(new Intent(OnBoardActivity.this, OrderListActivity.class));
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        queue = Volley.newRequestQueue(this);
        queue.add(requestorderlist);
        dialogorderlist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogorderlist.show();
    }
}

