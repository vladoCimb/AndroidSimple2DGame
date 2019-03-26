package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Bordertop extends  GameObject{

    //we need the Bitmap ref of the image
    private Bitmap image;



    public Bordertop(Bitmap res, int x, int y){
        width = 20;
        height = 150;
        //we get the cords from superclass GameObject
        this.x = x;
        this.y = y;

        //we set the dx var to have the movespeed value
        dx = GamePanel.MOVESPEED;

        //we are creating the image (we dont need for loop here cause we have 1 image version - 1 frame-)
        image = Bitmap.createBitmap(res, 0, 0, width, height);

    }//end




    public void update(){


        //we change the border position
        x +=dx;
    }

    public void draw(Canvas canvas){

        //then we draw
        canvas.drawBitmap(image, x, y, null);



    }
}
