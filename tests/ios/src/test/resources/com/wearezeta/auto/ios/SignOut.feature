

Feature: Sign Out

   @smoke
  @id343
  Scenario Outline: Sign out from ZClient 
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
    And I tap on my name <Name>
	And I click on Settings button on personal page
	And I click Sign out button from personal page
		
	Examples:
    |	Login		|	Password	|	Name			|
    |	aqaUser   	|	aqaPassword	|	aqaUser      	|