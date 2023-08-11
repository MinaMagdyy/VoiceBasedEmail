package com.modern5.voicebasedemailv2;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Asendofrecieve extends AppCompatActivity {

    Button send,recieve;

    TextToSpeech textToSpeech;
    final Handler handler = new Handler();

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asendofrecieve);
        send = findViewById(R.id.to_send_btn);
        recieve = findViewById(R.id.to_recieve_btn);



        String Email = getIntent().getStringExtra("email");
        String Password = getIntent().getStringExtra("pass");



        System.out.println("email " + Email +" pass "+ Password);


        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(Asendofrecieve.this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");


        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,2000);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,2000);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,1000);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {



            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

                System.out.println("error");




                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                textToSpeech.speak("", TextToSpeech.QUEUE_ADD, params);


            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    String text = matches.get(0);
                    System.out.println(matches.get(0));

                    if(text.equals("الوداع")||text.equals("خروج")||text.equals("الخروج")){

                        speak("شكرا لأستخدامك لتطيبق البريد الاكتروني الصوتي",true);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after the delay

                                finish();


                            }
                        }, 3000); // Delay in milliseconds
                    }
                    else if (text.equals("توقف")||text.equals("اصمت")||text.equals("اسكت")){

                        speak("سأتوقف عن الحديث", true);
                    }else if(text.equals("تسجيل خروج")){

                        Intent sendActivityIntent = new Intent(Asendofrecieve.this, loginArabic.class);

                        startActivity(sendActivityIntent);
                        finish();
                    }else if (matches.get(0).equals("ارسال") || matches.get(0).equals("أرسال")) {

                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.destroy();

                        Intent sendActivityIntent = new Intent(Asendofrecieve.this, Asend.class);

                        sendActivityIntent.putExtra("emailtosendscreen",Email);
                        sendActivityIntent.putExtra("passtosendscreen",Password);
                        System.out.println("email " + Email +" pass "+ Password);
                        startActivity(sendActivityIntent);
                        finish();


                    } else if (matches.get(0).equals("استقبال") || matches.get(0).equals("أستقبال") ) {
                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.destroy();
                        Intent sendActivityIntent = new Intent(Asendofrecieve.this, AEmails.class);

                        sendActivityIntent.putExtra("emailtorecieve",Email);
                        sendActivityIntent.putExtra("passtorecieve",Password);

                        startActivity(sendActivityIntent);
                        finish();


                    }else {




                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("عذرا, ما قلته لم يحدد اللغه", TextToSpeech.QUEUE_ADD, params);



                    }
                }



            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language
                    textToSpeech.setLanguage(new Locale("ar", "EG"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    textToSpeech.speak("مرحبا, من فضلك قل كلمة (ارسال) لأرسال رسائل البريد الاكتروني او كلمة (استقبال) لأستقبال الرسائل", TextToSpeech.QUEUE_ADD, params);




                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished
                if(counter <= 3) {
                    System.out.println("listening........");
                    View v = findViewById(R.id.aortv); //fetch a View: any one will do

                    v.post(new Runnable() {
                        public void run() {
                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        }
                    });

                }
                else if (counter == 4){

                    speak("شكرا لاستخدامك تطبيق البريد الاكتروني الصوتي",true);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Code to be executed after the delay

                            finish();

                        }
                    }, 5000); // Delay in milliseconds


                }


            }
        };

        textToSpeech.setOnUtteranceCompletedListener(listener);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToSpeech.stop();

                mSpeechRecognizer.stopListening();
                mSpeechRecognizer.destroy();
                Intent sendActivityIntent = new Intent(Asendofrecieve.this, Asend.class);

                sendActivityIntent.putExtra("emailtosendscreen",Email);
                sendActivityIntent.putExtra("passtosendscreen",Password);

                startActivity(sendActivityIntent);

                finish();

            }
        });

        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToSpeech.stop();

                mSpeechRecognizer.stopListening();
                mSpeechRecognizer.destroy();

                Intent sendActivityIntent = new Intent(Asendofrecieve.this, AEmails.class);

                sendActivityIntent.putExtra("emailtorecieve",Email);
                sendActivityIntent.putExtra("passtorecieve",Password);

                startActivity(sendActivityIntent);

                finish();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text, boolean add) {
        if(add == true) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }else if (add == false){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


}