package com.example.omistaja.aamukampa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.aamukampa.MESSAGE";

    private boolean notifications = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.savedDate);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defValue = "aamuja";
        String savedDate = sharedPref.getString(getString(R.string.saved_date_key), defValue);
        textView.setText(savedDate);


    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void toggleNotifications(View view){


        Button button = findViewById(R.id.button5);
        AlarmManager manager = this.getSystemService(AlarmManager.class);
        Intent intent = new Intent(this, DateReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(!notifications){
            button.setBackgroundColor(getResources().getColor(R.color.colorOn));
            manager.setRepeating(AlarmManager.RTC,5000,AlarmManager.INTERVAL_DAY,pIntent);
            notifications = true;
        }else{
            button.setBackgroundColor(getResources().getColor(R.color.colorOff));
            manager.cancel(pIntent);
            notifications = false;
        }


    }

}
