Feature: Search

  #Need to add close by swipe, close by android back button, open by swipe, open by click on search button
  @id2249 @smoke
  Scenario Outline: Open/Close Search by different actions in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with no conversations

    Examples: 
      | Name      |
      | user1Name |

  #Need to add close by swipe, close by android back button, open by swipe, open by click on search button
  @id2263 @smoke
  Scenario Outline: Open/Close Search by different actions in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with no conversations

    Examples: 
      | Name      |
      | user1Name |
