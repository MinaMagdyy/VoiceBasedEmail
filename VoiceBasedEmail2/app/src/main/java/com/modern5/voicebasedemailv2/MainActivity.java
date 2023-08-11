package com.modern5.voicebasedemailv2;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static int splach = 7000;
    private ImageView logo;
    private View topView, topView1, topView2, topView3;
    private View buttomView1, buttomView2, buttomView3, buttomView;
    private int count = 0;
    TextView welcometo, voicebased;
    TextToSpeech tts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_main);

        welcometo = findViewById(R.id.welcometo);
        voicebased = findViewById(R.id.voicebased);
        logo = findViewById(R.id.logo);
        topView = findViewById(R.id.topView);
        topView1 = findViewById(R.id.topView1);
        topView2 = findViewById(R.id.topView2);
        topView3 = findViewById(R.id.topView3);
        buttomView = findViewById(R.id.buttomView);
        buttomView1 = findViewById(R.id.buttomView1);
        buttomView2 = findViewById(R.id.buttomView2);
        buttomView3 = findViewById(R.id.buttomView3);

        Animation logoAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoomanim);
        Animation welcometoAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoomanim);
        Animation top_view = AnimationUtils.loadAnimation(MainActivity.this, R.anim.topviewsanim);
        Animation top_view1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.topviewsanim);
        Animation top_view2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.topviewsanim);
        Animation top_view3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.topviewsanim);
        Animation buttom_view = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttomviewsanim);
        Animation buttom_view1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttomviewsanim);
        Animation buttom_view2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttomviewsanim);
        Animation buttom_view3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttomviewsanim);

        topView.startAnimation(top_view);
        buttomView.startAnimation(buttom_view);

        top_view.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView1.setVisibility(View.VISIBLE);
                buttomView1.setVisibility(View.VISIBLE);

                topView1.startAnimation(top_view1);
                buttomView1.startAnimation(buttom_view1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        top_view1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView2.setVisibility(View.VISIBLE);
                buttomView2.setVisibility(View.VISIBLE);

                topView2.startAnimation(top_view2);
                buttomView2.startAnimation(buttom_view2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        top_view2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView3.setVisibility(View.VISIBLE);
                buttomView3.setVisibility(View.VISIBLE);

                topView3.startAnimation(top_view3);
                buttomView3.startAnimation(buttom_view3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        top_view3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                welcometo.setVisibility(View.VISIBLE);
                welcometo.startAnimation(welcometoAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        welcometoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                voicebased.setVisibility(View.VISIBLE);
                final String animatetxt = voicebased.getText().toString();
                voicebased.setText("");
                count = 0;

                new CountDownTimer(animatetxt.length() * 100, 100) {

                    @Override
                    public void onTick(long l) {
                        voicebased.setText(voicebased.getText().toString() + animatetxt.charAt(count));
                        count++;

                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, language.class);
                startActivity(intent);
                finish();
            }
        }, splach);
    }

}