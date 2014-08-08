

Feature: Sign In

  @smoke
  @id340
  Scenario Outline: Sign in to ZClient
	Given I see sign in screen
	When I press Sign in button
	And I have entered login <Login>
	And I have entered password <Password>
	And I press Login button
	Then Contact list appears with my name <Name>
	
  Examples:
    |	Login					|	Password	|	Name			|
    |	aqaUser            		|	aqaPassword	|	aqaUser 		|
  
   #Bug IOS-959-Zclient crashes after re-login  
   @id524
   @staging
  Scenario Outline: I can log out of one account and into another and the information switches properly
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	Then I see sign in screen
    And I Sign in using login <NewLogin> and password <NewPassword>
	Then I see Contact list with my name <NewName>
	And I see contact list loaded with User name <Contact>
	Then I tap on my name <NewName>
	And I see <NewName> user profile page
	
  Examples:
    |	Login					|	Password	|	Name			|   NewLogin                                |  NewPassword   |      NewName          |  Contact           |
    |	aqaUser            		|	aqaPassword	|	aqaUser 		| smoketester+changeusertest@wearezeta.com  |   changeuser   |   aqaChangeUserTest   |  Chad Bettencourt  |
