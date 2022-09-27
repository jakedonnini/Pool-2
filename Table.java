package org.cis120.Pool2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * makes the pool table and handles the logic
 */
public class Table extends JPanel {
    private final JLabel status; // current status text
    private final JLabel turnStatus; // current turn
    private boolean moving = false; // whether the game is running
    private boolean gameOver = false; // is the game still going
    private boolean hasScratched = false;
    private boolean firstHit = true;
    private boolean player1Won;

    // colors
    static final Color BROWN = new Color(101, 67, 33);
    static final Color POOL_GREEN = new Color(10, 108, 3);

    // Game constants
    static final int TABLE_WIDTH = 1000;
    static final int TABLE_HEIGHT = 500;
    static final int WALL_WIDTH = 50;
    static final int BOARD_WIDTH = TABLE_WIDTH + WALL_WIDTH * 2;
    static final int BOARD_HEIGHT = TABLE_HEIGHT + WALL_WIDTH * 2;

    /**
     * pockets
     * 6 pockets
     * 0 = x point
     * 1 = y point
     * 2 = radius
     */
    private final int pocketRadius = 30;
    private final int[][] pockets = {
            {TABLE_WIDTH + WALL_WIDTH, WALL_WIDTH, pocketRadius},
            {(TABLE_WIDTH + WALL_WIDTH) / 2, WALL_WIDTH, pocketRadius},
            {WALL_WIDTH, WALL_WIDTH, pocketRadius},
            {TABLE_WIDTH + WALL_WIDTH, TABLE_HEIGHT + WALL_WIDTH, pocketRadius},
            {(TABLE_WIDTH + WALL_WIDTH) / 2, TABLE_HEIGHT + WALL_WIDTH, pocketRadius},
            {WALL_WIDTH, TABLE_HEIGHT + WALL_WIDTH, pocketRadius}
    };
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 10;

    // create balls
    private ArrayList<Ball> allBalls;
    private CueBall cueBall;
    private Cue cue;
    private int p1Balls;
    private int p2Balls;

    public Table(JLabel status, JLabel turnStatus) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(POOL_GREEN);

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        /*
         * Listens for mouse clicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!moving && !hasScratched) {
                    Point p = e.getPoint();

                    // updates the model given the coordinates of the mouseclick
                    cue.setVisible(true);
                    cue.setMouse(p.x, p.y);

                    repaint(); // repaints the game board
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (!moving) {
                    if (hasScratched) {
                        // if they scratch end the streak
                        scratch(p.x, p.y);
                        hasScratched = false;
                    } else {
                        Hit hit = cue.shot(p.x, p.y);
                        cueBall.setVelX(hit.velX1);
                        cueBall.setVelY(hit.velY1);
                        cueBall.startVelX = hit.velX1;
                        cueBall.startVelY = hit.velY1;
                        // restarting the moving process
                        Turn.hasScoredThisTurn = false;
                        moving = true;
                        firstHit = true;
                        cue.setVisible(false);
                    }
                }
                repaint(); // repaints the game board
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);

        this.status = status;
        this.turnStatus = turnStatus;
        reset();
    }

    /**
     * This method saves the position of the balls and the players colors
     */
    public void save() {
        System.out.println("save");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Save.txt"));
            writer.write(String.valueOf(Turn.isPlayerOnesTurn));
            writer.newLine();
            String p1 = Turn.p1Color.getRed() + " " + Turn.p1Color.getGreen()
                    + " " + Turn.p1Color.getBlue();
            String p2 = Turn.p2Color.getRed() + " " + Turn.p2Color.getGreen()
                    + " " + Turn.p2Color.getBlue();
            writer.write(p1 + ", " + p2);
            writer.newLine();
            for (Ball ball : allBalls) {
                writer.write(ball.toString());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            status.setText("No Save File");
        }
    }

    /**
     * This method loads a saved file
     */
    public void load() {
        System.out.println("load");
        ArrayList<Ball> savedBalls = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
            String turnLine = reader.readLine();
            boolean turn = Boolean.parseBoolean(turnLine);
            String colorLine = reader.readLine();
            String[] colors = colorLine.split(", ");
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    String[] values = line.split(", ");

                    if (Integer.parseInt(values[0]) == 0) {
                        this.cueBall = new CueBall(Integer.parseInt(values[1]),
                                Integer.parseInt(values[2]));
                        // add the new ball
                        savedBalls.add(cueBall);
                    } else {
                        // create a new ball based on the file
                        Ball ball = new Ball(Integer.parseInt(values[1]),
                                Integer.parseInt(values[2]), Integer.parseInt(values[0]));
                        // add the new ball
                        savedBalls.add(ball);
                    }
                } else {
                    break;
                }
            }
            reader.close();
            allBalls = savedBalls;
            Turn.isPlayerOnesTurn = turn;
            // get the color of the cue
            String[] playerOneRgb = colors[0].split(" ");
            String[] playerTwoRgb = colors[1].split(" ");
            Color p1 = new Color(Integer.parseInt(playerOneRgb[0]),
                    Integer.parseInt(playerOneRgb[1]), Integer.parseInt(playerOneRgb[2]));
            Color p2 = new Color(Integer.parseInt(playerTwoRgb[0]),
                    Integer.parseInt(playerTwoRgb[1]), Integer.parseInt(playerTwoRgb[2]));
            Turn.setColors(p1, p2);
            // basically call reset without resetting the balls map
            this.cue = new Cue(cueBall);
            cue.setVisible(true);
            moving = false;
            gameOver = false;
            hasScratched = false;
            firstHit = true;
            status.setText("Hit the Cue Ball");

            // Make sure that this component has the keyboard focus
            requestFocusInWindow();
            repaint(); // repaints the game board
        } catch (IOException e) {
            status.setText("No Load File");
        }
    }

    /**
     * call when a ball hits a pocket and remove form list
     */
    public void scoreBall(Ball ball) {
        // the first ball in determines the color of each player
        if (allBalls.size() == 16 && ball.getNumber() != 0) {
            if (Turn.isPlayerOnesTurn) {
                if (ball.getNumber() < 8) {
                    Turn.setColors(Color.RED, Color.BLUE);
                } else {
                    Turn.setColors(Color.BLUE, Color.RED);
                }
            } else {
                if (ball.getNumber() > 8) {
                    Turn.setColors(Color.RED, Color.BLUE);
                } else {
                    Turn.setColors(Color.BLUE, Color.RED);
                }
            }
        }
        // calculate how many balls each player has left
        p1Balls = 0;
        p2Balls = 0;
        for (Ball ball1 : allBalls) {
            if (ball1.getNumber() > 8) {
                p2Balls++;
            } else if (ball1.getNumber() < 8 && ball1.getNumber() != 0) {
                p1Balls++;
            }
        }
        // win
        if ((p1Balls == 0 || p2Balls == 0) && ball.getNumber() == 8) {
            // System.out.println("P1 " + p1Balls + " P2 " + p2Balls);
            allBalls.remove(ball);
            moving = false;
            gameOver = true;
            player1Won = p1Balls != 0;
            // lose
        } else if (allBalls.size() != 2 && ball.getNumber() == 8) {
            allBalls.remove(ball);
            moving = false;
            gameOver = true;
            player1Won = !Turn.isPlayerOnesTurn;
        } else if (ball.getNumber() == 0) {
            // scratch
            allBalls.remove(ball);
            hasScratched = true;
        } else {
            // get normal ball in
            if (Turn.getCurrentPlayerColor().equals(ball.getColor())
                    || Turn.getCurrentPlayerColor().equals(Color.BLACK)) {
                Turn.scoreStreak();
            }
            allBalls.remove(ball);
        }
    }

    /**
     * place the cue ball after a scratch
     * @param lastMouseX mouse x pos
     * @param lastMouseY mouse y pos
     */
    public void scratch(int lastMouseX, int lastMouseY) {
        allBalls.remove(cueBall);
        this.cueBall = new CueBall(lastMouseX, lastMouseY);
        this.cue =  new Cue(cueBall);
        allBalls.add(0, cueBall);
    }

    /**
     * re-create all the balls
     */
    public void reset() {
        allBalls = new ArrayList<>();
        int midX = (BOARD_WIDTH / 4) * 3;
        int midY = BOARD_HEIGHT / 2;
        int spread = 22;
        // create a 2d array to line up the balls in a triangle
        int[][] triangle = {
                {midX, midY},
                {midX + spread, midY + spread}, {midX + spread, midY - spread},
                {midX + 2 * spread, midY + 2 * spread}, {midX + 2 * spread, midY},
                {midX + 2 * spread, midY - 2 * spread},
                {midX + 3 * spread, midY + 3 * spread}, {midX + 3 * spread, midY + spread},
                {midX + 3 * spread, midY - spread}, {midX + 3 * spread, midY - 3 * spread},
                {midX + 4 * spread, midY + 4 * spread}, {midX + 4 * spread, midY + 2 * spread},
                {midX + 4 * spread, midY}, {midX + 4 * spread, midY - 2 * spread},
                {midX + 4 * spread, midY - 4 * spread}
        };
        int[] order = {1, 15, 14, 2, 8, 3, 13, 4, 12, 5, 11, 6, 10, 7, 9};
        for (int i = 1; i < 16; i++) {
            // add each ball to the map
            allBalls.add(new Ball(triangle[i - 1][0], triangle[i - 1][1], order[i - 1]));
        }
        // create cue ball
        this.cueBall = new CueBall(BOARD_WIDTH / 4, midY);
        this.cue =  new Cue(cueBall);
        cue.setVisible(true);
        moving = false;
        gameOver = false;
        hasScratched = false;
        firstHit = true;
        // add cue ball to list
        allBalls.add(cueBall);
        status.setText("Hit the Cue Ball");
        Turn.reset();
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        repaint(); // repaints the game board
    }

    /**
     * Checks all the balls to see if they have stopped moving
     * if so changes moving to false
     */
    public void checkMoving() {
        int count = 0;
        for (Ball ball : allBalls) {
            if (ball.getVelX() == 0 && ball.getVelY() == 0) {
                count++;
            }
        }
        moving = count != allBalls.size();
        if (!moving) {
            // scratch if there was no hit
            if (firstHit) {
                hasScratched = true;
            }
            // make sure that the players turn ends if they scratch
            if (hasScratched) {
                Turn.hasScoredThisTurn = false;
            }
            // change turn
            Turn.changeTurn();
            for (Ball ball : allBalls) {
                System.out.print(ball.getNumber() + ", ");
            }
            System.out.println("allBalls");
        }
    }

    void tick() {
        if (moving && !gameOver) {
            // move the balls
            for (int i = 0; i < allBalls.size(); i++) {
                Ball ball = allBalls.get(i);
                double dist = ball.getRadius() * 2;
                // check for collision
                for (int j = i + 1; j < allBalls.size(); j++) {
                    Ball ball2 = allBalls.get(j);
                    if (ball != ball2) {
                        dist = Math.sqrt(Math.pow(ball.getPosX() - ball2.getPosX(), 2) +
                                                Math.pow(ball.getPosY() - ball2.getPosY(), 2));
                        // if the distance is less than the
                        // diameter of a ball then there is a collision
                        if (dist <=  ball.getRadius() * 2.0) {
                            // prevent multi hits if the balls are stuck inside each other
                            if (dist > ball.getRadius() * 0.5) {
                                //System.out.print(" " + dist + " ");
                                Hit hit = new Hit(ball, ball2);
                                // test if the first hit matches player color otherwise scratch
                                if (firstHit) {
                                    // if colors are decided, and the ball is not the players color,
                                    // and it's not the at the last ball
                                    if (!Turn.getCurrentPlayerColor().equals(Color.BLACK)
                                         && (!ball2.getColor()
                                            .equals(Turn.getCurrentPlayerColor()))) {
                                        if (!(p1Balls <= 1 && !Turn.isPlayerOnesTurn)
                                                && !(p2Balls <= 1 && Turn.isPlayerOnesTurn)) {
                                            hasScratched = true;
                                        }
                                    }
                                    firstHit = false;
                                }
                                ball.setVelX(hit.velX1);
                                ball.setVelY(hit.velY1);
                                ball.startVelX = hit.velX1;
                                ball.startVelY = hit.velY1;
                                ball2.setPosX(hit.posX2);
                                ball2.setPosY(hit.posY2);
                                ball2.setVelX(hit.velX2);
                                ball2.setVelY(hit.velY2);
                                ball2.startVelX = hit.velX2;
                                ball2.startVelY = hit.velY2;

                                double stopVal = 0.01;
                                // prevent infinite hitting
                                if (((ball.getVelX() < stopVal && ball.getVelX() > -stopVal)
                                        && (ball.getVelY() < stopVal && ball.getVelY() > -stopVal))
                                        || ((ball2.getVelX() < stopVal
                                        && ball2.getVelX() > -stopVal)
                                                && (ball2.getVelY()  < stopVal
                                                && ball2.getVelY() > -stopVal))) {
                                    ball.setPosX(ball.getPosX() - 1);
                                    ball.setPosY(ball.getPosY() - 1);
                                    ball2.setPosX(ball2.getPosX() + 1);
                                    ball2.setPosY(ball2.getPosY() + 1);
                                }
                            }


                            // prevent balls from sticking
                            if (dist < ball.getRadius() * 1.6) {
                                // the direction the balls were moving
                                int getSignX1 = (int) (ball.getVelX() / Math.abs(ball.getVelX()));
                                int getSignY1 = (int) (ball.getVelY() / Math.abs(ball.getVelY()));
                                int getSignX2 = (int) (ball2.getVelX() / Math.abs(ball2.getVelX()));
                                int getSignY2 = (int) (ball2.getVelY() / Math.abs(ball2.getVelY()));
                                if (getSignX1 == getSignX2) {
                                    getSignX2 = -getSignX2;
                                }
                                if (getSignY1 == getSignY2) {
                                    getSignY2 = -getSignY2;
                                }
                                ball.setPosX(ball.getPosX() + getSignX1);
                                ball.setPosY(ball.getPosY() + getSignY1);
                                ball2.setPosX(ball2.getPosX() + getSignX2);
                                ball2.setPosY(ball2.getPosY() + getSignY2);
                            }
                        }
                    }
                }
                // move the ball
                ball.move(dist);
                // check if hits pocket
                for (int[] pocket : pockets) {
                    double distFromPocket = Math.sqrt(Math.pow(ball.getPosX()
                            - (pocket[0] - pocket[2] / 2.0), 2) +
                            Math.pow(ball.getPosY() - (pocket[1] - pocket[2] / 2.0), 2));
                    if (distFromPocket < (pocket[2] + ball.getRadius() * 0.8)) {
                        scoreBall(ball);
                        break;
                    }
                }
                // check if hit wall
                ball.hitWall();
            }
            status.setText("Moving...");
            // check if the balls are moving
            checkMoving();
            // update the display
            repaint();
        } else {
            status.setText("Hit the Cue Ball");
            if (hasScratched) {
                status.setText("Click to place the ball");
            }
            if (Turn.isPlayerOnesTurn) {
                turnStatus.setText("Player 1's turn");
            } else {
                turnStatus.setText("Player 2's turn");
            }
            if (gameOver) {
                if (player1Won) {
                    status.setText("Player 1 Wins!");
                } else {
                    status.setText("Player 2 Wins!");
                }
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BROWN); // make brown
        // right vert wall
        g.fillRect(BOARD_WIDTH - WALL_WIDTH, WALL_WIDTH, WALL_WIDTH, TABLE_HEIGHT);
        // left vert wall
        g.fillRect(0, WALL_WIDTH, WALL_WIDTH, TABLE_HEIGHT);
        // bottom wall
        g.fillRect(0, BOARD_HEIGHT - WALL_WIDTH, BOARD_WIDTH, WALL_WIDTH);
        // top wall
        g.fillRect(0, 0, BOARD_WIDTH, WALL_WIDTH);

        // draw pockets
        g.setColor(Color.BLACK);
        for (int[] pocket : pockets) {
            g.setColor(Color.BLACK);
            g.fillOval(pocket[0] - pocket[2], pocket[1] - pocket[2],
                    pocket[2] * 2, pocket[2] * 2);
            g.setColor(Color.WHITE);
        }
        // cueBall.draw(g);
        for (Ball ball : allBalls) {
            ball.draw(g);
        }
        // if the cue is visible draw it
        if (cue.getVisible()) {
            cue.draw(g);
        }
        // display end game message
        if (gameOver) {
            if (player1Won) {
                g.setColor(Color.BLACK);
                g.drawString("PLAYER 1 WON!", BOARD_WIDTH / 2, BOARD_HEIGHT / 2);
            } else {
                g.setColor(Color.BLACK);
                g.drawString("PLAYER 2 WON!", BOARD_WIDTH / 2, BOARD_HEIGHT / 2);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
