Feature: Ping

  @C991 @regression @rc
  Scenario Outline: Verify you can send Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Ping button from input tools
    Then I see "<PingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        | PingMsg    |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat | YOU PINGED |