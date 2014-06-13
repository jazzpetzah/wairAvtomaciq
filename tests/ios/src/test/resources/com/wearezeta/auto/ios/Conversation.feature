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
    |	Login						|	Password	|	Name			|	Contact		|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	|


    Scenario Outline: Say Hello to user from contact list
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
    	When I tap on name <Contact>
    	And I see dialog page
    	And I multi tap on text input on dialog screen
    	Then I see Hello message in the dialog
    	
	Examples: 
    |	Login						|	Password	|	Name			|	Contact		|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	|
    
 
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
	   	And I see group chat page with users <Contact1> <Contact2>
	   	And I tap on text input
    	And I type the message
    	And I press send
    	Then I see my message in the dialog
	   	
	   	
	Examples: 
    |	Login						|	Password	|	Name			|	Contact1	|	Contact2	|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	| 	Piotr.mqa3	| 

