# Drawing With Clipping

This program provides the user with a drawing window. The drawing window has the ability of creating the following geometric objects with multiple colors: lines, unfilled rectangles, filled rectangles, unfilled polygons ( line loops ), filled polygons. In addition, the user has the option of drawing a clipping window. Which will then only allow the user to draw within the clipping window(s). The Cohen Sutherland algorithm was used for line clipping and the Sutherland Hodgman algorithm was used for polygon clipping.

# Files

This repository contains the following files: Figure.java: holds the various classes used to define the geometric objects and the classes dealing with clipping windows and their associated algorithms, Pen.java: holds the various classes used to define the various pens that can be selected for drawing, Painter.java: holds the various classes used to define the canvas and the behaviour associated with user interaction.
