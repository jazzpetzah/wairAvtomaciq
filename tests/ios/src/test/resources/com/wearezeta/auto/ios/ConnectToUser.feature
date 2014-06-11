Feature: Connect to User 
@torun  
	Scenario Outline: Send invitation message to a user
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
		When I swipe down contact list
		And I see People picker page
		And I tap on Search input on People picker page
		And I input in search field user name to connect to <Contact>
		And I see user <Contact> found on People picker page
		And I tap on user name <Contact> on People picker page
		And I see connect to <Contact> dialog
		And I input message in connect dialog
		And I tap on connect dialog Send button
		And I see contact list loaded with User name <Contact> first in list
		And I tap on name <Contact>
		And I see Pending Connect dialog
		
	Examples:     
    |	Login						|	Password	|	Name			|	Contact		|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	|