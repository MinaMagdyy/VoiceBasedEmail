package com.modern5.voicebasedemailv2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class display_msg extends AppCompatActivity {

    EditText from,titletv,message;

    String Email,password,title;
    String from_s ,title_s,message_s;

    TextToSpeech textToSpeech;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_msg);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(display_msg.this));
        }
        final Python py =  Python.getInstance();


        from = findViewById(R.id.emailrecieve);
        titletv = findViewById(R.id.subjectrecive);
        message = findViewById(R.id.messagerecive);


        Email = getIntent().getStringExtra("emailtodisplay");
        password = getIntent().getStringExtra("passtodisplay");
        title = getIntent().getStringExtra("title");

        System.out.println("email in display" + Email +" pass "+ password +" title "+ title);

        PyObject pyobj = py.getModule("funcion");
        PyObject fun_login_obj = pyobj.callAttr("emailed",Email,password,title);

        String data = fun_login_obj.toString();

        String[] parts = data.split("~");


        from.setText(parts[0]);
        from_s = parts[0];

        titletv.setText(title);
        title_s = title;

        message.setText(parts[1]);
        message_s = parts[1];


        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(display_msg.this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


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
                    text = text.replaceAll(" ", "");
                    text = text.toLowerCase();

                    System.out.println(text);
                    if (matches.get(0).equals("stop")){

                        textToSpeech.setLanguage(new Locale("en", "US"));
                        speak("i will stop speaking", true);


                    }else if(text.equals("exit")||text.equals("quit")){
                        speak("thank you for using voice based email app",true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after the delay

                                finish();

                            }
                        }, 3000); // Delay in milliseconds

                    }else if(text.equals("log out")){

                        Intent sendActivityIntent = new Intent(display_msg.this, activity_login.class);

                        startActivity(sendActivityIntent);
                        finish();
                    }else if (text.equals("goback") || text.equals("return")){

                        Intent sendActivityIntent = new Intent(display_msg.this, Emails.class);

                        sendActivityIntent.putExtra("emailtorecieve",Email);
                        sendActivityIntent.putExtra("passtorecieve",password);

                        textToSpeech.stop();

                        System.out.println("email back to  emails to display" + Email +" pass "+ password +" title "+ parts[0]);
                        startActivity(sendActivityIntent);
                        finish();
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




        LinearLayout parentLayout = findViewById(R.id.LinearlayoutE);



        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language
                    // Set the language to English and Arabic
                    textToSpeech.setLanguage(new Locale("en", "US"));

                    speak("Email from",true);
                    speak(from_s,true);

                    speak("with subject",true);
                    speak(title_s,true);

                    speak("contain",true);
                    speak(message_s,true);

                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    textToSpeech.speak("that's every thing i got, say go back to return and view different message"
                            , TextToSpeech.QUEUE_ADD, params);





                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished

                System.out.println("listening........");
                View v = findViewById(R.id.Erecivetv); //fetch a View: any one will do

                v.post(new Runnable() {
                    public void run() {
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    }
                });



            }
        };


        textToSpeech.setOnUtteranceCompletedListener(listener);



    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text, boolean add) {
        if(add) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }else if (!add){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent sendActivityIntent = new Intent(display_msg.this, Emails.class);

        sendActivityIntent.putExtra("emailtorecieve",Email);
        sendActivityIntent.putExtra("passtorecieve",password);

        textToSpeech.stop();

        System.out.println("email back to emails to display" + Email +" pass "+ password +" title "+ title);
        startActivity(sendActivityIntent);
        finish();

    }
}