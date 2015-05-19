Feature: Sign Out

  @smoke @id1790
  Scenario Outline: Change sign-in user
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my avatar on top of Contact list
    And I open self profile
    And I see user name on self profile page <Name2>
    And I see user email on self profile page <Login2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |
