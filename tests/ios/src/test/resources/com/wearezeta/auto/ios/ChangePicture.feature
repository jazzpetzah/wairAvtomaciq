Feature: Change Profile Picture
@torun
Scenario Outline: Change your profile picture  
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on my name <Name>
	And I swipe left to personal screen
	And I tap on personal screen
	And I press Camera button
	And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
	
	
	
	
	Examples:
    |	Login							|	Password	|	Name			|
    |	piotr.iazadji@wearezeta.com		|	asdfer123	|	Piotr Iazadji	|