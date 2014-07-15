Feature: Change Profile Picture

#need to add a delay before "I see changed user picture userpicture_mobile_check.jpg" step
@mute
@smoke
Scenario Outline: Change your profile picture  
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with my name <Name>
	When I tap on my name <Name>
	#And I swipe left to personal screen
	And I tap on personal screen
	And I press Camera button
	And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I return to personal page
    Then I see changed user picture userpicture_mobile_check.jpg
	
	
	
	
	Examples:
    |	Login		|	Password	|	Name	|
    |	aqaUser		|	aqaPassword	|	aqaUser	|