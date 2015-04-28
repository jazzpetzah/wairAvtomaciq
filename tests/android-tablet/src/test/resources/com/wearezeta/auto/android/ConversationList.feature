Feature: Conversation List

  @id2246 @staging
  Scenario Outline: Mute conversation in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    And I rotate UI to landscape
     Given I Sign in on tablet using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I tap on tablet contact name <Contact1>
    And I see tablet dialog page
    And I tap on profile button
    And I press options menu button
    And I press Silence conversation button
    Then Contact <Contact1> is muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id2260 @staging
  Scenario Outline: Mute conversation in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I tap on tablet contact name <Contact1>
    And I see tablet dialog page
    And I tap on profile button
    And I press options menu button
    And I press Silence conversation button
    And I return to group chat page
    And I navigate back from dialog page
    Then Contact <Contact1> is muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |
