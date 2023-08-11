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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AEmails extends AppCompatActivity {
    TextToSpeech textToSpeech;
    List<PyObject> javaList;

    boolean is_title_notfound = false;
    final Handler handler = new Handler();

    boolean is_arabic = true;
    boolean is_english = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aemails);


        String Email = getIntent().getStringExtra("emailtorecieve");
        String password = getIntent().getStringExtra("passtorecieve");


        System.out.println("email in emails screen " + Email +" pass "+ password);


        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(AEmails.this));
        }
        final Python py =  Python.getInstance();




        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(AEmails.this);
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
                    text = text.replaceAll(" ", "");
                    System.out.println(text);

                    if(text.equals("الوداع")||text.equals("خروج")||text.equals("الخروج")){

                        speak("شكرا لأستخدامك لتطيبق البريد الالكتروني الصوتي",true);

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
                    }else if(text.equals("تسجيل خروج")||text.equals("تسجيل الخروج")){

                        Intent sendActivityIntent = new Intent(AEmails.this, loginArabic.class);

                        startActivity(sendActivityIntent);
                        finish();

                    }else if(text.equals("ارسال") || text.equals("أرسال")|| text.equals("send")){

                        textToSpeech.stop();

                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.destroy();
                        Intent sendActivityIntent = new Intent(AEmails.this, Asend.class);

                        sendActivityIntent.putExtra("emailtosendscreen",Email);
                        sendActivityIntent.putExtra("passtosendscreen",password);

                        startActivity(sendActivityIntent);

                        finish();

                    }
                    else if (matches.get(0).equals("عربي")
                            || matches.get(0).equals("عربيتى")
                            || matches.get(0).equals("عربى")
                            || matches.get(0).equals("عربية")
                            || matches.get(0).equals("عربيه")
                            || matches.get(0).equals("العربيه")
                            || matches.get(0).equals("العربية")
                            || matches.get(0).equals("عربيتا")
                            || matches.get(0).equals("لغه عربية")
                            || matches.get(0).equals("لغه عربيه")
                            || matches.get(0).equals("arabic")) {

                        is_arabic = true;
                        is_english = false;

                        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("تحدث باللغة العربية الان", TextToSpeech.QUEUE_ADD, params);

                    } else if (matches.get(0).equals("\u200Fenglish")
                            || matches.get(0).equals("English")
                            || matches.get(0).equals("انجليش")
                            || matches.get(0).equals("انجلش")
                            || matches.get(0).equals("الانجليزية")
                            || matches.get(0).equals("الانجليزيه")
                            || matches.get(0).equals("انجليزى")
                            || matches.get(0).equals("انجليزي")
                            || matches.get(0).equals("انجليزيه")) {

                        is_english = true;
                        is_arabic = false;

                        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("تحدث باللغة الانجليزية الان", TextToSpeech.QUEUE_ADD, params);

                    }else {
                        for (PyObject element : javaList) {

                            String data = element.toString();

                            String[] parts = data.split("~");

                            String titlepy = parts[1].replaceAll(" ", "");
                            titlepy = titlepy.replace("أ", "ا");
                            titlepy = titlepy.toLowerCase();
                            text = text.toLowerCase();
                            if (text.equals(titlepy)) {

                                is_title_notfound = false;

                                mSpeechRecognizer.stopListening();
                                mSpeechRecognizer.destroy();


                                Intent sendActivityIntent = new Intent(AEmails.this, Adisplay_msg.class);

                                is_arabic = false;
                                is_english = false;

                                sendActivityIntent.putExtra("emailtodisplay", Email);
                                sendActivityIntent.putExtra("passtodisplay", password);
                                sendActivityIntent.putExtra("title", parts[1]);

                                System.out.println("email in emails to display" + Email + " pass " + password + " title " + parts[0]);
                                startActivity(sendActivityIntent);
                                finish();
                                break;

                            } else if (!text.equals(parts[1])) {

                                is_title_notfound = true;

                            }
                        }
                        if(is_title_notfound){

                            textToSpeech.setLanguage(new Locale("ar", "EG"));

                            speak("عذرا لا استطيع ايجاد",true);

                            if(is_arabic){
                                textToSpeech.setLanguage(new Locale("ar", "EG"));
                                speak(text,true);
                            }else if(is_english){
                                textToSpeech.setLanguage(new Locale("en", "US"));
                                speak(text,true);
                            }



                            textToSpeech.setLanguage(new Locale("ar", "EG"));

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak( "حاول مرة اخرى", TextToSpeech.QUEUE_ADD, params);
                            is_arabic = false;
                            is_english = false;
                        }

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




        LinearLayout parentLayout = findViewById(R.id.LinearlayoutA);



        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language
                    textToSpeech.setLanguage(new Locale("ar", "EG"));

                    speak("مرحبا ",true);




                    PyObject pyobj = py.getModule("funcion");
                    PyObject fun_login_obj = pyobj.callAttr("get_Names",Email,password);


                    javaList = fun_login_obj.asList();


                    // iterate over the list and print each element
                    for (PyObject element : javaList) {

                        String data = element.toString();

                        String[] parts = data.split("~");

                        textToSpeech.setLanguage(new Locale("ar", "EG"));

                        speak("بريد من" ,true);

                        textToSpeech.setLanguage(new Locale("en", "US"));
                        speak(parts[0],true);

                        textToSpeech.setLanguage(new Locale("ar", "EG"));

                        speak("عنوانه " ,true);

                        textToSpeech.setLanguage(new Locale("en", "US"));

                        if(isArabic(parts[1])){
                            textToSpeech.setLanguage(new Locale("ar"));
                            speak(parts[1] ,true);
                            textToSpeech.setLanguage(new Locale("en"));

                        }else if (isEnglish(parts[1])) {
                            textToSpeech.setLanguage(new Locale("en"));

                            speak(parts[1] ,true);

                        }else {
                            textToSpeech.setLanguage(new Locale("en"));

                        }


                        System.out.println(parts[1].replace("أ","ا") );
                        System.out.println("java list " + element +"\n");


                        TextView emailTextView = new TextView(AEmails.this);
                        emailTextView.setText(data.replace("~","\n"));
                        emailTextView.setTextColor(Color.parseColor("#F44336"));
                        emailTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                        emailTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        emailTextView.setGravity(Gravity.CENTER);

                        emailTextView.setBackgroundResource(R.drawable.seeeeeeeeeeeebtn);

                        // Set the layout parameters for the TextView
                        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                dpToPx(350),
                                dpToPx(100)
                        );

                        textViewParams.gravity = Gravity.CENTER;

                        textViewParams.topMargin = 30;


                        // Add the TextView to the LinearLayout
                        emailTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mSpeechRecognizer.stopListening();
                                mSpeechRecognizer.destroy();

                                Intent sendActivityIntent = new Intent(AEmails.this, Adisplay_msg.class);

                                sendActivityIntent.putExtra("emailtodisplay",Email);
                                sendActivityIntent.putExtra("passtodisplay",password);
                                sendActivityIntent.putExtra("title",parts[1]);

                                textToSpeech.stop();

                                System.out.println("email in emails to display" + Email +" pass "+ password +" title "+ parts[0]);
                                startActivity(sendActivityIntent);


                            }
                        });


                        emailTextView.setLayoutParams(textViewParams);

                        parentLayout.addView(emailTextView);

                    }



                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    textToSpeech.speak("هذا كل ما لدي قل عنوان البريد لعرضه بالكامل "+
                            "ايضا قل كلمة عربي لعرض الرسائل العربية"+
                            "او كلمة انجليزي لعرض الرسائل الانجليزية", TextToSpeech.QUEUE_ADD, params);





                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished


                System.out.println("listening........");
                View v = findViewById(R.id.LinearlayoutA); //fetch a View: any one will do

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


    // Convert dp to pixels
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public static boolean isArabic(String s) {
        char c = s.charAt(0);
        if (c < 0x0600 || c > 0x06FF) {
            System.out.println("false");
            return false;
        }else {
            System.out.println("true");

            return true;

        }

    }

    public static boolean isEnglish(String s) {
        char c = s.charAt(0);
        if (c < 'A' || c > 'Z' && c < 'a' || c > 'z') {
            System.out.println("false");

            return false;
        } else {
            System.out.println("true");

            return true;

        }

    }
}