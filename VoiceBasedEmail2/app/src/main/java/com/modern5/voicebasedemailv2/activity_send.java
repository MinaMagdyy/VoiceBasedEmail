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

public class activity_send extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);


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
                    Toast.makeText(activity_send.this,"Please fill the Email that you want to send to",Toast.LENGTH_SHORT).show();
                    speak("Please fill the Email that you want to send to" ,true);
                }else if(subjectt.getText().toString().isEmpty()){
                    Toast.makeText(activity_send.this,"Please fill the subject of the Email",Toast.LENGTH_SHORT).show();
                    speak("Please fill the subject of the Email" ,true);

                }else if(message.getText().toString().isEmpty()){
                    Toast.makeText(activity_send.this,"Please fill the message of the Email",Toast.LENGTH_SHORT).show();
                    speak("Please fill the message of the Email" ,true);

                }else if(!emaill.getText().toString().isEmpty() && !subjectt.getText().toString().isEmpty() && !message.getText().toString().isEmpty()){                    send_email();
                }
            }
        });

        final SpeechRecognizer EloginSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity_send.this);
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

                if(i == SpeechRecognizer.ERROR_NO_MATCH) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                    textToSpeech.speak("", TextToSpeech.QUEUE_ADD, params);
                }

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
                         if(text.equals("repeat the message")){

                            messageparts = 0;
                            messagefinal = "";
                            messagepart = "";
                            text = "";
                             message.setText("repeating the message");

                             is_itsmessage = true;
                             is_readytorepeat = false;

                             //EloginSpeechRecognizer.stopListening();
                            //speak("now repeat your message in 3 parts",true);
                            //EloginSpeechRecognizer.startListening(EloginSpeechRecognizerIntent);

                        }else if(!text.equals("repeat the message")){

                             EloginSpeechRecognizer.stopListening();

                             HashMap<String, String> params = new HashMap<>();
                             params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                             textToSpeech.speak("", TextToSpeech.QUEUE_ADD, params);

                         }
                    }
                    if(text.equals("exit")||text.equals("quit")){
                        speak("thank you for using voice based email app",true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after the delay

                                finish();

                            }
                        }, 3000); // Delay in milliseconds

                    }else if(text.equals("log out")){

                        Intent sendActivityIntent = new Intent(activity_send.this, activity_login.class);

                        startActivity(sendActivityIntent);
                        finish();
                    }
                    else if (text.equals("stop")){
                        speak("i will go for a walk", true);
                    }else if(text.equals("receive")){

                        textToSpeech.stop();

                        EloginSpeechRecognizer.stopListening();
                        EloginSpeechRecognizer.destroy();

                        Intent sendActivityIntent = new Intent(activity_send.this, Emails.class);

                        sendActivityIntent.putExtra("emailtorecieve", sender);
                        sendActivityIntent.putExtra("passtorecieve", password);

                        startActivity(sendActivityIntent);

                        finish();

                    }
                    else if(text.equals("yes")||text.equals("confirm")){

                        if(!is_Emailright && !emaill.getText().toString().isEmpty()){
                            is_Emailright = true;
                            is_itsubject = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("confirmed enter your subject now", TextToSpeech.QUEUE_ADD, params);

                        }else if (emaill.getText().toString().isEmpty()){
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("please say or write your Email", TextToSpeech.QUEUE_ADD, params);

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
                            textToSpeech.speak("confirmed enter your message now in 3 parts", TextToSpeech.QUEUE_ADD, params);

                            //speak("your subject confirmed",true);


                        }else if(subjectt.getText().toString().isEmpty()){
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("please say or write your subject", TextToSpeech.QUEUE_ADD, params);

                        }

                        else if(is_itsmessage&& !message.getText().toString().isEmpty()){

                            messagefinal = messagefinal + " " + messagepart;

                            messageparts++;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("this part is confirmed", TextToSpeech.QUEUE_ADD, params);

                                is_itsubject = false;
                                is_itsmessage = true;
                                is_Emailright = true;



                                if (messageparts == 3) {

                                    System.out.println("your final message is " + messagefinal);

                                    speak("your final message is " + messagefinal, true);

                                    message.setText(messagefinal);

                                    textToSpeech.speak("say yes to confirm or say repeat the message to start from the beginning", TextToSpeech.QUEUE_ADD, params);

                                    is_itsmessage = false;
                                    is_messageready = true;
                                    is_readytorepeat = true;
                                }




                        }else if (is_messageready){
                            speak("your message confirmed", true);
                            speak("SENDING NOW", true);
                            send_email();

                        }
                        else if(message.getText().toString().isEmpty()){

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("please say or write your message first", TextToSpeech.QUEUE_ADD, params);

                        }


                    }
                    else if(text.equals("no")){

                        if(!is_Emailright) {

                            is_Emailright = false;
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("unconfirmed Email try again", TextToSpeech.QUEUE_ADD, params);

                        }else if(is_itsubject){

                            is_subject = true;
                            is_Emailright = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("unconfirmed subject try again", TextToSpeech.QUEUE_ADD, params);
                        }else if (is_itsmessage){
                            is_subject = false;
                            is_Emailright = true;
                            is_itsmessage = true;

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak("unconfirmed message try again", TextToSpeech.QUEUE_ADD, params);
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

                        emaill.setText(text);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("say yes to confirm or repeat the right email", TextToSpeech.QUEUE_ADD, params);


                    }else if(is_itsubject){


                        System.out.println(text);

                        text = text.replace("underscore", "_");
                        text = text.replace(",", "");


                        subjectt.setText(text);


                        HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                        textToSpeech.speak("Your subject is " + text + "say yes to confirm or repeat the right subject", TextToSpeech.QUEUE_ADD, params);

                    }else if(is_itsmessage){

                        if(messageparts < 3) {



                            System.out.println(text);

                            text = text.replace("underscore", "_");
                            text = text.replace(",", "");


                            messagepart = text;


                            message.setText(messagepart);

                            speak("this part of message is " + messagepart,true);
                            System.out.println(messageparts);
                            
                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                            textToSpeech.speak( "this is the part number " +(messageparts+1)+" say yes to confirm or repeat the right part of message" , TextToSpeech.QUEUE_ADD, params);



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

                    speak("Welcome in sending screen",true);

                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    textToSpeech.speak("Please ,Say the Email you want to send to word by word without @gmail.com", TextToSpeech.QUEUE_ADD, params);





                }
            }


        });

        TextToSpeech.OnUtteranceCompletedListener listener = new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                // Speech finished


                System.out.println("listening........");
                View v = findViewById(R.id.send_btn); //fetch a View: any one will do

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
                        speak("successfully sent",true);


                    } catch (MessagingException e) {
                        System.out.println(e);
                        speak("error sending",true);


                    }
                }
            });

            thread.start();


        } catch (AddressException e) {
            Toast.makeText(activity_send.this,"please put the sending email",Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(activity_send.this,"Please fill the body of the message",Toast.LENGTH_SHORT).show();
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


}