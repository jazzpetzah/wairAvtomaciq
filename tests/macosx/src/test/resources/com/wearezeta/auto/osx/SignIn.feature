Feature: Sign In

  @smoke @id690
  Scenario Outline: Sign in ZClient
    Given I have 1 users and 0 contacts for 0 users
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start Sign In
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Sign In button
    Then I see my name <Name> in Contact list

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  #Not supported functionality - Sign Out
  @regression @id525
  Scenario Outline: Change Sign in user
    Given I have 2 users and 0 contacts for 0 users
    Given I Sign in using login <Login2> and password <Password>
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
      | Login   | Login2   | Password    | Name    | Name2    |
      | aqaUser | yourUser | aqaPassword | aqaUser | yourUser |
