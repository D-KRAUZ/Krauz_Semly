package com.example.krauz_semly;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import javax.xml.transform.OutputKeys;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 1;

    EditText et1 = null;

    EditText et2 = null;
    ImageButton ib1 = null;
    ImageButton ib2 = null;
    ImageButton ib3 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et1= (EditText) findViewById(R.id.editTextTextPersonName);
        et2= (EditText) findViewById(R.id.editTextTextPersonName2);
        et2.setText("Hello World!");
        ib1 = (ImageButton) findViewById(R.id.imageButton3);
        ib2= (ImageButton) findViewById(R.id.imageButton4);
        ib3= (ImageButton) findViewById(R.id.imageButton5);

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in,RESULT_PICK_CONTACT);
                MainActivity.super.onActivityResult(1,2,in);
            }
        });
        ib2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, et2.getText());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);


            }
        });
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},
                                10);
                    }
                    sendSMS();
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;

            }
        } else {
            Toast.makeText(this, "Failed to pick contact", Toast.LENGTH_LONG).show();
        }

    }


    private void contactPicked(Intent data)
    {
        Cursor cursor = null;
        try
        {
            String phoneNo = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            et1.setText(phoneNo);
        }
        catch(Exception e)
        {
            e.printStackTrace();

       }
    }

    private void sendSMS()
    {
        String phoneNo = et1.getText().toString().trim();
        String SMS = et2.getText().toString().trim();

        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("3850955509539",null,SMS,null,null);
            Toast.makeText(this,phoneNo, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this,"message is sent.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}