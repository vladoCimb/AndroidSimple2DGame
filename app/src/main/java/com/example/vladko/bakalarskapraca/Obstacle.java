package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Obstacle extends GameObject {

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Obstacle(Bitmap res , int x, int y, int numFrames){
        super.x = x;
        super.y = y;
        width = res.getWidth();
        height = res.getHeight();

        speed = 11;

        Bitmap[] images = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < images.length;i++){
            images[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);
        }

        animation.setFrames(images);
        animation.setDelay(100-speed);


    }
    public void update(){
        x-=speed;
        animation.update();

    }
    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}

    }

    @Override
    public int getWidth(){
        return width-10;
    }
}
