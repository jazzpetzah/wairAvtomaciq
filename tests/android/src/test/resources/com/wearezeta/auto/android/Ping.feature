Feature: Ping

  @C681 @id317 @regression @rc @rc42
  Scenario Outline: Send Ping & Hot Ping to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg> in the dialog

    Examples:
      | Name      | Contact   | Msg        |
      | user1Name | user2Name | YOU PINGED |

  @C408 @id1373 @regression
  Scenario Outline: Verify you can send Ping & Hot Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg1> in the dialog
    When I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg2> in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Msg1       | Msg2             |
      | user1Name | user3Name | user2Name | SendPingGroupChat | YOU PINGED | YOU PINGED AGAIN |

  @C701 @id1374 @regression @rc
  Scenario Outline: Verify you can receive Ping & Hot Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And Contact <Contact1> ping conversation <GroupChatName>
    And I see Ping message <Action1> in the dialog
    And Contact <Contact1> hotping conversation <GroupChatName>
    Then I see Ping message <Action2> in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        | Action1          | Action2                |
      | user1Name | user3Name | user2Name | ReceivePingGroupChat | user3Name PINGED | user3Name PINGED AGAIN |