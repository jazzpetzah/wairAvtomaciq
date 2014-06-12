
Feature: Sign Out

  Scenario Outline: Sign out from ZClient 
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on my name <Name>
	And I swipe left to personal screen
	And I swipe up for options
	And I press options button <OptionsButton>
	Then I see login in screen
		
	Examples:
    |	Login							|	Password	|	Name			|	OptionsButton	|
    |	piotr.iazadji@wearezeta.com		|	asdfer123	|	Piotr Iazadji	|	Sign out		|