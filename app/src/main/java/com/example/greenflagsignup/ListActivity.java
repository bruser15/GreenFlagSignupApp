package com.example.greenflagsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        TextView list = findViewById(R.id.tv_email_list);

        Intent signup = getIntent();
        String pass = signup.getStringExtra("password");
        String email = signup.getStringExtra("email");


        File path = this.getFilesDir();
        File emails = new File(path, "emails.txt");
        File passwords = new File(path, "passwords.txt");

        int l = (int) emails.length();
        byte[] b = new byte[l];
        try{
            try (FileInputStream ins = new FileInputStream(emails)) {
                ins.read(b);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String emailList = new String(b);
        emailList = emailList + email + "\n";

        try{
            try (FileOutputStream stream = new FileOutputStream(emails); FileOutputStream pStream = new FileOutputStream(passwords)) {
                stream.write(emailList.getBytes());
                pStream.write(pass.getBytes());
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        list.setText(emailList);

    }
}