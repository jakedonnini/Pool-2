package org.cis120.Pool2;

import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class PoolTests {

    // --------------- hit class -------------------

    @Test
    public void bothMovingHitTest() {
        Ball ball1 = new Ball(100, 100, 2);
        Ball ball2 = new Ball(120, 120, 1);
        ball1.setVelX(10);
        ball1.setVelY(5);
        ball2.setVelX(-10);
        ball2.setVelY(-5);
        Hit hit = new Hit(ball1, ball2);
        assertEquals(-5.0, hit.velX1, 0.1);
        assertEquals(-10.0, hit.velY1, 0.1);
        assertEquals(5.0, hit.velX2, 0.1);
        assertEquals(10.0, hit.velY2, 0.1);
    }

    @Test
    public void insideBallHitTest() {
        Ball ball1 = new Ball(100, 100, 2);
        Ball ball2 = new Ball(100, 100, 1);
        Hit hit = new Hit(ball1, ball2);
        assertEquals(99, hit.posX1);
        assertEquals(99, hit.posY1);
        assertEquals(101, hit.posX2);
        assertEquals(101, hit.posY2);
    }

    @Test
    public void oneMovingHitTest() {
        Ball ball1 = new Ball(100, 100, 2);
        Ball ball2 = new Ball(120, 120, 1);
        ball1.setVelX(10);
        ball1.setVelY(5);
        Hit hit = new Hit(ball1, ball2);
        assertEquals(2.5, hit.velX1, 0.1);
        assertEquals(-2.5, hit.velY1, 0.1);
        assertEquals(7.5, hit.velX2, 0.1);
        assertEquals(7.5, hit.velY2, 0.1);
    }

    // --------------- ball class -------------------

    @Test
    public void invalidNumberBallTest() {
        assertThrows(IllegalArgumentException.class, () ->  new Ball(100, 100, -1));
    }

    @Test
    public void ballHitWallTest() {
        Ball ball1 = new Ball(-10, 1000, 2);
        ball1.setVelX(10);
        ball1.setVelY(5);
        ball1.hitWall();
        assertEquals(-10, ball1.getVelX(), 0.1);
        assertEquals(-5, ball1.getVelY(), 0.1);
        assertEquals(50, ball1.getPosX());
        assertEquals(539, ball1.getPosY());
    }

    @Test
    public void stopBallTooCloseTest() {
        Ball ball1 = new Ball(120, 120, 2);
        Ball ball2 = new Ball(130, 130, 1);
        double dist = Math.sqrt(Math.pow(ball1.getPosX() - ball2.getPosX(), 2) +
                Math.pow(ball1.getPosY() - ball2.getPosY(), 2));
        ball1.setVelX(1);
        ball1.setVelY(1);
        Hit hit = new Hit(ball1, ball2);
        ball1.move(dist);
        assertEquals(119.0, hit.posX1, 0.1);
        assertEquals(119.0, hit.posY1, 0.1);
        assertEquals(132.0, hit.posX2, 0.1);
        assertEquals(132.0, hit.posY2, 0.1);
    }

    // --------------- turn class -------------------

    @Test
    public void resetTurn() {
        Turn.changeTurn();
        Turn.setColors(Color.WHITE, Color.GRAY);
        Turn.scoreStreak();
        assertFalse(Turn.isPlayerOnesTurn);
        assertEquals(Color.WHITE, Turn.p1Color);
        assertEquals(Color.GRAY, Turn.p2Color);
        assertTrue(Turn.hasScoredThisTurn);
        Turn.reset();
        assertTrue(Turn.isPlayerOnesTurn);
        assertEquals(Color.BLACK, Turn.p1Color);
        assertEquals(Color.BLACK, Turn.p2Color);
        assertFalse(Turn.hasScoredThisTurn);
    }

    // --------------- cue class -------------------

    @Test
    public void cueMaxHitTest() {
        CueBall ball = new CueBall(100, 100);
        Cue cue = new Cue(ball);
        Hit hit = cue.shot(500, 100);
        assertEquals(20.0, hit.velX1, 0.1);
        assertEquals(0, hit.velY1, 0.1);
        assertEquals(0, hit.velX2, 0.1);
        assertEquals(0, hit.velY2, 0.1);
    }

}
