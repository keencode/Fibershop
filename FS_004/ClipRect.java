import processing.core.PApplet;

public class ClipRect {
   PApplet parent;
   float xmin;
   float xmax;
   float ymin;
   float ymax;
   
   ClipRect(PApplet p, float _xmin, float _xmax, float _ymin, float _ymax) {
     parent = p;
     xmin = _xmin;
     xmax = _xmax;
     ymin = _ymin;
     ymax = _ymax;
     
   }
  
  public byte outcode(float x, float y) {
    byte code = 0;
    
    if(x < xmin) {
      code += 1;
      //parent.print("1");
    }
    else if(x > xmax) 
    {
      code |= 2;
      //parent.print("2");
    }
    
    if(y > ymax) {
      code |= 4;
      //parent.print("4");  
    }
    else if(y < ymin) {
      code |= 8;
      //parent.print("8");
    }
    
    //parent.print(" ");
    return code;
  }
  
  public ClipLine clip(float x0, float y0, float x1, float y1) {
    
    byte startcode = outcode(x0, y0);
    byte endcode = outcode(x1, y1);
    
    ClipLine clipme = new ClipLine();
    
    clipme.startx = x0;
    clipme.starty = y0;
    clipme.endx = x1;
    clipme.endy = y1;

    clipme.accept = false;
    
    // Line Basics
    float px = clipme.endx - clipme.startx;
    float py = clipme.endy - clipme.starty;
    float dxdy = 0;
    float dydx = 0;
    
    if(px != 0) dydx = py / px;
    if(py != 0) dxdy = px / py;
    
    for(int stage = 0; stage < 4; stage++) {       
      if((startcode | endcode) == 0) { // trivial accept and exit
        clipme.accept = true;
        return clipme;
      } else if((startcode & endcode) != 0) { // trivial reject and exit
        clipme.accept = false;
        return clipme;
      } 
      
      // solve for an intersection
      if(startcode == 0) { // swap start-point with end-point
            byte temp1 = startcode;
            startcode = endcode;
            endcode = temp1;
            float tempx = clipme.startx;
            float tempy = clipme.starty;
            clipme.startx = clipme.endx;
            clipme.starty = clipme.endy;
            clipme.endx = tempx;
            clipme.endy = tempy;
      }
          
      if((startcode & 1) == 1) {
        clipme.starty += dydx * (xmin - clipme.startx);
        clipme.startx = xmin;
      } else if((startcode & 2) == 2) {
        clipme.starty += dydx * (xmax - clipme.startx);
        clipme.startx = xmax; 
      } else if((startcode & 4) == 4) {
        clipme.startx += dxdy * (ymax - clipme.starty);
        clipme.starty = ymax;
      } else if((startcode & 8) == 8) {
        clipme.startx += dxdy * (ymin - clipme.starty);
        clipme.starty = ymin;
      }
      startcode = outcode(clipme.startx, clipme.starty);
    }
    
    clipme.accept = true;
    return clipme;
  }
}
