Feature: Register new user

@smoke
@regression
  Scenario Outline: Register new user using front camera
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
	#And I press continue registration
	#And Contact list appears with my name <Name>

    Examples:     
    |	Email						|	Password	    |	Name			|
    |	aqaUser             	    |	aqaPassword	    |	aqaUser       	|

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

@no-smoke
@no-regression   
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
    
    
@torun
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
  
