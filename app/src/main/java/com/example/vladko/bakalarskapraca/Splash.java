package com.example.vladko.bakalarskapraca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Vladko on 12. 3. 2018.
 */

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(4000);
                    Intent startgame = new Intent(getApplicationContext(),GameMenu.class);
                    startActivity(startgame);
                    finish();

                }catch(InterruptedException e){e.printStackTrace();}
            }
        };

        myThread.start();



    }

}
