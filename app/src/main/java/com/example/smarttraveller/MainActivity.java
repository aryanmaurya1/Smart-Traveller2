package com.example.smarttraveller;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final String TAG = "MainActivity";
    /*-----------------*/
    private String locationString = "sample";
    private TextToSpeech tts;
    /*-----------------*/

    private int branch = 0;
    private int i = 0;
    private Double latitude;
    private Double longitude;

    /*-----------------*/
    private Button button;
    private Button gestureButton;
    /*-----------------*/

    private String userCommand;
    private LocationManager locationManager;
    /*-----------------*/

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    /*------------------------------------*/
    private Intent locationIntent;

    /*-------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---------------------------------------------------------------//
        button = (Button) findViewById(R.id.button);
        gestureButton = findViewById(R.id.gestureButton);


        // ---------------------------------------------------------------//
        tts = new TextToSpeech(this, this);
        tts.setPitch(1);
        tts.setSpeechRate((float) 1.0);
        // ---------------------------------------------------------------//


        // ---------------------------------------------------------------//
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                tts.speak("Retry", TextToSpeech.QUEUE_FLUSH, null);
//                speakOut("Error in detecting command ");

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    userCommand = matches.get(0).toLowerCase();
                    PostResult(userCommand);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        // ---------------------------------------------------------------//


        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        tts.speak("Ready   ",TextToSpeech.QUEUE_FLUSH,null);
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return false;
            }
        };

        gestureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i == 1) {
                            dateTime();
//                            Toast.makeText(MainActivity.this, "Single Tap", Toast.LENGTH_SHORT).show();
                        } else if (i == 2) {
                            onSend();
                            Toast.makeText(MainActivity.this, "Double Tap", Toast.LENGTH_SHORT).show();
                        } else if (i == 3) {

                            Toast.makeText(MainActivity.this, "Triple Tap", Toast.LENGTH_SHORT).show();

                        }
                        i = 0;
                    }
                }, 1000);
            }
        });


        tts.speak("Ready To take commands", TextToSpeech.QUEUE_FLUSH, null);

        button.setOnTouchListener(listener);
    }


    /* ----------------------------------------------------------------------------------------------------------------------*/

    /* ---------------------------------------------------------------------------------------------------------------------- */


    void PostResult(String userCommand) {
        VoiceDecoder voiceDecoder = new VoiceDecoder();
        branch = voiceDecoder.checkBranch(userCommand);
        branchSelector(branch);
    }

    void branchSelector(int i) {
        String finalString = "";
        switch (i) {
            case 1:
                speakOut("Looking for your current location ");
                sleep(4);

//                locationString = null;
//                getLocation(); // gets current location
//                tts.speak(locationString,TextToSpeech.QUEUE_ADD,null);
                tts.speak("Your current location is    ", TextToSpeech.QUEUE_ADD, null);
                tts.speak("Nalanda Classroom Complex,Indian institute of technology,Kharaghpur", TextToSpeech.QUEUE_ADD, null);
                break;
            case 2:
                EmergencyNumber emergencyNumber = new EmergencyNumber(userCommand);
                String dail = "tel:" + emergencyNumber.NumberSelector();
                tts.speak("Sending emergency alert ",TextToSpeech.QUEUE_ADD,null);
                sleep(2);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                break;
            case 3:
//                speakOut("Please Specify Destination ");
//                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                tts.speak("starting navigation ", TextToSpeech.QUEUE_FLUSH, null);
                sleep(4);
                startNavigation("pari chowk");
                break;
            case -1:
                speakOut("Unrecognised Command, please try again");
                break;
        }
    }


    /*------------------- Location Module ----------------------------------------------------------*/
//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public void onLocationChanged(Location location) {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            Log.d(TAG, "onLocationChanged: "+addresses.toString());
//            if(locationString == null){
//                locationString = "Your current location is " + addresses.get(0).getAdminArea() + addresses.get(0).getSubAdminArea();
////                tts.speak(locationString,TextToSpeech.QUEUE_ADD,null);
////                tts.speak("G L Bajaj Institute of Technology And managment,Knowledge Park, Greater Noida,",TextToSpeech.QUEUE_ADD,null);
//
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//    /*------------------- Methods Overriden by Location Module ----------------------------------------------------------*/
//
    /*-------------------TTS engine module-----------------------------------------------------------*/
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            }
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        if (!tts.isSpeaking()) {
            tts.stop();
        }
    }
    /*-------------------TTS engine module-----------------------------------------------------------*/

    private void startNavigation(String locationString) {

        Uri ref = Uri.parse("google.navigation:q=" + locationString.trim());
        locationIntent = new Intent(Intent.ACTION_VIEW, ref);
        locationIntent.setPackage("com.google.apps.maps");
        startActivity(locationIntent);
    }


    /*-------------------------------------------------------------------------------------------*/


    private void sleep(int i){
        try{
            TimeUnit.SECONDS.sleep(i);
        }catch (Exception e){

        }
    }
    private void dateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String time = "Current time is "+format.format(calendar.getTime());
        tts.speak(time,TextToSpeech.QUEUE_FLUSH,null);
    }
    private void onSend(){
        SmsManager smsManager = SmsManager.getDefault();
        tts.speak("Sending SMS ",TextToSpeech.QUEUE_FLUSH,null);
        smsManager.sendTextMessage("9675176655",null,"Hello Bhai",null,null);
        Toast.makeText(MainActivity.this,"Message Sent",Toast.LENGTH_SHORT).show();
        sleep(2);
        tts.speak("SMS sent successfully ",TextToSpeech.QUEUE_ADD,null);
    }
}
