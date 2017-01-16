Feature: Rotations

  @C476 @regression
  Scenario Outline: Conversation view rotations
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

  @C475 @regression
  Scenario Outline: (AN-2901) Search rotations
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact2> is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I wait until <Contact2> exists in backend search results
    When I open Search UI
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    Then I see Single unconnected user details popover
    When I rotate UI to landscape
    Then I do not see Single unconnected user details popover
#    When I rotate UI to portrait
#    Then I see Outgoing Connection popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
