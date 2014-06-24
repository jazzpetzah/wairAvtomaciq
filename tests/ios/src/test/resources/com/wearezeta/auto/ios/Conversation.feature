Feature: Conversation

  Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message
    And I press send
    Then I see my message in the dialog

	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|

 @torun
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
    
    
	Scenario Outline: Start group chat with users from contact list
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
	   	When I tap on contact name <Contact1>
    	And I see dialog page
	   	And I swipe left on dialog page
	   	And I see <Contact1> user profile page
	   	And I swipe down other user profile page
	   	And I see People picker page
	   	And I input in People picker search field user name <Contact2>
	   	And I see user <Contact2> found on People picker page
	   	And I tap on user name found on People picker page <Contact2>
	   	And I see Add to conversation button
	   	And I click on Add to conversation button
	   	Then I see group chat page with users <Contact1> <Contact2>
	   	
	   	
	Examples: 
    |	Login		|	Password	|	Name	|	Contact1	|	Contact2	|
    |	aqaUser 	|	aqaPassword	|	aqaUser	|	aqaContact1	| 	aqaContact2	| 

   
Scenario Outline: Send message to group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
   	And I tap on text input
	And I type the message
	And I press send
	Then I see my message in the dialog
		
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
    
@torun
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
 
Scenario Outline: Leave from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
	When I create group chat with <Contact1> and <Contact2>
   	And I swipe left on group chat page
	And I swipe up on group chat info page
	And I press leave converstation button 
	And I see leave conversation alert 
	Then I press leave
	And I open archived conversations
	And I see <Contact1> and <Contact2> chat in contact list
	And I tap on a group chat with <Contact1> and <Contact2>
	And I can see You Have Left
		
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
    

