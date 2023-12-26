package org.cis1200.SpaceInvaders;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSpaceInvaders implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        final JButton undo = new JButton("Revive");
        control_panel.add(undo);
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                court.undo();
                undo.setVisible(false);
            }
        }) ;


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();

        //making the instructions
        JFrame instructFrame = new JFrame("Instructions for Space Invaders");
        instructFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea text = new JTextArea("\n" +
                 "  *PLEASE LEAVE THIS WINDOW OPEN*\n" +
                 "  \n" +
                 "  Welcome to SWAP VS BUGS\n" +
                 "  In this game, Swap is trying to debug your code!\n" +
                 "  Your goal is to shoot at all the bugs to debug your code.\n" +
                 "  To do this: \n" +
                 "  Click the Up arrow button to shoot\n" +
                 "  Click the Left arrow button to move left\n" +
                 "  Click the Right arrow button to move right\n" +
                 "  You will only be able to be revived once so be smart about reviving!!"
                );
        text.setPreferredSize(new Dimension(540, 200));
        instructFrame.getContentPane().add(text);

        instructFrame.setLocationRelativeTo(frame);
        instructFrame.pack();
        instructFrame.setVisible(true);
    }
}