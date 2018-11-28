package com.example.omistaja.aamukampa;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class TjCalculator {

    public String date;
    private Context context;

    public TjCalculator(String input, Context context){

        date = input;
        this.context = context;
        saveDate();

    }

    public TjCalculator(Context context){
        this.context = context;
        date = loadDate();
    }

    private String loadDate() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defValue = "aamuja";
        String savedDate = sharedPref.getString(context.getString(R.string.saved_date_key), defValue);

        return savedDate;

    }

    public void saveDate() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.saved_date_key), date);
        editor.apply();
    }

    public int[] calculateDifference() throws ParseException

    {
        int[] aamut = new int[3];
        Date today = new Date();
        //System.out.println(today.toString());
        Date input = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        input.setTime(input.getTime());

        System.out.println(input.getTime());
        System.out.println(today.getTime());

        //muutetaan millisekunnit päiviksi tunneiksi ja minuuteiksi
        long deltaTime = input.getTime() - today.getTime() ;
        long seconds = deltaTime / 1000;
        long minutes = seconds / 60;

        long remaining = minutes;

        int daysTJ = (int)minutes / 60 / 24;

        remaining = remaining - daysTJ  * 60 * 24;

        int hoursTJ = (int)remaining / 60;

        remaining = remaining - hoursTJ * 60;

        int minutesTJ = (int)remaining;

        aamut[0] = daysTJ;
        aamut[1] = hoursTJ;
        aamut[2] = minutesTJ;

        return aamut;

    }

}
