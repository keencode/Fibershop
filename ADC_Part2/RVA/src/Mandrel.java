import processing.core.PApplet;
import processing.core.PGraphics;

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

/**
 * Mandrel class
 * 
 * Contains the dimensions of the Mandrel, and includes algorithms to convert
 * points to 3D Mandrel.
 * 
 */

public class Mandrel {
	PApplet parent;

	// Size in Inches one Processing Unit (used in converstion)
	float unitSize;

	// Dimensions for Isoscolese Triangle
	// _l = length
	float l;
	// _top_w = top width (ideal width of the top)
	float top_w;
	// _front_w = front width (ideal width of the front)
	float front_w;
	// _back_w = back width (ideal width of the back)
	float back_w;
	// _top_front_r = radius of the top-front corner
	float top_front_r;
	// _front_back_r = radius of the front-back corner
	float front_back_r;
	// _back_top_r = radius of the back-top corner
	float back_top_r;

	// Actual Mandrel Lengths (for the patterns)
	float top_len; // (width of the top flat part)
	float front_len; // (width of the top flat part)
	float back_len; // (width of the top flat part)

	float top_front_len; // (arc length of the top-front corner)
	float front_back_len; // (arc length of the front-back corner)
	float back_top_len; // (arc length of the back-top corner)

	// Pattern Dimensions in (u,v) space (2-D, x=u, y=f(v), and z=f(v))
	float patternHeight; // sketch Heigh and Width
	float patternWidth;

	// PATTERN STARTS AT (u,v) = (0,0). Which is (x,y,z) = (0, (top_len/2)/sqrt(3), 0)
	float top_front_v;
	float front_back_v;
	float back_top_v;

	// Pre-calculated points of the mandrel :
	
	// Center of corner circles
	float top_front_cy;
	float top_front_cz;
	float front_back_cy;
	float front_back_cz;
	float back_top_cy;
	float back_top_cz;

	// End-points of flat sections
	float top_front_front_y; // FRONT flat side
	float top_front_front_z;
	float front_back_front_y;
	float front_back_front_z;
	float front_back_back_y; // BACK flat side
	float front_back_back_z;
	float back_top_back_y;
	float back_top_back_z;
	float back_top_top_y; // TOP flat side
	float back_top_top_z;
	float top_front_top_y;
	float top_front_top_z;
	
	// Points that reside on the flat sides:
	Pt topFrontPt = null;
	Pt frontPt = null;
	Pt backPt = null;
	Pt topBackPt = null;

	// Clip Zones on the Mandrel
	ClipRect mandrelClip; // entire mandrel as a clip zone

	// v-Crossings - generate a collection of pattern-v points to test in render
	float [] sectionV = new float[51];
	
	// ClipRect topClip;
	// ClipRect halftopClip;
	// ClipRect halftopClip2;
	// ClipRect botClip;
	// //////////////////////// float _xmin, float _xmax, float _ymin, float _ymax
	// ClipRect cr = new ClipRect(this, width/4.0, 3.0*width/4.0, height/4.0,
	// 3.0*height/4.0);

	/// NOTE : MAY NEED TO CORRECT THE CORNER RADII depending on the ACTUAL Flat Length 
	// of EACH Side.
	
	// Assume Side Widths of 7 inches.
	// Measure ACTUAL FLAT side Length.
	// Solve for individual RADIUS.  
	// * may need to mix two radii if flat lengths are un-even... or average the radii !!! *

	
/*	MANDREL VERSION 1.0 - calculates dimensions from the idealized Mandrel's Widths
	Mandrel(PApplet p, float _l, float _top_w, float _front_w, float _back_w,
			float _top_front_r, float _front_back_r, float _back_top_r,
			float _scale) {
		parent = p;
		unitSize = 1.0f / _scale; // inches per pixel

		// Convert Inches to Processing UNITs
		// _l = length
		l = _l * _scale;
		// _top_w = top width (ideal width of the top)
		top_w = _top_w * _scale;
		// _front_w = front width (ideal width of the front)
		front_w = _front_w * _scale;
		// _back_w = back width (ideal width of the back)
		back_w = _back_w * _scale;
		// _top_front_r = radius of the top-front corner
		top_front_r = _top_front_r * _scale;
		// _front_back_r = radius of the front-back corner
		front_back_r = _front_back_r * _scale;
		// _back_top_r = radius of the back-top corner
		back_top_r = _back_top_r * _scale;

		// Actual Mandrel Lengths (for the patterns)
		top_front_len = (2.0f * p.PI / 3.0f) * top_front_r; // (arc length of
															// the top-front
															// corner)
		front_back_len = (2.0f * p.PI / 3.0f) * front_back_r; // (arc length
																// of the
																// front-back
																// corner)
		back_top_len = (2.0f * p.PI / 3.0f) * back_top_r; // (arc length of
															// the back-top
															// corner)

		// Subtract the length of rounded corners (NOT the arc-lengths)
		top_len = top_w - (p.tan(p.PI / 3.0f) * top_front_r)
				- (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top
														// flat part)
		front_len = front_w - (p.tan(p.PI / 3.0f) * top_front_r)
				- (p.tan(p.PI / 3.0f) * front_back_r); // (width of the top
														// flat part)
		back_len = back_w - (p.tan(p.PI / 3.0f) * front_back_r)
				- (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top
														// flat part)
*/
	// Mandrel Version 2.0
	Mandrel(PApplet p, float _l, float _top_len, float _front_len, float _back_len,
			float _top_front_r, float _front_back_r, float _back_top_r,
			float _scale) {
		parent = p;
		unitSize = 1.0f / _scale; // inches per pixel

		// Convert Inches to Processing UNITs
		// _l = length
		l = _l * _scale;
		// _top_len = top len (measured length of top)
		top_len = _top_len * _scale;
		// _front_len = front len (measured length of front)
		front_len = _front_len * _scale;
		// _back_len = back len (measured length of back)
		back_len = _back_len * _scale;
		// _top_front_r = radius of the top-front corner
		top_front_r = _top_front_r * _scale;
		// _front_back_r = radius of the front-back corner
		front_back_r = _front_back_r * _scale;
		// _back_top_r = radius of the back-top corner
		back_top_r = _back_top_r * _scale;

		// Actual Mandrel Lengths (for the patterns)
		top_front_len = (2.0f * p.PI / 3.0f) * top_front_r;    // (arc length of the top-front corner)
		front_back_len = (2.0f * p.PI / 3.0f) * front_back_r;  // (arc length of the front-back corner)
		back_top_len = (2.0f * p.PI / 3.0f) * back_top_r;      // (arc length of the back-top corner)

		// Subtract the length of rounded corners (NOT the arc-lengths)
		// top_len = top_w - (p.tan(p.PI / 3.0f) * top_front_r) - (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top flat part)
	    top_w = top_len + (p.tan(p.PI / 3.0f) * top_front_r) + (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top flat part)
		// front_len = front_w - (p.tan(p.PI / 3.0f) * top_front_r) - (p.tan(p.PI / 3.0f) * front_back_r); // (width of the top flat part)
	    front_w = front_len + (p.tan(p.PI / 3.0f) * top_front_r) + (p.tan(p.PI / 3.0f) * front_back_r); // (width of the top flat part)
		// back_len = back_w - (p.tan(p.PI / 3.0f) * front_back_r) - (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top flat part)	
		back_w = back_len + (p.tan(p.PI / 3.0f) * front_back_r) + (p.tan(p.PI / 3.0f) * back_top_r); // (width of the top flat part)	
	
		parent.println("Mandrel Dimensions (inches) top width: " + unitSize * top_w + " front width: " + unitSize * front_w + " back width: " + unitSize * back_w + " length: " + unitSize * l);
		parent.println("   Top-Front r: " + unitSize * top_front_r + " Front-Back r: " + unitSize * front_back_r + " Back-Top r: " + unitSize * back_top_r + " inches per unit: " + unitSize);
		parent.println("Flattened Pattern Dimensions (inches): flat top: " + unitSize * top_len + " flat front: " + unitSize * front_len + " flat back: " + unitSize * back_len);
		parent.println("Top-Front Arc: " + unitSize * top_front_len + "  Front-Back Arc: " + unitSize * front_back_len + "  Back-Top Arc: " + unitSize * back_top_len);

		/*
		 * if(w < h) { shortestDim = w; } else { shortestDim = h; }
		 */

		patternHeight = top_len + top_front_len + front_len + front_back_len + back_len + back_top_len;
		patternWidth = l;

		// mandrel limits
		top_front_v = (top_len / 2.0f) + (top_front_len / 2.0f);
		front_back_v = top_front_v + (top_front_len / 2.0f) + front_len + (front_back_len / 2.0f);
		back_top_v = front_back_v + (front_back_len / 2.0f) + back_len + (back_top_len / 2.0f);

		// pre-calculated mandrel dimensions
		// Center of corner circles
		top_front_cy = ((top_w / 2.0f) / p.sqrt(3.0f)) - top_front_r;
		top_front_cz = top_len / 2.0f;
		front_back_cy = -(front_w / p.sqrt(3.0f)) + front_back_r;
		front_back_cz = 0;
		back_top_cy = ((top_w / 2.0f) / p.sqrt(3.0f)) - back_top_r;
		back_top_cz = -top_len / 2.0f;

		// End-points of flat sections
		top_front_front_y = p.sin(p.radians(-30.0f))*top_front_r + top_front_cy; // FRONT flat side
		top_front_front_z = p.cos(p.radians(-30.0f))*top_front_r + top_front_cz;
		front_back_front_y = p.sin(p.radians(-30.0f))*front_back_r + front_back_cy;
		front_back_front_z = p.cos(p.radians(-30.0f))*front_back_r + front_back_cz;
		front_back_back_y = p.sin(p.radians(-150.0f))*front_back_r + front_back_cy; // BACK flat side
		front_back_back_z = p.cos(p.radians(-150.0f))*front_back_r + front_back_cz;
		back_top_back_y = p.sin(p.radians(210.0f))*back_top_r + back_top_cy;
		back_top_back_z = p.cos(p.radians(210.0f))*back_top_r + back_top_cz;
		back_top_top_y = p.cos(p.radians(90.0f))*back_top_r + back_top_cy; // TOP flat side
		back_top_top_z = p.cos(p.radians(90.0f))*back_top_r + back_top_cz;
		top_front_top_y = p.sin(p.radians(90.0f))*top_front_r + top_front_cy;
		top_front_top_z = p.cos(p.radians(90.0f))*top_front_r + top_front_cz;
		
		// clip zones
		// //////ClipRect//Params//////// float _xmin, float _xmax, float _ymin, float _ymax
		mandrelClip = new ClipRect(p, 0.0f, patternWidth, 0.0f, patternHeight);

		// Back up a radius in length
		// topClip = new ClipRect(p, 0.0f, PApplet.parseFloat(patternWidth),
		// -bottom_back_r, patternHeight/2 + top_front_r);
		// botClip = new ClipRect(p, 0.0f, PApplet.parseFloat(patternWidth),
		// patternHeight/2 - top_front_r, PApplet.parseFloat(patternHeight) +
		// bottom_back_r);
		
		// Calculate vSection Crossings
		
		//--- FLAT PNTS BEFORE TOP FRONT CORNER	(5)	
		sectionV[0] = top_front_v - (top_front_len / 2.0f) - inch2unit(0.5f);
		sectionV[1] = top_front_v - (top_front_len / 2.0f) - inch2unit(0.375f);		
		sectionV[2] = top_front_v - (top_front_len / 2.0f) - inch2unit(0.25f);
		sectionV[3] = top_front_v - (top_front_len / 2.0f) - inch2unit(0.125f);
		sectionV[4] = top_front_v - (top_front_len / 2.0f) - inch2unit(0.0625f);
		
		// TOP FRONT CORNER (7)
		sectionV[5] = top_front_v -  (top_front_len / 2.0f);
		sectionV[6] = top_front_v -  (top_front_len / 3.0f);
		sectionV[7] = top_front_v -  (top_front_len / 4.0f);
		sectionV[8] = top_front_v;
		sectionV[9] = top_front_v + (top_front_len / 4.0f);
		sectionV[10] = top_front_v + (top_front_len / 3.0f);
		sectionV[11] = top_front_v + (top_front_len / 2.0f);

		//--- FLAT PNTS AFTER TOP FRONT CORNER (5)	
		sectionV[12] = top_front_v + (top_front_len / 2.0f) + inch2unit(0.0625f);
		sectionV[13] = top_front_v + (top_front_len / 2.0f) + inch2unit(0.125f);
		sectionV[14] = top_front_v + (top_front_len / 2.0f) + inch2unit(0.25f);
		sectionV[15] = top_front_v + (top_front_len / 2.0f) + inch2unit(0.375f);
		sectionV[16] = top_front_v + (top_front_len / 2.0f) + inch2unit(0.5f);

		// --- FLAT PNTS BEFORE FRONT BACK CORNER (5)
		sectionV[17] = front_back_v - (front_back_len / 2.0f) - inch2unit(0.5f);
		sectionV[18] = front_back_v - (front_back_len / 2.0f) - inch2unit(0.375f);
		sectionV[19] = front_back_v - (front_back_len / 2.0f) - inch2unit(0.25f);
		sectionV[20] = front_back_v - (front_back_len / 2.0f) - inch2unit(0.125f);
		sectionV[21] = front_back_v - (front_back_len / 2.0f) - inch2unit(0.0625f);
		
		// FRONT BACK CORNER (7)
		sectionV[22] = front_back_v - (front_back_len / 2.0f);
		sectionV[23] = front_back_v - (front_back_len / 3.0f);	
		sectionV[24] = front_back_v - (front_back_len / 4.0f);	
		sectionV[25] = front_back_v;
		sectionV[26] = front_back_v + (front_back_len / 4.0f);
		sectionV[27] = front_back_v + (front_back_len / 3.0f);	
		sectionV[28] = front_back_v + (front_back_len / 2.0f);

		// --- FLAT PNTS AFTER FRONT BACK CORNER (5)
		sectionV[29] = front_back_v + (front_back_len / 2.0f) + inch2unit(0.0625f);
		sectionV[30] = front_back_v + (front_back_len / 2.0f) + inch2unit(0.125f);
		sectionV[31] = front_back_v + (front_back_len / 2.0f) + inch2unit(0.25f);
		sectionV[32] = front_back_v + (front_back_len / 2.0f) + inch2unit(0.375f);
		sectionV[33] = front_back_v + (front_back_len / 2.0f) + inch2unit(0.5f);

		// --- FLAT PNTS BEFORE BACK TOP CORNER (5)
		sectionV[34] = back_top_v - (back_top_len / 2.0f) - inch2unit(0.5f);
		sectionV[35] = back_top_v - (back_top_len / 2.0f) - inch2unit(0.375f); 
		sectionV[36] = back_top_v - (back_top_len / 2.0f) - inch2unit(0.25f); 
		sectionV[37] = back_top_v - (back_top_len / 2.0f) - inch2unit(0.125f); 
		sectionV[38] = back_top_v - (back_top_len / 2.0f) - inch2unit(0.0625f);
		
		// BACK TOP CORNER (7)
		sectionV[39] = back_top_v - (back_top_len / 2.0f);
		sectionV[40] = back_top_v - (back_top_len / 3.0f);	
		sectionV[41] = back_top_v - (back_top_len / 4.0f);
		sectionV[42] = back_top_v;
		sectionV[43] = back_top_v + (back_top_len / 4.0f);
		sectionV[44] = back_top_v + (back_top_len / 3.0f);		
		sectionV[45] = back_top_v + (back_top_len / 2.0f);

		// --- FLAT PNTS AFTER BACK TOP CORNER (5)
		sectionV[46] = back_top_v + (back_top_len / 2.0f) + inch2unit(0.0625f);
		sectionV[47] = back_top_v + (back_top_len / 2.0f) + inch2unit(0.125f); 
		sectionV[48] = back_top_v + (back_top_len / 2.0f) + inch2unit(0.25f); 
		sectionV[49] = back_top_v + (back_top_len / 2.0f) + inch2unit(0.375f); 
		sectionV[50] = back_top_v + (back_top_len / 2.0f) + inch2unit(0.5f);
		
		// Print-out the sectionV's:
		// for(int i = 0; i < sectionV.length; i++) {
		// 	parent.println("  SectionV[" + i + "]: v = " + sectionV[i]);
		// }
	}

	public float inch2unit(float inches) {
		return inches / unitSize;
	}

	public float unit2inch(float units) {
		return units * unitSize;
	}

	float linearScale(float a1, float a2, float b, float b1, float b2) {
		float a_diff = 0;
		float b_diff = 0;
		float prop = 0;
		float a = 0;

		b_diff = b1 - b2;
		a_diff = a1 - a2;
		prop = (b - b1) / b_diff;
		a = a1 + prop * a_diff;

		// parent.println("    Input Value: " + b1 + " - (" + b + ") - " + b2);
		// parent.println("  Scaled Output: " + a1 + " => (" + a + ") <= " + a2);
		
		return a;
	}

	// Mandrel Pattern View
	public void drawMandrel(PGraphics pattern) {
		// stroke(166, 152, 247);
		pattern.stroke(65, 56, 131);
		// fill(166, 152, 247);
		pattern.fill(65, 56, 131);

		// Lines showing the center of the corners
		//pattern.text("TOP-FRONT", pattern.width / 2, PApplet.parseInt(top_front_v));
		pattern.line(0, PApplet.parseInt(top_front_v), patternWidth, PApplet.parseInt(top_front_v));
		//pattern.text("FRONT-BACK", pattern.width / 2, PApplet.parseInt(front_back_v));
		pattern.line(0, PApplet.parseInt(front_back_v), patternWidth, PApplet.parseInt(front_back_v));
		//pattern.text("BACK-TOP", pattern.width / 2, PApplet.parseInt(back_top_v));
		pattern.line(0, PApplet.parseInt(back_top_v), patternWidth, PApplet.parseInt(back_top_v));

		// Lines Showing the Corner Radius Extents
		pattern.stroke(65, 56, 131, 128);

		pattern.strokeWeight(0.5f);
		
		pattern.line(0, (top_front_v - (top_front_len / 2.0f)), patternWidth, (top_front_v - (top_front_len / 2.0f)));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt(top_front_v - (top_front_len / 2.0f)));

		pattern.line(0, (top_front_v + (top_front_len / 2.0f)), patternWidth, (top_front_v + (top_front_len / 2.0f)));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt(top_front_v + (top_front_len / 2.0f)));
		
		pattern.line(0, ( front_back_v - (front_back_len / 2.0f) ), patternWidth, ( front_back_v - (front_back_len / 2.0f) ));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt( front_back_v - (front_back_len / 2.0f) ));	

		pattern.line(0, ( front_back_v + (front_back_len / 2.0f) ), patternWidth, ( front_back_v + (front_back_len / 2.0f) ));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt( front_back_v + (front_back_len / 2.0f) ));			
		
		pattern.line(0, ( back_top_v - (back_top_len / 2.0f) ), patternWidth, ( back_top_v - (back_top_len / 2.0f) ));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt( back_top_v - (back_top_len / 2.0f) ));	

		pattern.line(0, ( back_top_v + (back_top_len / 2.0f) ), patternWidth, ( back_top_v + (back_top_len / 2.0f) ));		
		// pattern.text("BLAH BLAH", 15, 15 + PApplet.parseInt( back_top_v + (back_top_len / 2.0f) ));	
				
		// Draw the v sections:
/*		
		pattern.stroke(255,0,0);
		for(int i = 0; i < sectionV.length; i++) {
			pattern.line(0, sectionV[i], patternWidth, sectionV[i]);
			// parent.println("  SectionV[" + i + "]: v = " + sectionV[i]);
		}		
*/		
		// Show Caps Limits - 1 inch
		// pattern.line(inch2unit(1.0f), 0, inch2unit(1.0f), pattern.height);
		// pattern.line(pattern.width - inch2unit(1.0f), 0, pattern.width -
		// inch2unit(1.0f), pattern.height);
	}

	/**
	 * Projects an XY Pattern Point to the XYZ Mandrel pnt - Assumes the (s,t)
	 * coordinates are set.
	 */

	public Pt projectPt(Pt pnt) {
		float v = 0; // v parameter
		float t = 0; // t parameter
		float mag  = 0; // magnitude for vectors
		
		// Calculate (x,y,z) and normals (n_x, n_y, n_z) and normals magnitude
		// for the point
		pnt.x = pnt.u; // x = u

		// Calculate y and z based on (u,v)

		// Make sure the u of the point is on the Mandrel Pattern:
		if ((pnt.u >= 0) && (pnt.u <= patternWidth)) {
			if (pnt.v > patternHeight) { // Wrap the v coordinate if it falls off of the pattern:
				v = pnt.v % patternHeight;
				// parent.println("Wrapped v: " + v + " from: " + pnt.v);
			} else if (pnt.v < 0) { // Negative v values get looped:
				v = pnt.v + patternHeight;
				// parent.println("Looped v: " + v + " from: " + pnt.v);
			} else {
				v = pnt.v;
			}

			// Mandrel regions based on the v parameter:
			if (v < (top_front_v - (top_front_len / 2.0))) { // TOP_FRONT region
				pnt.type = pnt.TOP_FRONT;
				pnt.y = (top_w / 2.0f) / parent.sqrt(3.0f); // should be same as: pnt.y = top_front_top_y;
				pnt.z = v;
				pnt.i = 0;
				pnt.j = 1;
				pnt.k = 0;				
			} else if (v < (top_front_v + (top_front_len / 2.0))) { // TOP_FRONT_CORNER region
				pnt.type = pnt.TOP_FRONT_CORNER;
				t = linearScale(parent.radians(90.0f), parent.radians(-30.0f), v, top_front_v - (top_front_len/2.0f), top_front_v + (top_front_len/2.0f));
				// parent.println("  Degrees Output: " + parent.degrees(t));
				pnt.y = parent.sin(t)*top_front_r + top_front_cy;
				pnt.z = parent.cos(t)*top_front_r + top_front_cz;
				pnt.i = 0;
				pnt.j = parent.sin(t);
				pnt.k = parent.cos(t);								
			} else if (v < (front_back_v - (front_back_len / 2.0))) { // FRONT region
				pnt.type = pnt.FRONT;
				t = linearScale(0.0f, 1.0f, v, top_front_v + (top_front_len/2.0f), front_back_v - (front_back_len/2.0f));
				pnt.y = (front_back_front_y - top_front_front_y)*t + top_front_front_y;
				pnt.z = (front_back_front_z - top_front_front_z)*t + top_front_front_z;
				pnt.i = 0;
				pnt.j = parent.sin(parent.radians(-30.0f));
				pnt.k = parent.cos(parent.radians(-30.0f));	
			} else if (v < (front_back_v + (front_back_len / 2.0))) { // FRONT_BACK_CORNER region
				pnt.type = pnt.FRONT_BACK_CORNER;
				t = linearScale(parent.radians(-30.0f), parent.radians(-150.0f), v, front_back_v - (front_back_len/2.0f), front_back_v + (front_back_len/2.0f));
				// parent.println("  Degrees Output: " + parent.degrees(t));
				pnt.y = parent.sin(t)*front_back_r + front_back_cy;
				pnt.z = parent.cos(t)*front_back_r + front_back_cz;
				pnt.i = 0;
				pnt.j = parent.sin(t);
				pnt.k = parent.cos(t);					
			} else if (v < (back_top_v - (back_top_len / 2.0))) { // BACK region
				pnt.type = pnt.BACK;
				t = linearScale(0.0f, 1.0f, v, front_back_v + (front_back_len/2.0f), back_top_v - (back_top_len/2.0f));
				pnt.y = (back_top_back_y - front_back_back_y)*t + front_back_back_y;
				pnt.z = (back_top_back_z - front_back_back_z)*t + front_back_back_z;	
				pnt.i = 0;
				pnt.j = parent.sin(parent.radians(-150.0f));
				pnt.k = parent.cos(parent.radians(-150.0f));	
			} else if (v < (back_top_v + (back_top_len / 2.0))) { // BACK_TOP_CORNER region
				pnt.type = pnt.BACK_TOP_CORNER;
				t = linearScale(parent.radians(210.0f), parent.radians(90.0f), v, back_top_v - (back_top_len/2.0f), back_top_v + (back_top_len/2.0f));
				// parent.println("  Degrees Output: " + parent.degrees(t));
				pnt.y = parent.sin(t)*back_top_r + back_top_cy;
				pnt.z = parent.cos(t)*back_top_r + back_top_cz;
				pnt.i = 0;
				pnt.j = parent.sin(t);
				pnt.k = parent.cos(t);	
			} else { // TOP_BACK_REGION
				pnt.type = pnt.TOP_BACK;
				pnt.y = (top_w / 2.0f) / parent.sqrt(3.0f); // should be same as: pnt.y = back_top_top_y;
				pnt.z = -(patternHeight - v);
				pnt.i = 0;
				pnt.j = 1;
				pnt.k = 0;
			}
		} else { // Blank Point returned: U maxxed out
			parent.println("******************");
			parent.println("**** U MAXXED ****");
			parent.println("**** NO POINT ****");
			parent.println("* u= " + pnt.u +  " *");
			parent.println("******************");
			return pnt;
		} // u check

		// Convert mandrel coordinates to inches
		pnt.x = unitSize * pnt.x;
		pnt.y = unitSize * pnt.y;
		pnt.z = unitSize * pnt.z;
		
		// Normalize the Normals Vector
		mag = parent.mag(pnt.i, pnt.j, pnt.k);
		pnt.i = pnt.i / mag;
		pnt.j = pnt.j / mag;
		pnt.k = pnt.k / mag;
		
		return pnt;
	}
}