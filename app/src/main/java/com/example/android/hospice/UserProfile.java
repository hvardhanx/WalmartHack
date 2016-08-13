package com.example.android.hospice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class UserProfile extends AppCompatActivity {
    public String personName;
    public String personEmail;
    public String personId;
    public String personPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Intent intent = getIntent();
        personName = intent.getExtras().getString("personName");
        personEmail = intent.getExtras().getString("personEmail");
        personId = intent.getExtras().getString("personId");
        personPhoto = intent.getExtras().getString("personPhoto");


        TextView userName = (TextView) findViewById(R.id.user_name);

        TextView userEmail = (TextView) findViewById(R.id.user_email);
        ImageView userImage = (ImageView) findViewById(R.id.user_image);

        userName.setText(personName);
        userEmail.setError(personEmail);
    }
}
