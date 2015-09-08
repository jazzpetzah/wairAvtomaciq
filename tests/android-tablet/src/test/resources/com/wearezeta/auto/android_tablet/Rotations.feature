Feature: Rotations

  @id2185 @regression
  Scenario Outline: Self profile
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    # We try to set the self name to make sure this action is really visible and available
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page
    When I rotate UI to landscape
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page
    When I rotate UI to portrait
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page

    Examples:
      | Name      |
      | user1Name |

  @id2186 @regression
  Scenario Outline: Search (people picker)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with no conversations
    When I tap the Search input
    Then I see People Picker page
    When I rotate UI to landscape
    Then I see People Picker page
    When I rotate UI to portrait
    Then I see People Picker page

    Examples:
      | Name      |
      | user1Name |

  @id2187 @regression
  Scenario Outline: Conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends message "<Message>" to user Myself
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with conversations
    When I tap the conversation <Contact>
    Then I see the conversation view
    And I see the message "<Message>" in the conversation view
    When I rotate UI to landscape
    Then I see the message "<Message>" in the conversation view
    When I rotate UI to portrait
    Then I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact   | Message  |
      | user1Name | user2Name | Msg1     |

  @id2186 @regression
  Scenario Outline: Search (people picker)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact2> is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with conversations
    When I tap the Search input
    And I see People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    Then I see Outgoing Connection popover
    When I rotate UI to landscape
    Then I see Outgoing Connection popover
    When I rotate UI to portrait
    Then I see Outgoing Connection popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2908 @staging
  Scenario Outline: I want to exit fullscreen view in landscape
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe left on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    When I tap the new picture in the conversation view
    And I tap in the center of the screen
    Then I see Close Picture Preview button in the conversation view
    When I rotate UI to landscape
    Then I see Close Picture Preview button in the conversation view
    When I tap Close Picture Preview button in the conversation view
    Then I do not see Close Picture Preview button in the conversation view
    And I see a new picture in the conversation view

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
