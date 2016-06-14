/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import rainbowReef.RainbowReefGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Guoyi Ruan
 */
public class Pop extends Unit
{

    private ArrayList<ArrayList> everything;
    private int type;
    private URL[] soundUrl;
    private Degree degree = new Degree();



    public Pop(int x, int y, int imgTick, int speed, Image[] img,
               GameEvents events, int maxdamage, int damageto,
               ArrayList<ArrayList> everything, int type, URL[] soundUrl)
    {
        super(x, y, imgTick, speed, img, events, maxdamage, damageto);
        this.everything = everything;
        this.type = type;
        this.soundUrl = soundUrl;
    }

    @Override
    public void hitMe(Thing caller) {

    }
    // motion; update location;
    // check collision;
    @Override
    public void move() {
        if(this.getY() > 470) {
            this.setDone(true);
            AudioInputStream explSound;
            Clip clip;
            try {
                explSound = AudioSystem.getAudioInputStream(this.soundUrl[3]);
                clip = AudioSystem.getClip();
                clip.open(explSound);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        int widthChange = (int)(Math.cos(degree.getDegree())* getSpeed());
        int heightChange = (int)(Math.sin(degree.getDegree())* getSpeed());
        this.setX(this.getX() + widthChange);
        this.setY(this.getY() - heightChange);

        for (Thing thing : (ArrayList<Thing>) everything.get(2)) {
            if (this.collision(thing)) {
                getEvents().setCollision(this, thing);
            }
        }
        for (Thing thing : (ArrayList<Thing>) everything.get(1)) {
            if (this.collision(thing)) {
                getEvents().setCollision(this, thing);
            }
        }

        for (Thing thing : (ArrayList<Thing>) everything.get(0)) {
            if (thing == this) {
                continue;
            }
            if (this.collision(thing)) {
                getEvents().setCollision(this, thing);
            }
        }
    }
    // only update move location;
    public void largeMove() {

        int widthChange = (int)(Math.cos(degree.getDegree())* getSpeed());
        int heightChange = (int)(Math.sin(degree.getDegree())* getSpeed());
        this.setX(this.getX() + widthChange);
        this.setY(this.getY() - heightChange);
    }

    @Override
    public void dead() {
        this.setRDone(true);
    }

    @Override
    public void itHit(Unit u)
    {
        if (u instanceof Block) {
            if (this.getX() > u.getX() - u.getWidth()/2  && this.getX() < u.getX() + u.getWidth()/2) {
                degree.setDegree(2 * Math.PI - degree.getDegree());
            } else {
                degree.setDegree(Math.PI - degree.getDegree());
            }
            this.largeMove();
            this.largeMove();
            this.largeMove();
            if(((Block) u).getType()!= 1) {
                RainbowReefGame.score += 20;
            }
            AudioInputStream explSound;
            Clip clip;
            try {
                explSound = AudioSystem.getAudioInputStream(this.soundUrl[0]);
                clip = AudioSystem.getClip();
                clip.open(explSound);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (u instanceof PlayerParent) {

                if (((PlayerParent) u).getMvRight() && !((PlayerParent) u).getMvLeft()) {
                    degree.setDegree(2 * Math.PI - degree.getDegree() - (1.0 / 12.0) * Math.PI);

                } else if (((PlayerParent) u).getMvLeft() && !((PlayerParent) u).getMvRight()) {
                    degree.setDegree(2 * Math.PI - degree.getDegree() + (1.0 / 12.0) * Math.PI);

                } else {
                    degree.setDegree(2 * Math.PI - degree.getDegree());
                }
                if (degree.getDegree() <= 0) {
                    degree.setDegree((1 / 6.0) * Math.PI);
                }
                if (degree.getDegree() >= Math.PI) {
                    degree.setDegree((5 / 6.0) * Math.PI);
                }

                this.largeMove();
                this.largeMove();
                this.largeMove();

                AudioInputStream explSound;
                Clip clip;
                try {
                    explSound = AudioSystem.getAudioInputStream(this.soundUrl[2]);
                    clip = AudioSystem.getClip();
                    clip.open(explSound);
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        } else if (u instanceof Enemy ) {
            RainbowReefGame.score += 50;
            AudioInputStream explSound;
            Clip clip;
            try {
                explSound = AudioSystem.getAudioInputStream(this.soundUrl[1]);
                clip = AudioSystem.getClip();
                clip.open(explSound);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //update pop's situation.
    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {
            if(events.getTarget() == this)
            {
                hitMe((Thing) events.getTarget());
            }
        }
        if(events.getType() == 2) {
            if(events.getTarget() == this) {

            }
        }

    }

    public void update(int w, int h)
    {
        super.update(w, h);
        this.increaseImgTick();

    }

    public int getType() {
        return type;
    }
}