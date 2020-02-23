package com.example.sightofsound;

import java.util.ArrayList;
import java.util.List;

public class SpeechFrame {
    private float timer = 0;
    private int oldTimer;

    private String currentSubtitle = "";

    public float getTimer() {
        return timer;
    }
    public int getTimerInt() {return (int) timer;}
    public void setTimer(float timer) {this.timer = timer;}

    public int getOldTimer() {return oldTimer;}
    public void setOldTimer(int time){oldTimer = time;}

    public void reset(){
        timer = 0;
    }

    public void addFrame(float time){
        timer = timer + time;
    }

    public String getCurrentSubtitle() { return currentSubtitle; }
    public void setCurrentSubtitle(String currentSubtitle) {this.currentSubtitle = currentSubtitle;}
}
