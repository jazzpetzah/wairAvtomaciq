Feature: Sign In

  @smoke @id690
  Scenario Outline: Sign in ZClient
    Given There is 1 user where <Name> is me
    And I see Welcome screen
    When I start Sign In
    And I type login <Login>
    And I type password <Password>
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
    When I sign out
    And I Sign in using login <Login> and password <Password>
    Then I see my name <Name> in Contact list
    And I go to user <Name> profile
    And I see name <Name> in User profile
    And I see email of <Name> in User profile
    And I open picture settings
    And I see changed user picture

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |

  @regression @id1120 @torun
  Scenario Outline: Verify I see wrong address or password message
    And I see Welcome screen
    When I start Sign In
    And I type login <Login>
    And I type password <Password>
    And I press Sign In button
    Then I see wrong credentials message
    When I type login <Login2>
    Then I do not see wrong credentials message
    When I press Sign In button
    And I see wrong credentials message
    And I type password <Password2> by AppleScript
    Then I do not see wrong credentials message

    Examples: 
      | Login | Password | Login2 | Password2 |
      | aaa   | aaa      | aaa2   | aaa2      |

  @regression @id1116
  Scenario Outline: Verify Sign In progress behaviour while there are probelms with internet connectivity
    Given There is 1 user where <Name> is me
    Given Internet connection is lost
    And I see Welcome screen
    When I start Sign In
    And I type login <Login>
    And I type password <Password>
    And I sign in expecting No Internet message
    Then I see internet connectivity error message
    When Internet connection is restored
    And I press Sign In button
    Then I see my name <Name> in Contact list

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
