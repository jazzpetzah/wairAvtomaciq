Feature: Conversation

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
  @id333
	Scenario Outline: Start group chat with users from contact list
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
	   	When I tap on contact name <Contact1>
    	And I see dialog page
	   	And I swipe up on dialog page to open other user personal page
	   	And I see <Contact1> user profile page
	   	And I press Add button
	   	And I see People picker page
	   	And I input in People picker search field user name <Contact2>
	   	And I see user <Contact2> found on People picker page
	   	And I tap on user name found on People picker page <Contact2>
	   	And I see Add to conversation button
	   	And I click on Add to conversation button
	   	Then I see group chat page with users <Contact1> <Contact2>

	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

  #Muted till new sync engine client stabilization
  @mute
  @smoke 
  @id334
Scenario Outline: Send message to group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I type the message
	And I send the message
	Then I see my message in the dialog
	
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

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
@regression
@id489
Scenario Outline: Add user to a group conversation
	Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I return to the chat list
	And I see <Contact1> and <Contact2> chat in contact list
	And I tap on a group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	And I press Add button
	And I see People picker page
	And I input in People picker search field user name <Contact3>
	And I see user <Contact3> found on People picker page
	And I tap on user name found on People picker page <Contact3>
	And I see Add to conversation button
    And I click on Add to conversation button
	Then I see that conversation has <Number> people
	And I see <Number> participants avatars
    When I exit the group info page
    And I can see <Name> Added <Contact3>
	
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		| Contact3		| Number  |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| aqaContact3	| 4		  |

  # when you leave group chat - it disappears from contact list
  @mute
  @smoke
  @id335
Scenario Outline: Leave from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
	And I press leave converstation button 
	And I see leave conversation alert 
	Then I press leave
	And I open archived conversations
	And I see <Contact1> and <Contact2> chat in contact list
	And I tap on a group chat with <Contact1> and <Contact2>
	And I can see <Name> Have Left

Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

#Not stable
  @mute
  @smoke 
  @id336
 Scenario Outline: Remove from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	And I select contact <Contact2>
#	And I swipe up on other user profile page
	And I click Remove
	And I see warning message 
	And I confirm remove
	Then I see that <Contact2> is not present on group chat page

Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

  @smoke 
  @id404
Scenario Outline: I can edit the conversation name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	And I change the conversation name
	Then I see that the conversation name is correct with <Contact1> and <Contact2>
	And I exit the group info page
	And I see the new conversation name displayed in in conversation
	And I return to the chat list
	And I see the group conversation name changed in the chat list	
		
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact2	| aqaContact1	|

   #Muted till new sync engine client stabilization
   @mute
   @regression
   @id531
Scenario Outline: I can see the individual user profile if I select someone in participants view
	 Given I Sign in using login <Login> and password <Password>
     And I see Contact list with my name <Name>
     When I create group chat with <Contact1> and <Contact2>
     And I swipe up on group chat page
     And I select contact <Contact2>
     Then I see the user profile from <Contact2>
     
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

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

#Muted till new sync engine client stabilization
@mute
@regression
@id392
Scenario Outline: Verify correct group info page information
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	Then I see that the conversation name is correct with <Contact1> and <Contact2>
	And I see the correct number of participants in the title <ParticipantNumber>
	And I see the correct participant avatars
Examples:
    |  Login		| Password		| Name			| Contact1		        | Contact2	     	    | ParticipantNumber |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaPictureContact	    | aqaAvatar TestContact	| 		 3			|


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


#crash after relogin due to defect IOS-959
@mute   
@staging
@id169  
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

#fails to check email of first user due to defect IOS-990
@mute
@staging
@id395
Scenario Outline: Tap on participant profiles in group info page participant view
    Given I Sign in using login <Login> and password <Password>
    Given I have group chat named <GroupChatName> with an unconnected user, made by <GroupCreator>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
	And I swipe up on group chat page
	And I tap on all of the participants and check their emails and names
	
Examples:
    |  Login		| Password		| Name			| GroupCreator	        |  GroupChatName    |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaPictureContact	    |     TESTCHAT		|
    

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
@id393
Scenario Outline: Verify you can start 1:1 conversation from a group conversation profile
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	And I select contact <Contact1>
	And I tap on start dialog button on other user profile page
	And I type the message and send it
	Then I see my message in the dialog
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
      
@staging
@id393
Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I swipe up on group chat page
	And I change conversation name to <ChatName>
	And I swipe down on group chat info page
	And I swipe right on Dialog page
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	And I Sign in using login <Contact1> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I tap on group chat with name <ChatName>
	And I swipe up on group chat page	
	And I tap on not connected contact <Contact2>
	Then I see connect to <Contact2> dialog
	
	
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		| ChatName   |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| QAtestChat |
    
   
 @staging 
 @id407
  Scenario Outline: Send more than 200 chars message
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
	And I input 200 chars message and send it
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
