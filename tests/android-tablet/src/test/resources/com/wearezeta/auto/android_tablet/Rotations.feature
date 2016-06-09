Feature: Rotations

  @C474 @id2185 @regression
  Scenario Outline: Self profile
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    When I tap conversations list settings button
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

  @C475 @id2186 @regression
  Scenario Outline: (AN-2900) Search (people picker)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    When I open Search UI
    Then I see People Picker page
    When I rotate UI to landscape
    Then I see People Picker page
    When I rotate UI to portrait
    Then I see People Picker page

    Examples:
      | Name      |
      | user1Name |

  @C476 @id2187 @regression
  Scenario Outline: Conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends encrypted message <Message> to user Myself
    When I tap the conversation <Contact>
    And I see the message "<Message>" in the conversation view
    When I rotate UI to landscape
    Then I see the message "<Message>" in the conversation view
    When I rotate UI to portrait
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Msg1    |

  @C475 @id2186 @regression
  Scenario Outline: (AN-2901) Search (people picker)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact2> is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open Search UI
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

  @C510 @id2908 @regression
  Scenario Outline: I want to exit fullscreen view in landscape
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    When I tap the new picture in the conversation view
    Then I see Close Picture Preview button in the conversation view
    When I rotate UI to landscape
    Then I see Close Picture Preview button in the conversation view
    When I tap Close Picture Preview button in the conversation view
    Then I do not see Close Picture Preview button in the conversation view
    And I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |