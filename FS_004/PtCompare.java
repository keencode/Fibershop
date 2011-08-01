import java.util.Comparator;

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
  Class PtCompare 
     Sort Either in Ascending or Descending X direction.
     If X is equal, then sort in 
 **/

class PtCompare implements Comparator {
  boolean x_ascending;
  
  PtCompare(boolean _x_ascending) {
    x_ascending = _x_ascending;
  }
  
  public int compare(Object obj1, Object obj2) {
    Pt p1 = (Pt) obj1;
    Pt p2 = (Pt) obj2;

    if((obj1 == null) || (obj2 == null)) {
      return 0;  
    }

    if(p1.u == p2.u) { // equal x's - use the orientation and y-values
     /*
      if(p1.p_x > p2.p_x) {
        return 1; 
      } else if(p1.p_x < p2.p_x) {
        return -1;
      } else {
         System.out.println("PtCompare: Duplicate Points");
         return 0; 
      }
      // PRECISION: return int(p1.p_x - p2.p_x);
      */
      
      System.out.println("# PtCompare: X is equal: P1[" + p1.idx + "]: " + p1.u + "/" + p1.parent.theMandrel.unit2inch(p1.u) +
    		  				    			  " == P2[" + p2.idx + "]: " + p2.u + "/" + p2.parent.theMandrel.unit2inch(p2.u) );
      
      // Delete the first one
      
      return 0;
    } else if(p1.u > p2.u) { // p1's X is greater than p2's X
      if(x_ascending) { // Switch
        return 1;        
      } else { // Don't Switch
        return -1;
      }
    } else {
      if(x_ascending) {
        return -1;  // Don't Switch      
      } else {
        return 1;  // Switch
      }    
    }
  }
} 

