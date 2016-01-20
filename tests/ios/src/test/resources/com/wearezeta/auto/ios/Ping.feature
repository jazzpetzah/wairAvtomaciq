Feature: Ping

  @C991 @regression @rc @id1357
  Scenario Outline: Verify you can send Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open conversation details
    And I click Ping button
    Then I see You Pinged message in the dialog
    And I see <Action1> icon in conversation

    Examples:
      | Name      | Contact1  | Contact2  | Action1 | GroupChatName        | Color        |
      | user1Name | user2Name | user3Name | PINGED  | ReceivePingGroupChat | BrightOrange |

  @C992 @regression @id1358
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given User <Contact1> securely pings conversation <GroupChatName>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation

    Examples:
      | Name      | Contact1  | Contact2  | Action1 | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @C3243 @regression @id1356
  Scenario Outline: Verify you can see Ping on the other side (1:1 conversation)
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given User <Contact1> securely pings conversation <Name>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation

    Examples:
      | Name      | Contact1  | Action1 | Color        | ContactName |
      | user1Name | user2Name | PINGED  | BrightOrange | OtherUser   |