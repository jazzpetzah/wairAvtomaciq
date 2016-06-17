Feature: Chatheads

  @C150006 @regression @rc
  Scenario Outline: Verify tapping a chathead will open correct conversation view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact2>
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap the chathead notification
    Then I see the message "<Message1>" in the conversation view
    And I do not see chathead notification
    When User <Contact1> sends encrypted message <Message2> to user Myself
    And I tap the chathead notification
    Then I see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | Message1 | Message2 |
      | user1Name | user2Name | user3Name | ChatheadsCheck | Hello1   | Hello2   |
