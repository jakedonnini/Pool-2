package org.cis120.Pool2;
import java.awt.*;

/**
 * This is the ball class. It controls the pool ball's location,
 * speed and angle, number, and color.
 *
 */
public class Ball implements Hittable {
    // calculated from the center
    private int posX;
    private int posY;
    // drawn from the corner
    private int posDrawX;
    private int posDrawY;
    private final double radius = 14;
    final int drawOffsetX = -(int) radius;
    final int drawOffsetY = -(int) radius;
    private final int number;
    private double velX;
    private double velY;
    double startVelX;
    double startVelY;
    boolean isScored;
    private Color color = Color.WHITE;

    /**
     * make a ball
     * @param x starting position x
     * @param y starting position y
     * @param number the balls number
     */
    public Ball(int x, int y, int number) {
        if (number > 15 || number < 0) {
            throw new IllegalArgumentException("invalid ball number");
        }
        this.posX = x ;
        this.posY = y ;
        this.posDrawX = x + drawOffsetX;
        this.posDrawY = y + drawOffsetY;
        this.number = number;
        this.isScored = false;
    }

    /**
     * bounce off wall
     */
    public void hitWall() {
        if (posX > Table.WALL_WIDTH / 2.0 + Table.TABLE_WIDTH + radius
                || posX < Table.WALL_WIDTH) {
            velX *= -1;
            startVelX *= -1;
        }
        if (posY > Table.WALL_WIDTH / 2.0 + Table.TABLE_HEIGHT + radius
                || posY < Table.WALL_WIDTH) {
            velY *= -1;
            startVelY *= -1;
        }
        clip();
    }

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    private void clip() {
        this.posX = Math.min(Math.max(this.posX, Table.WALL_WIDTH),
                Table.WALL_WIDTH / 2 + Table.TABLE_WIDTH + (int) radius);
        this.posY = Math.min(Math.max(this.posY, Table.WALL_WIDTH),
                Table.WALL_WIDTH / 2 + Table.TABLE_HEIGHT + (int) radius);
    }

    /**
     * Moves the object by its velocity. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void move(double dist) { // drifts fix later
        // the friction that slows down the balls
        double friction = 0.02;

        // Makes the balls slow down better and look more realistic
        if (this.velX > 1 && this.velY > 1) {
            friction += 0.0075;
        } else {
            friction = 0.04;
        }

        // Following statements check the value of the X and Y values to slow
        // down the ball accordingly
        if (this.velX < 0.02 && this.velX > 0) {
            this.velX = 0;
        } else if (this.velX > -0.02 && this.velX < 0) {
            this.velX = 0;
        } else if (this.velX > 0) {
            this.velX -= friction;
        } else if (this.velX < 0) {
            this.velX += friction;
        }
        if (this.velY < 0.02 && this.velY > 0) {
            this.velY = 0;
        } else if (this.velY > -0.02 && this.velY < 0) {
            this.velY = 0;
        } else if (this.velY > 0) {
            this.velY -= friction;
        } else if (this.velY < 0) {
            this.velY += friction;
        }

        if (!(dist < 1 - (radius * 2))) {
            // have to convert to m form m/s
            this.posX += this.velX;
            this.posY += this.velY;
        }

        setPosX(posX);
        setPosY(posY);
    }


    /**
     * draw each ball
     */
    public void draw(Graphics g) {
        if (number == 8) {
            color = Color.BLACK;
            g.setColor(color);
        } else if (number > 8) {
            color = Color.BLUE;
            g.setColor(color);
        } else {
            color = Color.RED;
            g.setColor(color);
        }
        g.fillOval(posDrawX, posDrawY, (int) radius * 2, (int) radius * 2);
        g.setColor(Color.BLACK);
        // sets the number color according to ball
        if (number == 8) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString(Integer.toString(number),
                posDrawX + (int) (radius / 2), posDrawY + (int) ((radius * 6) / 4));
    }

    /**
     * returns string
     * @return returns the ball number, (x,y) position, (x,y) draw position
     */
    public String toString() {
        return number + ", " + posX + ", " + posY + ", " + posDrawX + ", " + posDrawY;
    }

    // --------- getters and setters ----------

    public double getRadius() {
        return this.radius;
    }

    public int getNumber() {
        return this.number;
    }

    public Color getColor() {
        return color;
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

    public void setPosX(int x) {
        this.posX = x;
        this.posDrawX = x + drawOffsetX;
    }

    public void setPosY(int y) {
        this.posY = y;
        this.posDrawY = y + drawOffsetX;
    }

    public void setVelX(double x) {
        this.velX = x;
    }

    public void setVelY(double y) {
        this.velY = y;
    }
}
