package org.cis1200.SpaceInvaders;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Bug extends GameObj {
    public static final String IMG_FILE = "files/bug.png";
    public static final int SIZE = 60;
    public static final int INIT_POS_X = 0;
    public static final int INIT_POS_Y = 0;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 0;
    private boolean isHit;

    private static BufferedImage img;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Bug(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        isHit = false;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    public void setIsHit() {
        isHit = true;
    }

    public boolean getIsHit() {
        return isHit;
    }



    /*@Override
    public void move() {
        if (this.getPx() > 1000 || this.getPy() < 0) {
            this.setPy(this.getPy() + 100);
            this.setVx(this.getVx() * -1);
        }
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());
    }*/
}