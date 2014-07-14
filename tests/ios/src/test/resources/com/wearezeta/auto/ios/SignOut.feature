
Feature: Sign Out

  @smoke
  Scenario Outline: Sign out from ZClient 
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on my name <Name>
	And I swipe up for options
	And I press options button <OptionsButton>
	Then I see login in screen
		
	Examples:
    |	Login		|	Password	|	Name			|	OptionsButton	|
    |	aqaUser   	|	aqaPassword	|	aqaUser      	|	Sign out		|