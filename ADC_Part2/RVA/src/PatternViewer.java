import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.dxf.RawDXF;

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
    
 * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class PatternViewer extends PApplet {

//  private float m_width = 150.0f;
//  private float m_height = 50.0f;
//  private float m_length = 400.0f;
//  private float m_radius = 25.0f;
  float thickness = 0.1f;  
//  private float unitSize = 0.01f;
  private static final float DEFAULT_TAPE_WIDTH = 10; 
  
  // Dynamic Materials
  private float tape_fill_r = 10;
  private float tape_fill_g = 10;
  private float tape_fill_b = 10;
  private float tape_stroke_r = 10;
  private float tape_stroke_g = 10;
  private float tape_stroke_b = 10;
  
  public Mandrel theMandrel;

  // Taping Segment Mechanics
  public static final int TAPE_START = 0;
  public static final int TAPE_MIDDLE = 1;
  public static final int TAPE_END = 2;
  public static final int TAPE_BOTH = 3;

  // Export File Flags:
  public int exportState = 0;
  String exportFile;

  public static final int exportNone = 0; 
  public static final int displayDXF = 1;
  public static final int rawDXF = 2;
  public static final int displayPDF = 3;
  public static final int rawPDF = 4;
  public static final int rawADC = 5;
  public static final int stlDXF = 6;
  public static final int displayTIF = 7;
  public static final int threeDeeDXF = 8;
  public static final int weightedADC = 9;

  // Export Variables
  RawDXF dxf;
  //PGraphics dxfGraphics;
  RawDXF dxfGraphics;
  PrintWriter ADCWriter;

  // Switches
  boolean looping = true;     // pauses the screen refreshes
  boolean verbose = false;    // prints out Verbose ADC files
  boolean normalize = false;   // normalizes the ADC points for output
  boolean smooth = false;	  // smoothes the points before the turn
  boolean clipping = false;   // clips the pieces of tape onto the mandrel
  
  // Texture Mapper
  PGraphics pattern;

  // Strip Objects
  // Vector thePly = new Vector(); 
  Ply thePly = new Ply(this);
  
  // Configuration to Generate Multi-file parts
  public int stripCount = 0;
  int maxStripCount = 100; // strips per file
  //int maxStripCount = 10; // strips per file
  
  // Default Setup
  public void setup() {
    // Set the Mandrel Size:
    // mandrel(<mandrel width>, <mandrel height>, <mandrel length>);  
    // mandrel(100, 100, 400, 20, 20, 0.1, 0.25);  
  }

  public void keyPressed() {
    switch(key) {    
/*    
    case 'T': // .tif export
      //exportState = PatternViewer.displayTIF;
      println("Exporting TIFF screenshot.");
      // saveFrame(sketchName() + "_" + hour() + "_" + minute() + "_" + second() + ".tif");
    break;
    case 'P': // display PDF
      exportState = PatternViewer.displayPDF;
      break; 
    case 'p': // raw PDF
      exportState = PatternViewer.rawPDF;     
      break; 
    case 'd': // display DXF
      exportState = PatternViewer.displayDXF;           
      break; 
*/ 
    case 'D': // raw DXF
      exportState = PatternViewer.rawDXF;           
      break; 
     
    case 'a': // raw ADC
      exportState = PatternViewer.rawADC;          
      break; 
    case 'w': // weighted ADC
      exportState = PatternViewer.weightedADC;   // works
      break;  
/*      
    case 'S': // stl DXF
      exportState = PatternViewer.stlDXF; 
      break;
*/      
    case 'R': // 3D DXF
      exportState = PatternViewer.threeDeeDXF; // works
      break;
    case ' ':
      if(looping) {
        noLoop();
      } else {
        loop();
      }
      looping = !looping;
      break;
    case 'v':   
      if(verbose) {
        verbose = false;
        println("Verbose OFF");
      } else {
        verbose = true;
        println("Verbose ON");
      }
      break;      
    case 'n':   
      if(normalize) {
        normalize = false;
        println("Normalize OFF");
      } else {
        normalize = true;
        println("Normalize ON");
      }
      break;      
    case 's':   
        if(smooth) {
        	smooth = false;
          println("Smooth OFF");
        } else {
        	smooth = true;
          println("Smooth ON");
        }
        break;   
    case 'c':   
      if(clipping) {
        clipping = false;
        println("Clipping OFF");
      } else {
        clipping = true;
        println("Clipping ON");
      }
      break;           
    } 
  }
 

  //////////////////////////////////////////////////////////////
  // Export Functions
 
  public void beginExport() {
    if(exportState != exportNone) {
      String filename;
      filename = sketchName() + "_" + hour() + "_" + minute() + "_" + second(); 
      print("BEGIN Exporting '"); 
      switch(exportState) {
/*      
      case PatternViewer.displayDXF:
        break;

      case PatternViewer.displayPDF:
        print(filename + ".pdf");        
        beginRecord(PDF, filename + ".pdf");
        break;
      case PatternViewer.rawPDF:
        break;
*/
      case PatternViewer.rawADC:
        print(filename + ".adc");
        if(maxStripCount == 0) {
          beginADCexport(filename + ".adc");
        }
        break;   
        
      case PatternViewer.weightedADC:
        print(filename + "weighted.adc");
        if(maxStripCount == 0) {
          beginADCexport(filename + "weighted.adc");
        }
        break;
        
       case PatternViewer.threeDeeDXF:  
        print(filename + "_3D.dxf");        
        dxfGraphics = (RawDXF) createGraphics(width, height, DXF, filename + "_3D.dxf");
        break;
        
       case PatternViewer.rawDXF:
           print(filename + ".dxf");        
           dxf = (RawDXF) createGraphics(width, height, DXF, filename + ".dxf");
           beginRaw(dxf);
           dxf.setLayer(1); // set the patterns to layer 1     	
           noFill();
           break;       
/*        
      case PatternViewer.stlDXF:
        print(filename + "_stl.dxf");        
        dxf = (RawDXF) createGraphics(width, height, DXF, filename + "_stl.dxf");
        beginRaw(dxf);
        dxf.setLayer(1); // set the patterns to layer 1 
        break;
*/        
      }
      println("': " + hour() + ":" + minute() + ":" + second());
    }
  }

  public void endExport() {
    if(exportState != exportNone) {

      switch(exportState) {
/*        
      case PatternViewer.displayTIF:
        //// saveFrame(sketchName() + "_" + hour() + "_" + minute() + "_" + second() + ".tif"); 
        break;
      case PatternViewer.displayDXF:      
        break;

      case PatternViewer.displayPDF:
// TODO - update to new Mandrel coordinates    	  
//        line(0, theMandrel.top_front_y, theMandrel.l, theMandrel.top_front_y);       
        endRecord();
        break;
      case PatternViewer.rawPDF:
        break;
*/        
      case PatternViewer.rawADC:
        if(maxStripCount == 0) { // create one large ADC file
          endADCexport();
        } else { // create multiple ADC files
          multiADCexport(maxStripCount,  sketchName() + "_" + hour() + "_" + minute() + "_" + second());
        }
        noLoop();
        break;      
      case PatternViewer.weightedADC:
        if(maxStripCount == 0) { // create one large ADC file
          endADCexport();
        } else { // create multiple ADC files
          multiADCexport(maxStripCount,  sketchName() + "_" + hour() + "_" + minute() + "_" + second());
        }
        noLoop();
        break;
      case PatternViewer.threeDeeDXF: 
    	  
    	thePly.exportDXF_3D(dxfGraphics, this.g, normalize, smooth);
        
        // Clean up of thePly
        thePly.removeAllElements();
        stripCount = 0;
        
        // Saves the Fiber Angle
        // saveFrame(sketchName() + "_" + hour() + "_" + minute() + "_" + second() + ".tif");
        
        // Pause for inspections
        noLoop();
        break;  
        
      case PatternViewer.rawDXF:
          endRaw();
          break;
 /*       
      case PatternViewer.stlDXF:  
        // Add a new layer for every bounding part of the Mandrel.
        noFill();
        
        dxf.setLayer(2); // Bottom          
//        rect(0,0, m_length, m_width);
        dxf.setLayer(3); // Back
//        rect(0, m_width, m_length, m_height);          
        dxf.setLayer(4); // Top
//        rect(0, m_width + m_height, m_length, m_width);
        dxf.setLayer(5); // Front
//        rect(0, 2*m_width + m_height, m_length, m_height); 
       
        // RVA Expanded Mandrel
        dxf.setLayer(2); // Length-wise       
//        line(0, height/2, theMandrel.l, height/2);
        dxf.setLayer(3); // Center-Line       
//        line(theMandrel.l, 0, theMandrel.l, height);
        
        endRaw();
        break;
*/       
      }
      println("DONE exporting: " + hour() + ":" + minute() + ":" + second());
      exportState = exportNone;
    }    
  }

  public void multiADCexport(int maxStrip, String filename) {
	  int fileNum = 0;
	  //int stripNum = 0;

	  // Export a Range of Strips from thePly.
	  Enumeration e = thePly.strips.elements();
	  
	  // Enumerate through the thePly, and print out all of the Strips:
	  //for(Enumeration e = thePly.elements(); e.hasMoreElements();) {

	  // Start a New File
	  //  if((stripNum % (maxStrip+1)) == 0) { // Start a New File

	  for(int i = 0; e.hasMoreElements(); i = i + maxStrip) {
		  fileNum++; // increment the File Number

		  if(exportState == PatternViewer.weightedADC) {
			  ADCWriter = createWriter(filename + "_" + fileNum + "_weighted.adc");
		  } else {
			  ADCWriter = createWriter(filename + "_" + fileNum + ".adc");
		  }

		  // Header Info
		  ADCWriter.println("# " + filename + " FileNum: " + fileNum);
		  ADCWriter.println("begin");
		  ADCWriter.println("#\t PlyNo \t PlyAngle");
		  // Make Each Strip Hold the Ply
		  // ADCWriter.println("ply\t1\t0.0");
		  ADCWriter.println("#\t StripNo");
		  ADCWriter.println("#\t idx \t X \t Y \t Z \t I \t J \t K \t SDist \t Angle \t InBounds \t Dir X \t Dir Y \t Dir Z");

		  //   stripNum = 1; // reset the strip number to 1
		  // }
		  // Enumeration exportADCRange(PrintWriter ADCWriter, boolean verbose, boolean weighted, boolean normalize, Enumeration e, int from, int to)
		  e = thePly.exportADCRange(ADCWriter, verbose, (exportState == PatternViewer.weightedADC), normalize, smooth, e, i, i + maxStrip);

		  // Increment the StripNum after printing
		  //   stripNum++;

		  // End the file if the Enumeration is out of elements or at the limit of stripNum's
//		  if(((stripNum % (maxStrip+1)) == 0) || (!e.hasMoreElements())) {    

		  // Saves the progress thus far as a .tif
		  // saveFrame(sketchName() + "_" + hour() + "_" + minute() + "_" + second() + "_" + fileNum + ".tif"); 

		  // End and wrap up the ADCfile
		  ADCWriter.println("end");

		  // Close down the ADCWriter  
		  ADCWriter.flush();
		  ADCWriter.close(); 
		  // }  // if end of file
		  // }  // if not reject
//		  } // enum Strips
	  }

	  // Clean up of thePly
	  thePly.removeAllElements();
	  stripCount = 0;
  }

  public void beginADCexport(String ADCfile) {
    ADCWriter = createWriter(ADCfile);
     
    // Header Info
    ADCWriter.println("# " + ADCfile);
    ADCWriter.println("begin");
    ADCWriter.println("#\t PlyNo \t PlyAngle");
    // Make Each Strip Hold the thePly
    // ADCWriter.println("ply\t1\t0.0");
    ADCWriter.println("#\t StripNo");
    ADCWriter.println("#\t idx \t X \t Y \t Z \t I \t J \t K \t SDist \t Angle \t InBounds \t Dir X \t Dir Y \t Dir Z");
  }

  public void endADCexport() {
    // exportADC(PrintWriter ADCWriter, boolean verbose, boolean weighted, boolean normalize)
	// if(exportState == PatternViewer.weightedADC) 
	thePly.exportADC(ADCWriter, verbose, (exportState == PatternViewer.weightedADC), normalize, smooth);
	  	  
    // Clean up of thePly
    thePly.removeAllElements();
    stripCount = 0;
    
    ADCWriter.println("end");
    
    // Close down the ADCWriter  
    ADCWriter.flush();
    ADCWriter.close(); 
         
    // saveFrame(sketchName() + "_" + hour() + "_" + minute() + "_" + second() + ".tif"); 
  }

  public String sketchName() {  
	// String[] path = split(sketchPath, '\\'); // PC Path name
	//    return path[path.length - 1];
	// println(sketchPath);
	String[] path = split(sketchPath, '/'); // Mac Path name
    return path[path.length - 2];
  }

  // Specify your taping Pattern Within this Method (treat like the draw() method)
  public void pattern() {
    // tape(width/2, height/2, mouseX, mouseY, false);
  }

  // PatternViewer Draw Method.  Do not modify, use the pattern(); method instead.
  public void draw() {
    background(255); 

    // File Export 
    beginExport();

    //------------ tape below line --------------------
    pattern();
    //------------- tape above line -------------------

    endExport();

    // Always draw the Mandrel
    theMandrel.drawMandrel(this.g);
  
    //unitTestMandrel();  // don't forget to change the size in the mandrel() function
  } 

  //////////////////////////////////////////////////////////////
  // Wraps the construction of the Mandrel Object
  // 
  public void mandrel(float _l, float _top_w, float _front_w, float _back_w, float _top_front_r, float _front_back_r, float _back_top_r, float _scale, float t) {
    // Set Mandrel Text
    // PFont font;
    // The font must be located in the sketch's "data" directory to load successfully
    // font = loadFont("Arial-ItalicMT-12.vlw"); 
    // textFont(font, 12);

    // Create a new Mandrel Object	
    theMandrel = new Mandrel(this, _l, _top_w, _front_w, _back_w, _top_front_r,  _front_back_r, _back_top_r, _scale);
    
    thickness = t;
    
    // Set the pattern window size:
    size(PApplet.parseInt(theMandrel.patternWidth), PApplet.parseInt(theMandrel.patternHeight), P3D);    
    // P3D - ugly render, necessary for 3D output
    
    // unitTestMandrel
    // size(PApplet.parseInt(4*theMandrel.inch2unit(_top_w)), PApplet.parseInt(4*theMandrel.inch2unit(_top_w)));
  }
  
  public void unitTestMandrel() {
	  Pt pnt = new Pt(this, 0, 0f, 0f);
	  
	  noFill();
	  
	  //ellipse(width/2, height/2, 20, 20);
	  ellipse(width/2, height/2, 3, 3);
	  
	  // Entire
	  for(pnt.v = 0.0f; pnt.v <= theMandrel.patternHeight; pnt.v = pnt.v + 0.1f) {  
	  
	  // TOP_FRONT	  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.top_front_v - (theMandrel.top_front_len/2.0)); pnt.v = pnt.v + 0.1f) {  
	  
	  // TOP_FRONT_CORNER
	  // for(pnt.v = (theMandrel.top_front_v - (theMandrel.top_front_len/2.0f)); pnt.v <= (theMandrel.top_front_v + (theMandrel.top_front_len/2.0f)); pnt.v = pnt.v + 0.2f) {  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.top_front_v + (theMandrel.top_front_len/2.0f)); pnt.v = pnt.v + 0.2f) {  

	  // FRONT
	  // for(pnt.v = (theMandrel.top_front_v + (theMandrel.top_front_len/2.0f)); pnt.v <= (theMandrel.front_back_v - (theMandrel.front_back_len/2.0f)); pnt.v = pnt.v + 1.0f) {  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.front_back_v - (theMandrel.front_back_len/2.0f)); pnt.v = pnt.v + 1.0f) {  
	  
	  // FRONT_BACK_CORNER
	  // for(pnt.v = (theMandrel.front_back_v - (theMandrel.front_back_len/2.0f)); pnt.v <= (theMandrel.front_back_v + (theMandrel.front_back_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.front_back_v + (theMandrel.front_back_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  
	  
	  // BACK 
	  // for(pnt.v = (theMandrel.front_back_v + (theMandrel.front_back_len/2.0f)); pnt.v <= (theMandrel.back_top_v - (theMandrel.back_top_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.back_top_v - (theMandrel.back_top_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  

	  // BACK_TOP_CORNER
	  // for(pnt.v = (theMandrel.back_top_v - (theMandrel.back_top_len/2.0f)); pnt.v <= (theMandrel.back_top_v + (theMandrel.back_top_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  
	  // for(pnt.v = 0.0f; pnt.v <= (theMandrel.back_top_v + (theMandrel.back_top_len/2.0f)); pnt.v = pnt.v + 1.0f) {  	  

	  // TOP_BACK
	  // for(pnt.v = (theMandrel.back_top_v + (theMandrel.back_top_len/2.0f)); pnt.v <= (theMandrel.patternHeight); pnt.v = pnt.v + 1.0f) {  	  

	  // Test Wrapped
	  // for(pnt.v = (theMandrel.patternHeight); pnt.v <= 2*(theMandrel.patternHeight); pnt.v = pnt.v + 1.0f) {  	  

		  pnt = theMandrel.projectPt(pnt);
		  pnt.printType();
		  println(" v= " + pnt.v + " -> (y= " + pnt.y + " z=" + pnt.z + ")	| normal = (" + pnt.i + ", " + pnt.j + ", " + pnt.k + ")");
		  //ellipse((width/2.0f) + theMandrel.inch2unit(pnt.z), (height/2.0f) - theMandrel.inch2unit(pnt.y), 3, 3); 
		  point((width/2.0f) + 3*theMandrel.inch2unit(pnt.z), (height/2.0f) - 3*theMandrel.inch2unit(pnt.y)); 
			
		  // Draw Normals
		  line((width/2.0f) + 3*theMandrel.inch2unit(pnt.z), (height/2.0f) - 3*theMandrel.inch2unit(pnt.y),
			   (width/2.0f) + 3*theMandrel.inch2unit(pnt.z + (0.3f*pnt.k)), (height/2.0f) - 3*theMandrel.inch2unit(pnt.y + (0.3f*pnt.j)) );
		  
		  // Clear the Point
		  pnt.x = 0.0f;
		  pnt.y = 0.0f;
		  pnt.z = 0.0f;
		  pnt.i = 0.0f;
		  pnt.j = 0.0f;
		  pnt.k = 0.0f;
	  }
	  
	  noLoop(); // halt
  }
  
  
  // Change the Thickness of the Tape
  public void tapeThickness(float t) {
    thickness = t;
  }  
    
  /** 
   *  Intersection Utility (converted to floating point intersection).
   *
   *  Returns a 3-tuple (x,y,parallel) where:
   *   float[0] is the x value 
   *   float[1] is the y value
   *   float[2] shows whether the lines intersect ( = 1 intersecting lines, = 0 non-interseting lines)
   * Following Line-Line Intersecion: 
   *   http://www.topcoder.com/tc?module=Static&d1=tutorials&d2=geometry2#line_line_intersection
   */
  
  // Intersection Object - array indexes for the 3-tuple:
  
  public final static int INTERSECTION_X = 0;
  public final static int INTERSECTION_Y = 1;
  public final static int INTERSECTION = 2;
  
  public final static float tolerance = 0.66f;
  
  // Integer Intersection
  public float[] intersection(int one_x1, int one_y1, int one_x2, int one_y2, 
                       int two_x1, int two_y1, int two_x2, int two_y2) {
  
     return intersection(PApplet.parseFloat(one_x1), PApplet.parseFloat(one_y1), PApplet.parseFloat(one_x2), PApplet.parseFloat(one_y2), 
                         PApplet.parseFloat(two_x1), PApplet.parseFloat(two_y1), PApplet.parseFloat(two_x2), PApplet.parseFloat(two_y2));
  }
  
  public float[] intersection(float one_x1, float one_y1, float one_x2, float one_y2, 
                       float two_x1, float two_y1, float two_x2, float two_y2) {
    float A1 = one_y2 - one_y1;
    float B1 = one_x1 - one_x2;
    float C1 = A1*one_x1 + B1*one_y1;
    float A2 = two_y2 - two_y1;
    float B2 = two_x1 - two_x2;
    float C2 = A2*two_x1 + B2*two_y1;
    float x,y; 
    float[] intersect = new float[3];

    // Lines are in the form: Ax + By = C.

    // Solve the determinent:
    float det = A1*B2 - A2*B1;
    if(det == 0) {
      // println("Parallel: (" + one_x1 + "," + one_y1 + ")-(" + one_x2 + "," + one_y2 + ") || (" + two_x1 + "," + two_y1 + ")-(" + two_x2 + "," + two_y2 + ")");
      // No intersection parallel lines:
      intersect[INTERSECTION_X] = 0.0f;
      intersect[INTERSECTION_Y] = 0.0f;
      intersect[INTERSECTION] = 0.0f;
    }  else {
      x = (B2*C1 - B1*C2) / det;
      intersect[INTERSECTION_X] = x;
      y = (A1*C2 - A2*C1) / det;
      intersect[INTERSECTION_Y] = y;
     
      float min_one_x = min(one_x1, one_x2)-tolerance;
      float max_one_x = max(one_x1, one_x2)+tolerance;      
      float min_one_y = min(one_y1, one_y2)-tolerance;
      float max_one_y = max(one_y1, one_y2)+tolerance;
      float min_two_x = min(two_x1, two_x2)-tolerance;
      float max_two_x = max(two_x1, two_x2)+tolerance;
      float min_two_y = min(two_y1, two_y2)-tolerance;
      float max_two_y = max(two_y1, two_y2)+tolerance;  
      
      // Set the intersection flag:
      if((min_one_x <= x) && 
         (x <= max_one_x) &&
         (min_one_y <= y) &&
         (y <= max_one_y) &&
         (min_two_x <= x) && 
         (x <= max_two_x) &&
         (min_two_y <= y) &&
         (y <= max_two_y)) {
         intersect[INTERSECTION] = 1;
      }
    }
    return intersect;
  }
  
  public boolean onPattern(float x, float y) {   
	  if((x >= 0) && (x <= width) && (y >= 0) && (y <= height)) {
		  return true;
	  } else {
		  return false; 
	  }
  }
  
  ////////////////////////////// Taping Primitives //////////////////////////
  
  // Tape - simple taping primitive.
  public void tape(float x1, float y1, float x2, float y2) {
	  segmentBegin(true);
	  segment(x1, y1, x2, y2);
	  segmentEnd();
  }
  
  public void tape(float x1, float y1, float x2, float y2, boolean wrapped) {
	  segmentBegin(wrapped);
	  segment(x1, y1, x2, y2);
	  segmentEnd();
  }
 
  // Polar Tape - specify a piece of tape as having and angle and a magnitude
  public void ptape(float x, float y, float angle, float magnitude, ClipRect cr) {
	  segmentBegin(true, cr);
	  segment(x, y, x + magnitude*cos(radians(angle)), y - magnitude*sin(radians(angle)));  
	  segmentEnd();
  }

  public void ptape(float x, float y, float angle, float magnitude) {
	  segmentBegin(true);
	  segment(x, y, x + magnitude*cos(radians(angle)), y - magnitude*sin(radians(angle))); 
	  segmentEnd();
  }

  // Spring tape, 2 ctapes reflected over the center line
  
  public void stape_lin(float x, float y, float from_angle, float to_angle, float back_angle, float magnitude, float c_width) {
	  float [] mid_pnt;
	  float [] end_pnt;
	  
	  segmentBegin(false);
	  // first curve
	  if(from_angle > 90) {
		  mid_pnt = neg_ctape_lin(x, y, from_angle, to_angle, magnitude, c_width);
	  } else {
		  mid_pnt = pos_ctape_lin(x, y, from_angle, to_angle, magnitude, c_width); 
	  }
	  // next curve
	  if(from_angle > 90) {
		  end_pnt = neg_ctape_lin(mid_pnt[0], mid_pnt[1], to_angle, back_angle, magnitude, c_width);
	  } else {
		  end_pnt = pos_ctape_lin(mid_pnt[0], mid_pnt[1], to_angle, back_angle, magnitude, c_width); 
	  }
	  segmentEnd();
  }
  
  public void stape(float x, float y, float from_angle, float to_angle, float back_angle, float magnitude) {
	  float [] mid_pnt;
	  float [] end_pnt;
	  
	  segmentBegin(false);
	  // first curve
	  if(from_angle > 90) {
		  mid_pnt = neg_ctape(x, y, from_angle, to_angle, magnitude);
	  } else {
		  mid_pnt = pos_ctape(x, y, from_angle, to_angle, magnitude); 
	  }
	  // next curve
	  if(from_angle > 90) {
		  end_pnt = neg_ctape(mid_pnt[0], mid_pnt[1], to_angle, back_angle, magnitude);
	  } else {
		  end_pnt = pos_ctape(mid_pnt[0], mid_pnt[1], to_angle, back_angle, magnitude); 
	  }
	  segmentEnd();
  }

  public void s2tape(float x, float y, float from_angle, float mid_angle, float to_angle, float mag1, float mag2) {
	  float [] mid_pnt;
	  float [] mid2_pnt;	  
	  float [] mid3_pnt;
	  
	  segmentBegin(false);
	  // 1st quarter
	  if(from_angle > 90) {
		  mid_pnt = neg_ctape(x, y, from_angle, mid_angle, mag1);
	  } else {
		  mid_pnt = pos_ctape(x, y, from_angle, mid_angle, mag1); 
	  }
	  // 2nd quarter
	  if(mid_angle > 90) {
		  mid2_pnt = neg_ctape(mid_pnt[0], mid_pnt[1], mid_angle, to_angle, mag2);
	  } else {
		  mid2_pnt = pos_ctape(mid_pnt[0], mid_pnt[1], mid_angle, to_angle, mag2); 
	  }
	  // 3rd quarter
	  if(to_angle > 90) {
		  mid3_pnt = neg_ctape(mid2_pnt[0], mid2_pnt[1], to_angle, mid_angle, mag2);
	  } else {
		  mid3_pnt = pos_ctape(mid2_pnt[0], mid2_pnt[1], to_angle, mid_angle, mag2); 
	  }	  
	  // 4th quarter
	  if(mid_angle > 90) {
		  neg_ctape(mid3_pnt[0], mid3_pnt[1], mid_angle, from_angle, mag1);
	  } else {
		  pos_ctape(mid3_pnt[0], mid3_pnt[1], mid_angle, from_angle, mag1); 
	  }	  
	  segmentEnd();
  }  
  
  // Curved Tape through crude interpolation.  Grossly redundant code.
  
  public float [] ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
	  float[] end_pnt;
	  
	  segmentBegin(false);
	  if(from_angle > 90) {
		  end_pnt = neg_ctape(x, y, from_angle, to_angle, magnitude);
	  } else {
		  end_pnt = pos_ctape(x, y, from_angle, to_angle, magnitude); 
	  }
	  segmentEnd();
	  
	  return end_pnt;
  }

  public void ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
	  segmentBegin(false);
	  if(from_angle > 90) neg_ctape_trig(x, y, from_angle, to_angle, magnitude);
	  else pos_ctape_trig(x, y, from_angle, to_angle, magnitude); 
	  segmentEnd();
  }

  public float [] ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude, float c_width) {
	  float[] end_pnt;
	  
	  segmentBegin(false);    
	  if(from_angle > 90) {
		  end_pnt = neg_ctape_lin(x, y, from_angle, to_angle, magnitude, c_width);
	  } else {
		  end_pnt = pos_ctape_lin(x, y, from_angle, to_angle, magnitude, c_width); 
	  }
	  segmentEnd();
	  
	  return end_pnt;
  }

  public float [] neg_ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    boolean wrap = false;
 
    // end_point
    float[] end_pnt = new float[2];
    
    // Tape a number of sub-sections:
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      segment(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);
      
      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
        segment(x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height);
      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress + height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress < 0) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress - delta_y;
      angle_progress = angle_progress + delta_angle;
      
      // println("Curve segment [" + i + "] - angle_progress: " + angle_progress);
    }

    // println("----------------------------------------------------------------");
    
    // return the end_pnt of the curve
    end_pnt[0] = x_progress; // x-component
    end_pnt[1] = y_progress; // y-component
    return end_pnt;
  }
    
  public float [] pos_ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    boolean wrap = false;
    
    // end_point
    float[] end_pnt = new float[2];
    
    // Tape a number of sub-sections:
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      
      // TODO - This adds an extra wrapped piece in order to show a clean visualization of the
      // curved wrapping process.  This extra piece however messes up building the taped progress. 
      // Need to separate display from the fabrication.
      
      // if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
      //  segment(x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height);
      // }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress % height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress + delta_y;
      angle_progress = angle_progress + delta_angle;
      
      // println("Curve segment [" + i + "] - angle_progress: " + angle_progress);
    }
    
    // println("----------------------------------------------------------------");
    
    // return the end_pnt of the curve
    end_pnt[0] = x_progress; // x-component
    end_pnt[1] = y_progress; // y-component
    return end_pnt;
  }

  public void neg_ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      segment(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);
      
      // TODO - see above
//      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
//        segment(x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height);
//      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress + height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress < 0) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress - delta_y;
      
      // Use a cos to intensify the starting degrees
      angle_progress = angle_progress + (0.05f)*delta_angle + (0.95f)*delta_angle*(cos(pi_progress) + 1); // trig factor varies from 2 to 1
      pi_progress = pi_progress + slice_of_pi;
    }
  }
    
  public void pos_ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;    
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));

      segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      
      // TODO - see above
//      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
//        segment(x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height);
//      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress % height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress + delta_y;
      
      // Use a cos to intensify the starting degrees
      angle_progress = angle_progress + (0.05f)*delta_angle + (0.95f)*delta_angle*(cos(pi_progress) + 1); // trig factor varies from 2 to 1
      pi_progress = pi_progress + slice_of_pi;
    }
  }

  public float [] neg_ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude, float c_width) {
	float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;
    boolean wrap = false;
    
    // end_point
    float[] end_pnt = new float[2];
    
    // Tape a number of sub-sections:
    // for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x - x) < c_width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      segment(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);
      
      // TODO - see above      
//      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
//        segment(x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height);
//      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress + height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress < 0) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress - delta_y;

      // angle_progress = angle_progress + delta_angle*(100*(x_progress / width)*(x_progress / width))/10;
      angle_progress = angle_progress + delta_angle*(100*((x_progress-x) / c_width)*((x_progress-x) / c_width))/10;
      
      // check to make sure angle_progress does not over-run it's pattern
      if(delta_angle < 0) { 
    	  if(angle_progress < (180 - to_angle)) {
    		  angle_progress = (180 - to_angle);
    	  }
      } else if(delta_angle > 0) {
    	  if(angle_progress > (180 - to_angle)) {
    		  angle_progress = (180 - to_angle);
    	  }
      }
      println("NEG Curve segment [" + i + "] - angle_progress: " + angle_progress);
    }

    println("----------------------------------------------------------------");

    // return the end_pnt of the curve
    end_pnt[0] = x_progress; // x-component
    end_pnt[1] = y_progress; // y-component
    return end_pnt;
  }
    
  public float [] pos_ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude, float c_width) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x = 0.0f;
    float delta_y = 0.0f;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;    
    boolean wrap = false;
    
    // end_point
    float[] end_pnt = new float[2];
    
    // Tape a number of sub-sections:
    // for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x) < width); i++) {
    for(int i = 0; (i < (int) num_sections) && ((x_progress + delta_x - x) < c_width); i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      
      // TODO - see above
//      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
//        segment(x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height);
//      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress % height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress + delta_y;
//      angle_progress = angle_progress + delta_angle*(100*(x_progress / width)*(x_progress / width))/10;
      angle_progress = angle_progress + delta_angle*(100*((x_progress-x) / c_width)*((x_progress-x) / c_width))/10;
    
      // Check to make sure angle_progress does not over-run it's pattern
      if(delta_angle < 0) { 
    	  if(angle_progress < to_angle) {
    		  angle_progress = to_angle;
    	  }
      } else if(delta_angle > 0) {
    	  if(angle_progress > to_angle) {
    		  angle_progress = to_angle;
    	  }
      }  
      println("POS Curve segment [" + i + "] - angle_progress: " + angle_progress);
    }
    println("----------------------------------------------------------------");   
    
    // return the end_pnt of the curve
    end_pnt[0] = x_progress; // x-component
    end_pnt[1] = y_progress; // y-component
    return end_pnt;
  }
  
  // Make the test_width limit larger than any pattern that is possible.
  public int polynonmial_ctape(float x, float y, float from_angle, float to_angle, float delta_mag, float strip_width, double order) {
	  return polynonmial_ctape(x, y, from_angle, to_angle, delta_mag, strip_width, order, 2*theMandrel.patternWidth); // test_width limit is beyond any pattern
  }
  
  public int polynonmial_ctape(float x, float y, float from_angle, float to_angle, float delta_mag, float strip_width, double order, float test_width) {
	  int num_wraps = 0;
	  
	  segmentBegin(false);    
	  if(from_angle > 90) {
		  num_wraps = neg_polynonmial_ctape(x, y, from_angle, to_angle, delta_mag, strip_width, order, test_width);
	  } else {
		  num_wraps = pos_polynonmial_ctape(x, y, from_angle, to_angle, delta_mag, strip_width, order, test_width);
	  }
	  segmentEnd();
	  return num_wraps;
  }
	  
  public int pos_polynonmial_ctape(float x, float y, float from_angle, float to_angle, float delta_mag, float strip_width, double order, float test_width) { 
	    int num_wraps = 0;
	    float t_progress = 0.0f; // parameter: 0-2.0f (1.0f in the middle of the piece)
	    float angle_progress = from_angle;

	    float delta_x = 0.0f;
	    float delta_y = 0.0f;
	    
	    float x_progress = x;
	    float y_progress = y;
	    
	    boolean wrap = false;
	    	    
	    // Tape the length of the segment
	    for(int i = 0; ((x_progress - x) < strip_width) && ((x_progress - x) < test_width); i++) {
	    	// Calculate parameter t based on x_progress:
	    	t_progress = ((x_progress - x) / strip_width) * 2.0f;
	    	
	    	// Use a Polynomial Curve to calculate angle
	    	angle_progress = (from_angle - to_angle) * ((float) Math.pow(Math.abs(t_progress - 1), order)) + to_angle;
	    	
		    // println("Polynomial Curve segment [" + i + "] t: " + t_progress + " - angle_progress: " + angle_progress);
	    	
	    	delta_x = delta_mag*sin(radians(angle_progress)); 
	    	delta_y = delta_mag*cos(radians(angle_progress));
	       
	    	segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);

		    // println("  From (" + x_progress + "," + y_progress + ") to (" + (x_progress + delta_x) + "," + (y_progress + delta_y) + ")");
		    	
	    	// check if the tape needs to be wrapped
	    	if(wrap) { // we just taped a wrap - correct the progress
	    		// println("*** WRAPPED ***");
	    		y_progress = y_progress % height;
	    		wrap = false;
	    		num_wraps++;
	    	}
	      
	    	x_progress = x_progress + delta_x;

	    	if(y_progress > ((y_progress + delta_y) % height)) { // the next Tape will be a wrapped piece
	    		wrap = true;
	    	}      

	    	y_progress =  y_progress + delta_y;
	    }
	    // println("----------------------------------------------------------------"); 
	    if(mousePressed) {
	    	println("pos_polynonmial_ctape y: " + y + " diff: " + (y_progress - y)); 
	    }
	    return num_wraps;
  }
  
  public int neg_polynonmial_ctape(float x, float y, float from_angle, float to_angle, float delta_mag, float strip_width, double order, float test_width) { 
	    int num_wraps = 0;
	    float t_progress = 0.0f; // parameter: 0-2.0f (1.0f in the middle of the piece)
	    float angle_progress = 180-from_angle;

	    float delta_x = 0.0f;
	    float delta_y = 0.0f;
	    
	    float x_progress = x;
	    float y_progress = y;
	    
	    boolean wrap = false;
	    	    
	    // Tape the length of the segment
	    for(int i = 0; ((x_progress - x) < strip_width) && ((x_progress - x) < test_width); i++) {
	    	// Calculate parameter t based on x_progress:
	    	t_progress = ((x_progress - x) / strip_width) * 2.0f;
	    	
	    	// Use a Polynomial Curve to calculate angle
	    	angle_progress = ((180-from_angle) - (180-to_angle)) * ((float) Math.pow(Math.abs(t_progress - 1), order)) + (180-to_angle);
	    	
		    //println("Polynomial Curve segment [" + i + "] t: " + t_progress + " - angle_progress: " + angle_progress);
	    	
	    	delta_x = delta_mag*sin(radians(angle_progress)); 
	    	delta_y = delta_mag*cos(radians(angle_progress));
	       
	    	segment(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);

		    //println("  From (" + x_progress + "," + y_progress + ") to (" + (x_progress + delta_x) + "," + (y_progress - delta_y) + ")");
		    
	    	// check if the tape needs to be wrapped
		    if(wrap) { // we just taped a wrap - correct the progress
		    	y_progress = y_progress + height;
		        wrap = false;
			    num_wraps++;
		    }    
		    
		    x_progress = x_progress + delta_x;

		    if(y_progress < 0) { // the next Tape will be a wrapped piece
		    	wrap = true;
		    }      

		    y_progress =  y_progress - delta_y;		    
	    }
	    //println("----------------------------------------------------------------");
	    if(mousePressed) {
	    	println("neg_polynonmial_ctape y: " + y + " diff: " + (y_progress - y)); 
	    }
	    return num_wraps;
}
  
  Strip ADCTape;
  boolean wrapped = true;
  ClipRect clipTo;
  
  public void segmentBegin(boolean w) {
	  segmentBegin(w, theMandrel.mandrelClip);
  }
  
  public void segmentBegin(boolean w, ClipRect cr) {
	  wrapped = w;
	  clipTo = cr;
	  
	  // Check if Exporting
	  if((exportState != exportNone) && (exportState != displayPDF) && (exportState != rawDXF)) {
		  ADCTape = new Strip(this);
	  }
  }
  
  public void segment(float x1, float y1, float x2, float y2) {
	  // Check if Exporting
	  if((exportState != exportNone) && (exportState != displayPDF) && (exportState != rawDXF)) {
		  segmentExport(x1, y1, x2, y2);
	  } else if(wrapped) { // Draw wrapped
		  segmentWrap(x1, y1, x2, y2);
	  } else { // Draw Clipped
		  ClipLine cl;
		  cl = clipTo.clip(x1,y1,x2,y2); // Clip the tape to the specified ClipRect
		    if(cl.accept)    
		     segmentDraw(cl.startx, cl.starty, cl.endx, cl.endy); 
	  }
  }
  
  public void segmentEnd() {
	  if((exportState != exportNone) && (exportState != displayPDF) && (exportState != rawDXF)) {		 
		  thePly.add(ADCTape);
	  }
  }

  public void segmentExport(float x1, float y1, float x2, float y2) {
	  ADCTape.add(x1, y1, x2, y2);
  }
  
  public void segmentWrap(float x1, float y1, float x2, float y2) {
	  float new_x = 0;
	  float new_y = 0;

	  // Get the Tangent (in radians)
	  float angle_deg = atan2(y1 - y2,  x2 - x1);
	  if(angle_deg < 0) { // atan2 should take care of this
		  angle_deg = 2*PI + angle_deg;
	  }

	  // Debug
	  //println("Angle Degrees: " + angle_deg);

	  // Debug: Displays the Limits
	  /*
	    stroke(200,0,0);
	    line(x1, y1, 0, 0);
	    line(x1, y1, 0, height);
	    line(x1, y1, width, 0);
	    line(x1, y1, width, height);
	   */

	  // Calculate the Degrees of the Limits
	  float ne_limit = atan2(y1,  width - x1);
	  float nw_limit = atan2(y1,  -x1);
	  float sw_limit = 2*PI + atan2(y1 - height,  -x1);
	  float se_limit = 2*PI + atan2(y1 - height,  width - x1);

	  if(angle_deg < ne_limit) {
		  //println("Angle North East");
		  new_x = width;
		  new_y = tan(angle_deg) * ((x1 + (y1 / tan(angle_deg))) - width);
		  segmentDraw(x1, y1, new_x, new_y); // Draw the cut segment     
	  } else if(angle_deg < nw_limit) {
		  //println("Angle North"); 
		  new_x = x1 + (y1 / tan(angle_deg));
		  new_y = y2 + height; // assumes that y2 is negative

		  segmentDraw(x1, y1, new_x, 0); // Draw the Segment 
		  segmentWrap(new_x, height, x2, new_y); // Recursively call wtape
	  } else if(angle_deg < sw_limit) {
		  //println("Angle West");
		  new_x = 0;
		  new_y = tan(angle_deg) * (x1 + (y1 / tan(angle_deg)));
		  segmentDraw(x1, y1, new_x, new_y); // Draw the cut segment
	  } else if(angle_deg < se_limit) {
		  //println("Angle South");
		  new_x = x1 - (height - y1) / tan(angle_deg);
		  new_y = y2 - height; // assumes that y2 is negative

		  segmentDraw(x1, y1, new_x, height); // Draw the Segment 
		  segmentWrap(new_x, 0, x2, new_y); // Recursively call wtape
	  } else if(angle_deg < 360) {
		  //println("Angle South East");     
		  new_x = width;
		  new_y = tan(angle_deg) * ((x1 + (y1 / tan(angle_deg))) - width);
		  segmentDraw(x1, y1, new_x, new_y); // Draw the cut segment           
	  } else {
		  println("Undefined Angle Degrees: " + angle_deg); 
	  }
  }
  
  public void segmentDraw(float x1, float y1, float x2, float y2) {
	  float angle = atan2((y2-y1), (x2-x1));
	  float tape_len = dist(x1, y1, x2, y2);
	  float x_tape = 0.0f;
	  float y_tape = 0.0f;
	  //float w = thickness/theMandrel.unitSize;
	  float w = theMandrel.inch2unit(thickness);
	  
	  // Draw Tape Centerlines
	  if(exportState == rawDXF) {
		line(x1, y1, x2, y2);
	  } else { // Tape is modeled as opaque material
		fill(tape_fill_r, tape_fill_g, tape_fill_b); // values are dynamic
	  	stroke(tape_stroke_r, tape_stroke_g, tape_stroke_b);

	  	pushMatrix(); // save our coordinate system

	  	translate(x1, y1); // translate to the start point of the tape
	  	rotate(angle); // rotate the coordinate system to tape at an angle

	  	// Draws a straight piece of tape
	  	beginShape();
	  	vertex(x_tape, y_tape - (w/2));
	  	vertex(x_tape + tape_len, y_tape - (w/2));      
	  	vertex(x_tape + tape_len, y_tape + (w/2));      
	  	vertex(x_tape, y_tape + (w/2));
	  	endShape(CLOSE);

	  	popMatrix(); 
	  }
  }
  
  
} // PatternViewer 
