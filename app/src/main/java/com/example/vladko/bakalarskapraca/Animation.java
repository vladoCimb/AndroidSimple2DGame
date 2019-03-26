package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;

/**
 * Created by Vladko on 7. 3. 2018.
 */

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;

    //Delay medzi obrázkami
    private long delay;

    //Pri explóziach chceme napríklad aby sa len 1 udiala
    private boolean playedOnce;


    public void setFrames(Bitmap[] frames){
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void update(){
        long elapsed = (System.nanoTime() - startTime)/1000000;

        if(elapsed>delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage(){
        return frames[currentFrame];
    }

    public int getFrame(){return currentFrame;}
    public boolean playedOnce(){return playedOnce;}
}
