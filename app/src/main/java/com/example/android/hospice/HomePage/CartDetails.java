package com.example.android.hospice.HomePage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.hospice.R;

public class CartDetails extends AppCompatActivity {
    public String orderName,emailId,addressOfUser,phoneNumber;
    OrderSummary orderSummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        Bitmap bitmap = MediStore.getBitmap();
        ImageView imageView = (ImageView) findViewById(R.id.imageSelected);
        Log.v("Image Path:",bitmap.toString());
        EditText name = (EditText) findViewById(R.id.name);
        EditText email = (EditText) findViewById(R.id.email);
        EditText address = (EditText) findViewById(R.id.address);
        EditText phone = (EditText) findViewById(R.id.phone);

        orderName = name.getText().toString();
        emailId = email.getText().toString();
        addressOfUser = address.getText().toString();
        phoneNumber = phone.getText().toString();

        orderSummary = new OrderSummary(orderName,emailId,addressOfUser,phoneNumber);
        imageView.setImageBitmap(bitmap);
    }

    public void onSubmitOrder(View v){
        Log.v("Summary: ",orderSummary.toString());
    }
}
