package com.example.greenflagsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Navigate to sign up page
        Button createAccount = findViewById(R.id.btn_create_account);
        createAccount.setOnClickListener(view -> {
            Intent create = new Intent();
            create.setClass(MainActivity.this, SignupActivity.class);
            startActivity(create);
        });
    }
}