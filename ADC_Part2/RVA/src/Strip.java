import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Strip {
	PatternViewer parent;

	boolean reject = false;
	int ID;
	int segmentCount;
	
	Vector segments;
	Segment first;
	Segment last;
	
	Strip(PatternViewer p, float x1, float y1, float x2, float y2) {
		reject = false; // initially accept this part

		parent = p;
		parent.stripCount++; // increment the stripCount (eeks! global object access)
		ID = parent.stripCount;

		segments = new Vector();
		segmentCount = 0;
		
		first = add(x1, y1, x2, y2);  // adds the line onto the Strip
		
		// Generating
		PApplet.println("Generated ply/strip " + ID + " Num Segments: " + segments.size() + " Angle: " + first.angle + " DirX: " + first.dirX + " DirY: " + first.dirY);     
	}    
	
	Strip(PatternViewer p) {
		reject = false; // initially accept this part

		parent = p;
		parent.stripCount++; // increment the stripCount (eeks! global object access)
		ID = parent.stripCount;

		segments = new Vector();
		segmentCount = 0;
		PApplet.println("Generated ply/strip " + ID);    
		first = null;
		last = null;
	} 

	public void printDirXY(float[] minus, float[] plus) {
		first.printDirXY(minus, plus);
	}
	
/*
	public float radialDeltaX(float rawY) {
		return first.radialDeltaX(rawY);
	}

	public float radialCenterX(float rawY) {
		return first.radialCenterX(rawY);
	}  

	public float radialDeltaY(float rawY) {
		return first.radialDeltaY(rawY);
	}

	public float calcXRate(int type) {
		return first.calcXRate(type);
	}
*/
	public void reverseSegments() {
		parent.println("*** Reversing segments: " + segments.size());
		Collections.reverse(segments);
		first = (Segment) segments.firstElement();
		last = (Segment) segments.lastElement();
	}
	
	public Segment add(float x1, float y1, float x2, float y2) {
		if(first == null) {
			first = new Segment(parent, segmentCount++, x1, y1, x2, y2);
			last = first;
		} else {
			last = new Segment(parent, segmentCount++, x1, y1, x2, y2);
		}
		
		segments.add(last);
		return last;
	}
	
	// Verification Methods
	public boolean verify() {
		// Verify States
		boolean increase_x;
		boolean increase_orientation;
		
		parent.print("Verify Strip: " + this.ID);
		if(reject) {
			parent.print(" *rejected* skipping... ");	
			return false;
		} else {
			// Check if first and last exist
			if(first == null) {
				parent.print(" *this.first segment does not exist* skipping... ");		
				return false;
			} else if(last == null) {
				parent.print(" *this.last segment does not exist* skipping... ");		
				return false;
			} else { // step through the Segments
				
				for(Enumeration e = segments.elements(); e.hasMoreElements();) {
					if(((Segment) e.nextElement()).verify() == false) {
						
						return false;
					}
				}
			}
		}
		
		parent.println(" OK ");
		return true;
	}
	
	// Processing Methods

	public void sortPoints() { 
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).sortPoints(); 
		}
	}

	public void uniquePoints() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).uniquePoints(); 
		}
	}

	public void processPts() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).processPts(); 
		}
	}
	
	public void numberPts() {
		int ptCount = 1; // need to pass through the ptCount to get a good count
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			ptCount = ((Segment) e.nextElement()).numberPts(ptCount); 
		}
	}
	
	public void processDir(boolean weighted) {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).processDir(weighted); 
		}
	}

	public void processSmooth() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).processSmooth(); 
		}
	}

	public void normalizeCornerDelta() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).normalizeCornerDelta(); 
		}
	}

	public void normalizeDelta() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).normalizeDelta(); 
		}
	}

	public void circlepolateDelta() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).circlepolateDelta(); 
		}
	}

	public void calcOrientation() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).calcOrientation(); 
		}
	}

	// Exporting Methods
	public void drawStrip() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).drawStrip(); 
		}
	}  

	public void drawFiberAngle() {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).drawFiberAngle(); 
		}  
	}

	public void printStrip(PrintWriter pw, boolean verbose) {
		printStrip(pw, ID, verbose);
	}

	// Replaces the ply's num with a customizable num as opposed to using the ID
	public void printStrip(PrintWriter pw, int num, boolean verbose) {
		// 4.17.2010 PApplet.println("Printing ply/strip " + ID + " segments " + segments.size() + " angle " + first.angle + " to " + last.angle); // strip ID
		// Which angle?
		// pw.println("ply\t" +  + num + "\t" + angle);
		pw.println("ply\t" +  num + "\t" + first.angle);
		pw.println("strip\t1");
		
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).printSegment(pw, verbose);  // verbose
		}
	}

	public void printGraphics(PGraphics g) {
		// 4.17.2010 PApplet.println("Graphing ply/strip " + ID + " segments " + segments.size() + " angle " + first.angle + " to " + last.angle); // strip ID
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).printGraphics(g); 
		}
	}

	public void drawNormalVector(PGraphics g) {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).drawNormalVector(g); 
		}
	}

	public void drawDirVector(PGraphics g) {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).drawDirVector(g); 
		}
	}

	public void drawDeltaVector(PGraphics g) {
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).drawDeltaVector(g); 
		}
	}
} 


