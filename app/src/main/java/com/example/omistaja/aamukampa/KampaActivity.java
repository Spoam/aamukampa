package com.example.omistaja.aamukampa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;

public class KampaActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private int TJ;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefsEditor;

    private TextView text;
    private TextView piikkiText;

    private Animation.AnimationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampa);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPrefsEditor = sharedPref.edit();

        text = findViewById(R.id.textView5);
        piikkiText = findViewById(R.id.textView6);

        listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("animn ended");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        TjCalculator calc = new TjCalculator(this);
        try{
            int[] aamut = calc.calculateDifference();
            TextView TJText = findViewById(R.id.TJ_TEXT);
            TJ = aamut[0] + 1;
            TJText.setText(String.format("%d", TJ));
        }catch (ParseException e){
            System.out.println("invalid date at kampa");
        }
        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        loadKampa();


    }

    private void addPiikki(int i) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View p2 = inflater.inflate(R.layout.piikki,null);
        parentLinearLayout.addView(p2, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        int piikit = parentLinearLayout.getChildCount();
        if(piikit > TJ){
            parentLinearLayout.removeView((View) v.getParent());
            piikkiText.setText(String.format("%d",piikit - 1));
            sharedPrefsEditor.putInt(getString(R.string.saved_kampa_key),piikit - 1);
            sharedPrefsEditor.apply();
        }else{
            text.setVisibility(TextView.VISIBLE);
        }


    }

    public void startAnim(View v){
        LinearLayout l = (LinearLayout)parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1);
        final ImageView im = (ImageView) l.getChildAt(0);
       AnimatedVectorDrawable d = (AnimatedVectorDrawable) im.getDrawable();
        //AnimatedVectorDrawable anim = (AnimatedVectorDrawable)im.getBackground();
        d.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                onDelete(im);
            }
        });
        d.start();
    }

    public void loadKampa(){

        int defValue = TJ + 1;
        int piikit = sharedPref.getInt(getString(R.string.saved_kampa_key), defValue);
        createKampa(piikit);

    }


    private void createKampa(int piikit){


        parentLinearLayout.removeAllViews();

        text.setVisibility(TextView.INVISIBLE);

        for(int i = 0; i < piikit; i++){
            addPiikki(i);
        }

        piikkiText.setText(String.format("%d",parentLinearLayout.getChildCount()));


        sharedPrefsEditor.putInt(getString(R.string.saved_kampa_key),parentLinearLayout.getChildCount());
        sharedPrefsEditor.apply();

    }

    public void readVal(View view){

        EditText piikit = findViewById(R.id.editText2);
        int p = Integer.parseInt(piikit.getText().toString());
        createKampa(p);



    }
}
