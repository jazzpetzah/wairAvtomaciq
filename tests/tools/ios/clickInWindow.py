#!/usr/bin/python

import subprocess
import sys
import time

from Quartz.CoreGraphics import (CGEventCreateMouseEvent, kCGMouseButtonLeft, kCGHIDEventTap,
    CGEventPost, kCGEventMouseMoved, kCGEventLeftMouseDown, kCGEventLeftMouseUp, CGEventCreate,
    CGEventGetLocation)

usageExit = '''%s
Click within an OSX window. You must specify relative, not absolute, coordinates. Relative coordinates are < 1 and represent
the percentage of the screen relative to the upper left corner.

Usage: %s <windowName> <X> <Y>

Example: %s "Simulator" 0.5 0.5
''' % (sys.argv[0], sys.argv[0], sys.argv[0])

WINDOW_CAPTION_HEIGHT = 22


def mouseEvent(type, posx, posy):
    theEvent = CGEventCreateMouseEvent(None, type, (posx, posy), kCGMouseButtonLeft)
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
    currentpos = CGEventGetLocation(ourEvent)  # Save current mouse position
    mousemove(posx, posy)
    mouseclick(posx, posy)
    mousemove(int(currentpos.x), int(currentpos.y))  # Restore mouse position


def clickRelative(posx0, posy0, posx, posy, sizeX, sizeY):
    x = int(posx0) + posx * float(sizeX)
    y = int(posy0) + WINDOW_CAPTION_HEIGHT + posy * float(float(sizeY) - WINDOW_CAPTION_HEIGHT)
    click(int(x), int(y))


def getWindowPosition(windowName):
    cmd = """
osascript<<END
    tell application "System Events" to tell application process "%s"
    	get position of window 1
    end tell
""" % windowName
    dimensions = subprocess.check_output(cmd, shell=True)
    result = dimensions.strip().split(", ")
    print "Window position of '%s' is: %s" % (windowName, result)
    return result


def moveWindowToForeground(windowName):
    cmd = """
osascript<<END
    tell application "System Events" to tell application "%s"
        activate
    end tell
""" % windowName
    subprocess.check_output(cmd, shell=True)
    time.sleep(2)


def getWindowSize(windowName):
    cmd = """
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
    pos0 = getWindowPosition(windowName)
    moveWindowToForeground(windowName)
    clickRelative(pos0[0], pos0[1], posX, posY, dim[0], dim[1])


if __name__ == "__main__":
    if (len(sys.argv) < 3):
        print usageExit
        sys.exit(1)
    clickInWindow("Simulator", float(sys.argv[1]), float(sys.argv[2]))
    print "Done"
