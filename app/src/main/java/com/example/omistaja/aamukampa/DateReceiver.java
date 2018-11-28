package com.example.omistaja.aamukampa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.ParseException;

public class DateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("fired");

        TjCalculator calc =  new TjCalculator(context);

        int[] aamut = new int[3];
        try{
            aamut = calc.calculateDifference();
        }catch (ParseException e){
            System.out.println("invalid date");
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(context.getResources().getString(R.string.TJ))
                .setContentText(String.format("%d", aamut[0]  + 1))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, mBuilder.build());

    }
}
