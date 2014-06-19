Feature: User Profile

Scenario Outline: Change user picture from image file
    Given I Sign in using login <Login> and password <Password>
    And I go to user <Name> profile
    When I open picture settings
	And I choose to select picture from image file
	And I select image file userpicture.jpg
#	Then I see changed user picture

Examples:
	|	Login			|	Password		|	Name		|
	|	aqaUser			|	aqaPassword		|	aqaUser		|

Scenario Outline: Change user picture from camera
    Given I Sign in using login <Login> and password <Password>
    And I go to user <Name> profile
    And I open picture settings
	When I choose to select picture from camera
	And I shoot picture using camera
#	Then I see changed user picture

Examples:
	|	Login			|	Password		|	Name		|
	|	aqaUser			|	aqaPassword		|	aqaUser		|
