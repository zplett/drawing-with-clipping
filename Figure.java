import com.jogamp.opengl.*;
import java.util.*;

class Point {
    public int x,y;
    
    Point(int x,int y){
	this.x=x; this.y=y;
    }
}

class FilledRectangle {
    // two points at opposite corners of a rectangle
    Point p1;
    Point p2;
    Point p3;
    Point p4;
    ArrayList<Point> points;
    
    FilledRectangle(Point p1, Point p2){
	points = new ArrayList<Point>();
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = new Point(p2.x, p1.y);
	this.p4 = new Point(p1.x, p2.y);
	this.points.add(this.p1);
	this.points.add(this.p4);
	this.points.add(this.p2);
	this.points.add(this.p3);
    }
    
    // draw the rectangle as a polygon
    public void draw(GL2 gl, int how){
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    gl.glBegin(GL2.GL_POLYGON);
	    gl.glVertex2f(p1.x,p1.y);
	    gl.glVertex2f(p1.x,p2.y);
	    gl.glVertex2f(p2.x,p2.y);
	    gl.glVertex2f(p2.x,p1.y);
	    gl.glEnd();
	    gl.glFlush();
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "POLYGON");
	    }
	}
    }
}

class LineLoop {
    ArrayList<Point> points;
    LineLoop(ArrayList<Point> points) { 
	this.points = points; 
    }
    
    public void draw(GL2 gl, int how) {
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    for (int i = 1; i < points.size(); i++) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f(points.get(i - 1).x, points.get(i - 1).y);
		gl.glVertex2f(points.get(i).x, points.get(i).y);
		gl.glEnd();
	    }
	    gl.glFlush();
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "lineLoop");
	    }
	}
    }
    public void drawfinal(GL2 gl, int how) {
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    gl.glBegin(GL2.GL_LINE_LOOP);
	    for (int i = 0; i < points.size()-1; i++) gl.glVertex2f(points.get(i).x, points.get(i).y); 
	    gl.glEnd();
	    gl.glFlush(); 
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "lineLoop");
	    }
	}
	gl.glEnd();
	gl.glFlush(); 
	points.clear();
    } 
}

class Polygon {
    ArrayList<Point> points;
    Polygon(ArrayList<Point> points) { 
	this.points = points; 
    }
    
    public void draw(GL2 gl, int how) {
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    for (int i = 1; i < points.size(); i++) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f(points.get(i - 1).x, points.get(i - 1).y);
		gl.glVertex2f(points.get(i).x, points.get(i).y);
		gl.glEnd();
	    }
	    gl.glFlush();
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "POLYGON");
	    }
	}
    }
    public void drawfinal(GL2 gl, int how) {
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    gl.glBegin(GL2.GL_POLYGON);
	    for (int i = 0; i < points.size()-1; i++) gl.glVertex2f(points.get(i).x, points.get(i).y); 
	    gl.glEnd();
	    gl.glFlush(); 
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "lineLoop");
	    }
	}
	gl.glEnd();
	gl.glFlush(); 
	points.clear();
    } 
}

class Line {
    // two points of the line
    Point p1;
    Point p2;
    
    Line(Point p1, Point p2){
	this.p1 = p1;
	this.p2 = p2;
    }
    
    // draw the rectangle as a polygon
    public void draw(GL2 gl, int how){
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    gl.glLogicOp(how);
	    gl.glBegin(GL2.GL_LINES);
	    gl.glVertex2f(p1.x, p1.y);
	    gl.glVertex2f(p2.x, p2.y);
	    gl.glEnd();
	    gl.glFlush();
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) drawClip(gl, clip, how);
	}
    }
    
    public void drawClip(GL2 gl, ClipRectangle clip, int how) {
	int p1x = p1.x, p2x = p2.x, p1y = p1.y, p2y = p2.y, newx = 0, newy = 0;
	int p1code = getRegion(clip, p1);
	int p2code = getRegion(clip, p2);
	boolean inframe = true;
	while (p1code != ClipRectangle.inside || p2code != ClipRectangle.inside) {
	    if ((p1code & p2code) != ClipRectangle.inside) {
		inframe = false;
		break;
	    }
	    int code = p1code == ClipRectangle.inside ? p2code : p1code;
	    if ((code & ClipRectangle.left) != ClipRectangle.inside) {
		newx = clip.xmin;
		newy = p1y + ((p2y - p1y)*(clip.xmin - p1x))/(p2x-p1x);
	    }
	    else if ((code & ClipRectangle.right) != ClipRectangle.inside) {
		newx = clip.xmax;
		newy = p1y +((p2y - p1y)*(clip.xmax - p1x))/(p2x-p1x);
	    }
	    else if ((code & ClipRectangle.top) != ClipRectangle.inside) {
		newy = clip.ymax;
		newx = p1x + ((p2x - p1x)*(clip.ymax - p1y))/(p2y-p1y);
	    }
	    else if ((code & ClipRectangle.bottom) != ClipRectangle.inside) {
		newy = clip.ymin;
		newx = p1x + ((p2x - p1x)*(clip.ymin - p1y))/(p2y-p1y);
	    }
	    if (p1code == code) {
		p1x = newx;
		p1y = newy;
		p1code = getRegion(clip, new Point(p1x, p1y));
	    }
	    else {
		p2x = newx;
		p2y = newy;
		p2code = getRegion(clip, new Point(p2x, p2y));
	    }
	}
	if (inframe) {
	    gl.glLogicOp(how);
	    gl.glBegin(GL2.GL_LINES);
	    gl.glVertex2f(p1x, p1y);
	    gl.glVertex2f(p2x, p2y);
	    gl.glEnd();
	    gl.glFlush();
	}
    }
    
    public int getRegion(ClipRectangle clip, Point p) {
	int code = ClipRectangle.inside;
	if (p.x < clip.xmin) {
	    code |= ClipRectangle.left;
	}
	else if (p.x > clip.xmax) {
	    code |= ClipRectangle.right;
	}
	else if (p.y < clip.ymin) {
	    code |= ClipRectangle.bottom;
	}
	else if (p.y > clip.ymax) {
	    code |= ClipRectangle.top;
	}
	return code;
    }
}

class ClipSingle {
    
    private ArrayList<ClipRectangle> list = new ArrayList<ClipRectangle>();
    private boolean clipped;
    private static ClipSingle clipper = new ClipSingle();
    
    private ClipSingle() {
	clipped = false;
    }
    
    public static ClipSingle Instance() {
	return clipper; 
    }
    
    public boolean isClip() {
	return clipped;
    }
    
    public ArrayList<ClipRectangle> listReturn() {
	return list;
    }
    
    public void addRect(ClipRectangle cliprect) {
	clipped = true;
	list.add(cliprect);
    }
}

class Rectangle {
    // two points at opposite corners of a rectangle
    Point p1;
    Point p2;
    Point p3;
    Point p4;
    ArrayList<Point> points;
    
    Rectangle(Point p1, Point p2){
	points = new ArrayList<Point>();
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = new Point(p2.x, p1.y);
	this.p4 = new Point(p1.x, p2.y);
	this.points.add(this.p1);
	this.points.add(this.p4);
	this.points.add(this.p2);
	this.points.add(this.p3);
    }
    
    // draw the rectangle as a polygon
    public void draw(GL2 gl, int how){
	gl.glLogicOp(how);
	ClipSingle temporary = ClipSingle.Instance();
	if (!temporary.isClip()) {
	    gl.glBegin(GL2.GL_LINE_LOOP);
	    gl.glVertex2f(this.p1.x, this.p1.y);
	    gl.glVertex2f(this.p4.x, this.p4.y);
	    gl.glVertex2f(this.p2.x, this.p2.y);
	    gl.glVertex2f(this.p3.x, this.p3.y);
	    gl.glEnd();
	    gl.glFlush();
	}
	else {
	    ArrayList<ClipRectangle> rect = temporary.listReturn();
	    for (ClipRectangle clip: rect) {
		clip.drawClipped(gl, how, points, "lineLoop");
	    }
	}
    }
}

class ClipRectangle {
    Point p1, p2, p3, p4;
    ArrayList<Point> verticies;
    int xmin, xmax, ymin, ymax;
    public static final int inside = 0, left = 1, right = 2, top = 4, bottom = 8;
    
    public int getCode(Point p) {
   	int code = ClipRectangle.inside;
   	if (p.x < xmin) {
   	    code |= ClipRectangle.left;
   	}
   	else if (p.x > xmax) {
   	    code |= ClipRectangle.right;
   	}
   	else if (p.y < ymin) {
   	    code |= ClipRectangle.bottom;
   	}
   	else if (p.y > ymax) {
   	    code |= ClipRectangle.top;
   	}
   	return code;
    }
    
    ClipRectangle(Point p1, Point p2) {
	verticies = new ArrayList<Point>();
	this.p1 = p1;
	this.p2 = p2;
	xmin = Math.min(p1.x, p2.x);
	xmax = Math.max(p1.x, p2.x);
	ymin = Math.min(p1.y, p2.y);
	ymax = Math.max(p1.y, p2.y);
	verticies.add(this.p1);
	this.p3 = new Point(p2.x, p1.y);
	verticies.add(this.p3);
	verticies.add(this.p2);
	this.p4 = new Point(p1.x, p2.y);
	verticies.add(this.p4);
    }
    
    public void draw(GL2 gl, int how){
	ClipSingle temporary = ClipSingle.Instance();
	gl.glLogicOp(how);
	gl.glBegin(GL2.GL_LINE_LOOP);
	gl.glVertex2f(p1.x,p1.y);
	gl.glVertex2f(p2.x,p1.y);
	gl.glVertex2f(p2.x,p2.y);
	gl.glVertex2f(p1.x,p2.y);
	gl.glEnd();
	gl.glFlush();
    }
    
    public boolean isInside(Point e1, Point e2, Point v) { return (((e2.x - e1.x)*(v.y-e1.y)) > ((e2.y-e1.y)*(v.x-e1.x))); }
    
    public Point getIntersect (Point p1, Point p2, Point e1, Point e2) {
	Point intersect;
	float x, y, xd, yd, xn, yn, x1, x2, x3, x4, y1, y2, y3, y4;
	x1 = (float) p1.x;
	y1 = (float) p1.y;
	x2 = (float) p2.x;
	y2 = (float) p2.y;
	x3 = (float) e2.x;
	y3 = (float) e2.y;
	x4 = (float) e1.x;
	y4 = (float) e1.y;
	xn = ((((x1*y2)-(y1*x2))*(x3 - x4)) - ((x1 - x2)*((x3*y4) - (y3*x4))));
	xd = (((x1 - x2)*(y3 - y4)) - ((y1 - y2)*(x3 - x4)));
	yn = ((((x1*y2)-(y1*x2))*(y3 - y4)) - ((y1 - y2)*((x3*y4) - (y3*x4))));
	yd = (((x1 - x2)*(y3 - y4)) - ((y1 - y2)*(x3 - x4)));
	x = xn / xd;
	y = yn / yd;
	intersect = new Point((int)x, (int)y);
	return intersect;
    }
    
    public void drawClipped (GL2 gl, int how, ArrayList<Point> polyv, String shape) {
	ArrayList<Point> newpolyv = new ArrayList<Point>(polyv);
	Point e1 = this.verticies.get(this.verticies.size()-1);
	for (int i = 0; i < verticies.size(); i++) {
	    Point e2 = this.verticies.get(i);
	    ArrayList<Point> inputList = new ArrayList<Point>(newpolyv);
	    newpolyv.clear();
	    if (inputList.size() == 0) return;
	    Point a = inputList.get(inputList.size()-1);
	    for (int j = 0; j < inputList.size(); j++) {
		Point b = inputList.get(j);
		boolean aIn = isInside(e1,e2,a);
		boolean bIn = isInside(e1,e2,b);
		if(bIn) {
		    if (!aIn) newpolyv.add(getIntersect(a,b,e1,e2));
		    newpolyv.add(b);
		}
		else if (aIn) {
		    newpolyv.add(getIntersect(a,b,e1,e2));
		}
		a = b;
	    } 
	    e1 = e2;
	}
	
	if (shape == "lineLoop") {
	    gl.glLogicOp(how);
	    gl.glBegin(GL2.GL_LINE_LOOP);
	    
	    for (Point p: newpolyv) {
		gl.glVertex2f(p.x, p.y);
	    }
	    gl.glEnd();
	    gl.glFlush();
	}
	else {
	    gl.glLogicOp(how);
	    gl.glBegin(GL2.GL_POLYGON);
	    
	    for (Point p: newpolyv) {
		gl.glVertex2f(p.x, p.y);
	    }
	    gl.glEnd();
	    gl.glFlush();
	}
    }
    
    public void finalize() {
	ClipSingle temporary = ClipSingle.Instance();
	temporary.addRect(this);
    }
    
}
