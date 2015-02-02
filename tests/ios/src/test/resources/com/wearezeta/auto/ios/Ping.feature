Feature: Ping

  @regression @id1357
  Scenario Outline: Verify you can send Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog
    #And I see <Action1> icon in conversation
    And I click Ping button
    And I see You Pinged Again message in the dialog
    #And I see <Action2> icon in conversation

    Examples:
      | Login      | Password      | Name      | Contact1    | Contact2   | Action1 | Action2      | GroupChatName        |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | PINGED  | PINGED AGAIN | ReceivePingGroupChat |

  @regression @id1358
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    And User <Contact1> Ping in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    #And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    And I see User <Contact1> Pinged Again message in the conversation
    #And I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   | Action1 | Action2      | GroupChatName        |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | PINGED  | PINGED AGAIN | ReceivePingGroupChat |
