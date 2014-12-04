Feature: Ping
 
 @id1373 @regression
  Scenario Outline: Verify you can send Ping & Hot Ping in a group conversation
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    And I see <Action1> icon
    And I see Hello-Hey message <Message> with <Action1> in the dialog
    And I press Ping button
    Then I see <Action2> icon
    And I see Hello-Hey message <Message> with <Action2> in the dialog

    Examples: 
      | Login   | Password    | GroupChatName     | Contact1    | Contact2    | Message | Action1 | Action2      |
      | aqaUser | aqaPassword | SendPingGroupChat | aqaContact1 | aqaContact2 | YOU     | PINGED  | PINGED AGAIN |

  @id1374 @regression
  Scenario Outline: Verify you can receive Ping & Hot Ping in a group conversation
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And Contact <Contact1> ping conversation <GroupChatName>
    And I see Hello-Hey message <Contact1> with <Action1>  in the dialog
    And I see <Action1> icon
    And Contact <Contact1> hotping conversation <GroupChatName>
 	Then I see Hello-Hey message <Contact1> with <Action2>  in the dialog
 	And I see <Action2> icon
    
    Examples: 
      | Login   | Password    | GroupChatName        | Contact1    | Contact2    | Action1 | Action2      |
      | aqaUser | aqaPassword | ReceivePingGroupChat | aqaContact1 | aqaContact2 | PINGED   | PINGED AGAIN |
