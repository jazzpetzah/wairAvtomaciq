Feature: Register new user

@smoke
@regression
Scenario Outline: Register new user using front camera
	Given I am signed out from ZClient
	And I see Sign In screen
	When I start registration
	And I choose register using camera
	And I take registration picture from camera
	And I enter name <Name>
	And I enter email <Email>
	And I enter password <Password>
	And I submit registration data
	Then I see confirmation page 
	And I verify registration address
	And I see contact list of registered user
	
Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|

@smoke
@regression
Scenario Outline: Register new user with image
	Given I am signed out from ZClient
	And I see Sign In screen
	When I start registration
	And I choose register with image
	And I take registration picture from image file <ImageFile>
	And I enter name <Name>
	And I enter email <Email>
	And I enter password <Password>
	And I submit registration data
	Then I see confirmation page 
	And I verify registration address
	And I see contact list of registered user
	
Examples:     
    |	Email						|	Password	    |	Name			|	ImageFile					|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|	userpicture_landscape.jpg	|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|	userpicture_portrait.jpg	|

@smoke
@regression
  Scenario Outline: Do not accept email with spaces
	Given I am signed out from ZClient
	And I see Sign In screen
	When I start registration
	And I choose register using camera
	And I take registration picture from camera
	And I enter email <Email>
	Then I see email <Email> without spaces

    Examples:     
    |	Email										    |
    |	email with spaces@weare zeta.com           	    |

@smoke
@regression
Scenario: Fail registration on incorrect email
	Given I am signed out from ZClient
	And I see Sign In screen
	When I start registration
	And I choose register using camera
	And I take registration picture from camera
	And I enter invalid emails
	Then I see that all emails not accepted