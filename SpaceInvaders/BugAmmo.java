package org.cis1200.SpaceInvaders;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class BugAmmo extends Ammo {
    final private Color color;

    public BugAmmo(int courtWidth, int courtHeight, Color color) {
        super(courtWidth, courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    @Override
    public void move() {
        this.setPy(this.getPy() + this.getVy());
    }
}