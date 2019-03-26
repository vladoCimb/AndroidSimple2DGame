package com.example.vladko.bakalarskapraca;

import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.SyncStateContract;

/**
 * Created by Vladko on 6. 3. 2018.
 */

public class Background {
    private Bitmap image;
    private int x,y,dx;

    public Background(Bitmap res){
        image = res;
        dx=GamePanel.MOVESPEED; //TODO: zmenit na triedu CONSTANTS aj s tou sirkou a vyskou v gamepanel
    }
    public void update(){
        x+=dx;

        if(x<-GamePanel.WIDTH){
            x=0;
        }

    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
        if(x<0){
            canvas.drawBitmap(image,x+GamePanel.WIDTH,y,null);
        }

    }


}
