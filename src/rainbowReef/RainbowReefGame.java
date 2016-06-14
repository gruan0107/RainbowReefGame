/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rainbowReef;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.sound.midi.*;
import javax.swing.*;

import myGames.*;

/**
 * Rainbow Reef game is a single player game. The goal of the game is to destroy biglegs and pass all levels.
 * For this game, there are 2 levels; and a score board will record your score;
 * For player:  arrow keys <- ->  control movement;
 * Lives and scores are showed one the botton left;
 *
 * @author Guoyi Ruan
 */
public class RainbowReefGame extends Game
{
    private GameSpace screen;
    private ArrayList<ArrayList> everything;
    private ArrayList<Thing> things;
    static public ArrayList<PlayerParent> players;
    static public int score = 0;
    private Image[] myPop,myKatch,bigleg,bigleg_s;
    private GameController gcontroller;
    private TankEvents events;
    private Level level;
    private Degree degree;
    private boolean gameover;
    private boolean destroy = false;
    private URL[] soundurl;
    private boolean inited = false;
    private Image lives;

    //creates and adds all the game panel to the applet
    //also sets up images, sounds, and creates and initializes state for most
    //variables and objects.
    @Override
    public void init()
    {      
        super.init();

        events = new TankEvents();
        level = new Level();
        Image[] blockers = new Image[11];
        blockers[0] = getSprite("/Resources/Block_double.gif");
        for(int i = 1; i < 8; i++ ) {
            blockers[i] = getSprite("/Resources/Block" + i +".gif");
        }
        blockers[8] = getSprite("/Resources/Block_life.gif");
        blockers[9] = getSprite("/Resources/Block_solid.gif");
        blockers[10]= getSprite("/Resources/Wall.gif");

        lives = getSprite("/Resources/Katch_small.png");

        Image[] backgroundImage = new Image[4];
        for(int i = 1; i < 3; i++ ) {
            backgroundImage [i-1] = getSprite("/Resources/background" + i +".png");
        }
        backgroundImage [2] = getSprite("/Resources/Congratulation.png");
        backgroundImage [3] = getSprite("/Resources/Title.png");
        screen = new GameSpace(backgroundImage, new DrawAbs(), blockers, 640, 480, events,level,lives);
        add(screen, BorderLayout.CENTER);
        setBackground(Color.white);


        everything = new ArrayList<ArrayList>();
        things = new ArrayList<Thing>();
        everything.add(things);
        players = new ArrayList<PlayerParent>();
        everything.add(players);
        ArrayList<Thing> blocks = screen.getBlocks();
        everything.add(blocks);

        KeyControl keys = new KeyControl(events);
        addKeyListener(keys);
        
        gcontroller = new GameController();
        score = 0;

        gameover = false;

    }
    
    //getting all image files
    @Override
    public void initImages()
    {
        try
        {
            myPop = new Image[45];
            BufferedImage bufferedImage_pop = getBufferedSprite("/Resources/Pop_strip45.png");
            int width_pop = bufferedImage_pop.getWidth()/45;
            for (int i = 0; i < 45; i++)
            {
                myPop[i] = bufferedImage_pop.getSubimage(i * 35, 0, width_pop, bufferedImage_pop.getHeight());
            }

            myKatch = new Image[24];
            BufferedImage bufferedImage_katch = getBufferedSprite("/Resources/Katch_strip24.png");
            int width_katch = bufferedImage_katch.getWidth()/24;
            for (int i = 0; i < 24; i++)
            {
                myKatch[i] = bufferedImage_katch.getSubimage(i * 80, 0, width_katch, bufferedImage_katch.getHeight());
            }

            bigleg = new Image[24];
            BufferedImage bufferedImage_bigleg = getBufferedSprite("/Resources/Bigleg_strip24.png");
            int width_bigleg = bufferedImage_bigleg.getWidth()/24;
            for (int i = 0; i < 24; i++)
            {
                bigleg[i] = bufferedImage_bigleg.getSubimage(i * 80, 0, width_bigleg, bufferedImage_bigleg.getHeight());
            }

            bigleg_s = new Image[24];
            BufferedImage bufferedImage_bigleg_s = getBufferedSprite("/Resources/Bigleg_small_strip24.png");
            int width_bigleg_s = bufferedImage_bigleg_s.getWidth()/24;
            for (int i = 0; i < 24; i++)
            {
                bigleg_s[i] = bufferedImage_bigleg_s.getSubimage(i * 40, 0, width_bigleg_s, bufferedImage_bigleg_s.getHeight());
            }

        } catch (Exception e)
        {
            System.out.println("Error in getting images: " + e.getMessage());
        }
    }
    
    //getting all sound files
    @Override
    public void initSound()
    {
        try
        {
        Sequence music;
        Sequencer seq;
        URL musicu = RainbowReefGame.class.getResource("/Resources/Music.mid");
        soundurl = new URL[5];
        soundurl[0] = RainbowReefGame.class.getResource("/Resources/Sound_block.wav");
        soundurl[1] = RainbowReefGame.class.getResource("/Resources/Sound_bigleg.wav");
        soundurl[2] = RainbowReefGame.class.getResource("/Resources/Sound_katch.wav");
        soundurl[3] = RainbowReefGame.class.getResource("/Resources/Sound_lost.wav");
        soundurl[4] = RainbowReefGame.class.getResource("/Resources/Sound_wall.wav");

            music =  MidiSystem.getSequence(musicu);
           seq = MidiSystem.getSequencer();
           seq.open();
           seq.setSequence(music);
           seq.start();
           seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {
            System.out.println("Error in midi: " + e.getMessage());
        }
    }

    //this creates Things when needed to make the gameplay pattern
    public class GameController
    {

        private int timer;

        public GameController()
        {
            timer = 0;
        }

        public void timeline()
        {
            switch (timer)
            {
                case 0:
                        players.add(new Katch(320, 440, 1, 15, myKatch,
                                events, 3, 0, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER,
                                KeyEvent.VK_SHIFT, 10, 5, 0, screen, 0));

                        things.add(new Enemy(320, 80, 1, 0, bigleg, events, 1, 0, everything,0, level, degree));
                        things.add(new Pop(320,350, 1, 5, myPop, events, 1, 1, everything, 0, soundurl));
                        inited = true;
                    break;

            }

            if (timer == Integer.MAX_VALUE) {
                timer = 1;
            } else {
                timer++;
            }
        }

    }

    //This is the player's katch
    public class Katch extends PlayerParent
    {
        private int startx, starty;
        private int deathTime = 0;


        public Katch(int x, int y, int imgTick, int speed, Image[] img,
                GameEvents events, int maxdamage, int damageto,
                int left, int right, int up, int down, int fire, int spfire,
                int shotTime, int fastShotTime, int deadTime, GameSpace gameSpace, int type)
        {
            super(x, y, imgTick, speed, img, events, maxdamage, damageto,
                    left, right, up, down, fire, spfire, shotTime, fastShotTime,
                    deadTime, gameSpace, type);

            startx = x;
            starty = y;

        }
        @Override
        public void update(int w, int h)
        {
            super.update(w, h);
            this.increaseImgTick();
        }
        //moves based on the keys pressed, but only with the basic update
        @Override
        public void move()
        {
            if(getMvLeft())
            {
                int widthChange = getSpeed();
                int x1 = this.getX() - widthChange;
                int y1 = this.getY();
                if (!this.gameSpace().valid(this, x1, y1)
                        || (this.getOtherPlayerParent() != null
                        && this.getOtherPlayerParent().isOverlap(this, x1, y1))) {
                    return;
                }
                changeX(-widthChange);
            }

            if(getMvRight())
            {
                int widthChange = getSpeed();
                int x1 = this.getX() + widthChange;
                int y1 = this.getY();
                if (!this.gameSpace().valid(this, x1, y1)
                        || (this.getOtherPlayerParent() != null
                        && this.getOtherPlayerParent().isOverlap(this, x1, y1))) {
                    return;
                }
                changeX(widthChange);
            }

            if(getMvUp()) { }

            if(getMvDown()) { }

            if(getShotDelay() > 0)
            {
                changeShotDelay(-1);
            }
        }


        @Override
        public void action()
        {
            boolean hasPop = false;
            for (int i = 0; i < things.size(); i++) {
                if (things.get(i) instanceof Pop) {
                    hasPop = true;
                    break;
                }
            }
            if (!hasPop) {
                this.setDamage( getDamage()+ 1 );
                if(getDamage() >= getMax()) {
                    setDone(true);
                } else {
                    reset();
                }
            }

        }

        //If dead, show score board and gameover background;
        @Override
        public void dead()
        {
            setRDone(true);
            write(score);
            level.setType(4);
        }


        @Override
        public void hitMe(Thing t)
        {
            if (t instanceof Pop) {
                t.itHit(this);
            }
        }

        //this is what this Thing does to a Unit that hit it
        @Override
        public void itHit(Unit u)
        {

        }
        @Override
        public int getWidth(){
            return super.getWidth() - 20;
        }

        @Override
        public int getHeight(){
            return super.getHeight() - 20;
        }

        public void increaseDead() {
            this.deathTime++;
        }

        public int getDeathTime() {
            return this.deathTime;
        }

        public void reset()
        {
            this.setDone(false);
            this.setX(startx);
            this.setY(starty);
            things.add(new Pop(320,350, 1, 5, myPop, events, 1, 1, everything, 0, soundurl));
        }
    }
    
    //adds an event type to GameEvents
    public class TankEvents extends GameEvents
    {


    }

    //Updates all Things and then draws everything
    //when the game is resetting, this method will also 
    @Override
    public void drawAll(int w, int h, Graphics2D g2)
    {
        Thing temp;

        Iterator<ArrayList> it = everything.listIterator();

        try {
            while (it.hasNext())
            {
                ArrayList<Thing> list = it.next();
                Iterator<Thing> it2 = list.listIterator();
                while (it2.hasNext())
                {
                    if (gameover)
                    {

                        break;
                    }
                    temp = it2.next();
                    temp.update(w, h);

                    if (temp.getRDone())
                    {
                        it2.remove();
                    }
                }
                if (gameover)
                {
                    break;
                }
            }
        }
        catch (Exception e) {

        }
        checkUpgrade();
        screen.updateBackground();
        screen.drawHere(everything, g2);
        screen.drawBackground(g2);


        if(destroy)
        {
            it = everything.listIterator();
            while(it.hasNext())
            {
                Iterator<Thing> it2 = it.next().listIterator();
                while(it2.hasNext())
                {
                    it2.next();
                    it2.remove();
                }
                
            }
            
            gcontroller = new GameController();
            destroy = false;
            gameover = false;
            events.deleteObservers();
            this.requestFocus();
        }
        
        gcontroller.timeline();
    }
    // check is it valid for a upgrade;
    // if you hit a bigleg; upgrade = true; and set it to next level;
   private void checkUpgrade() {
       if (!inited) {
           return;
       }
       boolean upgrade = true;
       for (Thing thing : things) {
           if (thing instanceof Enemy) {
               upgrade = false;
               break;
           }
       }

       if (upgrade) {
           if (level.getType() == 1) {
               level.setType(2);
               things.clear();
               things.add(new Enemy(320, 70, 1, 0, bigleg, events, 1, 0, everything,1, level,degree));
               things.add(new Enemy(320, 200, 1, 5, bigleg_s, events, 1, 0, everything,1, level,degree));
               players.get(0).setX(320);
               players.get(0).setY(440);
               things.add(new Pop(320,340, 1, 5, myPop, events, 1, 1, everything, 0, soundurl));
               everything.remove(2);
               everything.add(screen.getBlocks());
           } else if (level.getType() == 2) {
                write(score);
                level.setType(3);
                things.clear();
                everything.remove(2);
           } else if (level.getType() == 4) {
               things.clear();
               everything.remove(2);
           }
       }

   }
    // write score to file;
    private void write(int score) {

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("score", true)))) {
            out.println(score);
        }catch (IOException e) {

        }
    }

    public static void main(String[] args)
    {
        final RainbowReefGame game = new RainbowReefGame();
        game.init();
        final JFrame f = new JFrame("Rainbow Reef");
        f.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                f.dispose();
                System.exit(0);
            }
        });
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
        f.setResizable(false);
        game.start();
    }
}