package com.assistant.authenticator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements RecognitionListener,TextToSpeech.OnInitListener {

    static LinearLayout mainbox;
    List<QandA> answers;
    DBase db;
    String user;
    ArrayList<Integer> rand = new ArrayList<Integer>();
    ArrayList<Integer> question_number = new ArrayList<Integer>();
    CallbackManager callbackManager;
    static TextView returnedText;
    static TextView outputText;
    int case_val = 100;
    TextToSpeech tts;
    Button login;
    ProgressBar pb;
    private ToggleButton btnSpeak;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo("com.assistant.authenticator", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }*/
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "user_friends", "public_profile", "user_about_me", "read_custom_friendlists", "user_education_history",
                        "user_posts", "user_likes", "user_location", "user_status", "user_hometown"));

        login = (Button) findViewById(R.id.login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        login.setVisibility(View.GONE);
                        //DynamicQuestions.Interests();
                        //DynamicQuestions.posts();
                        //DynamicQuestions.friends();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        db = new DBase(getApplicationContext());
        rand.addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19));
        question_number.addAll(Arrays.asList(0, 0, 1, 1));
        returnedText = (TextView) findViewById(R.id.txtSpeechInput);
        outputText = (TextView) findViewById(R.id.txtSpeechOutput);
        btnSpeak = (ToggleButton) findViewById(R.id.btnSpeaker);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        //pb.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.US);
        mainbox = (LinearLayout) findViewById(R.id.mainbox);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        btnSpeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    btnSpeak.setVisibility(View.INVISIBLE);
                    pb.setVisibility(View.VISIBLE);
                    speech.startListening(recognizerIntent);
                } else {
                    pb.setVisibility(View.INVISIBLE);
                    btnSpeak.setVisibility(View.VISIBLE);
                    speech.stopListening();
                }
            }
        });
        getActionBar().hide();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
        btnSpeak.setChecked(false);
    }

    @Override
    public void onError(int i) {
        String errorMessage = getErrorText(i);
        returnedText.setText(errorMessage);
        if (errorMessage.equals("No match")) {
            if (case_val <= 4) {
                tts.speak("Sorry! Din't hear that " + answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                returnedText.setText("Couldn't hear you");
                boolean speakingEnd = tts.isSpeaking();
                do {
                    speakingEnd = tts.isSpeaking();
                } while (speakingEnd);
            } else {
                tts.speak("Sorry! Din't hear that.", TextToSpeech.QUEUE_ADD, null);
                returnedText.setText("Couldn't hear you");
                boolean speakingEnd = tts.isSpeaking();
                do {
                    speakingEnd = tts.isSpeaking();
                } while (speakingEnd);
            }
            btnSpeak.setChecked(true);
        } else {
            btnSpeak.setChecked(false);
        }
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        returnedText.setText(matches.get(0));
        if(returnedText.getText().toString().equalsIgnoreCase("cancel"))
        {
            returnedText.setText("Hey There! What's Up?");
            btnSpeak.setChecked(false);

        }
        else if (returnedText.getText().toString().contains("authenti")) {
            user = null;
            case_val = 0;
            //Arraylist shuffling
            Collections.shuffle(question_number);
            Collections.shuffle(rand);
            String input[] = returnedText.getText().toString().split(" ");
            if (input.length != 1) {
                for (int i = 0; i < input.length; i++) {
                    if (db.checkUser(input[i].toLowerCase(Locale.ENGLISH))) {
                        user = input[i].toLowerCase(Locale.ENGLISH);
                        answers = db.getAllQandA(user);
                        outputText.setText(answers.get(rand.get(case_val++)).getQ());
                        tts.speak(answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                        boolean speakingEnd = tts.isSpeaking();
                        do {
                            speakingEnd = tts.isSpeaking();
                        } while (speakingEnd);
                        btnSpeak.setChecked(true);
                    }
                }
            }
            if (user == null) {
                outputText.setText("What is your Username?");
                tts.speak("What is your Username?", TextToSpeech.QUEUE_ADD, null);
                case_val = 90;
                boolean speakingEnd = tts.isSpeaking();
                do {
                    speakingEnd = tts.isSpeaking();
                } while (speakingEnd);
                btnSpeak.setChecked(true);
            }
        } else if (returnedText.getText().toString().contains("register")) {
            Intent reg = new Intent(this, Register.class);
            startActivity(reg);
        } else if (case_val == 100 && returnedText.getText().toString().contains("register") || case_val == 90) {
            answers = db.getAllQandA(matches.get(0).toLowerCase());
            if (answers.size() == 0) {
                tts.speak("Please Register first", TextToSpeech.QUEUE_ADD, null);
                startActivity(new Intent(getApplicationContext(), Register.class));
            } else {
                case_val = 0;
                outputText.setText(answers.get(rand.get(case_val++)).getQ());
                tts.speak(answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                boolean speakingEnd = tts.isSpeaking();
                do {
                    speakingEnd = tts.isSpeaking();
                } while (speakingEnd);
                btnSpeak.setChecked(true);
            }
        } else if (case_val <= 4) {
            switch (case_val) {
                case 0:
                case 1:
                case 2:
                case 3:
                    if (compareResult(matches, answers.get(rand.get(case_val)).getA())) {

                        if (question_number.get(case_val-1) == 0) {

                            outputText.setText(answers.get(rand.get(case_val++)).getQ());
                            tts.speak(answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                            boolean speakingEnd = tts.isSpeaking();
                            do {
                                speakingEnd = tts.isSpeaking();
                            } while (speakingEnd);
                            btnSpeak.setChecked(true);
                        }
                        else {

                        }
                    }
                    else {
                        tts.speak("Sorry you got it wrong!" + answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                        boolean speakingEnd = tts.isSpeaking();
                        do {
                            speakingEnd = tts.isSpeaking();
                        } while (speakingEnd);
                        btnSpeak.setChecked(true);
                    }
                    break;
                case 4:
                    if(question_number.get(case_val)==0) {
                        if (compareResult(matches, answers.get(rand.get(case_val)).getA())) {
                            tts.speak("Authentication Successful!", TextToSpeech.QUEUE_ADD, null);
                            startActivity(new Intent(getApplicationContext(), Homepage.class));
                        } else {
                            tts.speak("Sorry you got it wrong!" + answers.get(rand.get(case_val - 1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                            boolean speakingEnd = tts.isSpeaking();
                            do {
                                speakingEnd = tts.isSpeaking();
                            } while (speakingEnd);
                            btnSpeak.setChecked(true);
                        }
                    }
                    else
                    {

                    }
                    break;

            }
        } else {
            returnedText.setText(matches.get(0));
        }
            /*if(case_val<4){
                if (compareResult(matches,answers.get(rand.get(case_val-1)).getA())) {
                    outputText.setText(answers.get(rand.get(case_val++)).getQ());
                    tts.speak(answers.get(rand.get(case_val-1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                    boolean speakingEnd = tts.isSpeaking();
                    do {
                        speakingEnd = tts.isSpeaking();
                    } while (speakingEnd);
                    btnSpeak.setChecked(true);
                }
                else {
                    tts.speak("Sorry you got it wrong!" + answers.get(rand.get(case_val-1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                    boolean speakingEnd = tts.isSpeaking();
                    do {
                        speakingEnd = tts.isSpeaking();
                    } while (speakingEnd);
                    btnSpeak.setChecked(true);
                }
            }
            else if(case_val==4){
                if (compareResult(matches,answers.get(rand.get(case_val-1)).getA())) {
                    tts.speak("Authentication Successful!", TextToSpeech.QUEUE_ADD, null);
                    startActivity(new Intent(getApplicationContext(), Homepage.class));
                }
                else {
                    tts.speak("Sorry you got it wrong!" + answers.get(rand.get(case_val-1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                    boolean speakingEnd = tts.isSpeaking();
                    do {
                        speakingEnd = tts.isSpeaking();
                    } while (speakingEnd);
                    btnSpeak.setChecked(true);
                }

            }
            else if(case_val==100 && returnedText.getText().toString().contains("register")|| case_val==90){
                answers = db.getAllQandA(matches.get(0).toLowerCase());
                if (answers.size() == 0) {
                    tts.speak("Please Register first", TextToSpeech.QUEUE_ADD, null);
                    startActivity(new Intent(getApplicationContext(), Register.class));
                } else {
                    case_val=0;
                    outputText.setText(answers.get(rand.get(case_val++)).getQ());
                    tts.speak(answers.get(rand.get(case_val-1)).getQ(), TextToSpeech.QUEUE_ADD, null);
                    boolean speakingEnd = tts.isSpeaking();
                    do {
                        speakingEnd = tts.isSpeaking();
                    } while (speakingEnd);
                    btnSpeak.setChecked(true);
                }
            }
            else {
                returnedText.setText(matches.get(0));
            }*/
    }


    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        returnedText.setText(matches.get(0));

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void onInit(int i) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    boolean compareResult(ArrayList <String> res,String ans){
        for(String temp:res){
            if (temp.equalsIgnoreCase(ans)) {
                returnedText.setText(ans);
                return true;
            }
        }
        return false;
    }

    public void getNewQuestion() {
        DynamicQuestions.friends();
        outputText.setText("Whom do you recognise?");
        tts.speak("Whom do you recognise?", TextToSpeech.QUEUE_ADD, null);
        boolean speakingEnd = tts.isSpeaking();
        do {
            speakingEnd = tts.isSpeaking();
        } while (speakingEnd);
        btnSpeak.setChecked(true);

    }
}


