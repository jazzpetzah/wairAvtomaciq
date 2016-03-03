Feature: Notifications

  @C779 @id2900 @regression @rc @rc44
  Scenario Outline: I can open conversation with message from notification
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact2>
    Given I swipe right to show the conversations list
    When I tap conversations list settings button
    And User <Contact1> sends encrypted message <Message> to user Myself
    And I tap the chathead notification
    Then I see the conversation view
    And I see the message "<Message>" in the conversation view
    And I do not see chathead notification

    Examples:
      | Name      | Contact1  | Contact2  | Message             |
      | user1Name | user2Name | user3Name | Hello From Chathead |