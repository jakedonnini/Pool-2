package org.cis120.Pool2;

import java.awt.*;

/**
 * hits the cue ball and adds mouse functionality
 */
public class Cue implements Hittable {
    // get the location of the cue ball
    private int startX;
    private int startY;
    private int posX;
    private int posY;
    private double velX;
    private double velY;
    private int mouseX;
    private int mouseY;
    private final CueBall ball;
    private boolean visible;

    /**
     * Creates a new cue at the current pos of the ball
     * @param ball the ball to be moved
     */
    public Cue(CueBall ball) {
        this.velX = 0.0;
        this.velY = 0.0;
        this.startX = ball.getPosX();
        this.startY = ball.getPosY();
        this.startX = ball.getPosX();
        this.startY = ball.getPosY();
        this.ball = ball;
    }

    /**
     * Makes a hit on the ball
     * @param mouseX x pos of mouse
     * @param mouseY y pos of mouse
     * @return a hit object that will dictate the direction and speed of the ball
     */
    public Hit shot(int mouseX, int mouseY) {
        update();
        // convert to polar coordinates
        int deltaX = startX - mouseX;
        int deltaY = startY - mouseY;
        double r = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double theta = Math.atan2(deltaY, deltaX);
        double v = r * 0.05; // kinda arbitrary adjust to taste
        //System.out.println(v);
        // cap the max speed at 20
        if (v >= 20) {
            v = 20;
        }
        velX = v * -Math.cos(theta);
        velY = v * -Math.sin(theta);
        return new Hit(this.ball, this);
    }

    public void draw(Graphics g) {
        update();
        startX = startX - (int) ball.getRadius();
        startY = startY - (int) ball.getRadius();
        g.setColor(Color.BLACK);
        int deltaX = startX - mouseX;
        int deltaY = startY - mouseY;
        // convert to polar to draw stick
        double r = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double theta = Math.atan2(deltaY, deltaX);
        double phi = 0.02;
        // calc guide line
        int x0 = (int) (startX + ball.getRadius() + ball.getRadius() * -Math.cos(theta));
        int y0 = (int) (startY + ball.getRadius() + ball.getRadius() * -Math.sin(theta));
        int x01 = (int) (startX + ball.getRadius() + 110 * -Math.cos(theta));
        int y01 = (int) (startY + ball.getRadius() + 110 * -Math.sin(theta));

        int x1 = (int) (startX + ball.getRadius() + r * Math.cos(theta - phi));
        int y1 = (int) (startY + ball.getRadius() + r * Math.sin(theta - phi));
        // stick is half the length of the table long
        int x2 = (int) (startX + ball.getRadius()
                + (r + Table.TABLE_WIDTH / 2) * Math.cos(theta - phi));
        int y2 = (int) (startY + ball.getRadius()
                + (r + Table.TABLE_WIDTH / 2) * Math.sin(theta - phi));
        // mirrored to mke the stick
        int x3 = (int) (startX + ball.getRadius() + r * Math.cos(theta + phi));
        int y3 = (int) (startY + ball.getRadius() + r * Math.sin(theta + phi));
        // stick is half the length of the table long
        int x4 = (int) (startX + ball.getRadius()
                + (r + Table.TABLE_WIDTH / 2) * Math.cos(theta + phi));
        int y4 = (int) (startY + ball.getRadius()
                + (r + Table.TABLE_WIDTH / 2) * Math.sin(theta + phi));
        int[] pointsX = {x1, x3, x4, x2};
        int[] pointsY = {y1, y3, y4, y2};
        // if the color is not determined make black
        if (Turn.isPlayerOnesTurn) {
            g.setColor(Turn.p1Color);
        } else {
            g.setColor(Turn.p2Color);
        }
        g.fillPolygon(pointsX, pointsY, 4);
        g.setColor(Color.BLACK);
        g.drawLine(x0, y0, x01, y01);
    }

    public void update() {
        this.startX = ball.getPosX();
        this.startY = ball.getPosY();
    }

    public void setMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void setVisible(boolean b) {
        this.visible = b;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public double getVelX() {
        return this.velX;
    }

    public double getVelY() {
        return this.velY;
    }

    public int getNumber() {
        return -1;
    }
}
