import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class radianStuff extends PApplet {



//Here's some code to demonstrate the use of DYNAMIC ARRAYS to
//add and remove OBJECTS from a program.

//The objects in this case are BALLOONS (faces!) that live for a little while
//and are then extinguished. 

//This time a dynamic list is used to continuously create new balloons on mouseclick. They die after a while and are removed from memory.

//A global variable is used to record the number of balloons that have
//lived so far.

//also demonstrated: IF statements, pushMatrix/popMatrix techniques, PImage and font embedding
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//TRISTAN MILLER 2014
///////////////////////////////////////////////////////////////////////////////////////////////////////////


int balloonNumber;
int balloonLifetime = 240; //how many frames should a balloon last for?

boolean radianMode = false;
boolean invertMode = false;
boolean HSBMode = false;
ArrayList balloonList;   //must declare this at the beginning! Once created, this will store the many balloons we'll create.

int balloonLimit = 40;   //how many balloons should be allowed to exist simultaneously? Usually a good idea to set something up like this.


PFont font;


public void setup() {
  size(1920, 1080);

  balloonNumber = 0;

  balloonList = new ArrayList();  //create the balloonList (an ArrayList object)

  background(200);


  font = loadFont("Amstrad-CPC464-32.vlw");
  textFont(font, 48);
}



public void draw() {
  blendMode(BLEND);
  //background(0);    //UNCOMMENT THIS to get rid of the trails

  if (radianMode == false) {
    for (int i = 0; i < balloonList.size(); i++) {
      NumberedBalloon thisBalloon = (NumberedBalloon) balloonList.get(i);
      thisBalloon.rotationSpeed = radians(balloonNumber);
    }
  } 
  else {
    for (int i = 0; i < balloonList.size(); i++) {
      NumberedBalloon thisBalloon = (NumberedBalloon) balloonList.get(i);
      thisBalloon.rotationSpeed = balloonNumber;
    }
  }

  if (invertMode == false) {
    for (int i = 0; i < balloonList.size(); i++) {
      NumberedBalloon thisBalloon = (NumberedBalloon) balloonList.get(i);
      thisBalloon.shadowColour = color(0, 0, 0);
      thisBalloon.shadowAlpha = thisBalloon.balloonAlpha;
      blendMode(BLEND);
    }
  } 
  else {
    for (int i = 0; i < balloonList.size(); i++) {
      NumberedBalloon thisBalloon = (NumberedBalloon) balloonList.get(i);
      thisBalloon.shadowColour = color(255, 255, 255);
      thisBalloon.shadowAlpha = 0;
      blendMode(SCREEN);
    }
  }

  if (balloonList.size() != 0) { //if there are any balloons in balloonList, do the following:
    for (int i = 0; i < balloonList.size(); i++) { //for each balloon in balloonList,
      NumberedBalloon balloon = (NumberedBalloon) balloonList.get(i);  //get the balloon (here we have to be specific and mention that it's a NumberedBalloon object),

      balloon.update(); //update position and rotation of this balloon

      if (balloon.balloonTimer >= balloonLifetime) { //fade this balloon if it's lived too long
        balloon.fade();
      }

      balloon.display(); //display this balloon on screen

      if (balloon.balloonAlpha <= 0) { //remove this balloon from the list if it's faded completely
        balloonList.remove(i);
      }
    }
  }
}

public void mouseClicked() {          //do this stuff when the mouse is clicked

    if (balloonList.size() < balloonLimit) {  //if there aren't already too many balloons around...
    NumberedBalloon newBalloon = new NumberedBalloon(mouseX, mouseY, random(-3, 3), random(-3, 3), color(random(0, 255), random(0, 255), random(0, 255)));  //declare and create a new balloon, with random properties
    balloonList.add(newBalloon); //add this balloon to the list before it disappears
    balloonNumber ++;   //increase the global balloon number, so that the next one has a higher number
  }

  //alternatively, use a FOR LOOP to generate 5 balloons at once on each click...uncomment the following (and comment the above) to test

  /*
    for(int i = 0; i < 5; i++){
   if(balloonList.size() < balloonLimit){
   NumberedBalloon newBalloon = new NumberedBalloon(mouseX, mouseY, random(-3,3), random(-3,3), color(random(0,255),random(0,255),random(0,255)));  //declare and create a new balloon, with random properties
   balloonList.add(newBalloon); //add this balloon to the list before it disappears
   }
   } 
   */
} 


public boolean sketchFullScreen() {  //plop these lines at the end of your code to engage presentation mode (thanks S.H.)
  return true;
} 

public void keyPressed() {
  if (keyCode == 82) {
    if (radianMode == false) {
      radianMode = true;
    } 
    else {
      radianMode = false;
    }
  }

  if (keyCode == 73) {
    if (invertMode == false) {
      invertMode = true;
      background(0);
    } 
    else {
      invertMode = false;
      background(200);
    }
  }

  if (keyCode == 68 && balloonList.size() < 3*balloonLimit) {
    detonate();
  }

  if (keyCode == 67) {
    if (HSBMode == false) {
      HSBMode = true;
      colorMode(HSB);
    } 
    else {
      HSBMode = false;
      colorMode(RGB);
    }
  }
}

public void detonate() {
  int lenny = balloonList.size();
  for (int i = 0; i < lenny; i++) {
    NumberedBalloon thisBalloon = (NumberedBalloon) balloonList.get(i);
    if (thisBalloon.fragment == false) {

      NumberedBalloon redBalloon = new NumberedBalloon(thisBalloon.posiX, thisBalloon.posiY, random(-3, 3), random(-3, 3), color(red(thisBalloon.balloonColour), 0, 0));

      NumberedBalloon greenBalloon = new NumberedBalloon(thisBalloon.posiX, thisBalloon.posiY, random(-3, 3), random(-3, 3), color(0, green(thisBalloon.balloonColour), 0));
      NumberedBalloon blueBalloon = new NumberedBalloon(thisBalloon.posiX, thisBalloon.posiY, random(-3, 3), random(-3, 3), color(0, 0, blue(thisBalloon.balloonColour)));


      redBalloon.veloX = redBalloon.veloX + thisBalloon.veloX;
      redBalloon.veloY = redBalloon.veloY + thisBalloon.veloY;
      redBalloon.balloonTimer = thisBalloon.balloonTimer;
      redBalloon.balloonAlpha = thisBalloon.balloonAlpha;
      redBalloon.serialNumber = thisBalloon.serialNumber;
      redBalloon.fragment = true;

      greenBalloon.veloX = greenBalloon.veloX + thisBalloon.veloX;
      greenBalloon.veloY = greenBalloon.veloY + thisBalloon.veloY;
      greenBalloon.balloonTimer = thisBalloon.balloonTimer;
      greenBalloon.balloonAlpha = thisBalloon.balloonAlpha;
      greenBalloon.serialNumber = thisBalloon.serialNumber;
      greenBalloon.fragment = true;

      blueBalloon.veloX = redBalloon.veloX + thisBalloon.veloX;
      blueBalloon.veloY = redBalloon.veloY + thisBalloon.veloY;
      blueBalloon.balloonTimer = thisBalloon.balloonTimer;
      blueBalloon.balloonAlpha = thisBalloon.balloonAlpha;
      blueBalloon.serialNumber = thisBalloon.serialNumber;
      blueBalloon.fragment = true;

      balloonList.add(redBalloon);
      balloonList.add(greenBalloon);
      balloonList.add(blueBalloon);

      thisBalloon.balloonAlpha = 0;
      thisBalloon.balloonTimer = balloonLifetime;
    }
  }
}

//all of the below is to control and create balloons

class NumberedBalloon {
  /////////////////////
  ///CLASS VARIABLES (declare all variables that need to work across the object)
  /////////////////////

  int serialNumber;
  int diceRoll;


  float posiX;
  float posiY;
  float veloX;
  float veloY;

  int balloonAlpha;
  int shadowAlpha;
  int balloonColour;
  int shadowColour;

  float rotationSpeed;
  float angle;

  int balloonTimer;
  boolean fragment = false;

  //////////////////////
  //the CONSTRUCTOR (a function that runs once, when a new instance of the object is created)
  //////////////////////

  NumberedBalloon(float _posiX, float _posiY, float _veloX, float _veloY, int _colour) {

    serialNumber = balloonNumber;


    posiX = _posiX;     //start the balloon with the values specified when it was created
    posiY = _posiY;
    veloX = _veloX;
    veloY = _veloY;

    balloonAlpha = 255;

    rotationSpeed = radians(balloonNumber); 
    angle = 0;                     //between -1 and 1 degree per frame (approx 60 fps)

    balloonTimer = 0;
    balloonColour = color(red(_colour), green(_colour), blue(_colour), balloonAlpha);
    shadowColour = color(0, 0, 0);
    diceRoll = floor(random(1, 11));
  }

  ///////////////////////////////
  //METHODS (functions specific to this type of object)
  ///////////////////////////////

  public void update() {    //this METHOD is for updating the position of the balloon on the screen.

    posiX = posiX + veloX;
    posiY = posiY + veloY;

    //you could also have some code here to change the veloX and veloY each time update() is called.
    //This would be useful if you were simulating gravity.
    //For example, uncomment the following for some weak gravity:  

    veloY = veloY + 0.03f;

    //or give them some attraction to the mouse:


    posiX = posiX + random(-0.1f, 0.1f);                             //give the balloon a tiny amount of jitter, so it knows whether to turn left or right
    float dist = sqrt(sq(mouseX - posiX)+ sq(mouseY - posiY));    //calculate distace between balloon centre and mouse (remember distance formula or Pythagoras?)
    float veloOld = sqrt(sq(veloX) + sq(veloY));                  //calculate original velocity
    veloX =  veloX + 0.1f*(mouseX - posiX)/dist;                   //add some velocity based on distance to mouse
    veloY = veloY + 0.1f*(mouseY - posiY)/dist;
    float veloNew = sqrt(sq(veloX) + sq(veloY));                  //calculate new velocity
    veloX = veloX*(veloOld/veloNew);                              //these steps are there to keep the speed the same so that only their direction is affected 
    veloY = veloY*(veloOld/veloNew);                              //omit these if you want the balloons to also speed up towards the mouse 

    //If you wanted the balloon to bounce off the walls, this would be the place to
    //put the code for handling this (as it involves changing the veloX and veloY)


    angle = angle + rotationSpeed/frameRate; 



    balloonTimer = balloonTimer + 1;
  }



  public void display() {  //this METHOD is for displaying the balloon at the correct location.


    imageMode(CENTER);  //images will now be drawn according to the coords of their center (instead of a corner)

    pushMatrix();          //the following code is for temporarily setting the (0,0) position to wherever this balloon is.
    translate(posiX, posiY); 
    rotate(angle);


    tint(255, balloonAlpha);
    if (diceRoll > 0) {
      fill(balloonColour);
      stroke(50, balloonAlpha);
      strokeWeight(3);


      fill(red(balloonColour), green(balloonColour), blue(balloonColour), balloonAlpha);
      translate(5, 0);
    } 

    textAlign(CENTER, CENTER);
    textSize(24);
    translate(2, -3);
    fill(red(shadowColour), green(shadowColour), blue(shadowColour), shadowAlpha);       //slight adjustment to centre the text a bit better!
    text(serialNumber, 0, 0);
    translate(-2, -2);            //slight adjustment to centre the text a bit better!
    fill(red(balloonColour), green(balloonColour), blue(balloonColour), balloonAlpha);
    text(serialNumber, 0, 0);
    popMatrix();          //return the coordinate system to usual angle and origin
  }

  public void fade() {     //this METHOD gradually makes the balloon more and more transparent
    balloonColour = color(red(balloonColour), green(balloonColour), blue(balloonColour), balloonAlpha);
    if (balloonAlpha > 0) {
      balloonAlpha = balloonAlpha - 2;
      shadowAlpha = shadowAlpha - 2;

      //alternatively, maybe you want the transparency to increase and decrease periodically - use a periodic function like sine.
      //you might have to declare some of the variables as floats instead of ints though.
      //For example...
      // balloonAlpha = sq((sin(balloonTimer/10)))*255;
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "radianStuff" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
