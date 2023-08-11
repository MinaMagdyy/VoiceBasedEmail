package com.modern5.voicebasedemailv2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;

public class language extends AppCompatActivity {


    Button Eng , Arab;

    TextToSpeech textToSpeech;

    int counter = 0;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);






        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(language.this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");


        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,5000);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,15000);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,5000);

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
                counter++;

                System.out.println("Error");

                textToSpeech.setLanguage(new Locale("ar", "EG"));
                speak("تحدث بوضوح من فضلك ؟", false);

                textToSpeech.setLanguage(new Locale("en", "US"));
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                textToSpeech.speak("can you speak clearly ?", TextToSpeech.QUEUE_ADD, params);


            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    System.out.println(matches.get(0));

                    if(matches.get(0).equals("exit")||matches.get(0).equals("quit")||matches.get(0).equals("خروج")){
                        speak("thank you for using voice based email app",true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after the delay

                                finish();

                            }
                        }, 3000); // Delay in milliseconds

                    }else if (matches.get(0).equals("stop")){

                        textToSpeech.setLanguage(new Locale("en", "US"));
                        speak("i will stop speaking", true);


                    }else if(matches.get(0).equals("توقف")){

                        textToSpeech.setLanguage(new Locale("ar", "EG"));
                        speak("سأتوقف عن الكلام", true);

                    }
                    else if (matches.get(0).equals("\u200Fenglish")
                            || matches.get(0).equals("English")
                            || matches.get(0).equals("انجليش")
                            || matches.get(0).equals("انجلش")
                            || matches.get(0).equals("الانجليزية")
                            || matches.get(0).equals("الانجليزيه")
                            || matches.get(0).equals("انجليزى")
                            || matches.get(0).equals("انجليزي")
                            || matches.get(0).equals("انجليزيه")) {
                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.destroy();

                        startActivity(new Intent(language.this, activity_login.class));
                        finish();


                    } else if (matches.get(0).equals("عربي")
                            || matches.get(0).equals("عربيتى")
                            || matches.get(0).equals("عربى")
                            || matches.get(0).equals("عربية")
                            || matches.get(0).equals("عربيه")
                            || matches.get(0).equals("العربيه")
                            || matches.get(0).equals("العربية")
                            || matches.get(0).equals("عربيتا")
                            || matches.get(0).equals("لغه عربية")
                            || matches.get(0).equals("لغه عربيه")) {

                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.destroy();

                        startActivity(new Intent(language.this, loginArabic.class));
                        finish();


                    }else {


                        speak("عذرا ما قلته لم يحدد اللغه",false);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("sorry what you said isn't a language", TextToSpeech.QUEUE_ADD, params);



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
                    textToSpeech.setLanguage(new Locale("ar", "EG"));


                    speak("مرحبا فى تطبيق البريد الالكتروني الصوتي",true);
                    speak("من فضلك قم بأستخدام البصمة للتأكد من هويتك",true);

                    textToSpeech.setLanguage(new Locale("en", "US"));

                    speak("Welcome in Voice based Email app",true);
                    speak("Please use your fingerprint to verify your identity",true);


                    BiometricManager biometricManager=BiometricManager.from(language.this);
                    switch(biometricManager.canAuthenticate())
                    {
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            Toast.makeText(getApplicationContext(),"Device does not have fingerprint",Toast.LENGTH_SHORT).show();
                            speak("Device does not have fingerprint",true);

                            textToSpeech.setLanguage(new Locale("ar", "EG"));

                            // Set the language
                            speak("للتحويل الى العربية قل كلمة العربية",true);


                            textToSpeech.setLanguage(new Locale("en", "US"));

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                            textToSpeech.speak("for english say English", TextToSpeech.QUEUE_ADD, params);


                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                            Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                            speak("Not Working",true);

                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                            Toast.makeText(getApplicationContext(),"Device doesnt have fingerprint",Toast.LENGTH_SHORT).show();
                            speak("Device doesnt have fingerprint",true);

                    }
                    Executor executor= ContextCompat.getMainExecutor(language.this);

                    biometricPrompt=new BiometricPrompt(language.this, executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                        }
                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result){
                            super.onAuthenticationSucceeded(result);
                            Toast.makeText(getApplicationContext(),"Finger Print Is Right",Toast.LENGTH_SHORT).show();
                            textToSpeech.stop();

                            textToSpeech.setLanguage(new Locale("ar", "EG"));

                            // Set the language
                            speak("للتحويل الى العربية قل كلمة العربية",true);


                            textToSpeech.setLanguage(new Locale("en", "US"));

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                            textToSpeech.speak("for english say English", TextToSpeech.QUEUE_ADD, params);


                        }
                        @Override
                        public void onAuthenticationFailed(){
                            super.onAuthenticationFailed();
                        }
                    });
                    promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Fingerprint").setDescription("use fingerprint to login")
                            .setDeviceCredentialAllowed(true).build();
                    biometricPrompt.authenticate(promptInfo);





                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished
                if(counter <= 3) {

                    System.out.println("listening........");
                    View v = findViewById(R.id.English); //fetch a View: any one will do

                    v.post(new Runnable() {
                        public void run() {

                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        }
                    });

                }
                else if (counter == 4){
                    textToSpeech.setLanguage(new Locale("ar ", "EG"));

                    speak("شكرا لاستخدامك تطبيق البريد الاكتروني الصوتي",true);

                    textToSpeech.setLanguage(new Locale("en", "US"));

                    speak("thank you for using voice based email app",true);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Code to be executed after the delay

                            finish();

                        }
                    }, 10000); // Delay in milliseconds


                }


            }
        };

        textToSpeech.setOnUtteranceCompletedListener(listener);

        Eng = findViewById(R.id.English);
        Arab = findViewById(R.id.Arabic);

        Eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToSpeech.stop();

                mSpeechRecognizer.stopListening();
                mSpeechRecognizer.destroy();
                startActivity(new Intent(language.this,activity_login.class));

            }
        });

        Arab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();

                mSpeechRecognizer.stopListening();
                mSpeechRecognizer.destroy();

                startActivity(new Intent(language.this,loginArabic.class));

            }
        });

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text, boolean add) {



        if(add) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,null);
        }else if (!add){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
    }




}