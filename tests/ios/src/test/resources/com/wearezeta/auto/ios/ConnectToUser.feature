Feature: Connect to User 
   
    @smoke 
	Scenario Outline: Send invitation message to a user
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
		When I swipe down contact list
		And I see People picker page
		And I tap on Search input on People picker page
		And I input in People picker search field user name <Contact>
		And I see user <Contact> found on People picker page
		And I tap on user name found on People picker page <Contact>
		And I see connect to <Contact> dialog
		And I input message in connect to dialog
		Then I see contact list loaded with User name <Contact>
		And I tap on contact name <Contact>
		And I see Pending Connect to <Contact> message on Dialog page
		
	Examples:     
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	yourUser	|
    

    @smoke 
    Scenario Outline: Get invitation message from user
 		Given I have connection request from <Contact>
 		And I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
    	When I see connection request from <Contact>
    	And I confirm connection request
    	Then I see contact list loaded with User name <Contact>
		
	Examples:     
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	yourContact	|