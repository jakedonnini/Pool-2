package org.cis120.Pool2;

import java.awt.*;

/**
 * the Queue ball extends the ball class because it should behave the same, but
 * it has extra properties. Only a cue ball can be used with the cue
 */
public class CueBall extends Ball implements Hittable {
    public CueBall(int x, int y) {
        super(x, y, 0);
    }

    /**
     * draw the ball
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(getPosX() + drawOffsetX, getPosY() + drawOffsetY,
                (int) (super.getRadius() * 2), (int) (super.getRadius() * 2));
    }
}