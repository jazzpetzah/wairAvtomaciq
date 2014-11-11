
Feature: Sign In

  @smoke
  @id340
  Scenario Outline: Sign in to ZClient
	Given I see sign in screen
	When I press Sign in button
	And I have entered login <Login>
	And I have entered password <Password>
	And I press Login button
	Then I see Contact list with my name <Name>
	
  Examples:
    |	Login					|	Password	|	Name			|
    |	aqaUser            		|	aqaPassword	|	aqaUser 		|
    
   #Known issue is IOS-989, once it is fixed test should be updated
   @staging
   @id524
  Scenario Outline: I can change sign in user on iOS
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <UserA>
	And I tap on my name <UserA>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
	And I Sign in using login <UserB> and password <Password>
	Then I see Personal page
	And I see name <UserB> on Personal page
	And I see email <UserB> on Personal page
	
  Examples:
    |	Login					|	Password	|	UserA			| UserB			|
    |	aqaUser            		|	aqaPassword	|	aqaUser 		| aqaContact1	|    
 
@regression
@id300 
 Scenario Outline: Open and close the terms of service on the welcome screen
  	Given I see sign in screen
  	And I press Terms of Service link
  	Then I see the terms info page
  	And I return to welcome page
  	
  	Examples: 
    |	Login	|	Password	|	Name	|	Contact		|
    |	aqaUser	|	aqaPassword	|	aqaUser	|	aqaContact1	|

#Muted due to missing notification support from sync engine    
@mute
@regression
@id314
 Scenario Outline: Notification if SignIn credentials are wrong
 	Given I see sign in screen
 	When I press Sign in button
 	And I enter wrong email <WrongMail>
 	Then I see error with email notification
 	And I have entered login <Login>
 	Then I see no error notification
 	And I enter wrong password <WrongPassword>
 	And I press Login button
 	Then I see wrong credentials notification
 	And I have entered password <Password>
 	And I press Login button
 	And I see Contact list with my name <Name>
 	
 	Examples: 
    |	Login	|	Password	|	Name	|	WrongMail		| WrongPassword |
    |	aqaUser	|	aqaPassword	|	aqaUser	|	wrongwrong	    | wrong			|
