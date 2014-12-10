Feature: Ping

  @staging @id1357
  Scenario Outline: Verify you can send Ping in a group conversation
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I see Contact list with my name <Login>
    When I tap on group chat with name <GroupChatName>
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog
    And I see <Action1> icon in conversation
    And I click Ping button
    And I see You Pinged Again message in the dialog
    And I see <Action2> icon in conversation

    Examples: 
      | Login   | Password    | GroupChatName        | Contact1    | Contact2    | Action1 | Action2      |
      | aqaUser | aqaPassword | ReceivePingGroupChat | aqaContact1 | aqaContact2 | PINGED  | PINGED AGAIN |

  @staging @id1358
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given I have 1 users and 2 contacts for 1 users
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Login>
    And I tap on group chat with name <GroupChatName>
    And User <Contact1> Ping in chat <GroupChatName> by BackEnd
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <GroupChatName> by BackEnd
    And I see User <Contact1> Pinged Again message in the conversation
    And I see <Action2> icon in conversation

    Examples: 
      | Login   | Password    | GroupChatName        | Contact1    | Contact2    | Action1 | Action2      |
      | aqaUser | aqaPassword | ReceivePingGroupChat | aqaContact1 | aqaContact2 | PINGED  | PINGED AGAIN |
