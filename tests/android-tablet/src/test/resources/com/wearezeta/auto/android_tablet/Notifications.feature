Feature: Notifications

  @id2900 @regression @rc
  Scenario Outline: I can open conversation with message from notification
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    When I tap my avatar on top of conversations list
    And Contact <Contact> sends message "<Message>" to user Myself
    And I tap the chathead notification
    Then I see the conversation view
    And I see the message "<Message>" in the conversation view
    And I do not see chathead notification

    Examples:
      | Name      | Contact   | Message             |
      | user1Name | user2Name | Hello From Chathead |
