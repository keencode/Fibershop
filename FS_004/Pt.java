import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Point along a piece of Strip, a component of the Strip Class.
 */
public class Pt {
  PatternViewer parent;
  float u; // pattern's 'x' component
  float v; // pattern's 'y' component
  
  int idx;
  
  int type; // Point Type...
  
  //... for ISOSCELES Triangle
  public static final int TOP_FRONT = 1;
  public static final int TOP_FRONT_CORNER = 2;  
  public static final int FRONT = 3;
  public static final int FRONT_BACK_CORNER = 4;
  public static final int BACK = 5;
  public static final int BACK_TOP_CORNER = 6; 
  public static final int TOP_BACK = 7;
  
  // 3-D Information for the ADC File
  float x;
  float y;
  float z;
  float i;
  float j;
  float k;
  float SDist;
  int p_dist;
  float angle;
  float dirX;
  float dirY;
  float dirZ;  
  float deltaX;
  float deltaY;
  float deltaZ;
  float deltaXRaw;
  float deltaYRaw;
  float deltaZRaw;
  float orientation;
  float flatDirX;
  float flatDirYZ;
  float flatO;  
  float xRate;
  
  boolean flatCorner;
  boolean smoothMe;
  boolean removeMe;

  Pt(PatternViewer p, int i, float _u, float _v) {
	  idx = i; // initialize index
	  
	  parent = p;
	  u = _u;
	  v = _v;
	  
	  // Distance and orientation
	  SDist = 0;
	  p_dist = 0;
	  angle = 0;
	  orientation = 0;
	  
	  dirX = dirY = dirZ = 0;
	  deltaX = deltaY = deltaZ = 0;
	  deltaXRaw = deltaYRaw = deltaZRaw = 0;

	  // Point Tags
	  smoothMe = false;
	  flatCorner = false;
	  removeMe = false;

	  xRate = 1.0f; // default large xRate
  }

  public void setxRate(float _xRate) {
	 xRate = parent.theMandrel.unit2inch(_xRate); // convert to inches for visualization
  }
  
  public void setIndex(int i) {
    idx = i; 
  }
  
  public void printType() {
    switch(type) {
      case Pt.TOP_FRONT: // = 1;
        parent.print("TOP_FRONT");      
      break;
      case Pt.TOP_FRONT_CORNER: // = 2;
        parent.print("TOP_FRONT_CORNER ");
      break;
      case Pt.FRONT: // = 3;
        parent.print("FRONT");  
      break;
      case Pt.FRONT_BACK_CORNER: // = 4;
        parent.print("FRONT_BACK_CORNER ");
      break;
      case Pt.BACK: // = 5;
        parent.print("BACK"); 
      break;
      case Pt.BACK_TOP_CORNER: // = 6; 
        parent.print("BACK_TOP_CORNER ");
      break;
      case Pt.TOP_BACK: // = 7;
        parent.print("TOP_BACK"); 
      break;
    } 
  }

  public boolean onCorner() {
    if( (type == TOP_FRONT_CORNER) ||
        (type == FRONT_BACK_CORNER) ||
        (type == BACK_TOP_CORNER) ) {
        return true;
      } else {
        return false; 
      }    
  }
/*
  public boolean onFlatCorner(Pt prev) {
    if( ((prev.type == TOP_FRONT) && (type == TOP))    ||
        ((prev.type == TOP_FRONT) && (type == FRONT))  ||
        ((type == TOP_FRONT) && (prev.type == TOP))    ||
        ((type == TOP_FRONT) && (prev.type == FRONT))  ||              
        ((prev.type == BOTTOM_BACK) && (type == BACK)) ||
        ((prev.type == BOTTOM_BACK) && (type == BOTTOM)) ||
        ((type == BOTTOM_BACK) && (prev.type == BACK))   ||
        ((type == BOTTOM_BACK) && (prev.type == BOTTOM))
        ) 
        {
          return true;  
        } else {
          return false; 
        }
  }

  public void setFlatCorner() {
   flatCorner = true; 
  }
  */

  public void setSDist(float _sdist) {
    SDist = _sdist;
  }

  // Calculate Smoothed Tangential Vector from previous point P
  public void setDir(Pt prev3, Pt prev2, Pt prev1, Pt next1, Pt next2, Pt next3) {
    dirX = 0;
    dirY = 0;
    dirZ = 0;
    
    float totals = 0;
    
    //parent.println("New   (" + dirX + "," + dirY + "," + dirZ + ") ");
    
    if(prev3 != null) {
        dirX += (0.2f*prev3.deltaX);
        dirY += (0.2f*prev3.deltaY);
        dirZ += (0.2f*prev3.deltaZ);
        totals += 0.2f;
        //parent.println("   " + prev3.idx + " (" + dirX + "," + dirY + "," + dirZ + ") ");        
    }
    if(prev2 != null) {
        dirX += (0.3f*prev2.deltaX);
        dirY += (0.3f*prev2.deltaY);
        dirZ += (0.3f*prev2.deltaZ);
        totals += 0.3f;
        //parent.println("   " + prev2.idx + " (" + dirX + "," + dirY + "," + dirZ + ") "); 
    }    
    if(prev1 != null)  {
        dirX += (0.5f*prev1.deltaX);
        dirY += (0.5f*prev1.deltaY);
        dirZ += (0.5f*prev1.deltaZ);
        totals += 0.5f;       
        //parent.println("   " + prev1.idx + " (" + dirX + "," + dirY + "," + dirZ + ") "); 
    }   
    
    //parent.println("    <<<" + idx + ">>> ");
    dirX += deltaX;
    dirY += deltaY;
    dirZ += deltaZ;
    totals += 1.0f;
    
    if(next1 != null) {
        dirX += (0.5f*next1.deltaX);
        dirY += (0.5f*next1.deltaY);
        dirZ += (0.5f*next1.deltaZ);
        totals += 0.5f; 
        //parent.println("   " + next1.idx + " (" + dirX + "," + dirY + "," + dirZ + ") ");   
    }   
    if(next2 != null) {
        dirX += (0.3f*next2.deltaX);
        dirY += (0.3f*next2.deltaY);
        dirZ += (0.3f*next2.deltaZ);
        totals += 0.3f;
        //parent.println("   " + next2.idx + " (" + dirX + "," + dirY + "," + dirZ + ") ");
    } 
    if(next3 != null) {
        dirX += (0.2f*next3.deltaX);
        dirY += (0.2f*next3.deltaY);
        dirZ += (0.2f*next3.deltaZ);
        totals += 0.2f;
        //parent.println("   " + next3.idx + " (" + dirX + "," + dirY + "," + dirZ + ") ");   
    }    
    
    // float magma = parent.mag(dirX, dirY, dirZ);

    // Store Magnitude to render DIR vector in 3D:
    // p_dist = int(magma*10)+ 1;
    // Calculated with Delta's

    // Average by the sum of the weights
    dirX = dirX / totals;
    dirY = dirY / totals;
    dirZ = dirZ / totals;        
    
    //parent.println("     dirXYZ  (" + dirX + "," + dirY + "," + dirZ + ") m:" + totals); 
  }
  
  // Use the Calculated Delta
  public void setDir() {
    dirX = deltaX;
    dirY = deltaY;
    dirZ = deltaZ; 
  }

  // Calculate Tangential Vector from previous point P's Delta Vectors
  public void setDelta(Pt prev) {
    // Direction Vector needs to be flipped
    //dirX = x - p.x;
    //dirY = y - p.y;
    //dirZ = z - p.z;

    //if(type != TOP_FRONT) { // curved portions are already set
    deltaX = prev.x - x;
    //}  
    deltaY = prev.y - y;
    deltaZ = prev.z - z;

    // Save delta's in Raw
    deltaXRaw = deltaX;
    deltaYRaw = deltaY;
    deltaZRaw = deltaZ;

    float magma = parent.mag(deltaX, deltaY, deltaZ);

    // Store Magnitude to render DIR vector in 3D:
    p_dist = PApplet.parseInt(magma*10)+ 1;

    deltaX = deltaX / magma;
    deltaY = deltaY / magma;
    deltaZ = deltaZ / magma;
  }

  // Calculate the Orientation Angle using the Normal and the Direction Components
  public void calcOrientation() {
    // i,j,k = nx, ny, nz
    // orientation = parent.degrees(parent.atan(dirX/((j - dirZ) - (k - dirY))));
    orientation = parent.degrees(parent.atan2(dirX , ((j * dirZ) - (k * dirY))));
  }
  
  // Calculate the Orientation using the Delta Vectors (in radians)
  public float deltaOrientation() {
    //return parent.atan(deltaX / ( (j - deltaZ) - (k - deltaY) ));
    return parent.atan2(deltaX, ( (j * deltaZ) - (k * deltaY) ));
  }

  // Normalizing Algorithms for the Delta and Direction Vectors
  
  public void setFlatData(float fDX, float fDYZ, float fO) {
	    flatDirX = fDX;
	    flatDirYZ = fDYZ;
	    flatO = fO; 
  }
    
  public void normalizeDelta() {
      float norm = 0;
              
      // Flat point delta's:
      // Really all we need from the Flat Points are two pieces of info:
      // 1. Orientation (a function of normal Y, normal Z, direction X, direction Y, direction Z
      // 2. normal Y and normal Z is standard for flat sides
      // 3. direction X, Y and Z can be extrapolated.
      
      float o; 
      // Use the Pt's flatO and flatDirX:
      o = flatO;
                
     // Calculate the constants with our normals
     float a = 0.0f;
     float b = 0.0f;
     float c = 0.0f;
          
     /*
          if(cur.j == 0) {
            return; // don't divide by zero  
          }
      */
          
     a = (k / j); // nz / ny
     b = flatDirX / (parent.tan(o) * j); // Dx / (tan(o) * ny)
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
                    
     ///parent.println("  Current Point nx: " + i + " ny: " + j + " nz: " + k + " mag: " + parent.mag(i, j, k));
     ///parent.println("  Constants a: " + a + " b: " + b + " c: " + c + " descrim: " + desc);
     ///parent.println("  Current dY: " + deltaY + " dZ: " + deltaZ + ", Solved [dY: " + dY1 + ", dZ: " + dZ1 + " dSQ: " + dSQ1 + "] [dY: " + dY2 + ", dZ: " + dZ2 + " dSQ: " + dSQ2 + "]");
          
     // Confirm Sum of Squares Solutions
     float sum1 = parent.sqrt(flatDirX*flatDirX + dY1*dY1 + dZ1*dZ1);  
     float sum2 = parent.sqrt(flatDirX*flatDirX + dY2*dY2 + dZ2*dZ2);  
     float mag1 = parent.mag(flatDirX, dY1, dZ1);
     float mag2 = parent.mag(flatDirX, dY2, dZ2);
          
     ///parent.println("  Check Sums 1: " + sum1 + " 2: " + sum2 + " Mag 1: " + mag1 + " 2: " + mag2);
          
     float o1 = parent.atan2(flatDirX, ( (j * dZ1) - (k * dY1) ));   // Version flatPoint
     float o2 = parent.atan2(flatDirX, ( (j * dZ2) - (k * dY2) ));   // Version flatPoint
                    
     // parent.println("  Check Flat Orientation: " + parent.degrees(o) + " [Orientation 1: " + parent.degrees(o1) + " Orientation 2: " + parent.degrees(o2) + "]");
          
     ///parent.println("------------------------------------------------------------------------------------------");
          
     // Choose a dY and dZ to go with
          
     //// Produces Nice Swept Curves, but only for Small deltaX (shallow curves) ////
          /*
          cur.deltaX = flatDirX;
          cur.deltaY = dZ2;
          cur.deltaZ = dY2;
          */
          
     if(!Float.isNaN(dY1) || !Float.isNaN(dZ1)) {
       if(parent.degrees(o) > 90) { // a Negative Orientation
         deltaX = -flatDirX;
         // cur.printType();
         // parent.println("NEGATIVE ORIENTATION");
// TODO - need to retro-fit for isosceles triangle
/*         
         if((type == TOP_FRONT) || (type == TOP) || (type == FRONT)) {
           deltaY = -dY1;
           deltaZ = dZ1;   
         } else if(type == BOTTOM_BACK) {
           deltaY = -dY1;
           deltaZ = -dZ1;  
         } else if(type == BACK_TOP) {
           deltaY = -dY1;
           deltaZ = dZ1;  
         }               
*/        
      } else { // Positive Orientation
        deltaX = flatDirX;
         /*     
              if((cur.type == Pt.BOTTOM_BACK) || (cur.type == Pt.BACK_TOP)) {
                cur.deltaZ = -dY2; // dZ1 does not change (BOTTOM-BACK reverse the sign)
              } else {
                cur.deltaZ = dY2; 
              }
          */
        deltaY = dY2; // dY1 does not change should change? 
 
// TODO - need to retro fit for isosceles triangle
/*
        if((type == BOTTOM_BACK) || (type == FRONT_BOTTOM)) {
          deltaZ = -dZ2; // dZ1 does not change (BOTTOM-BACK reverse the sign)
        } else {
          deltaZ = dZ2; 
        }
*/        
      }
    }
  }
 
  
// Rendering and Printing of Pt  
  
  public void drawPt(PGraphics graphics) {
    graphics.noFill();
    graphics.stroke(200,0,0,10);
    graphics.ellipse(u, v, idx*2, idx*2);
  }

  // render 2D fiber
  public void drawFiberAngle(PGraphics graphics) {
    graphics.stroke(0,0,0,20);
    graphics.pushMatrix();
    // Translate the origin to the point 
    // (wrapping the v parameter! and adding a patternHeight to account for negative v's)
    graphics.translate(u, ((v % parent.theMandrel.patternHeight) + parent.theMandrel.patternHeight) % parent.theMandrel.patternHeight); 
    graphics.rotate(parent.radians(angle));

    // Draws a random number of fibers
    int h;
    int halfThickness = parent.parseInt(parent.theMandrel.inch2unit(parent.thickness) / 2) + 1;
    int i = PApplet.parseInt(parent.random(3,6));
    for(; i > 0; i--) {
      // h: height determines the "width" of the tape
      h = PApplet.parseInt(parent.random(-halfThickness, halfThickness));
      // h = PApplet.parseInt(parent.random(-3, 3));
      graphics.line(-parent.random(20,30), h, parent.random(20,30), h);
    }
    
    graphics.popMatrix();
  }

  // render 3D
  public void drawNormalVector(PGraphics graphics) {
    float end_x;
    float end_y;
    float end_z;

    //end_x = x + SDist*i/20.0;       
    //end_y = y + SDist*j/20.0;
    //end_z = z + SDist*k/20.0;
    
 /*   
    end_x = x + 0.1*i;       
    end_y = y + 0.1*j;
    end_z = z + 0.1*k;
   */ 

    // Visualize the xRate (maybe even convert to inch)
    end_x = x + 1.0f*i*xRate;       
    end_y = y + 1.0f*j*xRate;
    end_z = z + 1.0f*k*xRate;    

    graphics.line(x, y, z, end_x, end_y, end_z);
    //     pushMatrix();
    //     translate(end_x, end_y, end_z);
    //     sphere(10);
    //     popMatrix();
  }

  // render 3D
  public void drawDirVector(PGraphics graphics) {
    float end_x;
    float end_y;
    float end_z;

    end_x = x + dirX;       
    end_y = y + dirY;
    end_z = z + dirZ;

    graphics.line(x, y, z, end_x, end_y, end_z);
  }

  // render 3D
  public void drawDeltaVector(PGraphics graphics) {
    float end_x;
    float end_y;
    float end_z;

    end_x = x + deltaX;       
    end_y = y + deltaY;
    end_z = z + deltaZ;

    graphics.line(x, y, z, end_x, end_y, end_z);
  }

  public String floatToString(float f, int precision) {
    if(Float.isNaN(f) || Float.isInfinite(f)) {
      return Float.toString(f);
    } else {
      String fString = Float.toString(f);
      String fSplit[] = parent.split(fString, '.');
      int currentPrecision = fSplit[1].length();

      for(int i = 0; i < (precision - currentPrecision); i++) 
      {
        fString += "0"; 
      }
      return fString;
    }
  }

  // Direction Vector and Taping Angle //
  // Output Format
  //#	         idx	    X		    Y		    Z		    I           J		    K		 SDist		  Angle		 InBounds	  Dir X		 Dir Y		 Dir Z
  // pt	 1	0.039370	7.861208	-1.000000	0.000000	0.000000	-1.000000	0.000000	89.080145	20000001H	0.016054	0.999871	0.000000
  public String toString(boolean verbose) {
    if(verbose) {
    return new String("pt\t" + idx + "\t" 
      + floatToString(x, 7) + "\t" + floatToString(y, 7) + "\t" + floatToString(z, 7) + "\t" 
      + floatToString(i, 7) + "\t" + floatToString(j, 7) + "\t" + floatToString(k, 7) + "\t" 
      + floatToString(SDist, 7) + "\t" + floatToString(angle, 7) + "\t20000001H\t" 
      + floatToString(dirX, 7) + "\t" + floatToString(dirY, 7) + "\t" +  floatToString(dirZ, 7) + "\t | " + floatToString(orientation, 7) + " | \t"      
      + floatToString(deltaX, 7) + "\t" + floatToString(deltaY, 7) + "\t" +  floatToString(deltaZ, 7) + "\t"
      + floatToString(deltaXRaw, 7) + "\t" + floatToString(deltaYRaw, 7) + "\t" +  floatToString(deltaZRaw, 7));   
    } else {  
    return new String("pt\t" + idx + "\t" 
      + floatToString(x, 7) + "\t" + floatToString(y, 7) + "\t" + floatToString(z, 7) + "\t" 
      + floatToString(i, 7) + "\t" + floatToString(j, 7) + "\t" + floatToString(k, 7) + "\t" 
      + floatToString(SDist, 7) + "\t" + floatToString(angle, 7) + "\t20000001H\t" 
      + floatToString(dirX, 7) + "\t" + floatToString(dirY, 7) + "\t" +  floatToString(dirZ, 7));
    }
  }
  /*
    String toString() {
   return new String("pt\t" + idx + "\t" + x + "\t" + y + "\t" + z + "\t" + i + "\t" + j + "\t" + k + "\t" + SDist + "\t" + angle + "\t20000001H\t" + dirX + "\t" + dirY + "\t" + dirZ);
   }
   */

  public void printGraphics(PGraphics graphics) {
    // Switch layers
    graphics.vertex(x, y, z); 
  }

  /*
    void print(PrintWriter pw) {
   pw.printf("pt \t %1$3d \t %2$7f \t %3$7f \t %4$7f \t %5$7f \t %6$7f \t %7$7f \t %8$7f \t 20000001H \t %9$7f \t %10$7f \t %11$7f", 
   idx, x, y, z, i, j, k, SDist, angle, dirX, dirY, dirZ);
   }
   */

} 

