Feature: Ping

  @C991 @regression @rc @id1357
  Scenario Outline: Verify you can send Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog
    And I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange |

  @C992 @regression @id1358
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    And User <Contact1> Ping in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @C3243 @regression @id1356
  Scenario Outline: Verify you can see Ping on the other side (1:1 conversation)
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact1>
    And User <Contact1> Ping in chat <Name> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |