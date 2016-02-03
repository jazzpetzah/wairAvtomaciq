#!/usr/bin/python

import sys, time, getopt, os, subprocess
from Quartz.CoreGraphics import *

usageExit = '''%s
Click within an OSX window. You must specify relative, not absolute, coordinates. Relative coordinates are < 1 and represent
the percentage of the screen relative to the upper left corner.

Usage: %s <windowName> <X> <Y>

Example: %s "Simulator" 0.5 0.5
''' % (sys.argv[0], sys.argv[0], sys.argv[0])

def mouseEvent(type, posx, posy):
    theEvent = CGEventCreateMouseEvent(None, type, (posx,posy), kCGMouseButtonLeft)
    CGEventPost(kCGHIDEventTap, theEvent)

def mousemove(posx, posy):
    mouseEvent(kCGEventMouseMoved, posx, posy)

def mouseclickdn(posx, posy):
    mouseEvent(kCGEventLeftMouseDown, posx, posy)

def mouseclickup(posx, posy):
    mouseEvent(kCGEventLeftMouseUp, posx, posy)

def mouseclick(posx, posy):
    mouseclickdn(posx, posy)
    mouseclickup(posx, posy)

def click(posx, posy):
    print "Clicking at (%d, %d)" % (posx, posy)
    ourEvent = CGEventCreate(None)
    currentpos = CGEventGetLocation(ourEvent) # Save current mouse position
    mousemove(posx, posy)
    mouseclick(posx, posy)
    mousemove(int(currentpos.x), int(currentpos.y)) # Restore mouse position

def clickRelative(posx, posy, sizeX, sizeY):
    x = posx * float(sizeX)
    y = posy * float(sizeY)
    click(int(x), int(y))

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

def clickInWindow(windowName, posX, posY):
    dim = getWindowSize(windowName)
    moveWindowToZero(windowName)
    time.sleep(0.9)
    clickRelative(posX, posY, dim[0], dim[1])

if __name__ == "__main__":
    if (len(sys.argv) < 3):
        print usageExit
        sys.exit(1)
    clickInWindow("Simulator", float(sys.argv[1]), float(sys.argv[2]))
    print "Done"