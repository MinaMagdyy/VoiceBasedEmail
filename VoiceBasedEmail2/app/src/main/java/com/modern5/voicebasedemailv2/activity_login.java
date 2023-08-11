package com.modern5.voicebasedemailv2;


import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class activity_login extends AppCompatActivity {


    TextToSpeech textToSpeech;

    final Handler handler = new Handler();

    Button log;

    EditText Email_ed,password_ed;

    int counter = 0 ;
    boolean is_Emailright = false;
    boolean is_passright = false;
    boolean is_itpassword = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(activity_login.this));
        }
        final Python py =  Python.getInstance();


        //edittexts
        Email_ed = findViewById(R.id.email);
        password_ed = findViewById(R.id.password);


        final SpeechRecognizer EloginSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity_login.this);
        final Intent EloginSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,10000);//saket bs
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,40000);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,20000); //sakeet kda 5las

        EloginSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
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



                if(i == SpeechRecognizer.ERROR_NO_MATCH) {
                    counter++;
                    System.out.println("counter "+counter);

                    EloginSpeechRecognizer.stopListening();
                    // Provide feedback to the user that their speech was not recognized
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                    textToSpeech.speak("Can you speak clearly ?", TextToSpeech.QUEUE_ADD, params);

                }

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
                    System.out.println(text);


                    if(text.equals("log out")||text.equals("exit")||text.equals("quit")){
                        speak("thank you for using voice based email app",true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after the delay

                                finish();

                            }
                        }, 3000); // Delay in milliseconds
                    }
                    else if (text.equals("stop")){
                        speak("i will go for a walk", true);
                    }

                    else if(text.equals("yes")||text.equals("confirm") && !Email_ed.getText().toString().isEmpty()){

                        if(!is_itpassword && !Email_ed.getText().toString().isEmpty()){
                            is_Emailright = true;
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("confirmed enter your password now", TextToSpeech.QUEUE_ADD, params);

                            is_itpassword = true;
                        }else if (Email_ed.getText().toString().isEmpty()){

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("please say or write your Email first", TextToSpeech.QUEUE_ADD, params);

                        }
                        else  if(is_itpassword && !password_ed.getText().toString().isEmpty()){

                            //HashMap<String, String> params = new HashMap<>();
                            //params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            //textToSpeech.speak("your password confirmed", TextToSpeech.QUEUE_ADD, params);

                            speak("your password confirmed",true);

                            is_passright = true;
                        }else if (password_ed.getText().toString().isEmpty()){

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("please say or write your password first", TextToSpeech.QUEUE_ADD, params);

                        }

                        if(is_passright){

                            //check login code

                            PyObject pyobj = py.getModule("funcion");
                            PyObject fun_login_obj = pyobj.callAttr("login",Email_ed.getText().toString(),password_ed.getText().toString());

                            if(fun_login_obj.toBoolean()){

                                EloginSpeechRecognizer.stopListening();
                                EloginSpeechRecognizer.destroy();
                                speak("proceed login ",true);
                                Intent sendActivityIntent = new Intent(activity_login.this, sendofrecieve.class);
                                sendActivityIntent.putExtra("email",Email_ed.getText().toString());
                                sendActivityIntent.putExtra("pass",password_ed.getText().toString());
                                startActivity(sendActivityIntent);
                                finish();

                            }else if(!fun_login_obj.toBoolean()) {

                                is_itpassword = false;
                                is_passright = false;
                                is_Emailright = false;


                                HashMap<String, String> params = new HashMap<>();
                                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                                textToSpeech.speak("your Email or password is wrong please repeat them again" , TextToSpeech.QUEUE_ADD, params);

                                System.out.println(fun_login_obj.toString());

                            }

                        }


                    }else if(text.equals("no")){

                        if(!is_itpassword) {

                            is_Emailright = false;
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("unconfirmed Email try again", TextToSpeech.QUEUE_ADD, params);

                        }else if(is_itpassword){

                            is_passright = false;
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("unconfirmed password try again", TextToSpeech.QUEUE_ADD, params);
                        }


                    }
                    else if(!is_Emailright) {



                        text = text.replace("underscore", "_");
                        text = text.replace(" ", "");
                        text = text.replace(",", "");
                        text = text.toLowerCase();



                        System.out.println(text);


                        speak("Your Email is ",true);


                        for(int i = 0 ; i < text.length();i++){
                            char ch = text.charAt(i);
                            String s = "" + ch;
                            speak(s,true);

                        }

                        if (!text.endsWith("@gmail.com")) {
                            text = text + "@gmail.com";

                        }
                        speak("@gmail.com",true);

                        Email_ed.setText(text);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("say yes to confirm or repeat the right email", TextToSpeech.QUEUE_ADD, params);


                    }else if(is_itpassword){



                        text = text.replace("underscore", "_");
                        text = text.replace(" ", "");
                        text = text.replace(",", "");

                        text = text.toLowerCase();

                        System.out.println(text);


                        password_ed.setText(text);

                        speak("make sure you wear a headphones",true);

                        speak("Your password is ",true);


                        for(int i = 0 ; i < text.length();i++){
                            char ch = text.charAt(i);
                            String s = "" + ch;
                            speak(s,true);

                        }


                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak( "say yes to confirm or repeat the right password", TextToSpeech.QUEUE_ADD, params);

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

                    textToSpeech.setLanguage(new Locale("en", "US"));



                        speak("Welcome in Login screen",true);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                        textToSpeech.speak("Please ,Say Your Email Address word by word without @gmail.com", TextToSpeech.QUEUE_ADD, params);






                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished

                if(counter <= 3) {

                System.out.println("listening........");
                View v = findViewById(R.id.login); //fetch a View: any one will do

                v.post(new Runnable() {
                    public void run() {

                        EloginSpeechRecognizer.startListening(EloginSpeechRecognizerIntent);

                    }
                });

                }
                else  if (counter == 4){

                    speak("thank you for using voice based email app",true);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Code to be executed after the delay

                            finish();

                        }
                    }, 3000); // Delay in milliseconds
                }





            }
        };


        textToSpeech.setOnUtteranceCompletedListener(listener);


        log = findViewById(R.id.login);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Email_ed.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "please fill the Email address", Toast.LENGTH_SHORT).show();
                }else if(password_ed.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "please fill the password address", Toast.LENGTH_SHORT).show();
                }else if (!Email_ed.getText().toString().isEmpty() &&!password_ed.getText().toString().isEmpty()) {


                    PyObject pyobj = py.getModule("funcion");
                    PyObject fun_login_obj = pyobj.callAttr("login", Email_ed.getText().toString(), password_ed.getText().toString());

                    boolean is_email_right = fun_login_obj.toBoolean();

                    if (is_email_right) {

                        textToSpeech.stop();

                        EloginSpeechRecognizer.stopListening();
                        EloginSpeechRecognizer.destroy();
                        speak("proceed login ", true);
                        Intent sendActivityIntent = new Intent(activity_login.this, sendofrecieve.class);
                        sendActivityIntent.putExtra("email", Email_ed.getText().toString());
                        sendActivityIntent.putExtra("pass", password_ed.getText().toString());

                        System.out.println("email in login screen " + Email_ed.getText().toString() + " pass " + password_ed.getText().toString());

                        startActivity(sendActivityIntent);
                        finish();

                    } else if (!is_email_right) {

                        speak("your Email or password is wrong please repeat them again", true);


                    }
                }
            }
        });

    }






    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text, boolean add) {
        if(add) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }else if (!add){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


}