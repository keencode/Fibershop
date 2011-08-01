import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Segment {
    PatternViewer parent;
    
    boolean reject = false;
    
    // Start- and End-points of the Strip in the Pattern Coordinate System
    int ID;
    int _x1;
    int _y1;
    int _x2;
    int _y2;
    float angle;
    float dirX;
    float dirY; 
    
    // Used in new add(...)        
    float deltaX;
    float deltaY; 
    float deltaMag;   
    float flatDirX; 
    float flatDirY;
    float flatO;
    
    // X limits
    float[] xlim_patternZeroY;    // top (y=0) edge of pattern
    float[] xlim_patternHeightY;  // bottom (y=height) edge of pattern
    
    float[] xlim_top_front_minus;
    float[] xlim_top_front;
    float[] xlim_top_front_plus;
    
    float[] xlim_front_bottom_minus;
    float[] xlim_front_bottom;
    float[] xlim_front_bottom_plus;
        
    float[] xlim_bottom_back_minus; 
    float[] xlim_bottom_back; 
    float[] xlim_bottom_back_plus; 

    float[] xlim_bottom_back_minus_real; 

    float[] xlim_back_top_minus; 
    float[] xlim_back_top; 
    float[] xlim_back_top_plus; 

    Vector points;

    Segment(PatternViewer p, int _id, float x1, float y1, float x2, float y2) {
      reject = false; // initially accept this part
      
      parent = p;
      ID = _id;

      // Generating the Limits  

// TODO - need to retro-fit      
/*     
      xlim_patternZeroY = parent.intersection(x1, y1, x2, y2, 0, 0, parent.theMandrel.patternWidth, 0);     // top (y=0) edge of pattern
      xlim_patternHeightY = parent.intersection(x1, y1, x2, y2, 0, parent.theMandrel.patternHeight, parent.theMandrel.patternWidth, parent.theMandrel.patternHeight); // bottom (y=height) edge of pattern
    
      xlim_top_front_minus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.top_front_y - parent.theMandrel.top_front_r, 
                                                          parent.theMandrel.patternWidth, parent.theMandrel.top_front_y - parent.theMandrel.top_front_r); // TOP FRONT CORNER
      xlim_top_front = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.top_front_y, parent.theMandrel.patternWidth, parent.theMandrel.top_front_y); // TOP FRONT CORNER
      xlim_top_front_plus =  parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.top_front_y + parent.theMandrel.top_front_r, 
                                                          parent.theMandrel.patternWidth, parent.theMandrel.top_front_y + parent.theMandrel.top_front_r); // TOP FRONT CORNER
    
      xlim_front_bottom_minus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.front_bottom_y - parent.theMandrel.front_bottom_r, 
                                                            parent.theMandrel.patternWidth, parent.theMandrel.front_bottom_y - parent.theMandrel.front_bottom_r); // FRONT BOTTOM CORNER
      xlim_front_bottom = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.front_bottom_y, parent.theMandrel.patternWidth, parent.theMandrel.front_bottom_y); // FRONT BOTTOM CORNER
      xlim_front_bottom_plus = parent.intersection(x1, y1, x2, y2, 0, parent.theMandrel.front_bottom_y + parent.theMandrel.front_bottom_r, 
                                                            parent.theMandrel.patternWidth, parent.theMandrel.front_bottom_y + parent.theMandrel.front_bottom_r); // FRONT BOTTOM CORNER
        
      xlim_bottom_back_minus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.bottom_back_y - parent.theMandrel.bottom_back_r, 
                                                            parent.theMandrel.patternWidth, parent.theMandrel.bottom_back_y - parent.theMandrel.bottom_back_r); // BOTTOM BACK CORNER  

      xlim_bottom_back_minus_real = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.patternHeight - parent.theMandrel.bottom_back_r, 
                                                            parent.theMandrel.patternWidth, parent.theMandrel.patternHeight - parent.theMandrel.bottom_back_r); // BOTTOM BACK CORNER  
            
      xlim_bottom_back = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.bottom_back_y, parent.theMandrel.patternWidth, parent.theMandrel.bottom_back_y); // BOTTOM BACK CORNER 
      xlim_bottom_back_plus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.bottom_back_y + parent.theMandrel.bottom_back_r, 
                                                            parent.theMandrel.patternWidth, parent.theMandrel.bottom_back_y + parent.theMandrel.bottom_back_r); // BOTTOM BACK CORNER 
      
      xlim_back_top_minus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.back_top_y - parent.theMandrel.back_top_r, 
                                                            parent.theMandrel.patternWidth,  parent.theMandrel.back_top_y - parent.theMandrel.back_top_r); // BACK TOP CORNER 
      xlim_back_top = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.back_top_y, parent.theMandrel.patternWidth, parent.theMandrel.back_top_y); // BACK TOP CORNER 
      xlim_back_top_plus = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.back_top_y + parent.theMandrel.back_top_r, 
                                                           parent.theMandrel.patternWidth, parent.theMandrel.back_top_y + parent.theMandrel.back_top_r); // BACK TOP CORNER 
*/
      
      points = new Vector();
      
      add(x1, y1, x2, y2);  // adds the line onto the Strip
 
      // Set the Fiber Angle from the Strip's Points
      // Check for Parallels:
      if(y1 == y2) { // parallel E/W
        angle = 0;
      } else if(x1 == x2) { // parallel N/S
        angle = 90;
      } else { // other angle
        float q12 = -parent.degrees(parent.atan2(y2 - y1, x1 - x2));
        if((q12 <= 90) && (q12 > -90)) {
          angle = q12;
          //println("Atan2: " + q12);
        } else if(q12 < 0) {
          angle = q12 + 180;
          //println("Atan2: " + (q12 + 180));
        } else {
          angle = q12 - 180;
          //println("Atan2: " + (q12 - 180));    
        }
      }
      
      // Calculate the DirX and DirY for the Strip:
      dirX = x1 - x2;
      dirY = y1 - y2;
      float dirM = parent.mag(dirX, dirY);
      dirX = dirX / dirM;
      dirY = dirY / dirM;
      
      // Generating
      PApplet.println("  Segment " + ID + " Num Points: " + points.size() + " Angle: " + angle + 
    		  " (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ")" +
    		  " DirX: " + dirX + " DirY: " + dirY); 
      
   /*  
      // Use the xlim's to generate the DirX and DirY's:
      parent.print("TOP FRONT: ");
      printDirXY(xlim_top_front_minus, xlim_top_front_plus);
      parent.print("FRONT BOTTOM: ");
      printDirXY(xlim_front_bottom_minus, xlim_front_bottom_plus);
      parent.print("BOTTOM BACK: ");
      printDirXY(xlim_bottom_back_minus, xlim_bottom_back_plus);
      parent.print("BACK TOP: ");
      printDirXY(xlim_back_top_minus, xlim_back_top_plus);
    */
    
    }    

    public void printDirXY(float[] minus, float[] plus) {
       if(minus[PatternViewer.INTERSECTION] + plus[PatternViewer.INTERSECTION_X] > 0) {
         float dX = minus[PatternViewer.INTERSECTION_X] - plus[PatternViewer.INTERSECTION_X];
         float dY = minus[PatternViewer.INTERSECTION_Y] - plus[PatternViewer.INTERSECTION_Y];
         float dM = parent.mag(dX, dY);
         dX = - dX / dM;
         dY = - dY / dM;
         parent.println(" DirX: " + dX + " DirY: " + dY + " Mag: " + dM); 
       }
    }
    
  public float radialDeltaX(float rawY) {
          float y = 0;
          float dX = 0;
          Mandrel m = parent.theMandrel;
          
          // Wrap the Y coordinate:
          y = rawY % parent.theMandrel.patternHeight;
          if(y < 0) { // negative values get looped
            y = y + parent.theMandrel.patternHeight;
          }
 /*         
          if(y <= (m.bottom_back_y - m.bottom_back_r)) { // BOTTOM FLAT (BACK SIDE)
            dX = (xlim_top_front_minus[parent.INTERSECTION_X] - xlim_back_top_plus[parent.INTERSECTION_X]);  // *** special, same as TOP    
          } else 
  */        

// TODO - Need to retro-fit          
/*          
          if(y <= (m.bottom_back_r)) { // BOTTOM-BACK CURVE (III)
            dX = (xlim_bottom_back_plus[parent.INTERSECTION_X] - xlim_bottom_back_minus[parent.INTERSECTION_X]);
          } else if(y < (m.back_top_y - m.back_top_r)) { // BACK FLAT
            dX = (xlim_back_top_minus[parent.INTERSECTION_X] - xlim_bottom_back_plus[parent.INTERSECTION_X]);
          } else if(y <= (m.back_top_y + m.back_top_r)) {  // BACK-TOP CURVE (IV)
            dX = (xlim_back_top_plus[parent.INTERSECTION_X] - xlim_back_top_minus[parent.INTERSECTION_X]);
          } else if(y <= (m.top_front_y - m.top_front_r)) { // TOP FLAT
            dX = (xlim_top_front_minus[parent.INTERSECTION_X] - xlim_back_top_plus[parent.INTERSECTION_X]);
          } else if(y <= (m.top_front_y + m.top_front_r)) { // TOP-FRONT CURVE (I)
            dX = (xlim_top_front_plus[parent.INTERSECTION_X] - xlim_top_front_minus[parent.INTERSECTION_X]);
          } else if(y < (m.front_bottom_y - m.front_bottom_r)) { // FRONT FLAT
            dX = (xlim_front_bottom_minus[parent.INTERSECTION_X] - xlim_top_front_plus[parent.INTERSECTION_X]);
          } else if(y <= (m.front_bottom_y + m.front_bottom_r)) { // FRONT-BOTTOM CURVE (II)
            dX = (xlim_front_bottom_plus[parent.INTERSECTION_X] - xlim_front_bottom_minus[parent.INTERSECTION_X]);
          } else if(y <= (m.patternHeight - m.bottom_back_r)) { // BOTTOM FLAT (FRONT HALF)
            dX = (xlim_top_front_minus[parent.INTERSECTION_X] - xlim_back_top_plus[parent.INTERSECTION_X]); // *** special, same as TOP   
          } else if(y <= (m.patternHeight)) {
            dX = (xlim_bottom_back_plus[parent.INTERSECTION_X] - xlim_bottom_back_minus[parent.INTERSECTION_X]);
          } 
*/         
          return dX;
  }

  public float radialCenterX(float rawY) {
          float y = 0;
          float cX = 0;
          float dX = 0; 
          Mandrel m = parent.theMandrel;
          int wraps = 0;
          float patternDeltaX = 0;
                              
          // Wrap the Y coordinate:
          y = rawY % parent.theMandrel.patternHeight;
          if(y < 0) { // negative values get looped
            y = y + parent.theMandrel.patternHeight;
          }
         
          if(rawY == parent.theMandrel.patternHeight) {
            y = rawY;  
          }
          
          //patternDeltaX = parent.abs(xlim_patternZeroY[parent.INTERSECTION_X] - xlim_patternHeightY[parent.INTERSECTION_X]);
          patternDeltaX = xlim_patternZeroY[parent.INTERSECTION_X] - xlim_patternHeightY[parent.INTERSECTION_X];
          
         // parent.println("X_Zero: " + xlim_patternZeroY[parent.INTERSECTION_X] + 
         //                " X_HeightY: " + xlim_patternHeightY[parent.INTERSECTION_X] + 
         //                " patternDeltaX: " + patternDeltaX);
          
          // Y is wrapped, calculate dX:
          dX = radialDeltaX(y); // inefficient but necessary
 /*   
          if(y <= (m.bottom_back_y - m.bottom_back_r)) { // BOTTOM FLAT (BACK SIDE)
            cX = dX / 2; // starts at zero   
          } else 
 */         
          
          
// TODO - need to retro-fit
/*
          if(y <= (m.bottom_back_r)) { // BOTTOM-BACK CURVE (III)
            cX = xlim_bottom_back_minus[parent.INTERSECTION_X] + (dX / 2);
          } else if(y < (m.back_top_y - m.back_top_r)) { // BACK FLAT
            cX = xlim_bottom_back_plus[parent.INTERSECTION_X] + (dX / 2);
          } else if(y <= (m.back_top_y + m.back_top_r)) {  // BACK-TOP CURVE (IV)
            cX = xlim_back_top_minus[parent.INTERSECTION_X] + (dX / 2);
          } else if(y <= (m.top_front_y - m.top_front_r)) { // TOP FLAT
            cX = xlim_back_top_plus[parent.INTERSECTION_X] + (dX / 2);
          } else if(y <= (m.top_front_y + m.top_front_r)) { // TOP-FRONT CURVE (I)
            cX = xlim_top_front_minus[parent.INTERSECTION_X] + (dX / 2);
          } else if(y < (m.front_bottom_y - m.front_bottom_r)) { // FRONT FLAT
            cX = xlim_top_front_plus[parent.INTERSECTION_X]  + (dX / 2);
          } else if(y <= (m.front_bottom_y + m.front_bottom_r)) { // FRONT-BOTTOM CURVE (II)
            cX = xlim_front_bottom_minus[parent.INTERSECTION_X]  + (dX / 2);
          } else if(y <= (m.patternHeight - m.bottom_back_r)) { // BOTTOM FLAT (FRONT HALF)
            cX =  xlim_front_bottom_plus[parent.INTERSECTION_X] + (dX / 2);
          } else { // if(y <= m.patternHeight) { // BOTTOM-BACK CURVE (III) (FRONT HALF)
            cX = xlim_bottom_back_minus_real[parent.INTERSECTION_X] + (dX / 2);
          } 
*/
          
          // Wraps cX
          if(rawY < 0) { // check if wrapping in the negative zone
            //parent.print("[rawY: " + rawY + "] [pHeight: " + parent.theMandrel.patternHeight + "] ");
            wraps = PApplet.parseInt(-rawY / parent.theMandrel.patternHeight) + 1;
            cX = cX + wraps * patternDeltaX;
          }
          if(rawY > parent.theMandrel.patternHeight) {
            //parent.print("[rawY: " + rawY + "] [pHeight: " + parent.theMandrel.patternHeight + "] ");
            wraps = PApplet.parseInt(rawY / parent.theMandrel.patternHeight);
            cX = cX - wraps * patternDeltaX;          
          }
          //parent.println(" Wraps: " + wraps + " Wrapped cX: " + cX);
          
          // Calculate how many wraps away this CenterX is:
          if(cX > parent.width) {
            PApplet.println("Center X OUT OF BOUNDS: " + cX + " rawY: " + rawY + " y: " + y + " patternHeight: " + parent.theMandrel.patternHeight);         
          }
          
          return cX;
  }  
    
  public float radialDeltaY(float rawY) {
          float y = 0;
          float dY = 0;
          Mandrel m = parent.theMandrel;
          
          // Wrap the Y coordinate:
          y = rawY % parent.theMandrel.patternHeight;
          if(y < 0) { // negative values get looped
            y = y + parent.theMandrel.patternHeight;
          }
/*          
          if(y <= (m.bottom_back_y - m.bottom_back_r)) { // BOTTOM FLAT (BACK SIDE)
            dY = (xlim_top_front_minus[parent.INTERSECTION_Y] - xlim_back_top_plus[parent.INTERSECTION_Y]);  // *** special, same as TOP    
          } else 
*/          
    
// TODO - need to retro-fit
/*           
          if(y <= (m.bottom_back_r)) { // BOTTOM-BACK CURVE (III)
            dY = (xlim_bottom_back_plus[parent.INTERSECTION_Y] - xlim_bottom_back_minus[parent.INTERSECTION_Y]);
          } else if(y < (m.back_top_y - m.back_top_r)) { // BACK FLAT
            dY = (xlim_back_top_minus[parent.INTERSECTION_Y] - xlim_bottom_back_plus[parent.INTERSECTION_Y]);
          } else if(y <= (m.back_top_y + m.back_top_r)) {  // BACK-TOP CURVE (IV)
            dY = (xlim_back_top_plus[parent.INTERSECTION_Y] - xlim_back_top_minus[parent.INTERSECTION_Y]);
          } else if(y <= (m.top_front_y - m.top_front_r)) { // TOP FLAT
            dY = (xlim_top_front_minus[parent.INTERSECTION_Y] - xlim_back_top_plus[parent.INTERSECTION_Y]);
          } else if(y <= (m.top_front_y + m.top_front_r)) { // TOP-FRONT CURVE (I)
            dY = (xlim_top_front_plus[parent.INTERSECTION_Y] - xlim_top_front_minus[parent.INTERSECTION_Y]);
          } else if(y < (m.front_bottom_y - m.front_bottom_r)) { // FRONT FLAT
            dY = (xlim_front_bottom_minus[parent.INTERSECTION_Y] - xlim_top_front_plus[parent.INTERSECTION_Y]);
          } else if(y <= (m.front_bottom_y + m.front_bottom_r)) { // FRONT-BOTTOM CURVE (II)
            dY = (xlim_front_bottom_plus[parent.INTERSECTION_Y] - xlim_front_bottom_minus[parent.INTERSECTION_Y]);
          } else if(y <= (m.patternHeight - m.bottom_back_r)) { // BOTTOM FLAT (FRONT HALF)
            dY = (xlim_top_front_minus[parent.INTERSECTION_Y] - xlim_back_top_plus[parent.INTERSECTION_Y]); // *** special, same as TOP   
          } else if(y <= m.patternHeight) { // BOTTOM FLAT (FRONT HALF)
            dY = (xlim_bottom_back_plus[parent.INTERSECTION_Y] - xlim_bottom_back_minus[parent.INTERSECTION_Y]);
          }        
*/          
          return dY;
  }
   
   float fullLengthPercent = 0.001f;
   float flatPercent = 0.02f;
   float curvePercent = 0.01f;
    
   public void add(float x1, float y1, float x2, float y2) {
      float[] xlim_from = {0.0f, 0.0f, 0.0f};
      float[] xlim_to = {0.0f, 0.0f, 0.0f};
 
      // Clip the line onto the mandrel w.r.t. the x-direction
      if(x1 < x2) {
        if(x1 < 0) { // line needs to be clipped on the left
          xlim_from = parent.intersection(x1, y1, x2, y2, 0.0f, 0.0f, 0.0f, parent.theMandrel.patternHeight);
        } else {
          xlim_from[parent.INTERSECTION_X] = x1;
          xlim_from[parent.INTERSECTION_Y] = y1;
        }
        xlim_from[parent.INTERSECTION] = 1; // set as a valid point 

        if(x2 > parent.theMandrel.patternWidth) { // line needs to be clipped on the right
          xlim_to = parent.intersection(x1, y1, x2, y2, parent.theMandrel.patternWidth, 0.0f, parent.theMandrel.patternWidth, parent.theMandrel.patternHeight);
        } else {          
          xlim_to[parent.INTERSECTION_X] = x2;
          xlim_to[parent.INTERSECTION_Y] = y2;
        }
        xlim_to[parent.INTERSECTION] = 1; // set as a valid point

      } else { // x2 > x1
        if(x2 < 0) { // line needs to be clipped on the left     
          xlim_from = parent.intersection(x2, y2, x1, y1, 0.0f, 0.0f, 0.0f, parent.theMandrel.patternHeight);
        } else {
          xlim_from[parent.INTERSECTION_X] = x2;
          xlim_from[parent.INTERSECTION_Y] = y2;
        }
        xlim_from[parent.INTERSECTION] = 1; // set as a valid point

        if(x1 > parent.theMandrel.patternWidth) { // line needs to be clipped on the right
          xlim_to = parent.intersection(x2, y2, x1, y1, parent.theMandrel.patternWidth, 0.0f, parent.theMandrel.patternWidth, parent.theMandrel.patternHeight);        
        } else {
          xlim_to[parent.INTERSECTION_X] = x1;
          xlim_to[parent.INTERSECTION_Y] = y1;
        } 
        xlim_to[parent.INTERSECTION] = 1; // set as a valid point
      }
            
      // Calculate the Strips delta's and flatDir's
      deltaX = xlim_to[parent.INTERSECTION_X] - xlim_from[parent.INTERSECTION_X]; 
      deltaY = xlim_to[parent.INTERSECTION_Y] - xlim_from[parent.INTERSECTION_Y];
      deltaMag = parent.mag(deltaX, deltaY);
        
      // Calculate the Flat flatDirX, flatDirY, and Orientation
      flatDirX = deltaX / deltaMag;
      flatDirY = deltaY / deltaMag;  
      flatO = parent.atan2(flatDirX, flatDirY); 
      // Should it not be: flatO = parent.atan2(flatDirY, flatDirX); ???  

      Pt thePoint = new Pt(parent, 0, 0.0f,0.0f); // stub point 0
      float x = 0;
      float y = 0;
      
      // SPECIAL CASE: AXIAL LINES
      if(parent.degrees(flatO) == 90) { // Always 90 Degrees?    	  
    	x = xlim_from[parent.INTERSECTION_X];
        y = xlim_from[parent.INTERSECTION_Y];
        
        // thePoint = new Pt(parent, x, y);
        thePoint = parent.theMandrel.projectPt(new Pt(parent, 1, x, y));
        thePoint.setFlatData(flatDirX, flatDirY, flatO);
        
        // TODO - convert to projectPt - DONE
/*
        thePoint = parent.theMandrel.wrapPatternToMandrel(1, x, y, radialDeltaX(y), radialCenterX(y), flatDirX, flatDirY, flatO);
*/
        if(thePoint != null) {
          points.add(thePoint);
        }   
        x = xlim_to[parent.INTERSECTION_X];
        y = xlim_to[parent.INTERSECTION_Y];
        
        thePoint = parent.theMandrel.projectPt(new Pt(parent, 2, x, y));
        thePoint.setFlatData(flatDirX, flatDirY, flatO);
        
        if(thePoint != null) {
          points.add(thePoint);
        }    
      } else { // AXIAL STRIP - only render the crossings
    	  
        parent.println("Adding Strip " + ID + 
        " | from (" + xlim_from[parent.INTERSECTION_X] + "," + xlim_from[parent.INTERSECTION_Y] + ") -> to (" +
        xlim_to[parent.INTERSECTION_X] + "," + xlim_to[parent.INTERSECTION_Y] + ")" +
        " deltaX: " + deltaX + " deltaY: " + deltaY + 
        " dirX: " + flatDirX + " dirY: " + flatDirY + " o: " + parent.degrees(flatO));

        int ptCount = 0;
        float[] intersectV;
              
        // Add the FIRST point in the curve:
        ptCount++; // increment the ptCount
                
        thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, xlim_from[parent.INTERSECTION_X], xlim_from[parent.INTERSECTION_Y]));
        thePoint.setFlatData(flatDirX, flatDirY, flatO);
             
        if(thePoint != null) {
          points.add(thePoint);
        }
        
        // Cycle through the sectionV's of theMandrel
        for(int i = 0; i < parent.theMandrel.sectionV.length; i++) {
            
        	// parent.println("flatO: " + parent.degrees(flatO));
        	if(parent.degrees(flatO) > 90) { // Reverse Strips need different conditions       
        		// parent.println(" REVERSE SEGMENT");
        		// Reverse the from/to conditions
        		if( (parent.theMandrel.sectionV[i] < xlim_from[parent.INTERSECTION_Y]) && 
                    (parent.theMandrel.sectionV[i] > xlim_to[parent.INTERSECTION_Y]) ) {
                    	
                    	// Calculate the intersection:
                    	intersectV = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.sectionV[i], parent.theMandrel.patternWidth, parent.theMandrel.sectionV[i]); // Intersect with that V crossing

                    	if(intersectV[parent.INTERSECTION] == 0) { // ONLY A WARNING
                    		parent.println(" ERROR: SectionV[" + i + "] = " + parent.theMandrel.sectionV[i]);
                    	}
                    	
                    	if(intersectV != null) {
                    		ptCount++; // increment the ptCount

                    		// Intersect the sectionV to get the (x,y) on the pattern            
                    		thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, intersectV[parent.INTERSECTION_X], intersectV[parent.INTERSECTION_Y]));
                    		thePoint.setFlatData(flatDirX, flatDirY, flatO);
                        
                    		if(thePoint != null) {
                    			points.add(thePoint);
                    		}
                    	} // valid intersection
                    } // proper sectionV	
        	} else { // Forward Strips
        	
	            if( (parent.theMandrel.sectionV[i] > xlim_from[parent.INTERSECTION_Y]) && 
	            	(parent.theMandrel.sectionV[i] < xlim_to[parent.INTERSECTION_Y]) ) {
	            	
	            	// Calculate the intersection:
	            	intersectV = parent.intersection(x1, y1, x2, y2, 0.0f, parent.theMandrel.sectionV[i], parent.theMandrel.patternWidth, parent.theMandrel.sectionV[i]); // Intersect with that V crossing
	
	            	if(intersectV[parent.INTERSECTION] == 0) { // ONLY A WARNING
	            		parent.println(" ERROR: SectionV[" + i + "] = " + parent.theMandrel.sectionV[i]);
	            	}
	            	
	            	if(intersectV != null) {
	            		ptCount++; // increment the ptCount
	
	            		// Intersect the sectionV to get the (x,y) on the pattern            
	            		thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, intersectV[parent.INTERSECTION_X], intersectV[parent.INTERSECTION_Y]));
	            		thePoint.setFlatData(flatDirX, flatDirY, flatO);
	                
	            		if(thePoint != null) {
	            			points.add(thePoint);
	            		}
	            	} // valid intersection
	            } // proper sectionV
	            
        	} // Forward Strips
        }
 

        // Add the LAST point in the curve (might want to remove this - against shifts):
        // STEP 1: NEED to add the LAST point in the segment to get the Direction Vector Correct
        ptCount++; // increment the ptCount
                
        thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, xlim_to[parent.INTERSECTION_X], xlim_to[parent.INTERSECTION_Y]));
        thePoint.setFlatData(flatDirX, flatDirY, flatO);
             
        if(thePoint != null) {
            // STEP 2: Flag the Point as removed in order to prevent SHIFTS
        	thePoint.removeMe = true;
        	points.add(thePoint);
        }
      
      } // else AXIAL STRIP
 /* 
  * HI-PT DENSITY RENDERER
  *        
      // NON-AXIAL STRIP - Render through Linear Interpolation
      } else { 
   
	      // Adding Strip
      
//      parent.println("Adding Strip " + ID + 
//        " | from (" + xlim_from[parent.INTERSECTION_X] + "," + xlim_from[parent.INTERSECTION_Y] + ") -> to (" +
//        xlim_to[parent.INTERSECTION_X] + "," + xlim_to[parent.INTERSECTION_Y] + ")" +
//        " deltaX: " + deltaX + " deltaY: " + deltaY + 
//        " dirX: " + flatDirX + " dirY: " + flatDirY + " o: " + parent.degrees(flatO));
      int ptCount = 0;
      float xFrom = 0.0f;
      float xTo = 0.0f;
      
      // float xRate = parent.theMandrel.patternWidth * fullLengthPercent; // Stub xRate: 1000th the length of the Mandrel      
      // float xRate = parent.theMandrel.inch2unit(0.001f);
      float flatxRate = 0.2f;
      float cornerxRate = 0.01f;
      float xRate = cornerxRate; // start with a guaranteed high render rate
      float A = xlim_to[parent.INTERSECTION_Y] - xlim_from[parent.INTERSECTION_Y];
      float B = xlim_from[parent.INTERSECTION_X] - xlim_to[parent.INTERSECTION_X];
      float C = A*xlim_from[parent.INTERSECTION_X] + B*xlim_from[parent.INTERSECTION_Y];
      
      // Interpolate across the line from xlim_from[...] to xlim_to[...]:
      for(x = xlim_from[parent.INTERSECTION_X]; x < xlim_to[parent.INTERSECTION_X]; x = x + xRate) {
        // Calculate y from the x : Linear Interpolation
        y = (C-A*x) / B;
        ptCount++; // increment the ptCount
       
        thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, x, y));
        thePoint.setFlatData(flatDirX, flatDirY, flatO);
        
        if(thePoint != null) {
          points.add(thePoint);
        }
        
        // parent.print("X: " + x + " [rate=" + xRate + "] Y: " + y + " (Pt " + ptCount + " type: "); 
        
        // NOTE: assuming homogenous mandrel (symmetrical) 
        // NOTE: using the top_front corner radius, and the front side of the mandrel
        
        // Re-calculate the xRate if within one-inch of the corner fringe:
		if(y < 0) { // set xRate to it's smallest setting
			xRate = cornerxRate;
		} else if(y < (parent.theMandrel.top_front_v - (parent.theMandrel.top_front_len / 2.0f) - (parent.theMandrel.inch2unit(0.25f))) ) { // TOP_FRONT region (with 0.5 inch buffer)
			xRate = flatxRate; // rougher
		} else if (y < (parent.theMandrel.top_front_v + (parent.theMandrel.top_front_len / 2.0f) + (parent.theMandrel.inch2unit(0.25f))) ) { // TOP_FRONT_CORNER region
			xRate = cornerxRate;			
		} else if (y < (parent.theMandrel.front_back_v - (parent.theMandrel.front_back_len / 2.0f) - (parent.theMandrel.inch2unit(0.25f))) ) { // FRONT region
			xRate = flatxRate; // rougher
		} else if (y < (parent.theMandrel.front_back_v + (parent.theMandrel.front_back_len / 2.0f) + (parent.theMandrel.inch2unit(0.25f))) ) { // FRONT_BACK_CORNER region
			xRate = cornerxRate;			
		} else if (y < (parent.theMandrel.back_top_v - (parent.theMandrel.back_top_len / 2.0f) - (parent.theMandrel.inch2unit(0.25f))) ) { // BACK region
			xRate = flatxRate; // rougher		
		} else if (y < (parent.theMandrel.back_top_v + (parent.theMandrel.back_top_len / 2.0f) + (parent.theMandrel.inch2unit(0.25f))) ) { // BACK_TOP_CORNER region
			xRate = cornerxRate;			
		} else if(y < parent.theMandrel.patternHeight) { // TOP_BACK_REGION
			xRate = flatxRate; // rougher
		} else { // outofbounds		
			xRate = cornerxRate;
		}
        
        // Check that the xRate is at least greater than 0 (prevent infinite loops)
        if(xRate <= 0) {
          parent.println("!*!*!*! Zero xRate : Prevent Infinite Loops !*!*!*!");
          xRate = 0.01f; // set to a standard 0.1 of a point
        }   
        
        // Mark the xRate towards the next point:
        thePoint.setxRate(xRate);
        
      } // for-loop
      
      // Add the LAST point in the curve:
      ptCount++; // increment the ptCount
              
      thePoint = parent.theMandrel.projectPt(new Pt(parent, ptCount, xlim_to[parent.INTERSECTION_X], xlim_to[parent.INTERSECTION_Y]));
      thePoint.setFlatData(flatDirX, flatDirY, flatO);
           
      if(thePoint != null) {
        points.add(thePoint);
      }
    } // else AXIAL STRIP
 */
      
   }

    public void sortPoints() { 
      boolean x_ascend = true;
      if(angle < 0) {
        x_ascend = false;
      }
      
      Collections.sort(points, new PtCompare(x_ascend));
    
      // Index's the points (locally) (This might be redundant!)
      int index = 3;
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        Pt p = (Pt) e.nextElement();  
        p.setIndex(index++);       
      } 
    }

    public void uniquePoints() {
      Pt cur;
      Pt prev;
      Enumeration e = points.elements();
      cur = (Pt) e.nextElement(); 
      prev = cur;
      while(e.hasMoreElements()) { 
        prev = cur; 
        cur = (Pt) e.nextElement();
        prev.setDelta(cur);
        if(Float.isNaN(prev.deltaX) || Float.isNaN(prev.deltaY) || Float.isNaN(prev.deltaZ)) {
          points.remove(prev); 
        }
        // Clean zero changing points
        if( (prev.deltaX == 0) && (prev.deltaX == 0) && (prev.deltaX == 0) ) {
          points.remove(prev);
        } 
      }
    }

    public void trimDuplicatePoints() {
        Pt cur;
        Pt prev;
        Enumeration e = points.elements();
        cur = (Pt) e.nextElement(); 
        prev = cur;
        while(e.hasMoreElements()) { 
          prev = cur; 
          cur = (Pt) e.nextElement();
          // Remove points with matching coordinates
          if((prev.x == cur.x) && (prev.y == cur.y) && (prev.z == cur.z)) {
            points.remove(prev); 
          }
        }
      }

    
    
    // Calculates:
    // 		SDist
    //		angle
    //		deltaX, deltaY, deltaZ
    
    public void processPts() {
      float d = 0.0f;
      Pt prev;
      Pt cur;
      int ptCount = 1;

      sortPoints(); // Starts sorting at point idx = 3
      // trimDuplicatePoints();
      // sortPoints(); // sort again, just in case
      
      // Re-numbers Points
      Enumeration e = points.elements();
      cur = (Pt) e.nextElement();  
      cur.setSDist(d);
      cur.idx = ptCount;
      prev = cur;

      while(e.hasMoreElements()) {
        ptCount++;
        prev = cur; // save  Previous Point
        cur = (Pt) e.nextElement();
        cur.idx = ptCount; // index the point
        // Calculate it's SDist
        d = d + parent.dist(prev.x, prev.y, prev.z, cur.x, cur.y, cur.z);
        cur.setSDist(d);
        // Set the previous's point's Fiber Angle
        prev.angle = angle;
        // Set the point's direction
        prev.setDelta(cur);
  
// TODO - do we need to have the Flat Corner quality? NO DONE
/* 
        // Check if on a Flat Corner
        if(cur.onFlatCorner(prev)) {
           cur.setFlatCorner(); 
        }
*/
      }

      // Set the last Pt's Angle and Dir to the previous points:
      cur.angle = angle;
      cur.deltaX = prev.deltaX;
      cur.deltaY = prev.deltaY;
      cur.deltaZ = prev.deltaZ;
      
      cur.deltaXRaw = prev.deltaXRaw;
      cur.deltaYRaw = prev.deltaYRaw;
      cur.deltaZRaw = prev.deltaZRaw;
     
     /*     
      cur.dirX = prev.dirX;
      cur.dirY = prev.dirY;
      cur.dirZ = prev.dirZ;
      */
    }

    public void processDir(boolean weighted) {
      Pt p3 = null;
      Pt p2 = null;
      Pt p1 = null;
      Pt cur = null;
      Pt n1 = null;
      Pt n2 = null;
      Pt n3 = null;
      
      Enumeration e = points.elements();
      cur = (Pt) e.nextElement();
      if(e.hasMoreElements()) {
         n1 = (Pt) e.nextElement();
      } else {
         n1 = null; 
      }  
      if(e.hasMoreElements()) {
         n2 = (Pt) e.nextElement();
      } else {
         n2 = null; 
      }      
      if(e.hasMoreElements()) {
         n3 = (Pt) e.nextElement();
      } else {
         n3 = null; 
      }      
      while(cur != null) {
        if(weighted) {
          cur.setDir(p3, p2, p1, n1, n2, n3);
        } 
        else { // not weighted
          cur.setDir();
        }
        
        // Increment the Points
        p3 = p2;
        p2 = p1;
        p1 = cur;
        cur = n1;
        n1 = n2;
        n2 = n3;
        if(e.hasMoreElements())  {
          n3 = (Pt) e.nextElement();
        } else {
          n3 = null; 
        } // e.hasMoreElements()
      } // while
    }

    public void processSmooth() {
      Pt p1 = null;
      Pt cur = null;
      Pt n1 = null;
      
      Pt s = null;
      Pt prev_s = null;

      Pt fromP = null;
      int from_i = 0;
      int from_dist = 10;
      
      Pt toP = null;
      int to_i = 0;
      int to_dist = 10;
      
      int cur_i;
      
      // Parameters
      
      Enumeration e = points.elements();
      cur = (Pt) e.nextElement();
      if(e.hasMoreElements()) {
         n1 = (Pt) e.nextElement();
      } else {
         n1 = null; 
      }  

      while(cur != null) {
        //if(cur.flatCorner == true) 
          if(cur.onCorner())
    	  {
          //parent.println(cur.toString(true));  
          
          // Find the index in the Vector of the current point.
          if((cur.idx - 10) < 0)
             cur_i = points.indexOf(cur, 0);  // -10 to give it some space to search
          else        
             cur_i = points.indexOf(cur, cur.idx - 10);  // -10 to give it some space to search
          //parent.println("My Index: " + cur.idx + " Vector points index: " + cur_i);
          
          // Find from Pt
          for(int i = 0; i < from_dist; i++) {
            if(!((cur_i - i) < 0)) {
              s = (Pt) points.elementAt(cur_i - i);
              if(s != null) {
                from_i = cur_i - i;
              }
            } 
          }  
          fromP = (Pt) points.elementAt(from_i);
          
          // Find to Pt
          if((cur_i + to_dist) > points.size()) { // over-shoot strip
            to_dist = points.size() - cur_i;
            //parent.println("Current Index: " + cur_i + " Points Size: " + points.size() + " New to_dist: " + to_dist);
          }
          
          for(int i = 0; i < to_dist; i++) {
            if(!((cur_i + i) > points.size())) {
              s = (Pt) points.elementAt(cur_i + i);
              if(s != null) {
                to_i = cur_i + i;
              }
            }
          }  
          toP = (Pt) points.elementAt(to_i);
          
          // Linearly Interpolate
          int num = to_i - from_i;
          float di = (toP.i - fromP.i)/num; 
          float dj = (toP.j - fromP.j)/num;
          float dk = (toP.k - fromP.k)/num;
          float ijk_mag;
          float ddirX = (toP.dirX - fromP.dirX)/num;
          float ddirY = (toP.dirY - fromP.dirY)/num;
          float ddirZ = (toP.dirZ - fromP.dirZ)/num;
          float dirXYZmag;
          
          // Calculate the Raw Values
          for(int i = (from_i + 1); i < to_i; i++) {
            s = (Pt) points.elementAt(i);
            prev_s = (Pt) points.elementAt(i-1);
            
            s.i = prev_s.i + di;
            s.j = prev_s.j + dj;           
            s.k = prev_s.k + dk;    
            
            s.dirX = prev_s.dirX + ddirX;
            s.dirY = prev_s.dirY + ddirY;           
            s.dirZ = prev_s.dirZ + ddirZ;
            
            //parent.println("Points Index RAW: " + i + " (" + s.i + ", " + s.j + ", " + s.k + ") [" + s.dirX + ", " + s.dirY + ", " + s.dirZ + "]");
          }

          // Second pass: normalize the vectors
          for(int i = (from_i + 1); i < to_i; i++) {
            s = (Pt) points.elementAt(i);

            ijk_mag = parent.mag(s.i, s.j, s.k);
            s.i = s.i / ijk_mag; 
            s.j = s.j / ijk_mag;
            s.k = s.k / ijk_mag;
            
            dirXYZmag = parent.mag(s.dirX, s.dirY, s.dirZ);
            s.dirX = s.dirX / dirXYZmag;
            s.dirY = s.dirY / dirXYZmag;           
            s.dirZ = s.dirZ / dirXYZmag;
            
            //parent.println("Points Index NORM: " + i + " (" + s.i + ", " + s.j + ", " + s.k + ") [" + s.dirX + ", " + s.dirY + ", " + s.dirZ + "]");
          }         
          
          s = (Pt) points.elementAt(to_i);
          //parent.println("Last Point Index: " + to_i + " (" + s.i + ", " + s.j + ", " + s.k + ") [" + s.dirX + ", " + s.dirY + ", " + s.dirZ + "]");
        }
        
        // Increment the Points
        p1 = cur;
        cur = n1;
        if(e.hasMoreElements())  {
          n1 = (Pt) e.nextElement();
        } else {
          n1 = null; 
        } // e.hasMoreElements()
      } // while
    }

    public void normalizeCornerDelta() {
      Pt p1 = null;
      Pt cur = null;
      Pt n1 = null;
      
      Pt s = null;

      Pt fromP = null;
      int from_i = 0;
      int from_dist = 10;
      
      Pt toP = null;
      int to_i = 0;
      int to_dist = 10;
      
      int cur_i;
      
      // Parameters
      
      Enumeration e = points.elements();
      cur = (Pt) e.nextElement();
      if(e.hasMoreElements()) {
         n1 = (Pt) e.nextElement();
      } else {
         n1 = null; 
      }  

      while(cur != null) {
        // if(cur.flatCorner == true) 
        if(cur.onCorner())
    	{
          //parent.println(cur.toString(true));  
          
          // Find the index in the Vector of the current point.
          cur_i = points.indexOf(cur, cur.idx-10); 
          //parent.println("My Index: " + cur.idx + " Vector points index: " + cur_i);
          
          // Find from Pt
          for(int i = 0; i < from_dist; i++) {
            s = (Pt) points.elementAt(cur_i - i);
            if(s != null) {
              from_i = cur_i - i;
            }
          }  
          fromP = (Pt) points.elementAt(from_i);
          
          // Find to Pt
          for(int i = 0; i < to_dist; i++) {
            s = (Pt) points.elementAt(cur_i + i);
            if(s != null) {
              to_i = cur_i + i;
            }
          }  
          toP = (Pt) points.elementAt(to_i);
          
          // Print the next 10 points 
          for(int i = (from_i+1); i < to_i; i++) {
            s = (Pt) points.elementAt(i);
            s.normalizeDelta();
            // parent.println("Points Index: " + i + " (" + s.i + ", " + s.j + ", " + s.k + ") [" + s.deltaX + ", " + s.deltaY + ", " + s.deltaZ + "]");
          }     
        }
        
        // Increment the Points
        p1 = cur;
        cur = n1;
        if(e.hasMoreElements())  {
          n1 = (Pt) e.nextElement();
        } else {
          n1 = null; 
        } // e.hasMoreElements()
      } // while
    }

    // Pt select point
    public Pt selectFlatPt() {
  
   // TODO - need to generate these points
   /* 	
      if(parent.theMandrel.topPt != null) 
        return parent.theMandrel.topPt;
      else if(parent.theMandrel.frontPt != null)
        return parent.theMandrel.frontPt;
      else if(parent.theMandrel.bottomPt!= null)
        return parent.theMandrel.bottomPt;
      else if(parent.theMandrel.backPt != null)
        return parent.theMandrel.backPt;
      else
   */    
        return null;
    }

    public void normalizeDelta() {
      float norm = 0;
      Pt cur = null;
      Pt flatPoint = null;
      
      Enumeration e = points.elements();
      // parent.println("Normalizing Delta: " + ID + " pts: " + points.size());
            
      while(e.hasMoreElements()) { 
        cur = (Pt) e.nextElement();
        flatPoint = null; // clear the selected flatPoint
        
        // if((cur != null) && (flatPoint != null)) {
        if((cur != null) && cur.onCorner()) {        
          // Flat point delta's:
          // Really all we need from the Flat Points are two pieces of info:
          // 1. Orientation (a function of normal Y, normal Z, direction X, direction Y, direction Z
          // 2. normal Y and normal Z is standard for flat sides
          // 3. direction X, Y and Z can be extrapolated.
          //
               
          // flatPoint = parent.theMandrel.frontPt;
          float o; 
          float flatDirX;
          
          // Calc Orientation for flat point:
          // o = parent.atan(flatPoint.deltaX / ( (flatPoint.j - flatPoint.deltaZ) - (flatPoint.k - flatPoint.deltaY) ));
          // parent.println("Flat Point o: " + parent.degrees(o) + " Deltas: (" + flatPoint.deltaX + "," + flatPoint.deltaY + "," + flatPoint.deltaZ + ")" + " mag: " + parent.mag(flatPoint.deltaX, flatPoint.deltaY, flatPoint.deltaZ));
          
          // Use the Pt's flatO and flatDirX:
          o = cur.flatO;
          flatDirX = cur.flatDirX;
          
          // Calculate the constants with our normals
          float a = 0.0f;
          float b = 0.0f;
          float c = 0.0f;
          
          /*
          if(cur.j == 0) {
            return; // don't divide by zero  
          }
          */
          
          a = (cur.k / cur.j); // nz / ny
          b = flatDirX / (parent.tan(o) * cur.j); // Dx / (tan(o) * ny)
          c = b*b + flatDirX*flatDirX - 1; // b^2 + flatDirX^2 - 1
                    
          float desc = 0;

          desc = parent.abs(a*a*b*b - (1 + a*a)*c); // no small rounding negatives
          
          // Solve for deltaY (two soln)
          float dY1, dY2, dZ1, dZ2;
          float dSQ1, dSQ2;
          
          dY1 = (-a*b + parent.sqrt(desc)) / (1 + a*a);
          dY2 = (-a*b - parent.sqrt(desc)) / (1 + a*a);
          
          dSQ1 = 1 - (flatDirX * flatDirX) - (dY1*dY1);
          dSQ2 = 1 - (flatDirX * flatDirX) - (dY2*dY2);
          
          dZ1 = parent.sqrt(parent.abs(dSQ1)); // take the absolute value of dSQ in case of rounding err  
          dZ2 = parent.sqrt(parent.abs(dSQ2));  
                    
          ///parent.println("  Current Point nx: " + cur.i + " ny: " + cur.j + " nz: " + cur.k + " mag: " + parent.mag(cur.i, cur.j, cur.k));
          ///parent.println("  Constants a: " + a + " b: " + b + " c: " + c + " descrim: " + desc);
          ///parent.println("  Current dY: " + cur.deltaY + " dZ: " + cur.deltaZ + ", Solved [dY: " + dY1 + ", dZ: " + dZ1 + " dSQ: " + dSQ1 + "] [dY: " + dY2 + ", dZ: " + dZ2 + " dSQ: " + dSQ2 + "]");
          
          // Confirm Sum of Squares Solutions
          float sum1 = parent.sqrt(flatDirX*flatDirX + dY1*dY1 + dZ1*dZ1);  
          float sum2 = parent.sqrt(flatDirX*flatDirX + dY2*dY2 + dZ2*dZ2);  
          float mag1 = parent.mag(flatDirX, dY1, dZ1);
          float mag2 = parent.mag(flatDirX, dY2, dZ2);
          
          ///parent.println("  Check Sums 1: " + sum1 + " 2: " + sum2 + " Mag 1: " + mag1 + " 2: " + mag2);
          
          float o1 = parent.atan2(flatDirX, ( (cur.j * dZ1) - (cur.k * dY1) ));   // Version flatPoint
          float o2 = parent.atan2(flatDirX, ( (cur.j * dZ2) - (cur.k * dY2) ));   // Version flatPoint
                    
          // parent.println("  Check Flat Orientation: " + parent.degrees(o) + " [Orientation 1: " + parent.degrees(o1) + " Orientation 2: " + parent.degrees(o2) + "]");
          
          // parent.println("------------------------------------------------------------------------------------------");
          
          // Choose a dY and dZ to go with
          
          //// Produces Nice Swept Curves, but only for Small deltaX (shallow curves) ////
          /*
          cur.deltaX = flatDirX;
          cur.deltaY = dZ2;
          cur.deltaZ = dY2;
          */

// TODO - retro-fit

          if(!Float.isNaN(dY1) || !Float.isNaN(dZ1)) {
            if(parent.degrees(o) > 90) { // a Negative Orientation
              cur.deltaX = -flatDirX;
                // cur.printType();
                // parent.println("NEGATIVE ORIENTATION");
                if(cur.type == Pt.TOP_FRONT_CORNER) {
                  cur.deltaY = -dY1;
                  cur.deltaZ = dZ1;   
                } else if(cur.type == Pt.FRONT_BACK_CORNER) { // might have to split depending on the y value
                  cur.deltaY = -dY1;
                  cur.deltaZ = -dZ1;  
                } else if(cur.type == Pt.BACK_TOP_CORNER) {
                  cur.deltaY = -dY1;
                  cur.deltaZ = dZ1;  
                }
                           
            } else { // Positive Orientation
              cur.deltaX = flatDirX;
         /*     
              if((cur.type == Pt.BOTTOM_BACK) || (cur.type == Pt.BACK_TOP)) {
                cur.deltaZ = -dY2; // dZ1 does not change (BOTTOM-BACK reverse the sign)
              } else {
                cur.deltaZ = dY2; 
              }
          */
              
// TODO - retro-fit 
              cur.deltaY = dY2; // dY1 does not change should change? 
            
              if(cur.type == Pt.FRONT_BACK_CORNER) {
                cur.deltaZ = -dZ2; // dZ1 does not change (BOTTOM-BACK reverse the sign)
              } else {
                cur.deltaZ = dZ2; 
              }
            } 
          }
        }
      } // TODO - add back in
    } // TODO - add back in
 
    public void circlepolateDelta() {
   // TODO - retro fit
   /* 	
      float norm = 0;
      Pt cur = null;
      Pt flatPoint = null;
      Enumeration e = points.elements();
      while(e.hasMoreElements()) { 
        cur = (Pt) e.nextElement();
        flatPoint = null; // clear the selected flatPoint
        
        switch(cur.type) {
          case Pt.TOP: // = 1;
          break;
          case Pt.TOP_FRONT: // = 2;
          //parent.print("TOP FRONT ");
          if(parent.theMandrel.topPt != null) {
            //parent.print(" topPt: ");
            flatPoint = parent.theMandrel.topPt;
          } else {
            flatPoint = selectFlatPt();
          }
          break;
          case Pt.FRONT: // = 3;
          break;
          case Pt.FRONT_BOTTOM: // = 4;
          //parent.print("FRONT_BOTTOM ");
          if(parent.theMandrel.frontPt != null) {
            //parent.print(" frontPt: ");
            flatPoint = parent.theMandrel.frontPt;
          } else {
            flatPoint = selectFlatPt();
          }
          break;
          case Pt.BOTTOM: // = 5;
          break;
          case Pt.BOTTOM_BACK: // = 6; 
          //parent.print("BOTTOM_BACK ");
          if(parent.theMandrel.bottomPt != null) {
            //parent.print(" bottomPt: ");
            flatPoint = parent.theMandrel.bottomPt;
          } else {
            flatPoint = selectFlatPt();
          } 
          break;
          case Pt.BACK: // = 7;
          break;
          case Pt.BACK_TOP: // = 8; 
          //parent.print("BACK_TOP ");
          if(parent.theMandrel.backPt != null) {
            //parent.print(" backPt: ");
            flatPoint = parent.theMandrel.backPt;
          } else {
            flatPoint = selectFlatPt();
          }  
          break;
        }
        
        if((cur != null) && (flatPoint != null)) {
          // Normalize these points:
          //norm = flatPoint.deltaX / cur.deltaX;
         //parent.print(" norm: " + norm + "(" + cur.deltaX + "," + cur.deltaY + "," + cur.deltaZ + ")");
          //cur.deltaX = norm * cur.deltaX; // converts it to the selected dirX above.
          //cur.deltaY = norm * cur.deltaY;
          //cur.deltaZ = norm * cur.deltaZ;
          // parent.println("->(" + cur.deltaX + "," + cur.deltaY + "," + cur.deltaZ + ")");
 */       
          // Reverse Calculate the Necessary DirX:
          /*
          float o; 
          // Calc Orientation for flat point:
          o = parent.atan(flatPoint.deltaX / ( (flatPoint.j - flatPoint.deltaZ) - (flatPoint.k - flatPoint.deltaY) ));
          
          parent.print(" o: " + parent.degrees(o) + "(" + cur.deltaX + "," + cur.deltaY + "," + cur.deltaZ + ")");
                    
          // Solve for Dir X
          cur.deltaX = parent.tan(o) * ((cur.j - cur.deltaZ) - (cur.k - cur.deltaY));
          parent.println("->(" + cur.deltaX + "," + cur.deltaY + "," + cur.deltaZ + ")");
          */
        
          // Flat point delta's:
          // Really all we need from the Flat Points are two pieces of info:
          // 1. Orientation (a function of normal Y, normal Z, direction X, direction Y, direction Z
          // 2. normal Y and normal Z is standard for flat sides
          // 3. direction X, Y and Z can be extrapolated.
          //

// TODO - retro-fit
/*    	
          float topO = 0.0f;
          float frontO = 0.0f;
          float bottomO = 0.0f;
          float backO = 0.0f;
          
          if(parent.theMandrel.topPt != null) {
            topO = parent.theMandrel.topPt.deltaOrientation();
          } else {
            parent.println("Top Orientation NOT available.");
          }

          if(parent.theMandrel.frontPt != null) {
            frontO = parent.theMandrel.frontPt.deltaOrientation();
          } else {
            parent.println("Front Orientation NOT available.");
          }

          if(parent.theMandrel.bottomPt != null) {
            bottomO = parent.theMandrel.bottomPt.deltaOrientation();
          } else {
            parent.println("Bottom Orientation NOT available.");
          }

          if(parent.theMandrel.backPt != null) {
            backO = parent.theMandrel.backPt.deltaOrientation();
          } else {
            parent.println("Back Orientation NOT available.");
          }
       
          // Print the Orientations:
          parent.println("Top O: " + parent.degrees(topO) + " Front O: " + parent.degrees(frontO) + " Bottom O: " + parent.degrees(bottomO) + " Back O: " + parent.degrees(backO));
     
          // flatPoint = parent.theMandrel.frontPt;
          float o; 
          float flatDirX;
          
          // Calc Orientation for flat point:
          // o = parent.atan(flatPoint.deltaX / ( (flatPoint.j - flatPoint.deltaZ) - (flatPoint.k - flatPoint.deltaY) ));
          // parent.println("Flat Point o: " + parent.degrees(o) + " Deltas: (" + flatPoint.deltaX + "," + flatPoint.deltaY + "," + flatPoint.deltaZ + ")" + " mag: " + parent.mag(flatPoint.deltaX, flatPoint.deltaY, flatPoint.deltaZ));
          
          // Use the Pt's flatO and flatDirX:
          o = cur.flatO;
          flatDirX = cur.flatDirX;
          
          // Calculate the c constant with our normals
          float c = 0;
          // c = cur.j - (flatPoint.deltaX / parent.tan(o)) - cur.k; // ny = j, nz = k // Version Dy
          // c = cur.k + (flatPoint.deltaX / parent.tan(o)) - cur.j; // Version flatPoint
          c = cur.k + (flatDirX / parent.tan(o)) - cur.j;
          float desc = 0;
          // desc = 2 - 2 * flatPoint.deltaX * flatPoint.deltaX - c; // Version Dy
          // desc = 2 - (2 * flatPoint.deltaX * flatPoint.deltaX) - (c*c); // Version flatPoint
          desc = 2 - (2*flatDirX*flatDirX) - (c*c);
          
          // Solve for deltaY (two soln)
          float dY1, dY2, dZ1, dZ2;
          //dY1 = (-c + parent.sqrt(desc)) / 2.0; // Version Dy
          //dY2 = (-c - parent.sqrt(desc)) / 2.0; // Version Dy
          //dZ1 = dY1 + c;                        // Version Dy
          //dZ2 = dY2 + c;                        // Version Dy
          
          dZ1 = (-c + parent.sqrt(desc)) / 2.0f;
          dZ2 = (-c - parent.sqrt(desc)) / 2.0f;
          //dY1 = parent.sqrt(1 - (flatPoint.deltaX * flatPoint.deltaX) - (dZ1*dZ1));  // Version flatPoint
          //dY2 = parent.sqrt(1 - (flatPoint.deltaX * flatPoint.deltaX) - (dZ2*dZ2));  // Version flatPoint
          dY1 = parent.sqrt(1 - (flatDirX * flatDirX) - (dZ1*dZ1));  // Version flatPoint
          dY2 = parent.sqrt(1 - (flatDirX * flatDirX) - (dZ2*dZ2));  // Version flatPoint
                    
          parent.println("  Current Point nx: " + cur.i + " ny: " + cur.j + " nz: " + cur.k + " mag: " + parent.mag(cur.i, cur.j, cur.k));
          parent.println("  Constants c: " + c + " descrim: " + desc);
          parent.println("  Current dY: " + cur.deltaY + " dZ: " + cur.deltaZ + ", Solved [dY: " + dY1 + ", dZ: " + dZ1 + "] [dY: " + dY2 + ", dZ: " + dZ2 + "]");
          
          // Confirm Sum of Squares Solutions
          //float sum1 = parent.sqrt(flatPoint.deltaX*flatPoint.deltaX + dY1*dY1 + dZ1*dZ1);   // Version flatPoint
          //float sum2 = parent.sqrt(flatPoint.deltaX*flatPoint.deltaX + dY2*dY2 + dZ2*dZ2);  
          //float mag1 = parent.mag(flatPoint.deltaX, dY1, dZ1);
          //float mag2 = parent.mag(flatPoint.deltaX, dY2, dZ2);
          float sum1 = parent.sqrt(flatDirX*flatDirX + dY1*dY1 + dZ1*dZ1);  
          float sum2 = parent.sqrt(flatDirX*flatDirX + dY2*dY2 + dZ2*dZ2);  
          float mag1 = parent.mag(flatDirX, dY1, dZ1);
          float mag2 = parent.mag(flatDirX, dY2, dZ2);
          
          parent.println("  Check Sums 1: " + sum1 + " 2: " + sum2 + " Mag 1: " + mag1 + " 2: " + mag2);
          
          // float o1 = parent.atan(flatPoint.deltaX / ( (cur.j - dZ1) - (cur.k - dY1) ));
          // float o2 = parent.atan(flatPoint.deltaX / ( (cur.j - dZ2) - (cur.k - dY2) ));
          // float o1 = parent.atan(flatDirX / ( (cur.j - dZ1) - (cur.k - dY1) ));   // Version flatPoint
          // float o2 = parent.atan(flatDirX / ( (cur.j - dZ2) - (cur.k - dY2) ));   // Version flatPoint
          // Use atan2:
          float o1 = parent.atan2(flatDirX, ( (cur.j - dZ1) - (cur.k - dY1) ));   // Version flatPoint
          float o2 = parent.atan2(flatDirX, ( (cur.j - dZ2) - (cur.k - dY2) ));   // Version flatPoint
                    
          parent.println("  Check Flat Orientation: " + parent.degrees(o) + " [Orientation 1: " + parent.degrees(o1) + " Orientation 2: " + parent.degrees(o2) + "]");
          
          parent.println("------------------------------------------------------------------------------------------");
          
          // Choose a dY and dZ to go with
          
          //// Produces Nice Swept Curves, but only for Small deltaX (shallow curves) ////
          cur.deltaX = flatDirX;
          cur.deltaY = dZ2;
          cur.deltaZ = dY2;
          
        }
      }
   */   
    }

   public void calcOrientation() {
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        Pt p = (Pt) e.nextElement();  
        p.calcOrientation();
      }
   }
   
   public int numberPts(int ptPos) {
	   Pt cur;
	   Pt trimMe;
	   int ptCount = ptPos;

	   Enumeration e = points.elements();
	   cur = (Pt) e.nextElement();  
	   cur.idx = ptCount;

	   while(e.hasMoreElements()) {
		   cur = (Pt) e.nextElement();
		   if(cur.removeMe) {
			 points.remove(cur);
		   } else {
			 ptCount++;
			 cur.idx = ptCount; // index the point
		   }
	   }
	   return (ptCount + 1);
   }
 
   // Printing and Rendering
   
    public void drawStrip() {
        float fromU = -1; 
        float fromV = -1;
        float toU = -1; 
        float toV = -1;
   
    	//sortPoints();
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        Pt p = (Pt) e.nextElement();        
        p.drawPt(parent.g);
        fromU = toU;
        fromV = toV;
        toU = PApplet.parseInt(p.u);
        toV = PApplet.parseInt(p.v);
        if((fromU >= 0) && (fromV >= 0) && (toU >= 0) && (toV >= 0)) {
          parent.g.stroke(255,0,0,75);
          parent.g.noFill();
          parent.g.line(fromU, fromV, toU, toV);  
        }         
      }
    }  
   
   public void drawFiberAngle() {
      //sortPoints();
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        Pt p = (Pt) e.nextElement();  
        p.drawFiberAngle(parent.g);
      }
    }
    
   // Replaces the ply's num with a customizable num as opposed to using the ID
   public void printSegment(PrintWriter pw, boolean verbose) {
	   pw.println("# Segment " + ID + " Angle: " + angle);
	   for(Enumeration e = points.elements(); e.hasMoreElements();) {
		   pw.println(((Pt) e.nextElement()).toString(verbose));  // verbose
	   }
   }

    public void printGraphics(PGraphics g) {
        // 4.17.2010 parent.println("  Graphing ply/strip/segment " + ID + " Angle: " + angle); // strip ID
        // CHECK CLOSED SHAPE?
        g.beginShape(g.LINES);
        //g.beginShape(); // Lines connect back to a single point over and over again
        for(Enumeration e = points.elements(); e.hasMoreElements();) {
          ((Pt) e.nextElement()).printGraphics(g); 
        }
        g.endShape();
    }
    
    public void drawNormalVector(PGraphics g) {
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        ((Pt) e.nextElement()).drawNormalVector(g); 
      }
    }
      
    public void drawDirVector(PGraphics g) {
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        ((Pt) e.nextElement()).drawDirVector(g); 
      }
    }

    public void drawDeltaVector(PGraphics g) {
      for(Enumeration e = points.elements(); e.hasMoreElements();) {
        ((Pt) e.nextElement()).drawDeltaVector(g); 
      }
    }
  
    public boolean verify() {
    	return true;
    }
} 


