#!/usr/bin/python

import sys, time, getopt, os, subprocess
from Quartz.CoreGraphics import * # imports all of the top-level symbols in the module

usageExit = '''%s
Swipe within an OSX window. You must specify relative, not absolute, coordinates. Relative coordinates are < 1 and represent
the percentage of the screen relative to the upper left corner. 

Usage: %s <windowName> <startX> <startY> <endX> <endY> 

Example: %s "Simulator" 0.5 0.5 0.5 0.8 
(drags from middle of the window down to approx. 80 percent of the window)
''' % (sys.argv[0], sys.argv[0], sys.argv[0])

def mouseEvent(type, posx, posy):
    theEvent = CGEventCreateMouseEvent(None, type, (posx,posy), kCGMouseButtonLeft)
    CGEventPost(kCGHIDEventTap, theEvent)
    
def mousemove(posx,posy):
    mouseEvent(kCGEventMouseMoved, posx,posy)
    
def mouseclickdn(posx,posy):
    mouseEvent(kCGEventLeftMouseDown, posx,posy)
    
def mouseclickup(posx,posy):
    mouseEvent(kCGEventLeftMouseUp, posx,posy)
    
def mousedrag(posx,posy):
    mouseEvent(kCGEventLeftMouseDragged, posx,posy)

# Swipe from start coordinate to end coordinate
def swipe(startX, startY, endX, endY):
    print "Swiping from (%d, %d) to (%d, %d)" % (startX, startY, endX, endY)
    ourEvent = CGEventCreate(None) 
    currentpos=CGEventGetLocation(ourEvent) # Save current mouse position
    mouseclickdn(startX, startY)
    y = startY
    if (startY < endY):
        time.sleep(0.05)
        for i in range(1,(endY - startY)):
            mousedrag(endX, y)
            y+=1
            time.sleep(0.001)
    if (endY < startY):
        time.sleep(0.05)
        for i in range(1,(startY - endY)):
            mousedrag(endX, y)
            y-=1
            time.sleep(0.001)
    mouseclickup(endX, endY)
    time.sleep(1)
    mousemove(int(currentpos.x),int(currentpos.y)) # Restore mouse position

# Swipe from start to end using relative coordinates (0.5, 0.5) would be middle of screen
# Specify dimensions with sizeX and sizeY
def swipeRelative(startX, startY, endX, endY, sizeX, sizeY):
    x1 = startX * float(sizeX)
    y1 = startY * float(sizeY)
    x2 = endX * float(sizeX)
    y2 = endY * float(sizeY)
    swipe (int(x1),int(y1),int(x2),int(y2))   
    
# Moves the window to 0,0 so relative coordinates will be accurate
def moveWindowToZero(windowName):
    print "Moving window '%s' to (0,0)" % windowName
    cmd="""
osascript<<END
    tell application "System Events" to tell application process "%s"
    	set position of window 1 to {0, 0}
    end tell
""" % windowName
    os.system(cmd)
    
# Use applescript to bring a window to the foreground
def activateWindow(windowName):
    print "Moving window '%s' to foreground" % windowName
    cmd="""
osascript<<END
    tell application "System Events" to tell application process "%s"
        set frontmost to true 
    end tell
""" % windowName
    os.system(cmd)    

# Use applescript to get the window size
def getWindowSize(windowName):
    cmd="""
osascript<<END
    tell application "System Events" to tell application process "%s"
    	get size of window 1
    end tell
""" % windowName
    dimensions = subprocess.check_output(cmd, shell=True)
    result = dimensions.strip().split(", ")
    print "Window size of '%s' is: %s" % (windowName, result)
    return result  
    
def swipeInWindow(windowName, startX, startY, endX, endY):
    dim = getWindowSize(windowName)
    print dim[0]
    print dim[1]
    moveWindowToZero(windowName)
    time.sleep(0.9)
    activateWindow(windowName)
    time.sleep(0.9)
    swipeRelative(startX,startY,endX,endY,dim[0],dim[1])
    
def main():
	swipeInWindow("Simulator",0.5,0.30,0.5,0.60)

     #Process args
    #if (len(sys.argv) < 5):
    #   print usageExit
    #   sys.exit(1)
    
    #swipeInWindow("Simulator", float(sys.argv[1]), float(sys.argv[2]), float(sys.argv[3]), float(sys.argv[4]))
    #print "Done"

if __name__ == "__main__":
    main()