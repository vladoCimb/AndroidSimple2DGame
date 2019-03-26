package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Hero extends  GameObject {
    private Bitmap spiritesheet;
    private int score;

    //Y hodnota vždy, keď sa dotkneme obrazovky
    private double dya;

    //Či hrdina pôjde hore alebo dole
    private boolean up;
    private boolean playing;

    private Animation animation = new Animation();
    private long startTime;

    public Hero(Bitmap res, int w, int h, int numFrames) {
        x = 100; //zmenit podla sirky a vysky telefonu potom
        y = GamePanel.HEIGHT / 2;

        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spiritesheet = res;//toto mozno zmazat

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spiritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);

        startTime = System.nanoTime();
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > 100) {
            score+=2;
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
            dy = (int)(dya-=1.5);

        }
        else{
            dy = (int)(dya+=1.1);
        }

        if(dy>14)dy = 14;
        if(dy<-14)dy = -14;

        y += dy*2;
        dy = 0;

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public void setUp(boolean b) {
        up = b;
    }

    public int getScore() {
        return score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYA() {
        dya = 0;
    }

    public void resetScore() {
        score = 0;
    }
}

