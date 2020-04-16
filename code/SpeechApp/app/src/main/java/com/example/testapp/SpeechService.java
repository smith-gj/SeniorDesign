package com.example.testapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.Locale;

import androidx.annotation.Nullable;

public class SpeechService extends Service {

    private String text = "hello";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        SpeechRecognizer speechRecognizer;
        Intent startIntent = new Intent("start");
        intent.putExtra("Start","start");
        for(int i = 0; i < 10; i++){
            if(i % 2 == 0){
                System.out.println("change text to test");
                text = "test";
            }
            else{
                System.out.println("change text to hello");
                text = "hello";
            }
            try{
                Thread.sleep(10000);

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        Intent stopIntent = new Intent("stop");
        intent.putExtra("Stop","stop");
        return START_STICKY;
    }

    private void sendMessage(String msg)
    {
        Intent intent = new Intent("speech");
        intent.putExtra("Status", msg);
        sendBroadcast(intent);
    }
    private void speak()
    {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try{
            startActivity(speechIntent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void myThread()
    {
        Thread thread = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    if(i % 2 == 0){
                        System.out.println("change text to test");
                        text = "test";
                    }
                    else{
                        System.out.println("change text to hello");
                        text = "hello";
                    }
                    try{
                        Thread.sleep(1000);

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
