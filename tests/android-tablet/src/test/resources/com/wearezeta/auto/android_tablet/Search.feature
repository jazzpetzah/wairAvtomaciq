Feature: Search

  @id2249 @regression
  Scenario Outline: Open/Close Search by different actions in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I hide keyboard
    And I navigate back
    Then I see the conversations list with conversation
    When I do long swipe down on conversations list
    And I see People Picker page
    And I hide keyboard
    And I do short swipe down on People Picker page
    Then I see People Picker page
    When I do long swipe down on People Picker page
    Then I see the conversations list with conversation
    And I do not see People Picker page
    When I do short swipe down on conversations list
    Then I do not see People Picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2263 @regression
  Scenario Outline: Open/Close Search by different actions in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I hide keyboard
    And I navigate back
    Then I see the conversations list with conversation
    When I do long swipe down on conversations list
    And I see People Picker page
    And I hide keyboard
    And I do short swipe down on People Picker page
    Then I see People Picker page
    When I do long swipe down on People Picker page
    Then I see the conversations list with conversation
    And I do not see People Picker page
    When I do short swipe down on conversations list
    Then I do not see People Picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
