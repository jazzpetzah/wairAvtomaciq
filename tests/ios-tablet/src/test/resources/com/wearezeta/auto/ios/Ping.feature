Feature: Ping

  @C2758 @regression @id2655
  Scenario Outline: Verify you can send Ping in a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    And I click Ping button
    Then I see You Pinged message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat |