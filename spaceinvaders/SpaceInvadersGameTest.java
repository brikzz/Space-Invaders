package org.cis1200.spaceinvaders;

import org.cis1200.SpaceInvaders.GameCourt;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class SpaceInvadersGameTest {

    //checks if the bugs don't move down right away
    @Test
    public void checkIfBugDoesntGoDownRightAway() {
        JLabel status = new JLabel("Running...");
        GameCourt game = new GameCourt(status);
        game.reset();
        boolean noDown = game.getFirstBugPy() == 0;
        assertTrue(noDown);
    }


    @Test
    public void checkingIfSwammoMovesWhenTriggeredTo() {
        JLabel status = new JLabel("Running...");
        GameCourt game = new GameCourt(status);
        game.tick();
        game.setClickedToTrue();
        boolean swammoVelo = game.getSwammo().getPy() < 550;
        assertTrue(swammoVelo);
    }

    //checking if starting position of swap and swammo is correct
    @Test
    public void swapAndSwammoStartingPositionIsCorrect() {
        JLabel status = new JLabel("Running...");
        GameCourt game = new GameCourt(status);
        game.reset();
        int swapExpectedPx = 500;
        int swapExpectedPy = 550;
        int swapAmmoExpectedPx = 530;
        int swapAmmoExpectedPy = 550;
        assertEquals(swapExpectedPx, game.getSwap().getPx());
        assertEquals(swapExpectedPy, game.getSwap().getPy());
        assertEquals(swapAmmoExpectedPx, game.getSwammo().getPx());
        assertEquals(swapAmmoExpectedPy, game.getSwammo().getPy());
    }

    //checking to see if the bug ammo is correctly being called to randomly shoot
    //because it starts in a negative y point
    @Test
    public void ifBammoWasUnleashed() {
        JLabel status = new JLabel("Running...");
        GameCourt game = new GameCourt(status);
        game.reset();
        game.btick();
        boolean bammoVelo = game.getBammo().getPy() > 0;
        assertTrue(bammoVelo);
    }

    @Test
    public void resetIsCorrect() {
        JLabel status = new JLabel("Running...");
        GameCourt game = new GameCourt(status);
        game.reset();
        assertFalse(game.isClicked());
        assertFalse(game.isLost());
        assertFalse(game.isWon());
        assertNotNull(game.getBammo());
        assertNotNull(game.getSwammo());
    }

}
