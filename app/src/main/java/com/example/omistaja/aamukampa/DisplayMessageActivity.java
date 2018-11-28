package com.example.omistaja.aamukampa;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;


public class DisplayMessageActivity extends AppCompatActivity {

    private int[] aamut = new int[3];
    private String input;
    private TjCalculator calc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        input = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(!input.isEmpty()){
            calc = new TjCalculator(input, getApplicationContext());
        }else{
            calc = new TjCalculator(getApplicationContext());
        }

        TextView textView = findViewById(R.id.textView);


        try{
             aamut = calc.calculateDifference();
             textView.setText(String.format("%d päivää %d tuntia %d minuuttia", aamut[0], aamut[1], aamut[2]));

        }catch(java.text.ParseException e){
            textView.setText("Kirjota se päivämäärä oikein.\n(pp.kk.vvvv)\nEsim. 20.12.2018)");

        }


    }



    public void backButton(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    public void sendNotification(View view){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getResources().getString(R.string.TJ))
                .setContentText(String.format("%d", aamut[0] + 1))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }

    public void gotoKampa(View view){
        Intent intent = new Intent(this, KampaActivity.class);
        startActivity(intent);

    }

}
