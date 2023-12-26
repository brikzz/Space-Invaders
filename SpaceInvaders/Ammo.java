package org.cis1200.SpaceInvaders;

import java.awt.*;

public abstract class Ammo extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_POS_X = -200;
    public static final int INIT_POS_Y = -200;
    public static final int INIT_VEL_X = 3;
    public static final int INIT_VEL_Y = 10;

    public Ammo(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

    }

    //@Override
    public abstract void draw(Graphics g);

    @Override
    public abstract void move();
}
