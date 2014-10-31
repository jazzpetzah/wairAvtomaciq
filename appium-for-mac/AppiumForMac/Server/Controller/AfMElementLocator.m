//
//  AppiumMacElementLocator.m
//  AppiumForMac
//
//  Created by Dan Cuellar on 8/3/13.
//  Copyright (c) 2013 Appium. All rights reserved.
//

#import "AfMElementLocator.h"
#import "AfMStatusCodes.h"

#define TICK   NSDate *startTime = [NSDate date]
#define TOCK   NSLog(@"%s(%d) Time: %f", __func__, __LINE__, -[startTime timeIntervalSinceNow])

@implementation AfMElementLocator

-(id) initWithSession:(AfMSessionController*)session strategy:(AppiumMacLocatoryStrategy)strategy value:(NSString*)value
{
    self = [super init];
    if (self) {
		self.session = session;
		self.strategy = strategy;
		self.value = value;
    }
    return self;
}

+(AfMElementLocator*) locatorWithSession:(AfMSessionController*)session using:(NSString*)using value:(NSString*)value
{
	if ([using isEqualToString:@"id"])
	{
		return [[AfMElementLocator alloc] initWithSession:session strategy:AppiumMacLocatoryStrategyID value:value];
	}
	else if ([using isEqualToString:@"name"])
	{
		return [[AfMElementLocator alloc] initWithSession:session strategy:AppiumMacLocatoryStrategyName value:value];
	}
	else if ([using isEqualToString:@"tag name"])
	{
		return [[AfMElementLocator alloc] initWithSession:session strategy:AppiumMacLocatoryStrategyTagName value:value];
	}
    else if ([using isEqualToString:@"class name"])
	{
		return [[AfMElementLocator alloc] initWithSession:session strategy:AppiumMacLocatoryStrategyClassName value:value];
	}
	else if ([using isEqualToString:@"xpath"])
	{
		return [[AfMElementLocator alloc] initWithSession:session strategy:AppiumMacLocatoryStrategyXPath value:value];
	}
	return nil;
}

-(BOOL) matchesElement:(PFUIElement*)element
{
	switch(self.strategy)
	{
		case AppiumMacLocatoryStrategyID:
		{
			if (element == nil)
			{
				return NO;
			}
			NSString *identifier = [element valueForAttribute:@"AXIdentifier"];
			return identifier != nil && [self.value isEqualToString:identifier];
		}
		case AppiumMacLocatoryStrategyName:
			return element != nil && [self.value isEqualToString:element.AXTitle];
		case AppiumMacLocatoryStrategyTagName:
			return element != nil && [self.value isEqualToString:element.AXRole];
		default:
			return NO;
	}
}

/*
- (NSRect) screenResolution {
    NSArray *activeScreens = [NSScreen screens];
    NSRect result;
    for (NSScreen *screen in activeScreens)
    {
        result = [screen visibleFrame];
        NSLog(@"Rectangle: %f,%f", result.size.width, result.size.height);
    }
    return result;
}*/

-(PFUIElement*) findUsingBaseElement:(PFUIElement*)baseElement statusCode:(int *)statusCode
{
	if (self.strategy == AppiumMacLocatoryStrategyXPath)
	{
		NSMutableDictionary *pathMap = [NSMutableDictionary new];
		GDataXMLDocument *doc = [self.session xmlPageSourceFromElement:baseElement pathMap:pathMap];
		NSError *error;
		NSArray *matches = [doc nodesForXPath:self.value error:&error];
		if (error != nil)
		{
			*statusCode = kAfMStatusCodeXPathLookupError;
			return nil;
		}
		if (matches.count < 1)
		{
			*statusCode = kAfMStatusCodeNoSuchElement;
			return nil;
		}
		NSString *matchedPath = [(GDataXMLElement*)[matches objectAtIndex:0] attributeForName:@"path"].stringValue;
		PFUIElement *matchedElement = [pathMap objectForKey:matchedPath];
		*statusCode = kAfMStatusCodeSuccess;
		return matchedElement;
	}

    if (self.strategy == AppiumMacLocatoryStrategyClassName)
    {
        AfMElementLocator *windowLocator = [AfMElementLocator locatorWithSession:self.session using:@"xpath" value:@"//AXWindow"];
        
        PFUIElement *matchedElement = nil;
        if (windowLocator != nil) {
            NSMutableArray *windows = [NSMutableArray new];
            [windowLocator findAllUsingBaseElement:nil results:windows statusCode:statusCode];
            if (windows.count > 0) {
                PFUIElement *mainWindow = [windows objectAtIndex:0];
                NSString *windowTitle = mainWindow.AXTitle;
                
                int startX = mainWindow.AXPosition.pointValue.x-100;
                int startY = mainWindow.AXPosition.pointValue.y-100;
                int endX = startX + mainWindow.AXSize.pointValue.x+200;
                int endY = startY + mainWindow.AXSize.pointValue.y+200;
                
                
                for (int j = startY; j < endY; j+=15) {
                    for (int i = startX; i < endX; i+=15) {

                        NSError *error = nil;
                        PFUIElement *newElement = [PFUIElement elementAtPoint:NSMakePoint(i, j) withDelegate:nil error:&error];
                        if ([self.value caseInsensitiveCompare:newElement.AXRole] == NSOrderedSame) {
                            matchedElement = newElement;
                            
                            BOOL isParentOK = NO;
                            NSArray *parents = newElement.parentPath;
                            for (PFUIElement * parent in parents)
                            {
                                if ([parent.AXTitle isEqualToString:windowTitle])
                                {
                                    isParentOK = true;
                                    break;
                                }
                            }
                            if (!isParentOK) continue;
                            
                            *statusCode = kAfMStatusCodeSuccess;
                            return matchedElement;
                        }
                    }
                }
                *statusCode = kAfMStatusCodeNoSuchElement;
                return nil;
            } else {
                *statusCode = kAfMStatusCodeNoSuchWindow;
                return nil;
            }
            if (matchedElement == nil) {
                *statusCode = kAfMStatusCodeNoSuchElement;
                return nil;
            }
            *statusCode = kAfMStatusCodeUnknownError;
            return nil;
        }
        
    }
    
    // check if this the element we are looking for
    if ([self matchesElement:baseElement])
    {
        // return the element if it matches
		*statusCode = kAfMStatusCodeSuccess;
        return baseElement;
    }

    // search the children
    NSArray *elementsToSearch;
	if (baseElement != nil)
    {
        if ([baseElement.AXRole caseInsensitiveCompare:@"AXUnknown"] != NSOrderedSame) {
            // search the children if this is an element
            elementsToSearch = baseElement.AXChildren;
        }
    }
    else
    {
		@try
		{
			// get elements from the current window if no base element is supplied
			if (self.session.currentApplication == nil)
			{
				*statusCode = kAfMStatusCodeNoSuchWindow;
				return nil;
			}

			elementsToSearch = self.session.currentApplication.AXChildren;
		}
		@catch (NSException *exception)
		{
			*statusCode = kAfMStatusCodeNoSuchWindow;
			return nil;
		}
    }

	// check the children
    if (elementsToSearch != nil)
    {
        TICK;
        for(PFUIElement* childElement in elementsToSearch)
        {
            TICK;
            // check the child
            PFUIElement *childResult = [self findUsingBaseElement:childElement statusCode:statusCode];

            //return the child if it matches
            if (childResult != nil)
            {
				*statusCode = kAfMStatusCodeSuccess;
                return childResult;
            }
            TOCK;
        }
        TOCK;
    }
    // return nil because there was no match
	*statusCode = kAfMStatusCodeNoSuchElement;
    return nil;
}

-(void)findAllUsingBaseElement:(PFUIElement*)baseElement results:(NSMutableArray*)results statusCode:(int *)statusCode
{
	// use different method for xpath
	if (self.strategy == AppiumMacLocatoryStrategyXPath)
	{
		NSMutableDictionary *pathMap = [NSMutableDictionary new];
		GDataXMLDocument *doc = [self.session xmlPageSourceFromElement:baseElement pathMap:pathMap];
		NSError *error;
		NSArray *matches = [doc nodesForXPath:self.value error:&error];
		if (error != nil)
		{
			*statusCode = kAfMStatusCodeXPathLookupError;
			return;
		}
		if (matches.count < 1)
		{
			*statusCode = kAfMStatusCodeNoSuchElement;
			return;
		}

		*statusCode = kAfMStatusCodeSuccess;
		for(GDataXMLElement *match in matches)
		{
			PFUIElement *matchedElement = [pathMap objectForKey:[match attributeForName:@"path"].stringValue];
			[results addObject:matchedElement];
		}
        return;
	}

    //and another different one for class name
    if (self.strategy == AppiumMacLocatoryStrategyClassName)
    {
        AfMElementLocator *windowLocator = [AfMElementLocator locatorWithSession:self.session using:@"xpath" value:@"//AXWindow"];
        
        if (windowLocator != nil) {
            NSMutableArray *windows = [NSMutableArray new];
            [windowLocator findAllUsingBaseElement:nil results:windows statusCode:statusCode];
            if (windows.count > 0) {
                PFUIElement *mainWindow = [windows objectAtIndex:0];
                NSString *windowTitle = mainWindow.AXTitle;
                int startX = mainWindow.AXPosition.pointValue.x-100;
                int startY = mainWindow.AXPosition.pointValue.y-100;
                int endX = startX + mainWindow.AXSize.pointValue.x+200;
                int endY = startY + mainWindow.AXSize.pointValue.y+200;
                
                NSMutableArray *rects = [NSMutableArray new];
                for (int j = startY; j < endY; j+=15) {
                    for (int i = startX; i < endX; i+=15) {
                        NSError *error = nil;
                        PFUIElement *newElement = [PFUIElement elementAtPoint:NSMakePoint(i, j) withDelegate:nil error:&error];
                        
                        //DEBUG
//                       CFTypeRef identifierRef = nil;
//                       AXUIElementCopyAttributeValue(newElement.elementRef, (__bridge CFStringRef)@"AXIdentifier", &identifierRef);
//                        NSString *identifier = (__bridge NSString*)identifierRef;
//                        NSLog(@"[%d,%d] %@: %@", i, j, newElement.AXRole, identifier);
                        //DEBUG
                        
                        if ([self.value caseInsensitiveCompare:newElement.AXRole] == NSOrderedSame)
                        {
                            BOOL isParentOK = NO;
                            NSArray *parents = newElement.parentPath;
                            for (PFUIElement * parent in parents)
                            {
                                if ([parent.AXTitle isEqualToString:windowTitle])
                                {
                                    isParentOK = true;
                                    break;
                                }
                            }
                            if (!isParentOK) continue;
                            
                            NSRect currentRect = NSMakeRect(
                                        newElement.AXPosition.pointValue.x,
                                        newElement.AXPosition.pointValue.y,
                                        newElement.AXSize.pointValue.x,
                                        newElement.AXSize.pointValue.y);
                            BOOL existingElement = NO;
                            for (NSValue *value in rects)
                            {
                                NSRect rect = value.rectValue;
                                if (NSPointInRect(NSMakePoint(i,j), rect))
                                {
                                    existingElement = YES;
                                }
                            }
                            if (!existingElement) {
                                [results addObject:newElement];
                                [rects addObject:[NSValue valueWithRect: currentRect]];
                            }
                        }
                    }
                }
            } else {
                *statusCode = kAfMStatusCodeNoSuchWindow;
            }
            if (results.count < 1) {
                *statusCode = kAfMStatusCodeNoSuchElement;
                return;
            }
            return;
        }

    }
    
    // check if this the element we are looking for
    if ([self matchesElement:baseElement])
    {
        // return the element if it matches
		*statusCode = kAfMStatusCodeSuccess;
        [results addObject: baseElement];
    }

    // search the children
    NSArray *elementsToSearch;
	if (baseElement != nil)
    {
        // search the children if this is an element
        elementsToSearch = baseElement.AXChildren;
    }
    else
    {
		// get elements from the current appliction if no base element is supplied
        if (self.session.currentApplication == nil)
        {
			*statusCode = kAfMStatusCodeNoSuchWindow;
			return;
        }
		elementsToSearch = self.session.currentApplication.AXChildren;
    }

	// check the children
    if (elementsToSearch != nil)
    {
        TICK;
        for(PFUIElement* childElement in elementsToSearch)
        {
            TICK;
            // check the child
            [self findAllUsingBaseElement:childElement results:results statusCode:statusCode];
            TOCK;
        }
        TOCK;
    }
}

@end
