# Drawing With Clipping

This program provides the user with a drawing window. The drawing window has\n the ability of creating the following geometric objects with multiple colors:\n
      - lines\n
      - unfilled rectangles\n
      - filled rectangles\n
      - unfilled polygons ( line loops )\n
      - filled polygons\n
In addition, the user has the option of drawing a clipping window. Which will\n then only allow the user to draw within the clipping window(s). The Cohen\n Sutherland algorithm was used for line clipping and the Sutherland Hodgman\n algorithm was used for polygon clipping.\n

# Files

This repository contains the following files:\n
      - Figure.java: holds the various classes used to define the geometric\n
                     objects and the classes dealing with clipping windows\n
                     and their associated algorithms.\n
      - Pen.java: holds the various classes used to define the various pens\n
                  that can be selected for drawing.\n
      - Painter.java: holds the various classes used to define the canvas\n
                      and the behaviour associated with user interaction.\n
