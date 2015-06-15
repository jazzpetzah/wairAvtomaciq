Feature: Search

  @id2249 @smoke
  Scenario Outline: Open/Close People picker in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list

    Examples: 
      | Name      |
      | user1Name |

  @id2263 @smoke
  Scenario Outline: Open/Close People picker in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list

    Examples: 
      | Name      |
      | user1Name |
