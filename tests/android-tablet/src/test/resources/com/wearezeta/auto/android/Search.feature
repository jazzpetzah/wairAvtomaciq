Feature: Search

  @id2249 @staging
  Scenario Outline: Open/Close People picker in  landscape mode
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I swipe down on tablet contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2263 @staging
  Scenario Outline: Open/Close People picker in  portrait mode
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I swipe down on tablet contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
