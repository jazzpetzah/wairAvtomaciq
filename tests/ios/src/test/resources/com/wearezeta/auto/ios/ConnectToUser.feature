Feature: Connect to User 
@torun
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
		And I tap connect dialog Send button
		Then I see contact list loaded with User name <Contact> first in list
		And I tap on contact name <Contact>
		And I see Pending Connect to <Contact> message on Dialog page
		
	Examples:     
    |	Login								|	Password	|	Name		|	Contact		|
    |	piotr.iazadji+mqa4@wearezeta.com	|	asdfer123	|	Piotr.mqa4	|	aqa207		|