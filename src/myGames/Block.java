package myGames;

import rainbowReef.RainbowReefGame;

import java.awt.*;
import java.util.Observable;

//Design for walls and blocks;
//blocks and walls are observable objects extends unit class;
//type: represent different kind of blocks, double health, lives;
//level: showed in different level;
//
//@author Guoyi Ruan
public class Block extends Unit {

    int health = 1;
    int type = 0;
    Level level = new Level();

    public Block(int x, int y, int imgTick, int speed, Image[] img, GameEvents events, int type, Level level) {
        super(x, y, imgTick, speed, img, events, 1 , 0);
        this.type = type;
        this.level = level;
        if(type == 3) {
            health *= 2;
        }
    }

    //movement type actions
    @Override
    public void move() {

    }

    @Override
    public void dead() {
        this.setRDone(true);
    }

    //if the wall is breakable, get damage; else return;
    @Override
    public void hitMe(Thing caller) {
        if (caller instanceof Pop) {
            if (type == 1) {
                return;
            }
            if(type == 2) {
                if (RainbowReefGame.players != null) {
                    RainbowReefGame.players.get(0).setMax(RainbowReefGame.players.get(0).getMax() + 1);
                }
            }
            health -= ((Pop) caller).getDamageTo();
            if (health <= 0) {
                this.setDone(true);
            }
        }
    }

    @Override
    public void itHit(Unit u) {

    }
    //update the situation of breakable and unbreakable walls.
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

    public int getType() {
        return type;
    }
}
