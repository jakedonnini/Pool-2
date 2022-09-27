=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: jdonnini
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections or Maps - The balls are stored in an ArrayList. The ArrayList, "allBalls",
  if of type "Ball" and contains all the balls. It iterates through the collisions between balls and draws them.
  It is an ArrayList because balls have to be added a removed multiple times during the game and its not a linked
  list because it doesn't matter.

  2. File I/O - Clicking the "save" button saves the turn, color of each player and the position of each ball.
  These are all written to the file Save.txt using a BufferedWriter. Then when the "load" button is clicked the
  state that was saved to the file is loaded and the game is continued from where it was saved. The file holds all
  the relevant data to a game.

  3. Inheritance and Subtyping - The classes Ball, CueBall and Cue all implement the interface Hittable. This
  denotes the classes as able to be collided with. The Hit class takes two hittable objects and performs the
  calculations. The CueBall class extend the Ball class and overrides the draw function. This is done so that it
  has all the functions of the Ball class but limits the Cue class to only working with the CueBall. The Cue and
  the CueBall both implement Hittable because the interactions is considered a hit just as between two balls.

  4. JUnit Testable Component - There are JUnit test to test some key features of the classes. In the Hit class the
  interactions between the balls in various states and positions are tested to verify the equations. The Ball
  class has the constructor and a few key methods tested. In the Turn class, the key logic of the turns are tested.
  The Cue class functionality is also tested.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
    - Table: Handles the table and inter ball interactions. It controls the pockets in the table and has a tick
             function that is executed every few milliseconds that updates the balls positions. Also implements
             the save and load functions.
    - Ball: Holds all the data of a ball (position, velocity, number) and detects if it hits a wall, moves the balls
            according to their velocity, and holds the redraw function.
    - CueBall: Holds all the data for the cue ball and interactions with the cue.
    - Cue: Draws the Cue around the cue ball and based on the distance of the mouse from the ball determines the power
           of the shot.
    - Turn: Handles the data for the first and second player and keeps track of and changes the turn.
    - Hit: Handles the calculations of the collisions between hittable objects.
    - Hittable: This interface defines the required classes used in Hit.
    - RunPool2: This class runs the game and swing components.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    - The physics of the balls was very difficult to implement, and I went through a few implementation
      of equations to get it right. I keep having a problem with the coalitions not registering and the balls getting
      stuck together.
    - Initially the turns were handled in the Table class, but they got too complex, so I made a separate class for it.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    - For the most part the separate classes do a good job at making the code effect. Table could be a bit
      less cluttered
    - Some parts of Table could be refracted like reworking "reset" to be more general as the code is partially reused
      once.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Pool Physics equations https://www.real-world-physics-problems.com/physics-of-billiards.html
