import com.jogamp.opengl.*;
import java.awt.event.*;
import java.util.ArrayList;

abstract class Pen {
    protected static final int FIRST=1, CONTINUE=2;
    protected int state;
    protected float r,g,b;
    protected GL2 gl;
    
    Pen(GL2 gl){
	this.gl = gl;
	state = FIRST;
    }
    
    public void mouseDown(MouseEvent e){}
    public void mouseUp(MouseEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseClick(MouseEvent e) {}
    public void setColor(float r, float g, float b){
	this.r = r; this.g = g; this.b = b;
    }
}

/*
  TODO:  Write the mouseDown, mouseUp, and mouseDragged methods for each type
  of Pen, except FilledRectanglePen
*/
class ClipRectanglePen extends Pen {
    Point p1,p2;
    ClipRectangle rct;
    
    ClipRectanglePen(GL2 gl){
	super(gl);
    }

    public void mouseDown(MouseEvent e){
	int xnow = e.getX();
	int ynow = e.getY();
	p1 = p2 = new Point(xnow,ynow);
	rct = new ClipRectangle(p1,p2);
	gl.glColor3f(1-r,1-g,1-b);
	rct.draw(gl, GL2.GL_XOR);
    }
    
    public void mouseUp(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new ClipRectangle(p1,p2);
	gl.glColor3f(r,g,b);
	// draw the new version permanently
	rct.draw(gl, GL2.GL_COPY);
	rct.finalize();
    }
    
    public void mouseDragged(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new ClipRectangle(p1,p2);
	// draw the new version
	rct.draw(gl, GL2.GL_XOR);
    }
}

class RectanglePen extends Pen {
    Point p1,p2;
    Rectangle rct;
    
    RectanglePen(GL2 gl){
	super(gl);
    }

    public void mouseDown(MouseEvent e){
	int xnow = e.getX();
	int ynow = e.getY();
	p1 = p2 = new Point(xnow,ynow);
	rct = new Rectangle(p1,p2);
	gl.glColor3f(1-r,1-g,1-b);
	rct.draw(gl, GL2.GL_XOR);
    }
    
    public void mouseUp(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new Rectangle(p1,p2);
	gl.glColor3f(r,g,b);
	// draw the new version permanently
	rct.draw(gl, GL2.GL_COPY);
    }
    
    public void mouseDragged(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new Rectangle(p1,p2);
	// draw the new version
	rct.draw(gl, GL2.GL_XOR);
    }
    
}

class FilledRectanglePen extends Pen {
    Point p1,p2;
    FilledRectangle rct;
    
    FilledRectanglePen(GL2 gl){
	super(gl);
    }

    public void mouseDown(MouseEvent e){
	int xnow = e.getX();
	int ynow = e.getY();
	p1 = p2 = new Point(xnow,ynow);
	rct = new FilledRectangle(p1,p2);
	gl.glColor3f(1-r,1-g,1-b);
	rct.draw(gl, GL2.GL_XOR);
    }
    
    public void mouseUp(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new FilledRectangle(p1,p2);
	gl.glColor3f(r,g,b);
	// draw the new version permanently
	rct.draw(gl, GL2.GL_COPY);
    }
    
    public void mouseDragged(MouseEvent e){
	// erase the last rectangle
	rct.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	rct = new FilledRectangle(p1,p2);
	// draw the new version
	rct.draw(gl, GL2.GL_XOR);
    }
}

class LinePen extends Pen {
    Point p1,p2;
    Line line;
    
    LinePen(GL2 gl){
	super(gl);
    }

    public void mouseDown(MouseEvent e){
	int xnow = e.getX();
	int ynow = e.getY();
	p1 = p2 = new Point(xnow,ynow);
	line = new Line(p1,p2);
	gl.glColor3f(1-r,1-g,1-b);
	line.draw(gl, GL2.GL_XOR);
    }
    
    public void mouseUp(MouseEvent e){
	// erase the last rectangle
	line.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	line = new Line(p1,p2);
	gl.glColor3f(r,g,b);
	// draw the new version permanently
	line.draw(gl, GL2.GL_COPY);
    }
    
    public void mouseDragged(MouseEvent e){
	// erase the last rectangle
	line.draw(gl, GL2.GL_XOR);
	// get the new corner point
	int xnow = e.getX();
	int ynow = e.getY();
	p2 = new Point(xnow,ynow);
	line = new Line(p1,p2);
	// draw the new version
	line.draw(gl, GL2.GL_XOR);
    }

}

class LineLoopPen extends Pen {
    
    int count = 0;
    LineLoop line;
    ArrayList<Point> points = new ArrayList<Point>();
    LineLoopPen(GL2 gl){
	super(gl);
    }
    public void mouseDown(MouseEvent e) {
	line = new LineLoop(points);
	gl.glColor3f(1-r, 1-g, 1-b);
	line.draw(gl, GL2.GL_XOR);
    }
    public void mouseUp(MouseEvent e) {
	ClipSingle temporary = ClipSingle.Instance();
	int xnow = e.getX();
	int ynow = e.getY();
	points.add(new Point(xnow, ynow));
	line = new LineLoop(points);
	gl.glColor3f(r, g, b);
	line.draw(gl, GL2.GL_COPY);
	if (count > 1) {
	    if ((points.get(count).x == points.get(count-1).x) && (points.get(count).y == points.get(count).y)) {
		line.drawfinal(gl, GL2.GL_COPY);
		count = 0;
		return; 
	    }
	}
	count++;
    }

}

class FilledPolygonPen extends Pen {
    int count = 0;
    Polygon polygon;
    ArrayList<Point> points = new ArrayList<Point>();
    FilledPolygonPen(GL2 gl){
	super(gl);
    }
    public void mouseDown(MouseEvent e) {
	polygon = new Polygon(points);
	gl.glColor3f(1-r, 1-g, 1-b);
	polygon.draw(gl, GL2.GL_XOR);
    }
    public void mouseUp(MouseEvent e) {
	ClipSingle temporary = ClipSingle.Instance();
	int xnow = e.getX();
	int ynow = e.getY();
	points.add(new Point(xnow, ynow));
	polygon = new Polygon(points);
	gl.glColor3f(r, g, b);
	polygon.draw(gl, GL2.GL_COPY);
	if (count > 1) {
	    if ((points.get(count).x == points.get(count-1).x) && (points.get(count).y == points.get(count).y)) {
		polygon.drawfinal(gl, GL2.GL_COPY);
		count = 0;
		return; 
	    }
	}
	count++;
    }
}

