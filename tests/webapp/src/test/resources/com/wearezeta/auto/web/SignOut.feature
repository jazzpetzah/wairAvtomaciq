Feature: Sign Out

  @C1736 @regression @id1790
  Scenario Outline: Switch signed-in user
    Given There are 2 users where <Name> is me
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    And I see user name on self profile page <Name>
    And I see user email on self profile page <Login>
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    Then I see user name on self profile page <Name2>
    And I see user email on self profile page <Login2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |