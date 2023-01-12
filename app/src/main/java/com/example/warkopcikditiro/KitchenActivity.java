package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warkopcikditiro.adapter.InformationAdapter;
import com.example.warkopcikditiro.adapter.KitchenSameProductAdapter;
import com.example.warkopcikditiro.adapter.ProductInformationAdapter;
import com.example.warkopcikditiro.adapter.ProductKitchenAdapter;
import com.example.warkopcikditiro.adapter.TableOrderListAdapter;
import com.example.warkopcikditiro.model.Information;
import com.example.warkopcikditiro.model.Product;
import com.example.warkopcikditiro.model.ProductInformation;
import com.example.warkopcikditiro.model.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class KitchenActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtkback;
    TextView tvkusername, tvkusertype;
    //leftbar
    RecyclerView rvkorderlisttable, rvkolgoinformation, rvkolgrinformation, rvkolinformation;
        Table table;
        Information informationgo, informationgr, information;
        List<Table> tablelist;
        List<Information> informationgolist, informationgrlist, informationlist;
        TableOrderListAdapter tableorderlistadapter;
        InformationAdapter informationgoadapter, informationgradapter, informationadapter;
    //rightbar
    TextView tvknote, tvkorderinformation;
    RecyclerView rvkproductlist;
        Product product;
        List<Product> productlist;
        ProductKitchenAdapter productkitchenadapter;
    RecyclerView rvkproductsame;
        List<Product> sameproductlist;
        KitchenSameProductAdapter sameproductadapter;
        Dialog dialogsameproduct;
            RecyclerView rvdkpslist;
                ProductInformation productinformation;
                List<ProductInformation> productinformationlist;
                ProductInformationAdapter productinformationadapter;
            Button btdkpsprocess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        //install variable
        ///Dialog
        dialogsameproduct = new Dialog(this);
        dialogsameproduct.setContentView(R.layout.dialog_kitchen_sameproduct);
        ///list container
        tablelist = new ArrayList<>();
        productlist = new ArrayList<>();
        sameproductlist = new ArrayList<>();
        informationgolist = new ArrayList<>();
        informationgrlist = new ArrayList<>();
        informationlist = new ArrayList<>();
        productinformationlist = new ArrayList<>();
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///topbar
        ibtkback = findViewById(R.id.ibtkback);
        tvkusername = findViewById(R.id.tvkusername);
        tvkusertype = findViewById(R.id.tvkusertype);
        //leftbar
        rvkorderlisttable = findViewById(R.id.rvkorderlisttable);
        rvkolgoinformation = findViewById(R.id.rvkolgoinformation);
        rvkolgrinformation = findViewById(R.id.rvkolgrinformation);
        rvkolinformation = findViewById(R.id.rvkolinformation);
        //rightbar
        tvkorderinformation = findViewById(R.id.tvkorderinformation);
        rvkproductlist = findViewById(R.id.rvkproductlist);
        tvknote = findViewById(R.id.tvknote);
        rvkproductsame = findViewById(R.id.rvkproductsame);
        rvdkpslist = dialogsameproduct.findViewById(R.id.rvdkpslist);
        btdkpsprocess = dialogsameproduct.findViewById(R.id.btdkpsprocess);

        //get data + fill layout
        extractapi_kitchen();
            ///topbar
        tvkusername.setText(sharedpref.getString("user_name", ""));
        tvkusertype.setText(sharedpref.getString("user_type", ""));
            ///leftbar
        rvkorderlisttable.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        setrvorderlisttable(tablelist);
        rvkolgoinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistgo(informationgolist);
        rvkolgrinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistgr(informationgrlist);
        rvkolinformation.setLayoutManager(new LinearLayoutManager(this));
        setrvorderlistta(informationlist);
            //rightbar
        rvkproductlist.setLayoutManager(new LinearLayoutManager(this));
        setrvproductlist(productlist);
        rvkproductsame.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        setrvsameproductlist(sameproductlist);
        rvdkpslist.setLayoutManager(new LinearLayoutManager(this));
        setrvsameproductlistdetail(productinformationlist);


        //install widget
        ibtkback.setOnClickListener(view -> finish());
        btdkpsprocess.setOnClickListener(view -> {
            dialogsameproduct.dismiss();
            processbyproductsame();
        });
    }

    private void extractapi_kitchen() {
        JsonObjectRequest jororderlistlist = new JsonObjectRequest(Request.Method.GET, ConstantUrl.KITCHEN, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONObject joorderlist = joresults.getJSONObject("order_list");
                JSONArray jatablelist = joorderlist.getJSONArray("table");
                for(int i=0; i<jatablelist.length();i++) {
                    JSONObject jotable = jatablelist.getJSONObject(i);
                    table = new Table();
                    table.setid(jotable.getInt("table_id"));
                    table.setnumber(jotable.getInt("table_number"));
                    table.setextend(jotable.getInt("table_extend"));
                    table.setorder_id(jotable.getInt("order_id"));
                    table.setorder_note(jotable.getString("order_note"));
                    tablelist.add(table);
                }
                setrvorderlisttable(tablelist);
                JSONArray jainformationgolist = joorderlist.getJSONArray("gojek");
                for(int i=0; i<jainformationgolist.length();i++) {
                    JSONObject jogoinformation = jainformationgolist.getJSONObject(i);
                    informationgo = new Information();
                    informationgo.setorder_information(jogoinformation.getString("information"));
                    informationgo.setorder_id(jogoinformation.getInt("order_id"));
                    informationgo.setorder_note(jogoinformation.getString("order_note"));
                    informationgolist.add(informationgo);
                }
                setrvorderlistgo(informationgolist);
                JSONArray jainformationgrlist = joorderlist.getJSONArray("grab");
                for(int i=0; i<jainformationgrlist.length();i++) {
                    JSONObject jogrinformation = jainformationgrlist.getJSONObject(i);
                    informationgr = new Information();
                    informationgr.setorder_information(jogrinformation.getString("information"));
                    informationgr.setorder_id(jogrinformation.getInt("order_id"));
                    informationgr.setorder_note(jogrinformation.getString("order_note"));
                    informationgrlist.add(informationgr);
                }
                setrvorderlistgr(informationgrlist);
                JSONArray jainformationlist = joorderlist.getJSONArray("takeaway");
                for(int i=0; i<jainformationlist.length();i++) {
                    JSONObject joinformation = jainformationlist.getJSONObject(i);
                    information = new Information();
                    information.setorder_information(joinformation.getString("information"));
                    information.setorder_id(joinformation.getInt("order_id"));
                    information.setorder_note(joinformation.getString("order_note"));
                    informationlist.add(information);
                }
                setrvorderlistta(informationlist);
                JSONArray josameproductlist = joresults.getJSONArray("productordered_list");
                for(int i=0; i<josameproductlist.length();i++) {
                    JSONObject josameproduct = josameproductlist.getJSONObject(i);
                    product = new Product();
                    product.setid(josameproduct.getInt("product_id"));
                    product.setname(josameproduct.getString("product_name"));
                    product.settotal(josameproduct.getInt("product_total"));
                    sameproductlist.add(product);
                }
                setrvsameproductlist(sameproductlist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jororderlistlist);
    }

    @SuppressLint("SetTextI18n")
    private void setrvorderlisttable(List<Table> xtablelist) {
        tableorderlistadapter = new TableOrderListAdapter(this,xtablelist);
        tableorderlistadapter.setonitemclicklistener(position -> {
            String order_id = String.valueOf(xtablelist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvkorderinformation.setText("Meja " + String.valueOf(xtablelist.get(position).getnumber()));
            tvknote.setText(xtablelist.get(position).getorder_note());
        });
        rvkorderlisttable.setAdapter(tableorderlistadapter);
    }

    private void setrvorderlistgo(List<Information> xinformationgolist) {
        informationgoadapter = new InformationAdapter(this, xinformationgolist);
        informationgoadapter.setonitemclicklistener(position -> {
            String order_id = String.valueOf(xinformationgolist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvkorderinformation.setText(xinformationgolist.get(position).getorder_information());
            tvknote.setText(xinformationgolist.get(position).getorder_note());
        });
        rvkolgoinformation.setAdapter(informationgoadapter);
    }

    private void setrvorderlistgr(List<Information> xinformationgrlist) {
        informationgradapter = new InformationAdapter(this, xinformationgrlist);
        informationgradapter.setonitemclicklistener(position -> {
            String order_id = String.valueOf(xinformationgrlist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvkorderinformation.setText(xinformationgrlist.get(position).getorder_information());
            tvknote.setText(xinformationgrlist.get(position).getorder_note());
        });
        rvkolgrinformation.setAdapter(informationgradapter);
    }

    private void setrvorderlistta(List<Information> xinformationlist) {
        informationadapter = new InformationAdapter(this, xinformationlist);
        informationadapter.setonitemclicklistener(position -> {
            String order_id = String.valueOf(xinformationlist.get(position).getorder_id());
            extractapi_orderlistlist(order_id);
            tvkorderinformation.setText(xinformationlist.get(position).getorder_information());
            tvknote.setText(xinformationlist.get(position).getorder_note());
        });
        rvkolinformation.setAdapter(informationadapter);
    }

    private void extractapi_orderlistlist(String order_id) {
        JsonObjectRequest jororderlistlist = new JsonObjectRequest(Request.Method.GET, ConstantUrl.ORDERLISTLIST+order_id, null, response -> {
            try{
                //product list ordered
                productlist = new ArrayList<>();
                JSONObject joresult = response.getJSONObject("results");
                JSONArray jaorderlist = joresult.getJSONArray("order_list_notdone");
                for(int i=0; i<jaorderlist.length();i++) {
                    JSONObject joproductordered = jaorderlist.getJSONObject(i);
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
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jororderlistlist);
    }

    private void setrvproductlist(List<Product> xproductlist) {
        productkitchenadapter = new ProductKitchenAdapter(this,xproductlist);
        productkitchenadapter.setonitemclicklistener(new ProductKitchenAdapter.onitemclicklistener() {
            @Override
            public void ondeleteclick(int position) {
                deleteorderlist(xproductlist, position);
            }
            @Override
            public void onprocessclick(int position) {
                changeorderliststatus(xproductlist, position, 2);
            }
            @Override
            public void ondoneclick(int position) {
                changeorderliststatus(xproductlist, position, 3);
            }
            @Override
            public void onserveclick(int position) {
                changeorderliststatus(xproductlist, position, 4);
            }
        });
        rvkproductlist.setAdapter(productkitchenadapter);
    }

    private void deleteorderlist(List<Product> xproductlist, int xposition) {
        int orderlist_id = xproductlist.get(xposition).getorderlist_id();
        JsonObjectRequest jordeleteorderlist = new JsonObjectRequest(Request.Method.GET, ConstantUrl.DELETEORDERLIST+String.valueOf(orderlist_id), null, response -> {
            try{
                if(response.getString("message").equals("OrderList - Delete - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeleteorderlist);
    }

    private void changeorderliststatus(List<Product> xproductlist, int xposition, int xstatus_id) {
        int orderlist_id = xproductlist.get(xposition).getorderlist_id();
        JSONObject joformupdateolstatus = new JSONObject();
        try{
            joformupdateolstatus.put("user_id",sharedpref.getString("user_id",""));
            joformupdateolstatus.put("order_list_status_id",String.valueOf(xstatus_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jpupdateolstatus = new JsonObjectRequest(Request.Method.POST, ConstantUrl.UPDATEORDERLISTSTATUS+String.valueOf(orderlist_id), joformupdateolstatus, response -> {
            try{
                if(response.getString("message").equals("OrderList - UpdateStatus - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jpupdateolstatus);
    }

    private void setrvsameproductlist(List<Product> xsameproductlist) {
        sameproductadapter = new KitchenSameProductAdapter(this, xsameproductlist);
        sameproductadapter.setonitemclicklistener(position -> {
            dialogsameproduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogsameproduct.show();
            productinformationlist = new ArrayList<>();
            extractapi_productsameinformation(xsameproductlist,position);
        });
        rvkproductsame.setAdapter(sameproductadapter);
    }

    private void extractapi_productsameinformation(List<Product> xsameproductlist, int xposition) {
        int product_id = xsameproductlist.get(xposition).getid();
        JsonObjectRequest jorolpsdetail = new JsonObjectRequest(Request.Method.GET, ConstantUrl.SAMEPRODUCTDETAIL+String.valueOf(product_id), null, response -> {
            try{
                if(response.getString("message").equals("SameProductDetail - Success")){
                    JSONArray jarolpsdetailresult = response.getJSONArray("results");
                    for (int i = 0; i < jarolpsdetailresult.length();i++){
                        JSONObject jorolpsdetailresult = jarolpsdetailresult.getJSONObject(i);
                        productinformation = new ProductInformation();
                        productinformation.setProduct_id(jorolpsdetailresult.getInt("product_id"));
                        productinformation.setOrder_id(jorolpsdetailresult.getInt("order_id"));
                        productinformation.setTable_id(jorolpsdetailresult.optInt("table_id"));
                        productinformation.setTable_number(jorolpsdetailresult.optInt("table_number"));
                        productinformation.setTable_extend(jorolpsdetailresult.optInt("table_extend"));
                        productinformation.setInformation(jorolpsdetailresult.optString("order_information"));
                        productinformation.setTotal(jorolpsdetailresult.getInt("total"));
                        productinformation.setOrderlist_id(jorolpsdetailresult.getInt("orderlist_id"));
                        productinformationlist.add(productinformation);
                    }
                    setrvsameproductlistdetail(productinformationlist);
                } else {
                    Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jorolpsdetail);
    }

    private void setrvsameproductlistdetail(List<ProductInformation> xproductinformationlist) {
        productinformationadapter = new ProductInformationAdapter(this, xproductinformationlist);
        productinformationadapter.setonitemclicklistener(position -> {
            //need?
        });
        rvdkpslist.setAdapter(productinformationadapter);
    }

    private void processbyproductsame() {
        JSONObject jouolsbpidform = new JSONObject();
        try{
            jouolsbpidform.put("user_id",sharedpref.getString("user_id",null));
            jouolsbpidform.put("product_id",productinformationlist.get(0).getProduct_id());
        } catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest  joruolsbpid = new JsonObjectRequest(Request.Method.POST, ConstantUrl.UPDATEOLSBYPRODUCTID, jouolsbpidform, response -> {
            try{
                if(response.getString("message").equals("OrderList - UpdateStatusByProductID - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joruolsbpid);
    }
}