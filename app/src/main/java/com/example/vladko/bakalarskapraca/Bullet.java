package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Bullet  extends GameObject{

    //vars we will need
//the speed of the bullet
    private int speed;

    //the animation to animate the bullet image
    private Animation animation = new Animation();
    //a bitmap reference of the image
    private Bitmap spritesheet;



    public Bullet(Bitmap res,int x, int y,int w, int h, int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        speed = 13;


        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;


        for(int i = 0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0,  i*height, width, height);
        }

        //then we have all the info of the img and we can do the animation
        animation.setFrames(image);

        //then we set the delay of the animation between the frames
        animation.setDelay(120-speed);






    }//end

    public void update(){

//every sec we want for our example to change the speed of the bullet
        x+=speed-4;

        animation.update();

    }//end update

    public void draw(Canvas canvas){

//end then finally we draw the bullet on the screen
        try{

            canvas.drawBitmap(animation.getImage(),x-30,y,null);
        }catch(Exception e){}
    }//end draw



//are we done?
    //no
    //we must go to Gamepanel and place the bullet.....
    /**
     * in this lesson i will explain how we will add the bullet to Gamepanel
     * ....but before that we will solve the R problem in our project :)
     */


}//end class