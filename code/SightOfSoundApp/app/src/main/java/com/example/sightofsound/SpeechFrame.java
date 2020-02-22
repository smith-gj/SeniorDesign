package com.example.sightofsound;

public class SpeechFrame {
    private float timer = 0;

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
    public void reset(){
        timer = 0;
    }

    public void addFrame(float time){
        timer = timer + time;
    }
}
