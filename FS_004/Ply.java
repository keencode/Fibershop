import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.dxf.RawDXF;

public class Ply {
	PatternViewer parent;
	Vector strips;

	Ply(PatternViewer p) {
		parent = p;

		strips = new Vector();
	}

	void add(Strip s) {
		// CHIPP // 1/23/08 PATCH
		if(s.first.angle < 0) { 
			s.reverseSegments();
		}
		
		strips.add(s);
	}
	
	void exportDXF_3D(RawDXF dxfGraphics, PGraphics graphics, boolean normalize, boolean smooth) {
		//dxfGraphics.beginDraw();
		// Enumerate through the thePly, and print out all of the Strips:
		for(Enumeration e = strips.elements(); e.hasMoreElements();) {
			Strip t = (Strip) e.nextElement();

			if(t.reject == false) {  
				//t.drawStrip();  
				t.processPts();
				if(normalize) {
					t.normalizeDelta();
					// t.normalizeCornerDelta();
					//println("Normalizing Delta");
				}        
				t.processDir(true);
				if(smooth)
					t.processSmooth(); // smooth the corners
				t.calcOrientation();
				t.drawFiberAngle();
				//t.printGraphics(dxfGraphics);
			} // if not reject
		} // enum Strips

		parent.beginRaw(dxfGraphics);
		
		// Coordinate System
//		dxfGraphics.setLayer(1); // set the patterns to layer 1 
//		parent.line(0,0,0, 50, 0, 0); 
//		dxfGraphics.setLayer(2); // set the patterns to layer 2  
//		parent.line(0,0,0, 0, 10, 0);
//		dxfGraphics.setLayer(3); // set the patterns to layer 3  
//		parent.line(0,0,0, 0, 0, 10);

		for(Enumeration e = strips.elements(); e.hasMoreElements();) {
			Strip t = (Strip) e.nextElement();

			if(t.reject == false) {
				dxfGraphics.setLayer(4); // set the patterns to layer 4
				t.printGraphics(graphics);
				dxfGraphics.setLayer(5); // set the patterns to layer 5         
				t.drawNormalVector(graphics);
				// dxfGraphics.setLayer(6); // set the patterns to layer 6          
				// t.drawDirVector(graphics);
				// dxfGraphics.setLayer(7); // set the patterns to layer 6          
				// t.drawDeltaVector(graphics);
			} // if not reject
		} // enum Strips

		//dxfGraphics.endDraw();
		parent.endRaw();	
	}
	
    
	void exportADC(PrintWriter ADCWriter, boolean verbose, boolean weighted, boolean normalize, boolean smooth) {
		// Enumerate through the thePly, and print out all of the Strips:
		for(Enumeration e = strips.elements(); e.hasMoreElements();) {
			Strip t = (Strip) e.nextElement();
			if(t.reject == false) {
				t.drawStrip();  
				t.processPts();
				// if(exportState == PatternViewer.weightedADC) {
				if(weighted) {
					if(normalize) {
						PApplet.println("Normalzing Delta");
						t.normalizeDelta();
						// t.normalizeCornerDelta();
					}
					t.processDir(true);
					if(smooth)
						t.processSmooth();
				} else {
					t.processDir(false);
				}
				t.calcOrientation();
				t.numberPts(); // need to re-number the Pts for composite strips
				t.printStrip(ADCWriter, verbose);
			} // if not reject
		} // enum Strips
	}
	
	Enumeration exportADCRange(PrintWriter ADCWriter, boolean verbose, boolean weighted, boolean normalize, boolean smooth, Enumeration e, int from, int to) {

		int stripNum = 0;
		
		if(e == null) {
			e = strips.elements();
		}
		
		for(int i = from; (i < to) && e.hasMoreElements(); i++) {

			// Get the Strip
			Strip t = (Strip) e.nextElement();
			stripNum++;

			// Is this a strip to reject?
			if(t.reject == false) {
				t.drawStrip();  
				t.processPts();
				//if(exportState == PatternViewer.weightedADC) {
				if(weighted) {
					if(normalize) {
						//parent.println("Normalizing Delta");
						t.normalizeDelta();
						// t.normalizeCornerDelta();
					}
					t.processDir(true);
					if(smooth)
						t.processSmooth();
				} else {
					t.processDir(false);
				}
				t.calcOrientation();
				t.numberPts(); // need to re-number the Pts for composite strips
				t.printStrip(ADCWriter, stripNum, verbose); // print the Strip with the counted a StripNum
			}
		}
		
		return e;
	}
	
	void removeAllElements() {
		strips.removeAllElements();
	}

}
