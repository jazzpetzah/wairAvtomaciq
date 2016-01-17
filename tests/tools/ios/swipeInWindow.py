#!/usr/bin/python

import sys, time, getopt, os, subprocess
from Quartz.CoreGraphics import * # imports all of the top-level symbols in the module

usageExit = '''%s
Swipe within an OSX window. You must specify relative, not absolute, coordinates. Relative coordinates are < 1 and represent
the percentage of the screen relative to the upper left corner. Optional paramater - swipe duration.

Usage: %s <windowName> <startX> <startY> <endX> <endY> (<durationMilliseconds>)

Example: %s "Simulator" 0.5 0.5 0.5 0.8 1000
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

SWIPE_STEP_DURATION_MILLISECONDS = 4
DEFAULT_SWIPE_DURATION_MILLISECONDS = 2000

def get_next_point(start, end, current, duration):
    return int(start + (end - start) / float(duration) * float(current))

def swipe(startX, startY, endX, endY, durationMilliseconds):
    """Swipe from start coordinate to end coordinate"""
    print "Swiping from (%d, %d) to (%d, %d) within %d milliseconds" % (startX, startY, endX, endY, durationMilliseconds)
    ourEvent = CGEventCreate(None)
    currentpos=CGEventGetLocation(ourEvent) # Save current mouse position
    mouseclickdn(startX, startY)
    currentMillisecond = 1
    while (currentMillisecond <= durationMilliseconds):
        time.sleep(SWIPE_STEP_DURATION_MILLISECONDS / 1000.0)
        x = get_next_point(startX, endX, currentMillisecond, durationMilliseconds)
        y = get_next_point(startY, endY, currentMillisecond, durationMilliseconds)
        mousedrag(x, y)
        currentMillisecond += SWIPE_STEP_DURATION_MILLISECONDS
    mouseclickup(endX, endY)
    time.sleep(1)
    mousemove(int(currentpos.x),int(currentpos.y)) # Restore mouse position

# Swipe from start to end using relative coordinates (0.5, 0.5) would be middle of screen
# Specify dimensions with sizeX and sizeY
def swipeRelative(startX, startY, endX, endY, sizeX, sizeY, durationMilliseconds):
    x1 = startX * float(sizeX)
    y1 = startY * float(sizeY)
    x2 = endX * float(sizeX)
    y2 = endY * float(sizeY)
    swipe (int(x1),int(y1),int(x2),int(y2), durationMilliseconds)

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

def swipeInWindow(windowName, startX, startY, endX, endY, durationMilliseconds=DEFAULT_SWIPE_DURATION_MILLISECONDS):
    dim = getWindowSize(windowName)
    print dim[0]
    print dim[1]
    moveWindowToZero(windowName)
    time.sleep(0.9)
    activateWindow(windowName)
    time.sleep(0.9)
    swipeRelative(startX,startY,endX,endY,dim[0],dim[1], durationMilliseconds)

if __name__ == "__main__":
    print('argv: ', len(sys.argv))
    if (len(sys.argv) < 5):
      print usageExit
      sys.exit(1)
    if (len(sys.argv) > 5):
        swipeInWindow("Simulator", float(sys.argv[1]), float(sys.argv[2]), float(sys.argv[3]), float(sys.argv[4]), int(sys.argv[5]))
    else:
        swipeInWindow("Simulator", float(sys.argv[1]), float(sys.argv[2]), float(sys.argv[3]), float(sys.argv[4]))
    print "Done"