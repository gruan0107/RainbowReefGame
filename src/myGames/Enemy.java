package myGames;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Set and update bigleg's situation.
 * Created by guoyiruan on 12/2/15.
 */
public class Enemy extends Unit {

    private int type = 0;
    private int maxdamage;
    private int speed;
    private Level level = new Level();
    private ArrayList<ArrayList> everything;
    private Degree degree = new Degree();


    public Enemy(int x, int y, int imgTick, int speed, Image[] img, GameEvents events,int maxdamage, int damageto,
                 ArrayList<ArrayList> everything, int type, Level level ,Degree degree) {
        super(x, y, imgTick, speed, img, events, maxdamage, damageto);
        this.speed = speed;
        this.type = type;
        this.level = level;
        this.maxdamage = maxdamage;
        this.degree.setDegree(0);

    }

    @Override
    public void hitMe(Thing caller) {
        if (caller instanceof Pop) {
            maxdamage -= ((Pop) caller).getDamageTo();
            if (maxdamage <= 0) {
                this.setDone(true);

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {
            if(events.getTarget() == this)
            {
                if (events.getCaller() instanceof Unit) {
                    ((Unit) events.getCaller()).itHit(this);
                }
                hitMe((Thing)events.getCaller());
            }
        }

    }

    @Override
    public void update(int w, int h)
    {
        super.update(w, h);
        this.increaseImgTick();
    }

    @Override
    public void move() {
        if(type == 1) {
            int widthChange = (int)(Math.cos(degree.getDegree())* getSpeed());
            this.setX(this.getX() + widthChange);
            if(this.getX() >= 460) {
                degree.setDegree(Math.PI);
            } else if (this.getX() <= 170) {
                degree.setDegree(0.0);
            } else {
                return;
            }
        }


    }

    @Override
    public void dead() {
        this.setRDone(true);
    }

}
