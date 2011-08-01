  
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * *
 * RVA DEMO  * (07/05/2007) * chipp@chipp.org
 * 
 * RVA Composite Fiber Placement Taping Studio Version 001
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 *
 * Instructions: 
 *   Click on the YOUR_PATTERN_HERE tab and edit the pattern()
 * method using the tape(...); primitives.  Clicking the run
 * button will present your pattern in a 2-D pattern viewer
 * and 3-D representation of the Taped Mandrel.
 *
 *   Feel free to view the code in MandrelViewer and PatternViewer,
 * BUT EDIT AT YOUR OWN RISK!
 *
 *   Keep this Cover Sheet free of code.  But feel free to
 * include your own notes below.
 *
 * Notes:
 * Keys that are active and what they do:
 
 'a' - 'g' - Batch One Preset Patterns
 
 'h' - 'm' - Batch Two Preset Patterns
 
 'n' - 'u' - Batch Three Preset Patterns
 
 'v' - 'y' - Batch Four Preset Patterns
 
 'x' - TOP CHOICE PATTERN
 
 'z' - Mouse Interactive Enable
 
 '1' - Eigth-Inch Tape Option
 '2' - 3/16th Inch Tape Option
 '3' - Quarter Inch Tape Option
 
    Changes:
    001 - Fork from WRAPPING_005 - has rbeam () reflect beam.
    002 - Clip Sections.
    002V - RVA Interactive Pattern Fork
    003V - RVA Interactive Demo - with changing Line Thicknesses
    DEMO - RVA Final Demo
    
    031 - Mid-Fall Working Demo
    
 * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class RVA_031 extends PatternViewer 
{
   public void setup() {
    // Isosceles Mandrel Size:
    // Top Side: 7", Front Side: 7", Back Side: 7"
    // Corner Radii : 0.0625

    // float myscale = 33; // life-size scale - 72 pixels per inch
    float myscale = 12;
    // float myscale = 24;
    
    ////// Mandrel  Version 1.0 //////
    
    // Pattern is 116 inches long
    // mandrel(116.0f, 7.0f, 7.0f, 7.0f, 0.0625f, 0.0625f, 0.0625f, myscale, 0.3125f);
    
    // Include 1/2 inch for warpped ends
    // ---> mandrel(116.5f, 7.0f, 7.0f, 7.0f, 0.0625f, 0.0625f, 0.0625f, myscale, 0.3125f);
    // ---> test mandrel -- > mandrel(10.0f, 7.0f, 7.0f, 7.0f, 0.0625f, 0.0625f, 0.0625f, myscale, 0.3125f);
    
    ////// Mandrel  Version 2.0 //////
    // Idealized Version:
    mandrel(116.5f, 6.783493f, 6.783493f, 6.783493f, 0.0625f, 0.0625f, 0.0625f, myscale, 0.375f); // 0.3125f);
    
    // Measured Version:
    // mandrel(116.5f, 6.75687500f, 6.75687500f, 6.75687500f, 0.10156250f, 0.10156250f, 0.10156250f, myscale, 0.3125f);
    
    println("--> Window Length: " + width + " Pattern Length: " + theMandrel.patternWidth );
    
    println("Commands that Currently work:");
    println("  D - export flat .dxf file of pattern");
    println("  R - export 3-D .dxf file of pattern");
    println("  w - export .ADC file for fabrication");    
 }
   
  public void keyPressed() {
    switch(key) {
    case '>':
    	order++;
    	println("Order: " + order);
    	break;
    	
    case '<':
    	order--;
    	println("Order: " + order);
    	break;
    
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
        // 3/8 ths Inch Tape
        tapeThickness(0.375f);
        println(" | 3/8 ths Tape |");
      break;  

    case '5':    
      // Half Inch
      tapeThickness(0.5f);
      println(" | Half Inch Tape |");
    break;  
        
      default:
        super.keyPressed();
      break;
    }  
  }
   
  
  // Key Editable Parameters
  double order = 3.0;
  
  // Specify your taping Pattern Within this Method (treat like the draw() method)
  public void pattern() {

//	  tape(0,0,theMandrel.patternWidth, theMandrel.patternHeight);
//	  tape(theMandrel.patternWidth, theMandrel.patternHeight, 0,0);

//	  tape(theMandrel.patternWidth, 0, 0, theMandrel.patternHeight);
//	  tape(0, theMandrel.patternHeight,theMandrel.patternWidth, 0);

	  
	  // tape((width/2.0f), 0, (width/2.0f) + 2*height*cos(radians(85.99997f)), 2*height*sin(radians(85.99997f)));
	  
	  // Half Inch Wraps
	  // tape(0, 0, theMandrel.inch2unit(0.5f), 2*height); // 2 wraps	  
	  // tape(0, 0, theMandrel.inch2unit(0.5f), 3*height); // 3 wraps
	  // tape(0, 0, theMandrel.inch2unit(0.5f), 4*height); // 4 wraps
	  // tape(0, 0, theMandrel.inch2unit(0.5f), 5*height); // 5 wraps

	  // Caps - utilized on 4/25/2010
	  // tape(0, 0, theMandrel.inch2unit(0.5f), 4.5f*height);
	  // tape(theMandrel.inch2unit(116.0f), 0, theMandrel.inch2unit(116.5f), 4.5f*height);
	  
	  // Axial Strips
	  // tape(theMandrel.inch2unit(0.25f), theMandrel.top_front_v, theMandrel.inch2unit(116.25f), theMandrel.top_front_v);
	  // tape(theMandrel.inch2unit(0.25f), theMandrel.back_top_v, theMandrel.inch2unit(116.25f), theMandrel.back_top_v);
	  // tape(theMandrel.inch2unit(0.25f), theMandrel.front_back_v, theMandrel.inch2unit(116.25f), theMandrel.front_back_v);
  	  
	  // Axial Strips Need to be Longer - 03.25.08
	  // tape(0.0f, theMandrel.top_front_v, theMandrel.inch2unit(118.0f), theMandrel.top_front_v);
	  // tape(0.0f, theMandrel.back_top_v, theMandrel.inch2unit(118.0f), theMandrel.back_top_v);
	  // tape(0.0f, theMandrel.front_back_v, theMandrel.inch2unit(118.0f), theMandrel.front_back_v);
	  
	  // Versions
	  // float from_angle = 14.0f;	  
	  
	  // RVA PART
	  // float from_angle = 13.980519f;	// A - 02.29.08 - THIS ONE MATCHES END TO END
	  // float to_angle = 4.0f; // A
	  // order = 3;
	  
	  // float from_angle2 = 13.961206f;	// A2
	  // float to_angle2 = 4.0f; // A2
	   
	  // float from_angle = 13.983477f;	// B
	  // float to_angle = 3.5f; // B
	  
	  // float from_angle = 14.021552f; // C
	  // float to_angle = 3.25f; // C
	  
	  // float from_angle = 13.9547415f; // D
	  // float to_angle = 3.0f; // D
	  
	  // float from_angle = 13.989224f; // E
	  // float to_angle = 2.5f; // E
	  
	  // float from_angle = 14.0244255f; // F
	  // float to_angle = 2.25f; // F
	  
	  // float from_angle = 13.978448f; // G 
	  // float to_angle = 2.0f; // G	
	  
	  // New Open Pattern (20 DEG)
	  // float from_angle = 20.229614f;	// H
	  // float to_angle = 4.0f; // H

	  // 18 DEG
	  // float from_angle = 18.133047f;	// I - 02.29.08 - THIS ONE
	  // float to_angle = 4.0f; // I
	  
	  // 18 DEG using the functions.
	  // float from_angle = 18.133047f;	// 03.25.08 - NEW 18 DEG part that is steeper (SET order to 3.0)
	  // float to_angle = 4.0f; // I
	  
	  // 18 DEG - change - 4.4.08 - 18 deg to 1.0 degree
	  // float from_angle = 18.133047f;	// 03.25.08 - NEW 18 DEG part that is steeper (SET order to 3.0)
	  // float to_angle = 1.0f; // I	  

	  // 18 DEG - change - 4.6.08 - FINAL EXTRA RVA PART - 18 deg to 3.0 degree, order = 3
	  // float from_angle = 18.133047f;	
	  // float to_angle = 3.0f; 
	  // order = 3;
	  
	  // Reversed 18 DEG - 4.17.2010 
	  // float from_angle = 3.0f; 
	  // float to_angle = 18.133047f;	
	  // order = 0.5;

	  // Reversed 13 DEG - 4.17.2010
	  float from_angle = 4.0f; 
	  // float to_angle = 14.03003f; // 14.5f; // 14.0f;	
	  float to_angle = 13.794706f; // 14.5f; // 14.0f;	
	  order = 0.3;
	  
	  float x_Offset = theMandrel.inch2unit(0.25f); //  
	  float y_Offset = 0.0f;
	  //double order = 3.0f; // normally order = 2.0, but set to 3.0 for function - 3.25.08 
	  int num_wraps = 0;
	  int secsPerWrap = 25;
	  
	  // Mouse Control
	  // order = 0.49f*((mouseX - width/2.0f) / (float) width) + order;
	  // to_angle = 1.0f*((mouseX - width/2.0f) / (float) width) + to_angle;
	  // to_angle2 = ((mouseY - height/2.0f) / (float) height) + to_angle2;
	  
	  // to_angle = (((float) mouseX / (float) width) * 4.0f) + 0.25f; // need a floor to stand on
	  //	  // println("  From angle: " + from_angle);
	  if(mousePressed) {
		  println("----------------------------------------------------------------");
	  	  println("  From angle: " + from_angle);
	 	  println("  To angle: " + to_angle);
	 	  println("  Order: " + order);	 	  
	 }
	  
	  // A3 Pattern	  
	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);  
	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);

	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);
	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);
	  
	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);
	  // num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);

	  // println("Total Wraps: " + num_wraps + " (" + secsPerWrap + " sec/wrap): " 
	  //		  + (num_wraps*secsPerWrap) + " seconds = " + (((num_wraps*secsPerWrap)/60)/60) + " hours, " 
	  //		  + (((num_wraps*secsPerWrap)/60)%60) + " minutes.");

	  // First Strip - upping segment size:

	  // 75% * + 0.3125 Offset: 
	  // y_Offset = theMandrel.inch2unit(0.3125f * (0.75f));

	  // 18 deg part: 
	  // 75% * - 0.3125 Offset: 
	  // 4.17.2010 - FINAL OFFSET for the part :
	  // y_Offset = theMandrel.inch2unit(-0.3125f * (0.75f)); // commented out to see no shift
	  
	  // 14 deg part:
	  // -0.3125 Offset:
	  // y_Offset = theMandrel.inch2unit(-0.3125f);
	 
	  // 4/26/2010 - offset compensations
	  // y_Offset = theMandrel.inch2unit(-0.5625f); // negative nine-sixteenths
	  y_Offset = theMandrel.inch2unit(0.5625f); // positive nine-sixteenths
	  	  
	  // println("*** COMPENSATE: y_Offset: " + 0.3125f * (0.75f) + " inches");
	  //	4/25/2010 - the pattern commented out  
	  // Front Side:
	  // if(mousePressed) {
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);  
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);
	  
	  // Back Side:
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);

	  // Top Side:
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order);
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order);	  
	  // }
	  //
	  // println("Total Wraps: " + num_wraps + " (" + secsPerWrap + " sec/wrap): " 
	  //		  + (num_wraps*secsPerWrap) + " seconds = " + (((num_wraps*secsPerWrap)/60)/60) + " hours, " 
	  //		  + (((num_wraps*secsPerWrap)/60)%60) + " minutes.");

/*	  	
	  // Front Side:
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
		  
	  // Back Side:
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side:
	  	num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	    num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
	    	    
*/
	  // Crosses Test Pattern:
/*
	  // Stubby Example
	  float test_width = theMandrel.inch2unit(4.0f); // limited test width
	  // float test_width = theMandrel.inch2unit(116.0f + x_Offset); // entire pattern
		
	  // 5-Wrap Caps:
	  tape(0, 0, theMandrel.inch2unit(0.7f), 5.0f*height);
	  tape(test_width - theMandrel.inch2unit(0.7f), 0, test_width, 5.0f*height);
	    
	  // 0.0 Offset: 
	  y_Offset = 0.0f;
		  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
	  
	  // +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f);
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // -0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(-0.3125f);
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
	  
	  // 50% * +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (0.5f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 50% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (0.5f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 150% * +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (1.5f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 150% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (1.5f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 25% * +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (.25f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 25% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (0.25f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
 	  
	  // 75% * + 0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (0.75f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 75% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (0.75f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 125% * +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (1.25f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 125% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (1.25f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
 
	  // 175% * +0.3125 Offset: 
	  y_Offset = theMandrel.inch2unit(0.3125f * (1.75f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  

	  // 175% * -0.3125 Offset:
	  y_Offset = theMandrel.inch2unit(-0.3125f * (1.75f));
	  
	  // Front Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);  
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);
	  
	  // Back Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.front_back_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);

	  // Top Side 6 inches:
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.back_top_v + y_Offset, from_angle, to_angle, 10, theMandrel.inch2unit(116.0f), order, test_width);
	  num_wraps += polynonmial_ctape(x_Offset, theMandrel.top_front_v + y_Offset, (float) (180 - from_angle), (float) (180 - to_angle), 10, theMandrel.inch2unit(116.0f), order, test_width);	  
*/
	  
	  // Show center line
	  // stroke(0,200,0,200); // center line
	  // line(width/2, 0, width/2, height);
	  // stroke(0,200,0,200);
	  // line(width/4, 0, width/4, height); // first quarter
	  // line(3*width/4, 0, 3*width/4, height); // third quarter

      // stroke(0,200,0,128);
	  // line(theMandrel.inch2unit(41.993828f), 0, theMandrel.inch2unit(41.993828f), height);

	  // Box the Front Side
	  // fill(200,0,0,100); 
	  // stroke(200,0,0); 
	  // rect(0, theMandrel.top_front_v, theMandrel.l, theMandrel.front_len + theMandrel.top_front_len);
  }
  // -------------------- DO NOT HAVE CODE BELOW THIS LINE -------------------------
}

