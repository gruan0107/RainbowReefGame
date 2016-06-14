/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import rainbowReef.RainbowReefGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JPanel;

/**
 * This is the space in which the game object live.
 * It draws the background and all objects.
 * This exact way in which the objects are drawn is determined by the DrawType
 * class
 *
 * @author Guoyi
 */
public class GameSpace extends JPanel {
    private int width = 640;
    private int height = 480;
    private Level level;
    final private int screenWidth;
    final private int screenHeight;
    private BufferedImage[] background;
    private ArrayList<ArrayList<Thing>> allBlocks = new ArrayList<ArrayList<Thing>>();

    private Image[][] blockImage;
    private GameEvents events;
    private Image[] backgroundImg;
    private DrawType drawer;
    private Image katch_s;


    public GameSpace(Image[] backgroundImage, DrawType drawer, Image[] blockImage,
                     int screenWidth, int screenHeight, GameEvents events, Level level, Image katch_s) {
        super();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.drawer = drawer;
        this.blockImage = new Image[11][1];
        for (int i = 0; i < 11; i++) {
            this.blockImage[i][0] = blockImage[i];
        }
        this.events = events;
        this.level = level;
        this.backgroundImg = new Image[4];
        this.background = new BufferedImage[4];
        this.katch_s = katch_s;
        this.initBlocks();

        for (int i = 0; i < 4; i++) {
            this.backgroundImg[i] = backgroundImage[i];
            this.background[i] = toBufferedImage(backgroundImage[i]);
        }

    }

    public void drawBackground(Graphics2D g2) {
        if (level.getType() == 1) {
            g2.drawImage(background[0], 0, 0, this);
        } else if (level.getType() == 2) {
            g2.drawImage(background[1], 0, 0, this);
        } else if (level.getType() == 3) {
            g2.drawImage(background[2], 0, 0, this);
            ArrayList<Integer> result = readTopScore();
            Collections.sort(result);
            Font font = new Font("Serif", Font.BOLD, 40);
            g2.setFont(font);
            g2.drawString("Scores :", 40, 30);
            if (result.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    g2.drawString(String.valueOf(result.get(result.size() - 1 - i)), 80, 80 + i * 40);
                }
            } else {
                for (int i = 0; i < result.size(); i++) {
                    g2.drawString(String.valueOf(result.get(result.size() - 1 - i)), 80, 80 + i * 40);
                }
            }

        } else if (level.getType() == 4) {
            g2.drawImage(background[3], 0, 0, this);
            ArrayList<Integer> result = readTopScore();
            Font font = new Font("Serif", Font.BOLD, 40);
            g2.setFont(font);
            Collections.sort(result);
            g2.drawString("Scores :", 40, 30);
            if (result.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    g2.drawString(String.valueOf(result.get(result.size() - 1 - i)), 80, 80 + i * 40);
                }
            } else {
                for (int i = 0; i < result.size(); i++) {
                    g2.drawString(String.valueOf(result.get(result.size() - 1 - i)), 80, 80 + i * 40);
                }
            }
        }
        if (RainbowReefGame.players != null && RainbowReefGame.players.size() > 0) {
            int lives = RainbowReefGame.players.get(0).getMax() - RainbowReefGame.players.get(0).getDamage();
            for (int i = 0; i < lives; i++) {
                g2.drawImage(katch_s, i * (katch_s.getWidth(null) + 5) + 20, 420, this);
            }
        }
        Font font = new Font("Serif", Font.PLAIN, 15);
        g2.setFont(font);

        g2.drawString("Scores :" + RainbowReefGame.score, 20, 400);
    }

    private ArrayList<Integer> readTopScore() {
        ArrayList<Integer> result = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("score");
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(Integer.valueOf(line));
            }

            bufferedReader.close();
        } catch (Exception e) {

        }
        return result;
    }


    //goes through and draws everything
    public void drawHere(ArrayList<ArrayList> everything, Graphics2D g2) {
        Graphics2D bGr;
        if (level.getType() == 1) {
            bGr = this.background[0].createGraphics();
        } else {
            bGr = this.background[1].createGraphics();
        }
        Thing temp;
        Iterator<ArrayList> it = everything.subList(0, 2).listIterator();
        while (it.hasNext()) {
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext()) {
                temp = it2.next();
                temp.draw(bGr, null);
            }
        }
        bGr.dispose();
    }

    public void setDrawType(DrawType drawer) {
        this.drawer = drawer;
    }

    public DrawType getDrawType() {
        return drawer;
    }

    private void initBlocks() {


        allBlocks.add(new ArrayList<Thing>());
        allBlocks.add(new ArrayList<Thing>());
        allBlocks.add(new ArrayList<Thing>());
        allBlocks.add(new ArrayList<Thing>());

        InputStream inputStream = this.getClass().getResourceAsStream("/Resources/block");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("//") || line.trim().equals("")) {
                    continue;
                }
                String[] list = line.split(",");
                if (list.length == 5) {
                    if (list[4].equals("1")) {
                        for (int i = Integer.valueOf(list[0]); i < this.width; i += blockImage[Integer.valueOf(list[2])][0].getWidth(null)) {
                            allBlocks.get(0).add(new Block(i, Integer.valueOf(list[1]), 0, 0, blockImage[Integer.valueOf(list[2])], this.events, 1, level));
                            allBlocks.get(1).add(new Block(i, Integer.valueOf(list[1]), 0, 0, blockImage[Integer.valueOf(list[2])], this.events, 1, level));
                        }
                    } else {
                        for (int j = Integer.valueOf(list[1]); j < this.height; j += blockImage[Integer.valueOf(list[2])][0].getHeight(null)) {
                            allBlocks.get(0).add(new Block(Integer.valueOf(list[0]), j, 0, 0, blockImage[Integer.valueOf(list[2])], this.events, 1, level));
                            allBlocks.get(1).add(new Block(Integer.valueOf(list[0]), j, 0, 0, blockImage[Integer.valueOf(list[2])], this.events, 1, level));
                        }
                    }
                } else if (list.length == 6) {
                    int[] input = new int[6];
                    for (int i = 0; i < 6; i++) {
                        input[i] = Integer.valueOf(list[i]);
                    }
                    for (int i = 0; i < input[5]; i++) {
                        allBlocks.get(input[4] - 1).add(new Block(input[0] + i * blockImage[input[2]][0].getWidth(null), input[1], 0, 0, blockImage[input[2]], this.events, input[3], level));
                    }
                } else {
                    int[] input = new int[6];
                    for (int i = 0; i < 6; i++) {
                        input[i] = Integer.valueOf(list[i]);
                    }
                    for (int i = 0; i < input[5]; i++) {
                        allBlocks.get(input[4] - 1).add(new Block(input[0] + (int) (1.5 * i * blockImage[input[2]][0].getWidth(null)), input[1], 0, 0, blockImage[input[2]], this.events, input[3], level));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        bGr.drawImage(img, 0, 0, width, height, null);

        for (Thing block : allBlocks.get(level.getType() - 1)) {
            block.draw(bGr, null);
        }
        bGr.dispose();
        return bimage;
    }

    public void updateBackground() {

        BufferedImage bimage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        if (level.getType() == 1) {
            bGr.drawImage(backgroundImg[0], 0, 0, width, height, null);
        } else if (level.getType() == 2) {
            bGr.drawImage(backgroundImg[1], 0, 0, width, height, null);
        } else if (level.getType() == 3) {
            bGr.drawImage(backgroundImg[2], 0, 0, width, height, null);
        } else {
            bGr.drawImage(backgroundImg[3], 0, 0, width, height, null);
        }

        Iterator<Thing> it = allBlocks.get(level.getType() - 1).listIterator();
        while (it.hasNext()) {
            Thing block = it.next();
            if (!block.getRDone()) {
                block.draw(bGr, null);
            }
        }
        bGr.dispose();
        if (level.getType() == 1) {
            this.background[0] = bimage;
        } else if (level.getType() == 2) {
            this.background[1] = bimage;
        } else if (level.getType() == 3) {
            this.background[2] = bimage;
        } else {
            this.background[3] = bimage;
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean valid(Thing thing, int x, int y) {
        for (Thing block : allBlocks.get(level.getType() - 1)) {
            if (block.isOverlap(thing, x, y)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Thing> getBlocks() {
        return allBlocks.get(level.getType() - 1);
    }


}
