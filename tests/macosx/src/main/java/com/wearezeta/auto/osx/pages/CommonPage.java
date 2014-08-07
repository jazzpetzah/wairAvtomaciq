package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class CommonPage extends OSXPage {

	static final String ELEMENT_ATT_POSITION = "AXPositon";
	static final String ELEMENT_ATT_SIZE = "AXSize";
	static final String DECREMENT_PAGE = "AXDecrementPage";
	static final String INCREMENT_PAGE = "AXIncrementPage";
	
	
	public CommonPage(String URL, String path) 
			throws MalformedURLException {
		
		super(URL, path);
	}
	
//	public void scrollToConversationInList(WebElement conversation) {
//    	NSPoint mainPosition = NSPoint.fromString(mainWindow.getAttribute("AXPosition"));
//    	NSPoint mainSize = NSPoint.fromString(mainWindow.getAttribute("AXSize"));
//    	
//    	NSPoint latestPoint =
//    			new NSPoint(mainPosition.x() + mainSize.x(), mainPosition.y() + mainSize.y());
//
//    	//get scrollbar for contact list
//    	WebElement peopleDecrementSB = null;
//    	WebElement peopleIncrementSB = null;
//
//    	WebElement scrollArea = driver.findElement(By.xpath(OSXLocators.xpathConversationListScrollArea));
//    
//    	WebElement userContact = conversation;
//    	boolean isFoundPeople = false;
//
//        NSPoint userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
//        if (userPosition.y() > latestPoint.y() || userPosition.y() < mainPosition.y()) {
//        	if (isFoundPeople) {
//    			WebElement scrollBar = scrollArea.findElement(By.xpath("//AXScrollBar"));
//    			List<WebElement> scrollButtons = scrollBar.findElements(By.xpath("//AXButton"));
//    			for (WebElement scrollButton: scrollButtons) {
//    				String subrole = scrollButton.getAttribute("AXSubrole");
//    				if (subrole.equals("AXDecrementPage")) {
//    					peopleDecrementSB = scrollButton;
//    				}
//    				if (subrole.equals("AXIncrementPage")) {
//    					peopleIncrementSB = scrollButton;
//    				}
//    			}
//    		}
//        	
//        	while (userPosition.y() > latestPoint.y()) {
//            	peopleIncrementSB.click();
//            	userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
//            }
//            while (userPosition.y() < mainPosition.y()) {
//            	peopleDecrementSB.click();
//            	userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
//            }
//        }
//		
//	}
//	
//	public void scrollDownTilMediaBarAppears() throws Exception {
//
//		NSPoint soundcloudPosition = NSPoint.fromString(soundCloudLinkButton
//				.getAttribute("AXPosition"));
//		NSPoint textInputPosition = NSPoint.fromString(newMessageTextArea
//				.getAttribute("AXPosition"));
//
//		// get scrollbar for conversation view
//		WebElement conversationDecrementSB = null;
//
//		WebElement scrollArea = driver.findElement(By
//				.id(OSXLocators.idConversationScrollArea));
//
//		if (soundcloudPosition.y() < textInputPosition.y()) {
//			WebElement scrollBar = scrollArea.findElement(By
//					.xpath("//AXScrollBar"));
//			List<WebElement> scrollButtons = scrollBar.findElements(By
//					.xpath("//AXButton"));
//			for (WebElement scrollButton : scrollButtons) {
//				String subrole = scrollButton.getAttribute("AXSubrole");
//				if (subrole.equals("AXDecrementPage")) {
//					conversationDecrementSB = scrollButton;
//				}
//			}
//			while (soundcloudPosition.y() < textInputPosition.y()) {
//				conversationDecrementSB.click();
//				soundcloudPosition = NSPoint.fromString(soundCloudLinkButton
//						.getAttribute("AXPosition"));
//			}
//		}
//	}
	
	public void scroll(WebElement fromElement, WebElement toElement){
		
		NSPoint fromElementPosition = NSPoint.fromString(fromElement.getAttribute(ELEMENT_ATT_POSITION));
    	NSPoint fromElementSize = NSPoint.fromString(fromElement.getAttribute(ELEMENT_ATT_SIZE));
    	
    	NSPoint latestPoint =
    			new NSPoint(fromElementPosition.x() + fromElementSize.x(), fromElementPosition.y() + fromElementSize.y());

    	//get scrollbar for contact list
    	WebElement decrementScrollBar = null;
    	WebElement incrementScrollBar = null;

    	WebElement scrollArea = driver.findElement(By.xpath(OSXLocators.xpathConversationListScrollArea));
    	boolean isFoundPeople = false;

        NSPoint toElementPosition = NSPoint.fromString(toElement.getAttribute(ELEMENT_ATT_POSITION));
        if (toElementPosition.y() > latestPoint.y() || toElementPosition.y() < fromElementPosition.y()) {
        	if(isFoundPeople){
    			WebElement scrollBar = scrollArea.findElement(By.xpath("//AXScrollBar"));
    			List<WebElement> scrollButtons = scrollBar.findElements(By.xpath("//AXButton"));
    			for (WebElement scrollButton: scrollButtons) {
    				String subrole = scrollButton.getAttribute("AXSubrole");
    				if (subrole.equals(DECREMENT_PAGE)) {
    					decrementScrollBar = scrollButton;
    				}
    				if (subrole.equals(INCREMENT_PAGE)) {
    					incrementScrollBar = scrollButton;
    				}
    			}
        	}
        	
        	while (toElementPosition.y() > latestPoint.y()) {
        		incrementScrollBar.click();
        		toElementPosition = NSPoint.fromString(toElement.getAttribute(ELEMENT_ATT_POSITION));
            }
            while (toElementPosition.y() < fromElementPosition.y()) {
            	decrementScrollBar.click();
            	toElementPosition = NSPoint.fromString(toElement.getAttribute(ELEMENT_ATT_POSITION));
            }
        }
		
	}
}