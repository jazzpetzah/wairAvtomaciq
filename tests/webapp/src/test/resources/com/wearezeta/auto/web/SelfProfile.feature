Feature: Self Profile

  @smoke @id1743
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open self profile
    And I see user name on self profile page <Name>
    And I change username to <NewName>
    Then I see user name on self profile page <NewName>
    And I see my name on top of Contact list

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewUserName | user2Name |

  @smoke @id1753
  Scenario Outline: Verify correct accent color showing after sign out and sign in
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open self profile
    And I set my accent color to <ColorName>
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open self profile
    Then I verify my accent color in color picker is set to <ColorName> color
    Then I verify my name color is the same as in color picker

    Examples: 
      | Login      | Password      | Name      | ColorName    |
      | user1Email | user1Password | user1Name | BrightOrange |