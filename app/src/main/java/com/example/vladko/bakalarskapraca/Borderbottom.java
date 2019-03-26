package com.example.vladko.bakalarskapraca;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Borderbottom extends  GameObject{

    //we need the Bitmap ref of the image
    private Bitmap image;



    public Borderbottom(Bitmap res, int x, int y){
        height = res.getHeight(); // TODO: Podla sirky a vysky telefon
        width = res.getWidth();


        this.x = x;
        this.y = y;

        dx = GamePanel.MOVESPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);

    }//end




    public void update(){
        x +=dx;
    }

    public void draw(Canvas canvas){

        //then we draw
        canvas.drawBitmap(image, x, y, null);



    }
}
