package com.example.omistaja.aamukampa;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;

public class KampaActivity extends AppCompatActivity {

    private ConstraintLayout parentLayout;
    private int TJ;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefsEditor;

    private TextView text;
    private TextView piikkiText;

    private MediaPlayer player;

    private View scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampa);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPrefsEditor = sharedPref.edit();

        text = findViewById(R.id.textView5);
        piikkiText = findViewById(R.id.textView6);

        TjCalculator calc = new TjCalculator(this);
        try{
            int[] aamut = calc.calculateDifference();
            TextView TJText = findViewById(R.id.TJ_TEXT);
            TJ = aamut[0] + 1;
            TJText.setText(String.format("%d", TJ));
        }catch (ParseException e){
            System.out.println("invalid date at kampa");
        }
        //parentLinearLayout = findViewById(R.id.parent_linear_layout);
        parentLayout = findViewById(R.id.parent_constraint_layout);

        loadKampa();
        player = MediaPlayer.create(this,R.raw.comb);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    private void addPiikki(int i) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout p2 = (LinearLayout) inflater.inflate(R.layout.piikki,null);

        /*ImageView im = new AppCompatImageView(this);
        im.setImageDrawable(getDrawable(R.drawable.piikki_vector_anim));
        im.setId(View.generateViewId());
        */

        p2.setId(View.generateViewId());
        parentLayout.addView(p2,parentLayout.getChildCount() - 1);
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) p2.getLayoutParams();
        p.topToTop = parentLayout.getId();
        p.topMargin = 10 + 10 * i;
        p2.requestLayout();

        final View p3 =  p2.getChildAt(0);
        p3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    player.start();
                }
                else if(event.getAction() != MotionEvent.ACTION_DOWN){
                    player.pause();
                    player.seekTo(0);
                    if(event.getEventTime() - event.getDownTime() < 150){
                        startAnim(p3);
                    }

                }
                return true;
            }
        });

        //set.connect(im.getId(),ConstraintSet.TOP,parentLayout.getId(),ConstraintSet.TOP, 10 );

        //parentLinearLayout.addView(p2, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
            int piikit = parentLayout.getChildCount();
            parentLayout.removeView((LinearLayout)v.getParent());
            piikkiText.setText(String.format("%d",piikit - 1));
            sharedPrefsEditor.putInt(getString(R.string.saved_kampa_key),piikit - 1);
            sharedPrefsEditor.apply();



    }

    public void startAnim(final View v){
        if(parentLayout.getChildCount() > TJ){

            final MediaPlayer player = MediaPlayer.create(this,R.raw.snap);
            player.start();

            //LinearLayout l = (LinearLayout)parentLayout.getChildAt(parentLayout.getChildCount() - 1);
            //final ImageView im = (ImageView) l.getChildAt(0);
            AnimatedVectorDrawable d = (AnimatedVectorDrawable) ((ImageView) v).getDrawable();
            //AnimatedVectorDrawable anim = (AnimatedVectorDrawable)im.getBackground();
            d.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    onDelete(v);
                    player.release();
                }
            });
            d.start();

        }else{
            text.setVisibility(TextView.VISIBLE);
        }
    }

    public void loadKampa(){

        int defValue = TJ + 1;
        int piikit = sharedPref.getInt(getString(R.string.saved_kampa_key), defValue);
        createKampa(piikit);

    }


    private void createKampa(int piikit){

        parentLayout.removeAllViews();
        //parentLinearLayout.removeAllViews();

        text.setVisibility(TextView.INVISIBLE);

        for(int i = 0; i < piikit; i++){
            addPiikki(i);
        }

        piikkiText.setText(String.format("%d",parentLayout.getChildCount()));


        sharedPrefsEditor.putInt(getString(R.string.saved_kampa_key),parentLayout.getChildCount());
        sharedPrefsEditor.apply();

    }

    public void readVal(View view){

        EditText piikit = findViewById(R.id.editText2);
        int p = Integer.parseInt(piikit.getText().toString());
        createKampa(p);



    }
}
