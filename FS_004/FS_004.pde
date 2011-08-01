 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Fiber Shop  * (03/05/2008) * chipp@chipp.org
 * 
 * Cornell Michael Silver Studio Spring 2008
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 *
 * Instructions: 
 *   Click on the YOUR_PATTERN_HERE tab and edit the pattern()
 * method using the tape(...); primitives.  Clicking the run
 * button will present your pattern in a 2-D pattern viewer
 * of the Taped Mandrel.
 *
 * Feel free to view all of the source, BUT EDIT IT AT YOUR OWN RISK!
 *
 * Keep this Cover Sheet free of code.  But feel free to include your own notes below.
 *   
 * Notes:
 *   Keys that are active and what they do:
 *
 *   'D' - export flat .dxf file of pattern
 *   'R' - export 3-D .dxf file of pattern
 *   'w' - export .ADC file for fabrication
 *
 *   '1' - visualize Eigth-Inch Tape Option
 *   '2' - visualize 3/16th Inch Tape Option
 *   '3' - visualize Quarter Inch Tape Option
 *
 * Changes:
 *    001 - Fork from RVA_031, imported into Processing
 *  
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
 
 
import processing.core.*;

public class FS_004 extends PatternViewer 
{
  
  PImage mygrad;
  
   public void setup() {
    
     mygrad = loadImage("gradient.jpg");
     
    // float myscale = 33; // life-size scale - 72 pixels per inch
    float myscale = 12;     // size of the window factor
    
    // Measured RVA Mandrel Dimensions
    
    // Length of the Mandrel = 116.5 inches    
    
    // Side Length = 6.783493 in
    // Corner Radii = 0.0625 in
    // Default Tape Width = 0.3125 in
    
    mandrel(116.5f, 6.783493f, 6.783493f, 6.783493f, 0.0625f, 0.0625f, 0.0625f, myscale, 0.3125f); 
    // println("--> Window Length: " + width + " Pattern Length: " + theMandrel.patternWidth );
    
    // Print Directions:
    println("Commands that Currently work:");
    println("  D - export flat .dxf file of pattern");
    println("  R - export 3-D .dxf file of pattern");
    println("  w - export .ADC file for fabrication");    
 }

  public void keyPressed() {
    switch(key) {
     case '1':
      // Eigth Inch
      tapeThickness(0.125f);
      println(" | Eighth Inch Tape  |");
    break;
  
    case '2':  
      // 3/16th Inch
      tapeThickness(0.1875f);
      println(" | 3/16th Inch Tape  |");
    break;
    
    case '3':    
      // Quarter Inch
      tapeThickness(0.25f);
      println(" | Quarter Inch Tape |");
    break;  
    
    case '4':    
      // Half Inch
      tapeThickness(0.5f);
      println(" | Half Inch Tape |");
    break;  
        
      default:
        super.keyPressed();
      break;
    }  
  }
   
  // Specify your taping Pattern Within this Method (treat like the draw() method)
  public void pattern() {   
      // image(mygrad, 0,0);
      
      // Polynomial Tape Primitive
      // polynonmial_ctape(0, 0, 14, 4, 10, theMandrel.inch2unit(116.0f), 2);
      if((mouseY > 0) && (mouseY < height)) {
       
        stroke(255,0,0);
        line(0, mouseY, width, mouseY);    
        
        if(mousePressed) {
          println("y level: " + mouseY);
        }
        
        // Option 1
        // int theY = 131; 
        // float from = 10.0f;
        // float to = 3.0f;
        
        // Option 2 - 
        // int theY = 126;  // EXTRA Part - 4/7/08
        //  int theY = 132; 
         int theY = 107; 
         
         // BEGIN MOUSE CONTROL
          // - this code to allow the mouse to change the pattern.
         theY = mouseY;  
         // END MOUSE CONTROL
    
        float from = 14.0f;
        float to = 2.0f;

        // Option 3
        //   int theY = 113; 
        //  float from = 18.0f; 
        //  float to = 2.0f;
        
        
        // float y_Offset = theMandrel.inch2unit(-0.3125f * (0.75f));
        // float y_Offset = theMandrel.inch2unit(-1.0);
        // float y_Offset = 0.0;
       
        gradient_ctape(0,  y_Offset, from, to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);
        gradient_ctape(0, height +  y_Offset, 180 - from, 180 - to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);
        
        gradient_ctape(0, (height/3) + y_Offset, from, to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);
        gradient_ctape(0, (height/3) + y_Offset, 180 - from, 180 - to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);	 
        
        gradient_ctape(0, (2*height/3)  + y_Offset, from, to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);
        gradient_ctape(0, (2*height/3) +  y_Offset, 180 - from, 180 - to, 10, theMandrel.inch2unit(116.5f), mygrad, theY);	 
   
       // ptape(0,height/2,87,height*10);
   
   
        // Draw a thick red line to show where the mouse is
        stroke(255,0,0);
        strokeWeight(4);
        line(0, theY, width, theY);
      }
  }
  // -------------------- DO NOT HAVE CODE BELOW THIS LINE -------------------------
}

