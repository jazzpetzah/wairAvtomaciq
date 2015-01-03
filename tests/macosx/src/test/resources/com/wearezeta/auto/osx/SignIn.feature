Feature: Sign In

  @smoke @id690
  Scenario Outline: Sign in ZClient
    Given There is 1 user where <Name> is me
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start Sign In
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Sign In button
    Then I see my name <Name> in Contact list

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  #Not supported functionality - Sign Out
  @regression @id525
  Scenario Outline: Change Sign in user
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login2> and password <Password2>
    And I see my name <Name2> in Contact list
    And I go to user <Name2> profile
    And I open picture settings
    And I choose to select picture from image file
    And I select image file userpicture_portrait.jpg
    And I see photo in User profile
    When I am signed out from ZClient
    And I Sign in using login <Login> and password <Password>
    Then I see my name <Name> in Contact list
    And I see name <Name> in User profile
    And I see email of <Name> in User profile
    And I see changed user picture

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |
