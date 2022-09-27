package org.cis120.Pool2;

/**
 * This controls and stores the hits between two balls or a ball and a que
 */
public class Hit {
    int posX1;
    int posY1;
    double velX1;
    double velY1;
    int posX2;
    int posY2;
    double velX2;
    double velY2;

    public Hit(Hittable ball1, Hittable ball2) {
        if (ball2 instanceof Cue) {
            velX1 = ball2.getVelX();
            velY1 = ball2.getVelY();
        } else {
            posX1 = ball1.getPosX();
            posY1 = ball1.getPosY();
            posX2 = ball2.getPosX();
            posY2 = ball2.getPosY();
            velX1 = ball1.getVelX();
            velY1 = ball1.getVelY();
            velX2 = ball2.getVelX();
            velY2 = ball2.getVelY();

            //Sets the angle for the bounce
            double theta = Math.atan2(posY2 - posY1, posX2 - posX1);

            //Finds both current angles both balls are following
            double vel1 = Math.cos(theta) * velX1 + Math.sin(theta) * velY1;
            double vel2 = Math.cos(theta) * velX2 + Math.sin(theta) * velY2;

            double mass1 = 0.17;
            double mass2 = 0.17;

            //Determines the bounce angles for both balls
            double bounceAngle1 = ((mass1 - mass2) * vel1 + 2 * mass2 * vel2) / (mass1 + mass2);
            double bounceAngle2 = ((mass2 - mass1) * vel2 + 2 * mass1 * vel1) / (mass1 + mass2);

            velX1 += Math.cos(theta) * (-vel1 + bounceAngle1);
            velY1 += Math.sin(theta) * (-vel1 + bounceAngle1);

            int normalVector = (int) ((-vel2 + bounceAngle2) / Math.abs(-vel2 + bounceAngle2));

            // make sure that it pushes the ball if too slow
            // maybe check to see if transferred velocity
            // is enough to clear the ball and if not move it
            if (velX1 < 1.5) {
                posX2 += (int) (Math.cos(theta) * normalVector * 2);
            }
            if (velY1 < 1.5) {
                posY2 += (int) (Math.sin(theta) * normalVector * 2);
            }

            double stopVal = 0.01;
            // prevent infinite hitting
            if (((velX1 < stopVal && velX1 > -stopVal)
                    && (velY1 < stopVal && velY1 > -stopVal)) ||
                    ((velX2 < stopVal && velX2 > -stopVal)
                            && (velY2 < stopVal && velY2 > -stopVal))) {
                posX1 = (posX1 - 1);
                posY1 = (posY1 - 1);
                posX2 = (posX2 + 1);
                posY2 = (posY2 + 1);
            }

            velX2 += Math.cos(theta) * (-vel2 + bounceAngle2);
            velY2 += Math.sin(theta) * (-vel2 + bounceAngle2);
        }
    }
}
