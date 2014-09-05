Feature: Conversation

#Muted till new sync engine client stabilization
@mute
@smoke
@id330
Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message 
    And I send the message
    Then I see my message in the dialog

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|


# Muted due to the bug https://wearezeta.atlassian.net/browse/IOS-947
@mute
@smoke
@id331
Scenario Outline: Send Hello to contact
		Given I Sign in using login <Login> and password <Password> 
    	And I see Contact list with my name <Name>
    	When I tap on contact name <Contact>
    	And I see dialog page
    	And I multi tap on text input
    	Then I see Hello message in the dialog
    	And I multi tap on text input
    	Then I see Hey message in the dialog
    	
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
    


#Muted till new sync engine client stabilization
@mute
@smoke 
@id460
Scenario Outline: Send a camera roll picture to user from contact list
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on contact name <Contact>
	And I see dialog page
	And I swipe the text input cursor
 	And I press Add Picture button
 	And I press Camera Roll button
 	And I choose a picture from camera roll
 	And I press Confirm button
 	Then I see new photo in the dialog

Examples: 
	|	Login		|	Password		|	Name		|	Contact			|
	|	aqaUser		|	aqaPassword		|	aqaUser		|	aqaContact1		|


  #Muted till new sync engine client stabilization
  @mute
  @smoke
  @id338
 Scenario Outline: Mute conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is not muted
Examples:
    |  Login		| Password		| Name			| Contact1    |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 |





@staging 
@id526  
  Scenario Outline: I can send and play inline youtube link
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    Then I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		| YouTubeLink	|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	| http://www.youtube.com/watch?v=Bb1RhktcugU |


#Muted due relogin issue
@mute  
@staging
@id614
   Scenario Outline: I am able to play inline YouTube link poster by others
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    And I tap on dialog window
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I see sign in screen
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I tap on contact name <Name>
    Then I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		| YouTubeLink	|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	| http://www.youtube.com/watch?v=Bb1RhktcugU |  


#muted due to issue IOS-959
@mute  
@staging
@id383
Scenario Outline: Play/pause SoundCloud media link from the media bar
	Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see playing media is paused
    And I press play in media bar
    Then I see media is playing
    And I stop media in media bar
    Then The media stopps playing
       
Examples:
    |  Login		| Password		| Name			| Contact1    | SoundCloudLink |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 | https://soundcloud.com/klinke-auf-cinch/04-whats-happening-boytalk-remix |
    
    
#muted due to defact IOS-985, still needs checking of mediabar and scrolling on simulator
@mute      
@staging
@id384
Scenario Outline: Conversation gets scrolled back to playing media when clicking on media bar
	Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>
    
Examples:
    |  Login		| Password		| Name			| Contact1    | SoundCloudLink |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 | https://soundcloud.com/klinke-auf-cinch/04-whats-happening-boytalk-remix |
    

@staging
@id488
Scenario Outline: Verify you can see conversation images in fullscreen
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on contact name <Contact1>
	And I see dialog page
	And I swipe the text input cursor
 	And I press Add Picture button
 	And I press Camera Roll button
 	And I choose a picture from camera roll
 	And I press Confirm button
 	And I see new photo in the dialog
 	And I memorize message send time
 	And I tap and hold image to open full screen
 	And I see Full Screen Page opened
 	And I see sender first name <Name> on fullscreen page
 	And I see send date on fullscreen page
 	And I see download button shown on fullscreen page
 	And I tap on fullscreen page
 	And I verify image caption and download button are not shown
 	And I tap on fullscreen page
 	And I tap close fullscreen page button
 	Then I see new photo in the dialog
 	   
Examples:    
    |  Login		| Password		| Name			| Contact1   |
 	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 |


#muted due to issue IOS-959
@mute
@staging
@id504
  Scenario Outline: Verify you can play/pause media from the Media Bar (YouTube)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <YouTubeLink>
    And I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened
    And I tap Pause button on Video player page
    And I tap on Done button on Video player page
    And I scroll media out of sight until media bar appears
    And I press play in media bar
    And I see video player page is opened
    And I tap on Done button on Video player page
    And I stop media in media bar
    Then The media stopps playing

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		| YouTubeLink	|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	| http://www.youtube.com/watch?v=Bb1RhktcugU |
 
    
#muted due to issue IOS-959
@mute   
@staging
@id387
Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
	Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I swipe right on Dialog page
    And I see play/pause button next to username <Contact1> in contact list
    And I tap on contact name <Contact2>
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I swipe right on Dialog page
    And I dont see play/pause button next to username <Contact1> in contact list
    And I see play/pause button next to username <Contact2> in contact list
    And I tap on play/pause button in contact list
    And I tap on contact name <Contact2>
    And I scroll media out of sight until media bar appears
    Then I see playing media is paused
    
Examples:
    |  Login		| Password		| Name			| Contact1    | Contact2    |SoundCloudLink |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 | aqaContact2 |https://soundcloud.com/klinke-auf-cinch/04-whats-happening-boytalk-remix |


#muted due to issue IOS-959
@mute    
@staging
@id386   
Scenario Outline: Verify the Media Bar disappears when playing media is back in view (SoundCloud)
	Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I scroll back to media container
    Then I dont see media bar on dialog page
    
Examples:
    |  Login		| Password		| Name			| Contact1    | SoundCloudLink |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1 | https://soundcloud.com/klinke-auf-cinch/04-whats-happening-boytalk-remix |
    
    
#muted due to issue IOS-959
@mute
@staging
@id385
  Scenario Outline: Verify the Media Bar dissapears after playback finishes (SoundCloud)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I see media bar on dialog page
    And I wait <time> seconds for media to stop playing
    Then I dont see media bar on dialog page

	Examples: 
    |	Login	|	Password	|	Name	|	Contact1		| SoundCloudLink 								| time |
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	| https://soundcloud.com/carl-cox/carl-cox-nexus| 28   | 

 @staging 
 @id415
  Scenario Outline: Send Message to contact after navigating away from chat page
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I scroll away the keyboard
    And I navigate back to conversations view
    Then I tap on contact name <Contact>
    And I send the message
    Then I see my message in the dialog

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaAvatar	|

   
 @staging 
 @id407
  Scenario Outline: Send more than 200 chars message
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
	And I input more than 200 chars message and send it
    And I see my message in the dialog
	And I swipe right on Dialog page
    And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	And I Sign in using login <Contact> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
    And I tap on contact name <Name>
    Then I see my message in the dialog
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
    
        
 @staging 
 @id414
  Scenario Outline: Send a text containing spaces
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
	And I try to send message with only spaces
    And I see message with only spaces is not send
	And I input message with leading empty spaces
    And I send the message
	And I see my message in the dialog
	And I input message with trailing emtpy spaces
	And I send the message
	Then I see my message in the dialog
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
  
    
 @staging 
 @id408 
  Scenario Outline: Send one line message with lower case and upper case
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
	And I input message with lower case and upper case
	And I send the message
	Then I see my message in the dialog
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
    
   
 @staging 
 @id416
  Scenario Outline: Keyboard up and navigate to main convo list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
	And I tap on text input
	And I see keyboard
	And I scroll away the keyboard
	And I dont see keyboard
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
    

 @staging 
 @id413
  Scenario Outline: Copy and paste to send the message
    Given I see sign in screen
	When I press Sign in button
	And I have entered login <text>
	And I tap and hold on Email input
	And I click on popup SelectAll item
	And I click on popup Copy item
	And I have entered login <Login>
	And I have entered password <Password>
	And I press Login button
	And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I tap and hold on message input
    And I click on popup Paste item
	And I send the message
	Then I see last message in dialog is expected message <text>
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact		| text 		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	| TextToCopy|

    
 @staging   
 @id394
 Scenario Outline: Tap the cursor to get to the end of the conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I scroll to the beginning of the conversation
    And I tap on text input
    Then I see last message in the dialog
    
    Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|
