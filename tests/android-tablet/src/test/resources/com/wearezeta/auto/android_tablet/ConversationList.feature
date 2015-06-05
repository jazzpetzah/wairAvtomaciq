Feature: Conversation List

  @id2246 @smoke
  Scenario Outline: Mute conversation in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    When I select <ItemSilence> menu item on Single user popover
    Then I see <ItemNotify> menu item on Single user popover
    And I tap Show Details button on conversation view page
    And I do not see the Single user popover
    And I see the conversation <Contact> in my conversations list is silenced

    Examples: 
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | SILENCE     | NOTIFY     |

  @id2260 @staging
  Scenario Outline: Mute conversation in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    Given I see Contact list
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
