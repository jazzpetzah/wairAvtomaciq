Feature: Ping

  @staging @id1357
  Scenario Outline: Verify you can send Ping in a group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog
    And I see <Action1> icon in conversation
    And I swipe the text input cursor
    And I click Ping button
    And I wait for 1 seconds
    And I see You Pinged Again message in the dialog
    Then I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange |