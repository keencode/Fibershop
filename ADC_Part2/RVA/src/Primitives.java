/*import java.util.Enumeration;
import java.util.Vector;

import processing.core.PGraphics;

 ///////////////////////////////////////////////////////////////
  // Finishing Primitives - BANDS
  public void band(float startX, float endX, float density)
  {
      float start = theMandrel.inch2unit(startX);
      float end = theMandrel.inch2unit(endX);
      
      float ang = atan2(height, (end - start));
      float len = (end - start) / cos(ang);
      
      //ptape(start, height, (90 - (ang / density)), density * len);
  }
  

  ///////////////////////////////////////////////////////////////
  // RVA PATTERN PRIMITIVES - (Turn into Objects)
  
  // Beam Primitive - shoots a density (number of lines) of tapes from a point.
  // x - in inches on the mandrel
  // y - in inches on the mandrel
  // angle - the direction of the top chord of the beam
  // beam_width - angle from the top_chord in the negative direction
  // density - number of beams
  public void beam(float x, float y, float angle, float beam_width, int density) {
    float unit_x = theMandrel.inch2unit(x);
    float unit_y = (theMandrel.top_front_y - theMandrel.top_front_r) - theMandrel.inch2unit(y);
    
    float delta_w = beam_width / density;
 
    for(int i = 0; i < density; i++) {
        //ptape(unit_x, unit_y , angle + i*delta_w, 20*height); // magnitude is something large 1000
    }
  }  
  
  // Beam Primitive - Copied onto 
  public void copy_beam(float x, float y, float angle, float beam_width, int density) {
    float unit_x = theMandrel.inch2unit(x);
    float unit_y = (theMandrel.top_front_y - theMandrel.top_front_r) - theMandrel.inch2unit(y);
    float rev_y = theMandrel.back_top_y - (unit_y - theMandrel.back_top_y);
    
    float copy_x = unit_x - theMandrel.l;
    float copy_y = unit_y + theMandrel.h + theMandrel.w;
    float rev_copy_y = rev_y + theMandrel.h + theMandrel.w;
    
    float delta_w = beam_width / density;
 
    for(int i = 0; i < density; i++) {
        // Original
        ptape(unit_x, unit_y, (angle + i*delta_w)%360, 20*height, theMandrel.topClip); // magnitude is something large 1000
        // Reflected
        ptape(unit_x, rev_y, (-angle - i*delta_w)%360, 20*height, theMandrel.topClip); // magnitude is something large 1000

        // Original
        ptape(copy_x, copy_y, (angle + i*delta_w)%360, 20*height, theMandrel.botClip); // magnitude is something large 1000
        // Reflected
        ptape(copy_x, rev_copy_y, (-angle - i*delta_w)%360, 20*height, theMandrel.botClip); // magnitude is something large 1000
    }
}  

public void rbeam(float x, float y, float angle, float beam_width, int density) {

    //println("rbeam(" + x + ", " + y + "," + angle + ", " + beam_width + ", " + density + ");");
    
    float mirror_x = theMandrel.unit2inch(theMandrel.l);
    
    copy_beam(x, y, angle, beam_width, density);
    if(!mousePressed)
      copy_beam(-x  + 2*mirror_x, y, 180 - angle, -beam_width, density);
  
    stroke(255,0,0);
    line(theMandrel.inch2unit(mirror_x), 0, theMandrel.inch2unit(mirror_x), height);
}



  ///////////////////////////////////////////////////////////////
  // TAPING PRIMITIVES
  //
    
  
//  Polar Tape - specify a piece of tape as having and angle and a magnitude
//     
  public void ptape(float x, float y, float angle, float magnitude, ClipRect cr) {
    tape(x, y, x + magnitude*cos(radians(angle)), y - magnitude*sin(radians(angle)), cr);  
  }

  public void ptape(float x, float y, float angle, float magnitude) {
    tape(x, y, x + magnitude*cos(radians(angle)), y - magnitude*sin(radians(angle)), thickness/unitSize, false); // turn off the old clipping
  }

//

//  void ptape(int x, int y, int angle, int magnitude, boolean user_clipping) {
//    tape(x, y, int(x + float(magnitude)*sin(radians(angle))), 
//      int(y - float(magnitude)*cos(radians(angle))), user_clipping);  
//  }
//
//  void ptape(float x, float y, float angle, float magnitude, boolean user_clipping) {
//    tape(x, y, x + magnitude*sin(radians(angle)), 
//            y - magnitude*cos(radians(angle)), user_clipping);  
//  }
//
//  void ptape(int x, int y, float angle, int magnitude, boolean user_clipping) {
//    tape(x, y, x + float(magnitude)*sin(radians(angle)), 
//               y - float(magnitude)*cos(radians(angle)), user_clipping);  
//  }


  // Curved Tape through crude interpolation.

  public void ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
    if(from_angle > 90) neg_ctape(x, y, from_angle, to_angle, magnitude);
    else pos_ctape(x, y, from_angle, to_angle, magnitude); 
  }

  public void ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
    if(from_angle > 90) neg_ctape_trig(x, y, from_angle, to_angle, magnitude);
    else pos_ctape_trig(x, y, from_angle, to_angle, magnitude); 
  }

  public void ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude) {
    if(from_angle > 90) neg_ctape_lin(x, y, from_angle, to_angle, magnitude);
    else pos_ctape_lin(x, y, from_angle, to_angle, magnitude); 
  }

  public void neg_ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress - delta_y, thickness/unitSize);
      
      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height, thickness/unitSize);
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
    }
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }
    
  public void pos_ctape(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize);
      
      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height, thickness/unitSize);
      }
 
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
    }
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }

  public void neg_ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress - delta_y, thickness/unitSize);
      
      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height, thickness/unitSize);
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
      
      // Use a cos to intensify the starting degrees
      angle_progress = angle_progress + (0.05f)*delta_angle + (0.95f)*delta_angle*(cos(pi_progress) + 1); // trig factor varies from 2 to 1
      pi_progress = pi_progress + slice_of_pi;
    }
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }
    
  public void pos_ctape_trig(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;    
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize);
      
      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height, thickness/unitSize);
      }
 
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
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }

  public void neg_ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = (180 - from_angle);
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = ((180 - to_angle) - (180 - from_angle)) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress - delta_y, thickness/unitSize);
      
      if((y_progress - delta_y) < 0) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress + height, x_progress + delta_x, y_progress - delta_y + height, thickness/unitSize);
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
//      
//      if((x_progress / width) < 0.3) {
//        angle_progress = angle_progress + 0.1*delta_angle;
//      } else if((x_progress / width) < 0.6) {
//        angle_progress = angle_progress + delta_angle;        
//      } else {
//        angle_progress = angle_progress + 10*delta_angle;         
//      }

      angle_progress = angle_progress + delta_angle*(100*(x_progress / width)*(x_progress / width))/10;
    }
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }
    
  public void pos_ctape_lin(float x, float y, float from_angle, float to_angle, float magnitude) {
    float delta_mag = 10;   // size of every sub-sections
    float delta_x;
    float delta_y;
    float x_progress = x;
    float y_progress = y;
    float angle_progress = from_angle;
    float num_sections = magnitude / delta_mag; // number of sub-sections of tape.
    float delta_angle = (to_angle - from_angle) / num_sections; 
    float slice_of_pi = PI / num_sections;
    float pi_progress = PI;    
    boolean wrap = false;
    
    // Tape a number of sub-sections:
    for(int i = 0; i < (int) num_sections; i++) {
      delta_x = delta_mag*sin(radians(angle_progress)); 
      delta_y = delta_mag*cos(radians(angle_progress));
       
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress - delta_y);  
      // tape(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize, false);
      // ptape(x_progress, y, angle_progress, delta_mag);
      // segment(x_progress, y_progress, x_progress + delta_x, y_progress + delta_y);
      segment(TAPE_BOTH, x_progress, y_progress, x_progress + delta_x, y_progress + delta_y, thickness/unitSize);
      
      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        segment(TAPE_BOTH, x_progress, y_progress - height, x_progress + delta_x, y_progress + delta_y - height, thickness/unitSize);
      }
 
      if(wrap) { // we just taped a wrap - correct the progress
        y_progress = y_progress % height;
        wrap = false;
      }
      
      x_progress = x_progress + delta_x;

      if(y_progress > ((y_progress + delta_y)%height)) { // the next Tape will be a wrapped piece
        wrap = true;
      }      

      y_progress =  y_progress + delta_y;
      

//      if((x_progress / width) < 0.3) {
//        angle_progress = angle_progress + 0.1*delta_angle;
//      } else if((x_progress / width) < 0.6) {
//        angle_progress = angle_progress + delta_angle;        
//      } else {
//        angle_progress = angle_progress + 10*delta_angle;         
//      }
//     
      angle_progress = angle_progress + delta_angle*(100*(x_progress / width)*(x_progress / width))/10;
    }
    
    // Draw the last bit of the sub-sections (don't worry about this - globally insufficient) 
  }

  
//  Ray Tape - like Polar Tape but it will constrain itself with in the pattern box
//  
  //void raytape(int x, int y, int angle, int bounces);
 
 public void tape(float x1, float y1, float x2, float y2, ClipRect cr) {
   // Clip the tape to the specified ClipRect
    ClipLine cl;
    cl = cr.clip(x1,y1,x2,y2);
    if(cl.accept)    
     tape(cl.startx, cl.starty, cl.endx, cl.endy, thickness/unitSize, false); // turn off the old clipping
 }
 
  // Default
  public void tape(float x1, float y1, float x2, float y2) {
    tape(x1, y1, x2, y2, thickness/unitSize); // use the width given by the thickness variable
  }

  public void tape(float x1, float y1, float x2, float y2, boolean user_clipping) {
    tape(x1, y1, x2, y2, thickness/unitSize, user_clipping); // use the width given by the thickness variable
  }

  // Entry Point : decides whether the tape needs to be wrapped or not.
  public void tape(float x1, float y1, float x2, float y2, float w) {

    // First Check if the tape is within the Pattern:
    if( (x1 >= 0) && (x1 <= width) && (y1 >= 0) && (y1 <= height) &&
        (x2 >= 0) && (x2 <= width) && (y2 >= 0) && (y2 <= height) ) {
      segment(TAPE_BOTH, x1, y1, x2, y2, w);
    } else {      
      if(clipping == true) { // clipping turned on
        clip_tape(x1, y1, x2, y2, w); 
      } else { // no clipping: wrapping
        // If Rendering, submit the entire piece of wrapped tape.
        if((exportState != exportNone) && (exportState != displayPDF)) {
          segmentExport(TAPE_BOTH, x1, y1, x2, y2, w);
        }  else { // Not Exporting only displaying 
          wtape(x1, y1, x2, y2, w);
        }
      }
    }
  }

  //// SELECTABLE clipped AND wrapped TAPES ////

  public void tape(float x1, float y1, float x2, float y2, float w, boolean user_clipping) {

    // First Check if the tape is within the Pattern:
    if( (x1 >= 0) && (x1 <= width) && (y1 >= 0) && (y1 <= height) &&
        (x2 >= 0) && (x2 <= width) && (y2 >= 0) && (y2 <= height) ) {
      segment(TAPE_BOTH, x1, y1, x2, y2, w);
    } else {      
      if(user_clipping == true) { // clipping turned on USER OVER-RIDE
        clip_tape(x1, y1, x2, y2, w); 
      } else { // no clipping: wrapping
        // If Rendering, submit the entire piece of wrapped tape.
        if((exportState != exportNone) && (exportState != displayPDF)) {
          segmentExport(TAPE_BOTH, x1, y1, x2, y2, w);
        }  else { // Not Exporting only displaying 
          wtape(x1, y1, x2, y2, w);
        }
      }
    }
  }



  // Replace this with Cohen-Sutherland algorithm
  // Draws the Clipped Piece of tape
  Vector sections;
  float[] top_sect;
  float[] bottom_sect;
  float[] right_sect;
  float[] left_sect;
  
  public void clip_tape(float x1, float y1, float x2, float y2) {
     clip_tape(x1, y1, x2, y2, DEFAULT_TAPE_WIDTH);
  }
  
  public void clip_tape(float x1, float y1, float x2, float y2, float w) {
      sections = new Vector();
      
      top_sect = intersection(1,1,width-1,1,x1,y1,x2,y2); // top
      if(top_sect != null) {
        if(top_sect[INTERSECTION] == 1) {
          sections.add(top_sect);
          if(exportState == exportNone) {
            stroke(255,0,0);
            ellipse(top_sect[INTERSECTION_X], top_sect[INTERSECTION_Y], 10,10);
          }  
      }
        //  println("Top: (" + top_sect[0] + "," + top_sect[1] + "," + top_sect[2] + ")");   
        // } else {
        //  stroke(0,255,0);
        //}   
      }
      
      bottom_sect = intersection(1,height-1,width-1,height-1,x1,y1,x2,y2); // bottom
      if(bottom_sect != null) {
        if(bottom_sect[INTERSECTION] == 1) {
          sections.add(bottom_sect);
          if(exportState == exportNone) {
            stroke(255,0,0);
            ellipse(bottom_sect[INTERSECTION_X], bottom_sect[INTERSECTION_Y], 10,10); 
          }  
        }
        //  println("Bot: (" + bottom_sect[0] + "," + bottom_sect[1] + "," + bottom_sect[2] + ")"); 
        //} else {
        //  stroke(0,255,0);
        //}        
      }  

      right_sect = intersection(width-1,1,width-1,height-1,x1,y1,x2,y2); // right
      if(right_sect != null) {
        if(right_sect[INTERSECTION] == 1) {
          sections.add(right_sect);
          if(exportState == exportNone) {
            stroke(255,0,0);
            ellipse(right_sect[INTERSECTION_X], right_sect[INTERSECTION_Y], 10,10); 
          }  
        }
        //  println("Rgt: (" + right_sect[0] + "," + right_sect[1] + "," + right_sect[2] + ")");  
        //} else {
        //  stroke(0,255,0);
        //}       
      } 

      left_sect = intersection(1,1,1,height-1,x1,y1,x2,y2); // left
      if(left_sect != null) {
        if(left_sect[INTERSECTION] == 1) {
        sections.add(left_sect);
          if(exportState == exportNone) {
            stroke(255,0,0);
            ellipse(left_sect[INTERSECTION_X], left_sect[INTERSECTION_Y], 10,10); 
          }
        }
        //println("Lft: (" + left_sect[0] + "," + left_sect[1] + "," + left_sect[2] + ")"); 
        //} else {
        //  stroke(0,255,0);
        //}          
      }
   
     if(sections.size() < 1) { // Zero Intersections
        // println("*** ZERO: (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ") -> ");
        // looping = false;
        // noLoop();                                         
     } else if(sections.size() < 2) { // Draw piece of tape only if there are two pieces of tape
        // Check if one of the Strip's lines is on the Pattern
        // one point within, one point clipped
        if(onPattern(x1,y1)) { // connect intersection with x1,y1
          Enumeration e = sections.elements();
          // int[] edge = (int[]) e.nextElement();  
          float[] edge = (float[]) e.nextElement();  
          segment(TAPE_BOTH, x1, y1, edge[INTERSECTION_X], edge[INTERSECTION_Y], w);      
        } else if(onPattern(x2,y2)) { // connect intersection with x2, y2
          Enumeration e = sections.elements();
          // int[] edge = (int[]) e.nextElement();
          float[] edge = (float[]) e.nextElement();  
          segment(TAPE_BOTH, x2, y2, edge[INTERSECTION_X], edge[INTERSECTION_Y], w);
        } else {
          stroke(255,0,0);
          line(x1,y1,x2,y2);
          println("*** ONE: (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ") -> ");
          //looping = false;
          //noLoop();
        }
      } else if(sections.size() == 2) {
        //print("Clipping: (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ") -> ");
        Enumeration e = sections.elements();
        float[] fro = (float[]) e.nextElement();
        float[] to = (float[]) e.nextElement();
        
        if(dist(fro[INTERSECTION_X],fro[INTERSECTION_Y],to[INTERSECTION_X],to[INTERSECTION_Y]) < 3) {
          if(onPattern(x1,y1)) { // connect intersection with x1,y1
            segment(TAPE_BOTH, x1, y1, to[INTERSECTION_X], to[INTERSECTION_Y], w); 
          } else if(onPattern(x2,y2)) { // connect intersection with x2, y2
            segment(TAPE_BOTH, x2, y2, to[INTERSECTION_X], to[INTERSECTION_Y], w);
          }
        } else {
          
          println("(" + fro[INTERSECTION_X] + "," + fro[INTERSECTION_Y] + ")-(" +
                         to[INTERSECTION_X] + "," + to[INTERSECTION_Y] + ")");
          
          segment(TAPE_BOTH, fro[INTERSECTION_X], fro[INTERSECTION_Y], to[INTERSECTION_X], to[INTERSECTION_Y], w);
          // stroke(0,0,255);
          // line(fro[INTERSECTION_X], fro[INTERSECTION_Y], to[INTERSECTION_X], to[INTERSECTION_Y]);
        }
     } else if(sections.size() < 5) { //
        // segment(TAPE_BOTH, x1, y1, x2, y2);   
        Enumeration e = sections.elements();
        float[] uno = (float[]) e.nextElement();
        float[] dos = (float[]) e.nextElement();
        float[] tres = (float[]) e.nextElement();
      
        if(dist(uno[INTERSECTION_X], uno[INTERSECTION_Y], dos[INTERSECTION_X], dos[INTERSECTION_Y]) <
           dist(dos[INTERSECTION_X], dos[INTERSECTION_Y], tres[INTERSECTION_X], tres[INTERSECTION_Y])) {           
              
//              println("Three (" + dos[INTERSECTION_X] + "," + dos[INTERSECTION_Y] + ")-(" +
//                       tres[INTERSECTION_X] + "," + tres[INTERSECTION_Y] + ")");
//              
              segment(TAPE_BOTH, dos[INTERSECTION_X], dos[INTERSECTION_Y], tres[INTERSECTION_X], tres[INTERSECTION_Y], w);            
           } else {
             
//              println("Three (" + uno[INTERSECTION_X] + "," + uno[INTERSECTION_Y] + ")-(" +
//                       dos[INTERSECTION_X] + "," + dos[INTERSECTION_Y] + ")");
//             
             segment(TAPE_BOTH, uno[INTERSECTION_X], uno[INTERSECTION_Y], dos[INTERSECTION_X], dos[INTERSECTION_Y], w);         
           } 
           // println("Three: (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ")"); 

      } else if(sections.size() > 4){ // yikes!
        stroke(0);
        line(x1,y1,x2,y2);
        println("*** YIKES! (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ") -> ");
        looping = false;
        noLoop();
      } // sections.size()
     // Step
     // looping = false;
     // noLoop();
  } // clip_tape()

 
  // Middle Segment of the Piece of Tape
  public void segment(float x1, float y1, float x2, float y2) {
    segment(TAPE_MIDDLE, x1, y1, x2, y2, DEFAULT_TAPE_WIDTH, pattern); 
  }

  // Middle Segment of the Piece of Tape
  public void segment(int type, float x1, float y1, float x2, float y2) {
    segment(type, x1, y1, x2, y2, DEFAULT_TAPE_WIDTH, pattern); 
  }

  public void segment(int type, float x1, float y1, float x2, float y2, float w) {
    segment(type, x1, y1, x2, y2, w, pattern); 
  }

  // x1   x-coordinate of the start point of the tape
  // y1   y-coordinate of the start point of the tape
  // x2   x-coordinate of the end point of the tape
  // y2   y-coordinate of the end point of the tape
  // w    width of the taped piece of line

  public void segment(int type, float x1, float y1, float x2, float y2, float w, PGraphics pg) {
    float angle = atan2((y2-y1), (x2-x1));
    float tape_len = dist(x1, y1, x2, y2);
    float x_tape = 0.0f;
    float y_tape = 0.0f;

    // Only Draw the Segment if within the boundaries of the pattern.
    if( (x1 >= 0) && (x1 <= width) && (y1 >= 0) && (y1 <= height) &&
        (x2 >= 0) && (x2 <= width) && (y2 >= 0) && (y2 <= height) ) {

      // Check if we're exporting the taped segment
      if((exportState != exportNone) && (exportState != displayPDF)) {
        segmentExport(type, x1, y1, x2, y2, w);
      }  else { // Not Exporting only displaying 
    
        // Tape is modelled as opaque material
        fill(tape_fill_r, tape_fill_g, tape_fill_b); // values are dynamic
        stroke(tape_stroke_r, tape_stroke_g, tape_stroke_b);
       
        pushMatrix(); // save our coordinate system
      
        translate(x1, y1); // translate to the start point of the tape
        rotate(angle); // rotate the coordinate system to tape at an angle

        // Draws a straight piece of tape
        beginShape();
        vertex(x_tape, y_tape - (w/2));
        vertex(x_tape + tape_len, y_tape - (w/2));
        if((type == TAPE_START) || (type == TAPE_MIDDLE)) { // END OF TAPE
          stroke(tape_stroke_r, tape_stroke_g, tape_stroke_b); // khaki - clears the END of TAPE line
        } 
        vertex(x_tape + tape_len, y_tape + (w/2));      
        vertex(x_tape, y_tape + (w/2));
        // START      
        if((type == TAPE_END) || (type == TAPE_MIDDLE)) { // END OF TAPE
          stroke(tape_stroke_r, tape_stroke_g, tape_stroke_b);// khaki - clears the END of TAPE line
        }     
        endShape(CLOSE);

        popMatrix();            
      }
    } else { // segment is off the pattern (clip the segment)
      clip_tape(x1, y1, x2, y2, w); /// !!! POTENTIAL LOOP ISSUE !!! ///
    }
  } // segment()

  // Middle Segment of the Piece of Tape
  public void wtape(float x1, float y1, float x2, float y2) {
    wtape(x1, y1, x2, y2, DEFAULT_TAPE_WIDTH);
  }

  public void wtape(float x1, float y1, float x2, float y2, float w) {
    wtape(TAPE_START, x1, y1, x2, y2, w);
  }
  
  public void wtape(int type, float x1, float y1, float x2, float y2, float w) {
    float new_x = 0;
    float new_y = 0;
    int new_type = TAPE_END;

    // Get the Tangent (in radians)
    float angle_deg = atan2(y1 - y2,  x2 - x1);
    if(angle_deg < 0) { // atan2 should take care of this
      angle_deg = 2*PI + angle_deg;
    }
    
    // Debug
    //println("Angle Degrees: " + angle_deg);

    // Debug: Displays the Limits

    stroke(200,0,0);
    line(x1, y1, 0, 0);
    line(x1, y1, 0, height);
    line(x1, y1, width, 0);
    line(x1, y1, width, height);


    // Calculate the Degrees of the Limits
    float ne_limit = atan2(y1,  width - x1);
    float nw_limit = atan2(y1,  -x1);
    float sw_limit = 2*PI + atan2(y1 - height,  -x1);
    float se_limit = 2*PI + atan2(y1 - height,  width - x1);

    if(angle_deg < ne_limit) {
      //println("Angle North East");
      new_x = width;
      new_y = tan(angle_deg) * ((x1 + (y1 / tan(angle_deg))) - width);
      segment(TAPE_END, x1, y1, new_x, new_y, w); // Draw the cut segment     
    } else if(angle_deg < nw_limit) {
      //println("Angle North"); 
      new_x = x1 + (y1 / tan(angle_deg));
      new_y = y2 + height; // assumes that y2 is negative

      segment(TAPE_MIDDLE, x1, y1, new_x, 0, w); // Draw the Segment 
      wtape(TAPE_MIDDLE, new_x, height, x2, new_y, w); // Recursively call wtape
    } else if(angle_deg < sw_limit) {
      //println("Angle West");
      new_x = 0;
      new_y = tan(angle_deg) * (x1 + (y1 / tan(angle_deg)));
      segment(TAPE_END, x1, y1, new_x, new_y, w); // Draw the cut segment
    } else if(angle_deg < se_limit) {
      //println("Angle South");
      new_x = x1 - (height - y1) / tan(angle_deg);
      new_y = y2 - height; // assumes that y2 is negative

      segment(TAPE_MIDDLE, x1, y1, new_x, height, w); // Draw the Segment 
      wtape(TAPE_MIDDLE, new_x, 0, x2, new_y, w); // Recursively call wtape
    } else if(angle_deg < 360) {
      //println("Angle South East");     
      new_x = width;
      new_y = tan(angle_deg) * ((x1 + (y1 / tan(angle_deg))) - width);
      segment(TAPE_END, x1, y1, new_x, new_y, w); // Draw the cut segment           
    } else {
     println("Undefined Angle Degrees: " + angle_deg); 
    }
  }*/