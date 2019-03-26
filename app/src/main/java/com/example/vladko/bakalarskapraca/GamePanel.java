package com.example.vladko.bakalarskapraca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vladko on 5. 3. 2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Random rand = new Random();
    private MainThread thread;

   public static final int WIDTH = PhoneAttributes.SCREEN_WIDTH;
   public static final int HEIGHT = PhoneAttributes.SCREEN_HIGHT;

   public static final int MOVESPEED = -5; // TODO: 14. 3. 2018 ten speed

   private Background bg;

    private Hero hero;

    private ArrayList<Bullet> bullets;
    private long bulletStartTime;

    private ArrayList<Enemy> alien;
    private long  alienStartTime;

    private ArrayList<Obstacle> obstacles;
    private long obstacleStartTime;

    private Explosion explosion;

    private ArrayList<Borderbottom> botBorder;
    private ArrayList<Bordertop> topBorder;
    private long borderStartTime;

    private boolean newGameCreated;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;

    private int bestScore;

    Bitmap hearthA;
    Bitmap hearthB;
    Bitmap hearthC;
    private int hearths = 3;

    Bitmap coinBonus;
    private int heroCoins;
    private long bonusStartTime;
    private ArrayList<Bonus> myCoins;


    //sounds
    MediaPlayer mp;
    SoundPool coinSound;
    int coinsoundid;

    Bitmap infoPanel;




    public GamePanel(Context context){
        super(context);

        //zachytávanie akcii
        getHolder().addCallback(this);


        //Aby mohol byť focusovaný a mohli prebiehať akcie
        setFocusable(true);

        mp = MediaPlayer.create(context,R.raw.arcademusicloop);
        coinSound = new SoundPool(99, AudioManager.STREAM_MUSIC,0);
        coinsoundid = coinSound.load(context,R.raw.pickedcoin,1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(),this); // TODO: vo videu add bottom border 47 a robi to error

        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.background));


        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.hero), 90, 90, 2);

        bullets = new ArrayList<Bullet>();
        bulletStartTime = System.nanoTime();

        alien=new ArrayList<Enemy>();
        alienStartTime= System.nanoTime();

        botBorder = new ArrayList<Borderbottom>();
        borderStartTime = System.nanoTime();

        topBorder = new ArrayList<Bordertop>(); // TODO: dorobit potom ten horny tak isto ako dolny len dat -480 namiesto 80 a nakreslit obrazok opacne

        obstacles = new ArrayList<Obstacle>();

        myCoins = new ArrayList<Bonus>();
        bonusStartTime = System.nanoTime();





        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0;

        while(retry && counter<1000){ // TODO: 12. 3. 2018 tento counter asi zbytocny
            try{
                thread.setRunning(false);

                thread.join();
                
                retry=false;
                thread = null;

            }catch (InterruptedException e){e.printStackTrace();}
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!hero.getPlaying() && newGameCreated && reset)
            {
                hero.setPlaying(true);
                hero.setUp(true);
            }
            if(hero.getPlaying())
            {
                if(!started)
                    started = true;

                reset = false;
                hero.setUp(true);
            }
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            hero.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {

        if(hero.getPlaying()) {
            mp.start();


            bg.update();
            hero.update();

            long bonustime = (System.nanoTime() - bonusStartTime)/1000000;

            if(((bonustime > (3000 - hero.getScore()/4)) ) ){
                int y = rand.nextInt(HEIGHT);
                if(y < 0+(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop).getHeight()/4)){
                    y = (BitmapFactory.decodeResource(getResources(), R.drawable.bordertop).getHeight()/4)+1;
                }
                if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.coin).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                    y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.coin).getHeight());
                }
                myCoins.add(new Bonus((BitmapFactory.decodeResource(getResources(), R.drawable.coin)),
                        WIDTH + 1, y, 1));
                bonusStartTime = System.nanoTime();

            }

            for(int i = 0; i < myCoins.size();i++){
                myCoins.get(i).update();
                if(collision(myCoins.get(i),hero)){
                    coinSound.play(coinsoundid,5,5,1,0,1);


                    myCoins.remove(i);
                    heroCoins++;
                    break;
                }
                if(myCoins.get(i).getX() < 0){
                    myCoins.remove(i);
                    break;
                }
            }

            long obstacleElapsed = (System.nanoTime() - obstacleStartTime)/1000000;

            if(obstacleElapsed >(1500 - hero.getScore()/4)){ // TODO:  ten cas dorobit
                obstacles.add(new Obstacle((BitmapFactory.decodeResource(getResources(), R.drawable.pipe)),WIDTH+10, HEIGHT-190+rand.nextInt(50),1));
            }
            obstacleStartTime = System.nanoTime();


            for(int i = 0; i < obstacles.size();i++){
                obstacles.get(i).update();
                if(collision(obstacles.get(i),hero)){
                    hero.setPlaying(false);
                    break;
                }
                if( obstacles.get(i).getX()<0)
                {
                    obstacles.remove(i);
                }
            }



            long bullettimer = (System.nanoTime() - bulletStartTime)/1000000;

            if(bullettimer >(2500 - hero.getScore()/4)){
                bullets.add(new Bullet((BitmapFactory.decodeResource(getResources(), R.drawable.bullet)),hero.getX()+60, hero.getY()+24,(BitmapFactory.decodeResource(getResources(), R.drawable.bullet)).getWidth(),(BitmapFactory.decodeResource(getResources(), R.drawable.bullet)).getHeight()/7,7));
                bulletStartTime = System.nanoTime();

            }
            for(int i = 0; i<bullets.size();i++)
            {
                bullets.get(i).update();
                if(bullets.get(i).getX()<-10)
                {
                    bullets.remove(i);
                }
            }

            long borderElapsed = (System.nanoTime()-borderStartTime)/1000000;

            if(borderElapsed >100 ) {
                botBorder.add(new Borderbottom(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom), WIDTH + 10, ((HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4)))));
                botBorder.add(new Borderbottom(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop), WIDTH + 10, 0-(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop).getHeight()/4)*3)); // TODO: 16. 3. 2018 skusit podla vysky telefonu a tak
                borderStartTime = System.nanoTime();
            }

            for(int i = 0; i<botBorder.size();i++) {
                botBorder.get(i).update();
                if (collision(botBorder.get(i), hero)) {
                    hero.setPlaying(false);
                    break;

                }
                if( botBorder.get(i).getX()< -50)
                {
                    botBorder.remove(i);
                }
            }


            long alienElapsed = (System.nanoTime()-alienStartTime)/1000000;

            if(alienElapsed > (2500 - hero.getScore()/4)){
                int generate = rand.nextInt(5);
                int y = rand.nextInt(HEIGHT);
                if(y < 0+(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop).getHeight()/4)){
                    y = (BitmapFactory.decodeResource(getResources(), R.drawable.bordertop).getHeight()/4)+1;
                }

                switch (generate){
                    case 0:
                        if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.enemy).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                            y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.enemy).getHeight());
                        }
                        alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                                WIDTH + 10, y, (BitmapFactory.decodeResource(getResources(), R.drawable.enemy)).getWidth()/3, (BitmapFactory.decodeResource(getResources(), R.drawable.enemy)).getHeight(), hero.getScore(), 3));
                        break;
                    case 1:
                        if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.enemybird).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                            y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.enemybird).getHeight());
                        }
                        alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemybird),
                                WIDTH + 10, y, (BitmapFactory.decodeResource(getResources(), R.drawable.enemybird)).getWidth()/2, (BitmapFactory.decodeResource(getResources(), R.drawable.enemybird)).getHeight(), hero.getScore(), 2));
                        break;
                    case 2:
                        if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.ufo).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                            y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.ufo).getHeight());
                        }
                        alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.ufo),
                                WIDTH + 10, y, (BitmapFactory.decodeResource(getResources(), R.drawable.ufo)).getWidth()/3, (BitmapFactory.decodeResource(getResources(), R.drawable.ufo)).getHeight(), hero.getScore(), 3));
                        break;
                    case 3:
                        if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.mine).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                            y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.mine).getHeight());
                        }
                        alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.mine),
                                WIDTH + 10, y, (BitmapFactory.decodeResource(getResources(), R.drawable.mine)).getWidth()/10, (BitmapFactory.decodeResource(getResources(), R.drawable.mine)).getHeight(), hero.getScore(), 10));
                        break;
                    case 4:
                        if(y + (BitmapFactory.decodeResource(getResources(), R.drawable.alienorange).getHeight()) > (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))){
                            y = (HEIGHT -(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom).getHeight()/4))- (BitmapFactory.decodeResource(getResources(), R.drawable.alienorange).getHeight());
                        }
                        alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.alienorange),
                                WIDTH + 10, y, (BitmapFactory.decodeResource(getResources(), R.drawable.alienorange)).getWidth()/3, (BitmapFactory.decodeResource(getResources(), R.drawable.alienorange)).getHeight(), hero.getScore(), 3));
                        break;

                }



                alienStartTime = System.nanoTime();
            }


            for(int i = 0; i<alien.size();i++) {
                alien.get(i).update();
                if (collision(alien.get(i), hero)) {

                    alien.remove(i);
                    hearths--;

                   // hero.setPlaying(false);

                    break;
                }


                if (alien.get(i).getX() < -100) {
                    alien.remove(i);
                    break;
                }

                for (int j = 0; j < bullets.size(); j++) {
                    if(collision(alien.get(i),bullets.get(j)))
                    {

                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),alien.get(i).getX(),alien.get(i).getY(),64,64,16);
                        alien.remove(i);
                        bullets.remove(j);


                        bestScore += 30;
                        break;
                    }
                    bullets.get(j).update();


                }


            }//end enemy

            explosion.update();
        } else {
            hero.resetDYA();
            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;

                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),hero.getX(),hero.getY(),64,64,16);

            }
            explosion.update();

            if(!newGameCreated) {
                newGame();
            }
        }

    }//end update

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);


        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas != null){
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);
            bg.draw(canvas);
            if(!dissapear) {
                hero.draw(canvas);
            }

            for(Bullet fp: bullets)
            {
                fp.draw(canvas);
            }

            for(Enemy aln: alien)
            {
                aln.draw(canvas);
            }

            for(Obstacle obsb: obstacles){
                obsb.draw(canvas);
            }


            for(Borderbottom brb: botBorder)
            {
                brb.draw(canvas);
            }

            for(Bonus bonus: myCoins){
                bonus.draw(canvas);
            }

            if(started) {
                explosion.draw(canvas);
            }

            drawText(canvas);
            infoPanel = BitmapFactory.decodeResource(getResources(), R.drawable.infopanel);

            if(!hero.getPlaying()&&newGameCreated&&reset) {
                Paint paint1 = new Paint();
                paint1.setTextSize(25);
                paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                canvas.drawText("PRESS TO START", WIDTH / 2 - 100, HEIGHT / 2 - 70, paint1);

                canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2 - 90, HEIGHT / 2 - 20, paint1);

                canvas.drawText("RELEASE TO GO DOWN", WIDTH / 2 - 90, HEIGHT / 2 + 20, paint1);
            }


            canvas.restoreToCount(savedState);
        }
    }
    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Distance: " + (hero.getScore())*2,10,HEIGHT-10,paint);
        canvas.drawText("Score: " + bestScore,WIDTH-215,HEIGHT-10,paint);

        coinBonus = BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        canvas.drawBitmap(coinBonus,WIDTH-130,0,null);
        canvas.drawText(" " + heroCoins, WIDTH-90,25,paint);


        if(hearths == 3){
            hearthA = BitmapFactory.decodeResource(getResources(),R.drawable.lifea);
            canvas.drawBitmap(hearthA,WIDTH/2 - hearthA.getWidth() , 0 , null);
            hearthB = BitmapFactory.decodeResource(getResources(),R.drawable.lifeb);
            canvas.drawBitmap(hearthB,WIDTH/2 - (hearthB.getWidth()*2) , 0 , null);
            hearthC = BitmapFactory.decodeResource(getResources(),R.drawable.lifec);
            canvas.drawBitmap(hearthC,WIDTH/2 - (hearthB.getWidth()*3) , 0 , null);
        }
        if(hearths == 2){
            hearthA = BitmapFactory.decodeResource(getResources(),R.drawable.lifea);
            canvas.drawBitmap(hearthA,WIDTH/2 - hearthA.getWidth() , 0 , null);
            hearthB = BitmapFactory.decodeResource(getResources(),R.drawable.lifeb);
            canvas.drawBitmap(hearthB,WIDTH/2 - hearthB.getWidth()*2 , 0 , null);
        }
        if(hearths == 1){
            hearthA = BitmapFactory.decodeResource(getResources(),R.drawable.lifea);
            canvas.drawBitmap(hearthA,WIDTH/2 - hearthA.getWidth() , 0 , null);
        }
        if(hearths == 0){
            hero.setPlaying(false);
            hearths = 3;
        }

    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    public void newGame(){
        dissapear = false;

        alien.clear();
        obstacles.clear();
        //we forgot to clear the borders and the bullet
        botBorder.clear();
        bullets.clear();

        hero.resetDYA();
        hero.resetScore();

        hero.setY(HEIGHT/2);
        myCoins.clear();
        heroCoins = 0;
        hearths = 3;
        bestScore=0;



        //we also need the newgamecreated var to be true so we can play
        newGameCreated=true;
    }

}
