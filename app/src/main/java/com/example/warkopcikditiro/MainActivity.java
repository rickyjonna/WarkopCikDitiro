package com.example.warkopcikditiro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    SharedPreferences tokenpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code ini akan mempause aplikasi selama 1,5secs dan semua yang di method run akan di run
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirstTime();
            }
        },1500);
    }

    private void isFirstTime() {
        //ngechek aplikasi berjalan pertama kali
        //kita perlu cek value di sharedpreferences
        tokenpreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        String token = tokenpreferences.getString("token",null);
        //default token = null
        if (token==null){
            //masuk ke Login
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();

        }
        else{
            //masuk ke halaman dashboard
            startActivity(new Intent(MainActivity.this, OnBoardActivity.class));
            finish();
        }
    }
}