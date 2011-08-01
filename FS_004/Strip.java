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
		PApplet.println("Printing ply/strip " + ID + " segments " + segments.size() + " angle " + first.angle + " to " + last.angle); // strip ID
		// Which angle?
		// pw.println("ply\t" +  + num + "\t" + angle);
		pw.println("ply\t" +  num + "\t" + first.angle);
		pw.println("strip\t1");
		
		for(Enumeration e = segments.elements(); e.hasMoreElements();) {
			((Segment) e.nextElement()).printSegment(pw, verbose);  // verbose
		}
	}

	public void printGraphics(PGraphics g) {
		PApplet.println("Graphing ply/strip " + ID + " segments " + segments.size() + " angle " + first.angle + " to " + last.angle); // strip ID
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


