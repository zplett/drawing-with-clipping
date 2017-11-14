import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Painter implements GLEventListener, ActionListener {

    private static final int FIRST = 1, CONTINUE = 2;
    private GLCanvas canvas;
    private boolean first = true;
    private Pen pen = null;  // current drawing pen
    private int ww = 800, wh = 600; // canvas width and height
    private float r=1,g=0,b=0;  // current drawing color

    // buttons must be instance variables so the action listeners can
    // access them
    private JRadioButton lineButton;
    private JRadioButton lineLoopButton;
    private JRadioButton polygonButton;
    private JRadioButton rectangleButton;
    private JRadioButton filledRectangleButton;
    private JRadioButton clipRectangleButton;
    private JRadioButton redButton;
    private JRadioButton yellowButton;
    private JRadioButton greenButton;
    private JRadioButton blueButton;
    private JRadioButton violetButton;
    private JRadioButton quitButton;
    
    // gl and glu are used to interface with OpenGL
    private GL2 gl;
    private GLU glu;
    
    // GLEventListener methods:  init, reshape, display, dispose
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(1, 1, 1, 1); // set clear color to white

	gl.glEnable(GL2.GL_COLOR_LOGIC_OP);
	
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0,ww,wh,0);
	
	pen = new FilledRectanglePen(gl);  // create an initial pen
	pen.setColor(r,g,b);    // and color
    }
    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
	ww = width; wh = height;  // save the canvas dimensions
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0.0, width, height, 0.0);
    }
    
    public void display(GLAutoDrawable drawable){
	if(first){
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT); // clear the canvas, once
	    first = false;
	}
    }
    
    public void dispose(GLAutoDrawable drawable){
    }

    public void actionPerformed(ActionEvent event){
	if (event.getSource() == redButton) {
	    r=1; g=0; b=0;
	}
	else if (event.getSource() == yellowButton)  {
	    r=1; g=1; b=0;
	}
	else if (event.getSource() == greenButton) {
	    r=0; g=1; b=0;
	}
	else if (event.getSource() == blueButton) {
	    r=0; g=0; b=1;
	}
	else if (event.getSource() == violetButton) {
	    r=.6f; g=0; b=1;
	}
	else if (event.getSource() == lineButton) {
	    pen = new LinePen(gl);
	}
	else if (event.getSource() == lineLoopButton) {
	    pen = new LineLoopPen(gl);
	}
	else if (event.getSource() == polygonButton) {
	    pen = new FilledPolygonPen(gl);
	}
	else if (event.getSource() == rectangleButton) {
	    pen = new RectanglePen(gl);
	}
	else if (event.getSource() == filledRectangleButton) {
	    pen = new FilledRectanglePen(gl);
	}
	else if (event.getSource() == clipRectangleButton) {
	    pen = new ClipRectanglePen(gl);
	}
	if (event.getSource() == quitButton) {
	    System.exit(0);
	}
	
	pen.setColor(r,g,b);
    }

    Painter(){
	GLProfile glp=GLProfile.getDefault();
	GLCapabilities caps = new GLCapabilities(glp);
	caps.setDoubleBuffered(false);
	canvas = new GLCanvas(caps);
	canvas.setSize(ww,wh);

	JFrame frame = new JFrame("Painter");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	frame.setLayout(new BorderLayout());
   
	JPanel west = new JPanel(new GridLayout(14,1)); // button panel
	lineButton = new JRadioButton( "line", true);
	lineLoopButton = new JRadioButton( "line loop");
	polygonButton = new JRadioButton( "polygon");
	rectangleButton = new JRadioButton( "rectangle");
	filledRectangleButton = new JRadioButton( "filled rectangle");
	clipRectangleButton = new JRadioButton( "clip rectangle");
	quitButton = new JRadioButton (" quit");
	ButtonGroup figureButtons = new ButtonGroup();
	figureButtons.add(lineButton);
	figureButtons.add(lineLoopButton);
	figureButtons.add(polygonButton);
	figureButtons.add(rectangleButton);
	figureButtons.add(filledRectangleButton);
	figureButtons.add(clipRectangleButton);
	figureButtons.add(quitButton);
	lineButton.addActionListener( this );
	lineLoopButton.addActionListener( this );
	polygonButton.addActionListener( this );
	rectangleButton.addActionListener( this );
	filledRectangleButton.addActionListener( this );
	clipRectangleButton.addActionListener( this );
	filledRectangleButton.setSelected(true);
	quitButton.addActionListener(this);
	west.add(lineButton);
	west.add(lineLoopButton);
	west.add(polygonButton);
	west.add(rectangleButton);
	west.add(filledRectangleButton);
	west.add(clipRectangleButton);
	west.add(quitButton);

	west.add(new JLabel("   "));

	redButton = new JRadioButton( "red", true);
	yellowButton = new JRadioButton( "yellow");
	greenButton = new JRadioButton( "green");
	blueButton = new JRadioButton( "blue");
	violetButton = new JRadioButton( "violet");
	ButtonGroup radioButtons = new ButtonGroup();
	radioButtons.add(redButton);
	radioButtons.add(yellowButton);
	radioButtons.add(greenButton);
	radioButtons.add(blueButton);
	radioButtons.add(violetButton);
	redButton.addActionListener( this );
	yellowButton.addActionListener( this );
	greenButton.addActionListener( this );
	blueButton.addActionListener( this );
	violetButton.addActionListener( this );
	redButton.setSelected(true);
	west.add(redButton);
	west.add(greenButton);
	west.add(blueButton);
	west.add(yellowButton);
	west.add(violetButton);

	JPanel center = new JPanel(new GridLayout(1, 1));
	center.add(canvas);
      
	frame.add(west,  BorderLayout.WEST);
	frame.add(center, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);

        canvas.addMouseListener(new MouseAdapter(){
		public void mousePressed(MouseEvent e){
		    gl.getContext().makeCurrent();
		    pen.mouseDown(e);
		}
		public void mouseReleased(MouseEvent e){
		    gl.getContext().makeCurrent();
		    pen.mouseUp(e);
		}
		public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2) {
			gl.getContext().makeCurrent();
			pen.mouseClick(e);
		    }
		}
	    });

        canvas.addMouseMotionListener(new MouseAdapter(){
		public void mouseDragged(MouseEvent e){
		    gl.getContext().makeCurrent();
		    pen.mouseDragged(e);
		}
	    });

	canvas.addGLEventListener(this);

	FPSAnimator animator = new FPSAnimator(canvas, 60);
	animator.start();    
    }

    static public void main(String[] args){
	new Painter();
    }

}
