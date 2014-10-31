Feature: Group Chat

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
	   	And I tap on connected user <Contact2> on People picker page
	   	And I see Add to conversation button
	   	And I click on Go button
	   	Then I see group chat page with users <Contact1> <Contact2>

	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

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
	And I tap on connected user <Contact3> on People picker page
	And I see Add to conversation button
    And I click on Go button
	Then I see that conversation has <Number> people
	And I see <Number> participants avatars
    When I exit the group info page
    And I can see <Name> Added <Contact3>
	
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		| Contact3		| Number  |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| aqaContact3	| 4		  |


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
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact2	| aqaContact3	|

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


#Muted till new sync engine client stabilization
@mute
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
    
    
#fails to check email of first user due to defect IOS-990
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

#Mute due to SE issue, MEC-270, not possible to create group from 1:1           
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

#Mute due to SE issue, MEC-270, not possible to create group from 1:1      
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
    
    
#Known issue ZIOS-1711. Muted test due to crash after relogin.
@staging
@id597 @mute
Scenario Outline: Verify the new conversation is created on the other end (1:1 source)
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
	And I see in contact list group chat named <ChatName>
	And I tap on my name <Contact1>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	And I Sign in using login <Contact2> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	Then I see in contact list group chat named <ChatName>
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		| ChatName   |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| QAtestChat |

    
#Muted due to app quit on logout workaround
@staging
@id602 @mute
Scenario Outline: Verify new users are added to a group conversation on the other end
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
	And I send predefined message <message>
	And I see message in group chat <message>
	And I swipe down on group chat page
	And I swipe up on group chat page in simulator
	And I change conversation name to <ChatName>
	And I add to existing group chat contact <Contact3>
	And I swipe down on group chat info page
	And I swipe right on Dialog page
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact3> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I see message in group chat <message>
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		| Contact3   | ChatName		| message 		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| aqaContact3| QAtestChat 	| Test Message  |
    

#Muted due to app quit on logout workaround
@staging
@id608 @mute
Scenario Outline: Verify you can see image, which was sent into a group conversation, on the second end
	Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	And I tap on group chat with name <ChatName>
	And I swipe the text input cursor
 	And I press Add Picture button
 	And I press Camera Roll button
 	And I choose a picture from camera roll
 	And I press Confirm button
 	And I see new photo in the dialog
 	And I verify image in dialog is same as template <Picture>
	And I swipe right on Dialog page
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact1> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I scroll to image in dialog
	And I verify image in dialog is same as template <Picture>
	And I swipe right on Dialog page
	And I tap on my name <Contact1>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	And I Sign in using login <Contact2> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I scroll to image in dialog
	And I verify image in dialog is same as template <Picture>
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|  ChatName		| Picture                   |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|  QAtestChat 	| userpicture_landscape.jpg |
    
#Muted due to app quit on logout workaround 
@staging
@id606 @mute
Scenario Outline: Verify you can see text message, which was sent into a group conversation, on the second end
	Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	And I tap on group chat with name <ChatName>
	And I send predefined message <message>
	And I see message in group chat <message>
	And I swipe right on Dialog page
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact1> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I see message in group chat <message>
	And I swipe right on Dialog page
	And I tap on my name <Contact1>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact2> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I see message in group chat <message>
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|  ChatName		| message 		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| QAtestChat 	| Test Message  |

    
#Muted due to app quit on logout workaround
@staging
@id607 @mute
Scenario Outline: Verify you can see multimedia message, which was sent into a group conversation, on the second end
	Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	And I tap on group chat with name <ChatName>
	And I post media link <YouTubeLink>
    And I tap on dialog window
	And I swipe right on Dialog page
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact1> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I see media link <YouTubeLink> and media in dialog
	And I click video container for the first time
    And I see video player page is opened
    And I tap on Done button on Video player page
	And I swipe right on Dialog page
	And I tap on my name <Contact1>
	And I click on Settings button on personal page
	And I click Sign out button from personal page	
	And I Sign in using login <Contact2> and password <Password>
	And I see Personal page
	And I swipe right on the personal page
	And I see in contact list group chat named <ChatName>
	And I tap on group chat with name <ChatName>
	And I see media link <YouTubeLink> and media in dialog
	And I click video container for the first time
    And I see video player page is opened
    And I tap on Done button on Video player page
	
	Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|  ChatName		| YouTubeLink								 |
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| QAtestChat 	| http://www.youtube.com/watch?v=Bb1RhktcugU |  
    

#Muted due to sync engine troubles(group chat is not created and app is closed after logout)
@mute
@smoke
@id600
Scenario Outline: Verify the new conversation is created on the other end (Search UI source)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I click clear button
    And I swipe down contact list
    And I see top people list on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I tap on connected user <Contact2> on People picker page
    And I see Create Conversation button on People picker page
    And I click Create Conversation button  on People picker page
    And I see group chat page with users <Contact1> <Contact2>
    And I swipe right on group chat page
    And I tap on my name <Name>
    And I see Personal page
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact1> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see <Contact1> and <Contact2> chat in contact list
    And I tap on my name <Name>
    And I see Personal page
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact2> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see <Contact1> and <Contact2> chat in contact list
    
	Examples: 
    |	Login	|	Password	|	Name	|	Contact1		| Contact2 		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1		| aqaContact2	|