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
    Given I wait until <Contact2> exists in backend search results
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
