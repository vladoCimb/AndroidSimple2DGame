package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;


public class Bonus extends GameObject{
    private int score;

    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Bonus(Bitmap res, int x, int y,  int numFrames)
    {
        super.x = x;
        super.y = y;
        width = res.getWidth();
        height = res.getHeight();


        speed = 4 + (int) (rand.nextDouble()*score/30);

        if(speed>40)speed = 40;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);



    }//end


    public void update(){

        x-=speed;
        animation.update();



    }

    public void draw(Canvas canvas){

        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}



    }//end draw


    @Override
    public int getWidth()
    {
        return width-10;
    }


}

