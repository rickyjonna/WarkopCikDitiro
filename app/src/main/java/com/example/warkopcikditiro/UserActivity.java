package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.example.warkopcikditiro.adapter.RVUserAdapter;
import com.example.warkopcikditiro.model.Payment;
import com.example.warkopcikditiro.model.User;
import com.example.warkopcikditiro.model.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    //topbar
    ImageButton ibtuback;
    TextView tvuusername, tvuusertype;
    //leftbar
    RecyclerView rvulist;
    User user;
    List<User> userlist;
    RVUserAdapter rvuseradapter;
    Button btuadd;
    //rightbar
    EditText etuname, etuaddress, etuphone_number, etupassword;
    Spinner sputype;
    UserType usertype;
    List<UserType> usertypelist;
    Button btusave;
    TextView tvutitle;
    int user_id, usertype_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //install variable
        ///variable
        user_id = 0;
        usertype_id = 0;
        ///sharedpref
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        ///listcontainer
        userlist = new ArrayList<>();
        usertypelist = new ArrayList<>();
        ///topbar
        ibtuback = findViewById(R.id.ibtuback);
        tvuusername = findViewById(R.id.tvuusername);
        tvuusertype = findViewById(R.id.tvuusertype);
        ///leftbar
        rvulist = findViewById(R.id.rvulist);
        btuadd = findViewById(R.id.btuadd);
        ///rightbar
        tvutitle = findViewById(R.id.tvutitle);
        etuname = findViewById(R.id.etuname);
        etuaddress = findViewById(R.id.etuaddress);
        etupassword = findViewById(R.id.etupassword);
        etuphone_number = findViewById(R.id.etuphone_number);
        sputype = findViewById(R.id.sputype);
        btusave = findViewById(R.id.btusave);

        //get data + fill layout
        extractapi_user();
        ///topbar
        tvuusername.setText(sharedpref.getString("user_name", ""));
        tvuusertype.setText(sharedpref.getString("user_type", ""));

        //install widget
        ibtuback.setOnClickListener(view -> finish());
        btuadd.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
        btusave.setOnClickListener(view -> extractapi_adduser());
    }

    private void extractapi_user() {
        JsonObjectRequest joruser = new JsonObjectRequest(Request.Method.GET, ConstantUrl.USER, null, response -> {
            try{
                JSONObject joresults = response.getJSONObject("results");
                JSONArray jauser = joresults.getJSONArray("user");
                for(int i=0; i<jauser.length();i++) {
                    JSONObject jouser = jauser.getJSONObject(i);
                    user = new User();
                    user.setId(jouser.getInt("user_id"));
                    user.setName(jouser.getString("name"));
                    user.setUser_type(jouser.getString("user_type"));
                    user.setPhone_number(jouser.getString("phone_number"));
                    user.setAddress(jouser.getString("address"));
                    userlist.add(user);
                }
                setrvuserlist(userlist);
                JSONArray jausertype = joresults.getJSONArray("user_type");
                for(int i=0; i<jausertype.length();i++) {
                    JSONObject jousertype = jausertype.getJSONObject(i);
                    usertype = new UserType();
                    usertype.setId(jousertype.getInt("id"));
                    usertype.setInformation(jousertype.getString("information"));
                    usertypelist.add(usertype);
                }
                setspinnerusertype(usertypelist);
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joruser);
    }

    private void setrvuserlist(List<User> xuserlist) {
        rvuseradapter = new RVUserAdapter(this,xuserlist);
        rvulist.setLayoutManager(new LinearLayoutManager(this));
        rvulist.setAdapter(rvuseradapter);
        rvuseradapter.setonitemclicklistener(new RVUserAdapter.onitemclicklistener(){
            @Override
            public void onitemclick(int position) {
                user_id = xuserlist.get(position).getId();
                usertype_id = xuserlist.get(position).getUser_type_id();

                tvutitle.setText("Edit Karyawan");
                etuname.setText(xuserlist.get(position).getName());
                etuaddress.setText(xuserlist.get(position).getAddress());
                etuphone_number.setText(xuserlist.get(position).getPhone_number());
                sputype.setSelection(getIndex(sputype, xuserlist.get(position).getUser_type()));
                etupassword.setText("********");
                etupassword.setFocusable(false);
                btusave.setOnClickListener(view -> extractapi_edituser());
            }
            @Override
            public void oneditclick(int position) {
                user_id = xuserlist.get(position).getId();
                usertype_id = xuserlist.get(position).getUser_type_id();

                tvutitle.setText("Edit Karyawan");
                etuname.setText(xuserlist.get(position).getName());
                etuaddress.setText(xuserlist.get(position).getAddress());
                etuphone_number.setText(xuserlist.get(position).getPhone_number());
                sputype.setSelection(getIndex(sputype, xuserlist.get(position).getUser_type()));
                etupassword.setText("********");
                etupassword.setFocusable(false);
                btusave.setOnClickListener(view -> extractapi_edituser());
            }
            @Override
            public void ondeleteclick(int position) {
                user_id = xuserlist.get(position).getId();
                extractapi_deleteuser();
            }
        });
    }

    private void setspinnerusertype(List<UserType> xusertypelist) {
        ArrayAdapter<UserType> usertypearrayadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, xusertypelist);
        usertypearrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sputype.setAdapter(usertypearrayadapter);
        sputype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                usertype_id = usertypearrayadapter.getItem(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private int getIndex(Spinner xspinner, String xusertypeinformation){
        for (int i=0;i<xspinner.getCount();i++){
            if (xspinner.getItemAtPosition(i).toString().equalsIgnoreCase(xusertypeinformation)){
                return i;
            }
        }
        return 0;
    }

    private void extractapi_adduser() {
        JSONObject joform = new JSONObject();
        try{
            joform.put("user_type_id",String.valueOf(usertype_id));
            joform.put("phone_number",etuphone_number.getText().toString());
            joform.put("password",etupassword.getText().toString());
            joform.put("name",etuname.getText().toString());
            joform.put("address",etuaddress.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest joraddtable = new JsonObjectRequest(Request.Method.POST, ConstantUrl.REGISTER, joform, response -> {
            try{
                if(response.getString("message").equals("Register - Success")){
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

    private void extractapi_edituser() {
        JSONObject joform = new JSONObject();
        try {
            joform.put("user_type_id",String.valueOf(usertype_id));
            joform.put("phone_number",etuphone_number.getText().toString());
            joform.put("name",etuname.getText().toString());
            joform.put("address",etuaddress.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joredituser = new JsonObjectRequest(Request.Method.POST, ConstantUrl.EDITUSER+String.valueOf(user_id), joform, response -> {
            try{
                if(response.getString("message").equals("EditUser - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(joredituser);
    }

    private void extractapi_deleteuser() {
        JsonObjectRequest jordeleteuser = new JsonObjectRequest(Request.Method.POST, ConstantUrl.DELETEUSER+String.valueOf(user_id), null, response -> {
            try{
                if(response.getString("message").equals("DeleteUser - Success")){
                    finish();
                    startActivity(getIntent());
                }
                Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,"Koneksi Terputus Harap Login Ulang", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(jordeleteuser);
    }
}