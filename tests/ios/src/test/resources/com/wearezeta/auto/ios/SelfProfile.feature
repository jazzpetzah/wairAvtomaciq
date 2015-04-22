Feature: Self Profile

  @smoke @id344 @deployPictures
  Scenario Outline: Change your profile picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples:
      | Login      | Password      | Name      | Picture                      | Contact   |
      | user1Email | user1Password | user1Name | userpicture_mobile_check.jpg | user2Name |

  @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1056
  Scenario Outline: Attempt to enter a name with 1 char
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Login      | Password      | Name      | username | Contact   |
      | user1Email | user1Password | user1Name | c        | user2Name |
      
  @regression @id1463
  Scenario Outline: Verify name change
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
	And I change name <Name> to <NewUsername>
	And I swipe right on the personal page
	And I see Contact list with my name <NewUsername>
	And I tap on my name <NewUsername>
	Then I see my new name <NewUsername>

    Examples: 
      | Login      | Password      | Name      | NewUsername        | Contact   |
      | user1Email | user1Password | user1Name | New Name           | user2Name |
      
  @staging @id667
  Scenario Outline: Verify changing and applying accent color
  	Given There are 1 users where <Name> is me
  	Given User <Name> change name to <NewName>
  	Given User <Name> change accent color to <Color>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I change my accent color via the colorpicker
    And I swipe right on the personal page
    Then I see my names <Name> accent color is changed
   
    Examples: 
      | Login      | Password      | Name      | NewName           | Color  |
      | user1Email | user1Password | user1Name | AccentColorChange | Violet |
  
