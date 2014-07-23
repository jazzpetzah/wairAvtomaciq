Feature: Register new user

@mute
@smoke
  Scenario Outline: Register new user using front camera (Real Device)
	Given I see sign in screen
	When I press Join button
	And I dismiss Vignette overlay
	And I take photo by front camera
	And I See photo taken
	And I confirm selection
	And I input name <Name> and hit Enter
	And I input email <Email> and hit Enter
	And I input password <Password> and hit Enter
	Then I see confirmation page 
	And I verify registration address
	And I press continue registration
	And Contact list appears with my name <Name>

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser            	        |	aqaPassword	    |	aqaUser       	|


@smoke
@regression
  Scenario Outline: Attempt to register an email with spaces
    Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I attempt to enter an email with spaces <Email>
	Then I verify no spaces are present in email

    Examples:     
    |	Email				    |	Password	        |  Name			    |
    |	aqaUser           	    |	aqaPassword	        |  aqaUser      	|
  
  
  # Not stable    
  @mute
  @regression
  Scenario Outline: Attempt to register an email with incorrect format
    Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I attempt to enter emails with known incorrect formats
	Then I verify that the app does not let me continue

    Examples:     
    |  Name			    |
    |  aqaUser      	|
    

  @regression
  Scenario Outline: Conserve user input throughout registration
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I enter email <Email>
	And I enter password <Password>
	Then I navigate throughout the registration pages and see my input

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|
    
    
  @regression
  Scenario Outline: Can return to email page to change email if input incorrectly
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I enter an incorrect email <Incorrect>
	And I enter password <Password>
	And I submit registration data
	Then I see error page
	And I return to the email page from error page
	And I enter email <Correct>
	And I submit registration data
	And I verify registration address

    Examples:     
    |	Correct				    |	Password	        |	Name	        |   Incorrect           |
    |   aqaUser	                |	aqaPassword	        |	aqaUser         |   error@wearezeta.com |


  @regression
  Scenario Outline: Register new user using username with maximum characters allowed
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter a username which is at most <MaxChars> characters long from <Language> alphabet 
	Then I verify that my username is at most <MaxChars> characters long
	
	 Examples:     
    |	Email 					    |	Password	    |    MaxChars    |    Language   |
    |	aqaUser             	    |	aqaPassword	    |    72          |    English    |
    

@staging
  Scenario Outline: Register new user using photo album
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I enter email <Email>
	And I enter password <Password>
	And I submit registration data
	Then I see confirmation page 
	And I verify registration address
	And Contact list loads with only my name

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|


@staging
  Scenario Outline: Switch between vignette overlay and full color (Real Device)
	Given I see sign in screen
	When I press Join button
	And I dismiss Vignette overlay
	And I take photo by front camera
	And I confirm selection
	And I click Back button
	And I see Vignette overlay
	And I dismiss Vignette overlay
	And I don't see Vignette overlay
	And I see full color mode
	And I click close full color mode button
	Then I see Vignette overlay
	
	Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|
    

@staging
  Scenario Outline: Take or select a photo label validation
  	Given I see sign in screen
	When I press Join button
	Then I see Take or select photo label and smile
	
	Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|
    

@staging
  Scenario Outline: Take or select a photo label not visible when picture is selected
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I confirm selection
	And I click Back button
	Then I don't see Take or select photo label and smile
	
	Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|    
    

@staging
  Scenario Outline: Take or select a photo label not visible when picture is selected (Real Device)
	Given I see sign in screen
	When I press Join button
	And I press Camera button on Registration page
	And I click Vignette overlay
	And I take photo by front camera
	And I confirm selection
	And I click Back button
	Then I don't see Take or select photo label and smile
	
	Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|
 

@staging
  Scenario Outline: Next Button should not be visible on first registration step visit
  	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I don't see Next button
	And I input name <Name> and hit Enter
	And I don't see Next button
	And I input email <Email> and hit Enter
	And I don't see Next button
	And I input password <Password> and hit Enter
	And I don't see Next button
	Then I see confirmation page
	And I don't see Next button
	
	Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	| 
    

@staging
  Scenario Outline: Automatic email verification
  	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I input name <Name> and hit Enter
	And I input email <Email> and hit Enter
	And I input password <Password> and hit Enter
	And I see confirmation page 
	And I verify registration address
	Then Contact list loads with only my name	

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|


@staging
  Scenario Outline: Register new user using rear camera (Real Device)
	Given I see sign in screen
	When I press Join button
	And I dismiss Vignette overlay
	And I take photo by rear camera
	And I confirm selection
	And I input name <Name> and hit Enter
	And I input email <Email> and hit Enter
	And I input password <Password> and hit Enter
	And I see confirmation page 
	And I verify registration address
	And Contact list loads with only my name

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|
    

@staging
  Scenario Outline: Minimum 8 chars password requirement validation
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I enter name <Name>
	And I enter email <Email>
	And I enter password <Password>
	And I input user data
	Then I see Create Account button disabled

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	12345		    |	aqaUser       	|   


@staging
  Scenario Outline: Change selected image during registratrion (Real Device)
	Given I see sign in screen
	When I press Join button
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I reject selected picture
	And I see Take or select photo label and smile
	And I press Picture button
	And I choose photo from album
	And I See selected picture
	And I confirm selection
	And I click Back button
	And I see selected image set as background
	And I dismiss Vignette overlay
	And I take photo by front camera
	And I See photo taken
	And I confirm selection
	And I see photo set as background
	Then I see background image is replaced
	
 	Examples:	
	|	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|