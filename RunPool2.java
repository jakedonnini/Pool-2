package org.cis120.Pool2;

import java.awt.*;
import javax.swing.*;

public class RunPool2 implements Runnable {
    public void run() {
        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Pool 2");
        frame.setLocation(300, 300);

        // info popup
        JFrame info = new JFrame("Pool 2");
        info.setLocation(700, 400);
        info.setBackground(Color.GREEN);
        final JPanel title = new JPanel();
        final JPanel directions = new JPanel();
        final JLabel info_title = new JLabel("How To Play");
        final JTextArea info_body = new JTextArea(
                        "Drag the mouse away form the cue ball to charge your shot. The \n" +
                        "farther it is the more powerful the shot. The first player to \n" +
                        "sink a ball will become the color of that ball. After each shot \n" +
                        "the turn switches, unless they made a shot of their color in which \n" +
                        "case they go again. If the cue ball is put in a pocket, the shot \n" +
                        "misses all balls or the cue ball hits the wrong color first then \n" +
                        "the next click places the cue ball in the best place for the \n" +
                        "other player. The game is over when the 8-ball is scored. If \n" +
                        "the player who scores it has no balls left they win and if \n" +
                        "they did have balls left the other player wins");
        info_body.setEditable(false);
        title.add(info_title);
        directions.add(info_body);
        info.add(directions, BorderLayout.CENTER);
        info.add(title, BorderLayout.NORTH);
        info.pack();

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        final JLabel turnStatus = new JLabel("Who's Turn");
        final JLabel spacer = new JLabel("      ");
        status_panel.add(status);
        status_panel.add(spacer);
        status_panel.add(turnStatus);

        // Main playing area
        final Table table = new Table(status, turnStatus);
        frame.add(table, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> table.reset());
        control_panel.add(reset);

        // save button
        final JButton save = new JButton("Save");
        save.addActionListener(e -> table.save());
        control_panel.add(save);

        // load button
        final JButton load = new JButton("Load");
        load.addActionListener(e -> table.load());
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        info.setVisible(true);
        // Start game
        table.reset();
    }
}
