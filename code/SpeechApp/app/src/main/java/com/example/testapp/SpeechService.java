package com.example.testapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import java.util.Locale;

import androidx.annotation.Nullable;

public class SpeechService extends Service {

    private String text = "hello";
    PyObject stt = null;
    boolean isRunning = false;

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
        Python py = Python.getInstance();
        stt = py.getModule("SpeechToText");
        speak();
        Intent stopIntent = new Intent("stop");
        intent.putExtra("Stop","stop");
        return START_STICKY;
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return super.registerReceiver(receiver, filter, broadcastPermission, scheduler);
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
            speakThread();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void restartService()
    {
        PyObject speech = stt.callAttr("RestartService");
    }


    public void speakThread()
    {
        Thread thread = new Thread(){
            @Override
            public void run() {
                PyObject speech = stt.callAttr("ListenToVoice");
                setText(speech.toString());
                sendMessage(speech.toString());
            }
        };
        thread.run();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
