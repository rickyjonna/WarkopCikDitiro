package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    EditText no_hp, pw;
    Button masuk;
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        sharedpref = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        no_hp = findViewById(R.id.no_hp);
        pw = findViewById(R.id.pw);
        masuk = findViewById(R.id.masuk);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        StringRequest respon = new StringRequest(
                Request.Method.POST,
                ConstantUrl.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonobjek = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobjek.getString("message"), Toast.LENGTH_SHORT).show();
                            if(jsonobjek.getString("message").equals("Login Berhasil")){
                                //simpantoken
                                JSONObject result = jsonobjek.getJSONObject("result");
                                String token = result.getString("token");
                                editor.putString("token",token);
                                editor.putString("merchant_id","1");
                                editor.apply();
                                //masuk dashboard
                                startActivity(new Intent(AuthActivity.this, OnBoardActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Toast.makeText(getApplicationContext(),"Tidak Ada Koneksi", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> form = new HashMap<>();
                form.put("phone_number",no_hp.getText().toString());
                form.put("password",pw.getText().toString());
                return form;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(respon);
    }
}