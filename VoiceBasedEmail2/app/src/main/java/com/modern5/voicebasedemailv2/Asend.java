package com.modern5.voicebasedemailv2;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Asend extends AppCompatActivity {

    EditText emaill;
    EditText subjectt;
    EditText message;
    Button buttonsnd;

    TextToSpeech textToSpeech;
    final Handler handler = new Handler();

    boolean is_Emailright = false;
    boolean is_subject = false;
    boolean is_itsubject = false;
    boolean is_messageready = false;
    boolean is_itsmessage = false;
    boolean is_readytorepeat = false;


    int silence_pause_time = 5000;
    int full_input_time = 5000;
    int silence_time_tofinish = 30000;

    int messageparts = 0;

    String sender;
    String password;

    String messagepart = "";

    String messagefinal = "";


    boolean is_arabic = true;
    boolean is_english = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asend);

        sender = getIntent().getStringExtra("emailtosendscreen");
        password = getIntent().getStringExtra("passtosendscreen");



        emaill = findViewById(R.id.emailrecieve);
        subjectt = findViewById(R.id.subjectrecive);
        message = findViewById(R.id.messagerecive);
        buttonsnd = findViewById(R.id.send_btn);

        buttonsnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emaill.getText().toString().isEmpty()){
                    Toast.makeText(Asend.this,"من فضلك قم بملىء البريد الالكتروني المراد الارسال له",Toast.LENGTH_SHORT).show();
                    speak("من فضلك قم بملىء البريد الالكتروني المراد الارسال له",true);
                }else if(subjectt.getText().toString().isEmpty()){
                    Toast.makeText(Asend.this,"من فضلك قم بملىء موضوع البريد الالكتروني",Toast.LENGTH_SHORT).show();
                    speak("من فضلك قم بملىء موضوع البريد الالكتروني",true);

                }else if(message.getText().toString().isEmpty()){
                    Toast.makeText(Asend.this,"من فضلك قم بملىء الرسالة",Toast.LENGTH_SHORT).show();
                    speak("من فضلك قم بملىء الرسالة",true);

                }else if(!emaill.getText().toString().isEmpty() && !subjectt.getText().toString().isEmpty() && !message.getText().toString().isEmpty() ){
                    send_email();
                }
            }
        });

        final SpeechRecognizer EloginSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(Asend.this);
        final Intent EloginSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,silence_time_tofinish);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,full_input_time);
        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, silence_pause_time);

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


                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                textToSpeech.speak("", TextToSpeech.QUEUE_ADD, params);


            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    String text = matches.get(0);
                    System.out.println(text);

                    if(is_readytorepeat){
                        if(text.equals("عيد الرساله")
                                || text.equals("عيد الرسالة")
                                || text.equals("اعد الرساله")
                                || text.equals("اعد الرسالة") ) {

                            messageparts = 0;
                            messagefinal = "";
                            messagepart = "";
                            text = "";
                            message.setText("جارى اعادة الرسالة");

                            is_itsmessage = true;
                            is_readytorepeat = false;

                            //EloginSpeechRecognizer.stopListening();
                            //speak("now repeat your message in 3 parts",true);
                            //EloginSpeechRecognizer.startListening(EloginSpeechRecognizerIntent);

                        }else {

                            EloginSpeechRecognizer.stopListening();

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("", TextToSpeech.QUEUE_ADD, params);

                        }
                    }

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

                        Intent sendActivityIntent = new Intent(Asend.this, loginArabic.class);

                        startActivity(sendActivityIntent);
                        finish();

                    }
                    else if(text.equals("استقبال") || text.equals("أستقبال")){

                        textToSpeech.stop();

                        EloginSpeechRecognizer.stopListening();
                        EloginSpeechRecognizer.destroy();

                        Intent sendActivityIntent = new Intent(Asend.this, Emails.class);

                        sendActivityIntent.putExtra("emailtorecieve", sender);
                        sendActivityIntent.putExtra("passtorecieve", password);

                        startActivity(sendActivityIntent);

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
                            || matches.get(0).equals("لغه عربيه")
                            || matches.get(0).equals("arabic")) {

                        is_arabic = true;
                        is_english = false;

                        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");

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

                        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("تحدث باللغة الانجليزية الان", TextToSpeech.QUEUE_ADD, params);
                    }

                    else if(text.equals("موافق")||text.equals("حسنا") || text.equals("نعم") ||text.equals("اوكي")  ||text.equals("تأكيد")||text.equals("تاكيد")||text.equals("yes")||text.equals("confirm") ){

                        if(!is_Emailright && !emaill.getText().toString().isEmpty()){
                            is_Emailright = true;
                            is_itsubject = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("تم التأكيد ادخل موضوع البريد الأن", TextToSpeech.QUEUE_ADD, params);

                        }else if (emaill.getText().toString().isEmpty()){
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("من فضلك ,اكتب او قل البريد الالكتروني المراد الارسال له اولا", TextToSpeech.QUEUE_ADD, params);

                        }
                        else  if(is_itsubject && !subjectt.getText().toString().isEmpty()){

                            is_itsubject=false;
                            is_itsmessage = true;
                            is_Emailright = true;

                            //silence_pause_time = 60000; //1 minute
                            //silence_time_tofinish = 60000; //1 minute
                            //full_input_time = 300000; // five minutes

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("تم التأكيد الان ادخل الرساله على ثلاثة اجزاء", TextToSpeech.QUEUE_ADD, params);

                            //speak("your subject confirmed",true);


                        }else if(subjectt.getText().toString().isEmpty()){

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("من فضلك ,اكتب او قل موضوع البريد اولا", TextToSpeech.QUEUE_ADD, params);

                        }

                        else if(is_itsmessage&& !message.getText().toString().isEmpty()){

                            messagefinal = messagefinal + " " + messagepart;

                            messageparts++;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("تم تأكيد هذا الجزء", TextToSpeech.QUEUE_ADD, params);

                            is_itsubject = false;
                            is_itsmessage = true;
                            is_Emailright = true;



                            if (messageparts == 3) {

                                System.out.println("your final message is " + messagefinal);

                                textToSpeech.setLanguage(new Locale("ar", "EG"));
                                speak("رسالتك النهائية هيا " , true);

                                if(is_arabic){
                                    textToSpeech.setLanguage(new Locale("ar", "EG"));
                                    speak(messagefinal,true);
                                }else if(is_english){
                                    textToSpeech.setLanguage(new Locale("en", "US"));
                                    speak(messagefinal,true);
                                }

                                message.setText(messagefinal);

                                textToSpeech.speak("قل موافق للتأكيد او قل اعد الرسالة للبدء من جديد", TextToSpeech.QUEUE_ADD, params);

                                is_itsmessage = false;
                                is_messageready = true;
                                is_readytorepeat = true;
                            }




                        }else if (is_messageready){
                            speak("لقد تم تأكيد الرساله", true);
                            speak("جارى الارسال الان", true);
                            send_email();

                        }
                        else if(message.getText().toString().isEmpty()){

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("من فضلك قل او اكتب الرسالة اولا", TextToSpeech.QUEUE_ADD, params);

                        }


                    }
                    else if(text.equals("لا")||text.equals("no")){

                        if(!is_Emailright) {

                            is_Emailright = false;
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("لم يتم تأكيد البريد الالكتروني المراد الارسال له حاول مرة اخري", TextToSpeech.QUEUE_ADD, params);
                            EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

                        }else if(is_itsubject){

                            is_subject = true;
                            is_Emailright = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("لم يتم تأكيد الموضوع حاول مرة اخرى", TextToSpeech.QUEUE_ADD, params);

                            if(is_arabic){
                                EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
                            }else if(is_english){
                                EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                            }


                        }else if (is_itsmessage){
                            is_subject = false;
                            is_Emailright = true;
                            is_itsmessage = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("لم يتم تأكيد الرسالة حاول مرة اخرى", TextToSpeech.QUEUE_ADD, params);

                            if(is_arabic){
                                EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
                            }else if(is_english){
                                EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                            }
                        }


                    }
                    else if(!is_Emailright) {



                        text = text.replace("underscore", "_");
                        text = text.replace(" ", "");
                        text = text.replace(",", "");
                        text = text.toLowerCase();



                        System.out.println(text);

                        textToSpeech.setLanguage(new Locale("ar ", "EG"));

                        speak("البريد المراد الارسال له هو ",true);

                        textToSpeech.setLanguage(new Locale("en ", "US"));

                        for(int i = 0 ; i < text.length();i++){
                            char ch = text.charAt(i);
                            String s = "" + ch;
                            speak(s,true);

                        }

                        if (!text.endsWith("@gmail.com")) {
                            text = text + "@gmail.com";

                        }
                        speak("@gmail.com",true);

                        emaill.setText(text);

                        textToSpeech.setLanguage(new Locale("ar ", "EG"));

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("قل كلمة نعم لتأكيد البريد الالكتروني او كلمة لا لأدخال البريد الصحيح", TextToSpeech.QUEUE_ADD, params);

                        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
                        is_arabic = true;

                    }else if(is_itsubject){


                        System.out.println(text);

                        text = text.replace("underscore", "_");
                        text = text.replace(",", "");


                        subjectt.setText(text);

                        textToSpeech.setLanguage(new Locale("ar ", "EG"));

                        speak("موضوع الرسالة هو ",true);

                        if(is_arabic){
                            textToSpeech.setLanguage(new Locale("ar", "EG"));
                            speak(text,true);

                        }else if(is_english){
                            textToSpeech.setLanguage(new Locale("en", "US"));
                            speak(text,true);
                        }

                        textToSpeech.setLanguage(new Locale("ar ", "EG"));
                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak( "قل نعم لتأكيد الموضوع او لأدخال الموضوع الصحيح", TextToSpeech.QUEUE_ADD, params);
                        EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
                        is_arabic = true;

                    }else if(is_itsmessage){

                        if(messageparts < 3) {



                            System.out.println(text);

                            text = text.replace("underscore", "_");
                            text = text.replace(",", "");


                            messagepart = text;


                            message.setText(messagepart);

                            textToSpeech.setLanguage(new Locale("ar ", "EG"));

                            speak("هذا الجزء من الرسالة هو ",true);

                            if(is_arabic){
                                textToSpeech.setLanguage(new Locale("ar", "EG"));
                                speak(messagepart,true);
                            }else if(is_english){
                                textToSpeech.setLanguage(new Locale("en", "US"));
                                speak(messagepart,true);
                            }

                            System.out.println(messageparts);

                            textToSpeech.setLanguage(new Locale("ar ", "EG"));

                            speak("هذا هو الجزء رقم" +(messageparts+1),true);
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak( "قل نعم لتأكيد هذا الجزء او لا لأدخال الجزء الصحيح من الرسالة" , TextToSpeech.QUEUE_ADD, params);
                            EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
                            is_arabic = true;



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


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language

                    textToSpeech.setLanguage(new Locale("ar ", "EG"));

                    speak("مرحبا فى صفحة ارسال البريد الالكتروني",true);

                    speak("من فضلك قم بأدخال البريد المراد الارسال له بدون",true);

                    textToSpeech.setLanguage(new Locale("en", "US"));

                    speak("@gmail.com",true);


                    textToSpeech.setLanguage(new Locale("ar ", "EG"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    textToSpeech.speak("فى النهاية", TextToSpeech.QUEUE_ADD, params);

                    EloginSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");






                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished


                System.out.println("listening........");
                View v = findViewById(R.id.Arecivetextv); //fetch a View: any one will do

                v.post(new Runnable() {
                    public void run() {

                        EloginSpeechRecognizer.startListening(EloginSpeechRecognizerIntent);

                    }
                });





            }
        };


        textToSpeech.setOnUtteranceCompletedListener(listener);




    }

    public void send_email(){
        try {
            //getting the username and password



            System.out.println("email in send screen " + sender +"pass "+ password);

            String host = "smtp.gmail.com";
            String to = emaill.getText().toString();
            String sub = subjectt.getText().toString();
            String txt = message.getText().toString();

            //declaring properies and putting host and port and enable the protocol to work
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.auth","true");

            //declaring the session and giving it the usernaem and password
            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender,password);
                }
            });

            //declaring the reciever of the message subject and the text to be send
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject(sub);
            mimeMessage.setText(txt);

            //sending the message
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        speak("تم الارسال بنجاح",true);


                    } catch (MessagingException e) {
                        System.out.println(e);
                        speak("مشكلة فى الارسال",true);


                    }
                }
            });

            thread.start();


        } catch (AddressException e) {
            Toast.makeText(Asend.this,"من فضلك ضع الأيميل المراد الأرسال له",Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(Asend.this,"من فضلك ضع موضوع الرسالة",Toast.LENGTH_SHORT).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text, boolean add) {
        if(add == true) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }else if (add == false){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
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