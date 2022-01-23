package com.example.greenflagsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
            FileInputStream ins = new FileInputStream(emails);
            try{
                ins.read(b);

            } finally {
                ins.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String emailList = new String(b);
        StringBuilder builder = new StringBuilder();
        builder.append(emailList).append(email).append("\n");
        emailList = builder.toString();

        try{
            FileOutputStream stream = new FileOutputStream(emails);
            FileOutputStream pStream = new FileOutputStream(passwords);
            try{
                stream.write(emailList.getBytes());
                pStream.write(pass.getBytes());
            }
            finally {
                stream.close();
                pStream.close();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        list.setText(emailList);

    }
}