Feature: Group Ephemeral Messages

  @C259589 @rc @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are disabled in a group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see Hourglass button in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C318636 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    When I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    Then I tap Confirm button on Picture preview page
    # wait to make the transition and image arrival
    And I wait for 3 seconds
    When I remember asset container state at cell 1
    And I wait for <Timeout> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timeout | DeviceName |
      | user1Name | user2Name | user3Name | Epheme grp    | 15      | device2    |

  @C320772 @staging @fastLogin
  Scenario Outline: ZIOS-7568 Verify timer is applied to the all messages until turning it off
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I type the default message and send it
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    Given I tap default message in conversation view
    Given I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    Given I tap Add Picture button from input tools
    Given I accept alert if visible
    Given I accept alert if visible
    Given I select the first picture from Keyboard Gallery
    Given I tap Confirm button on Picture preview page
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    Given I tap on image in conversation view
    Given I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I tap Time Indicator button in conversation view
    And I set ephemeral messages expiration timer to Off
    And I type the "<TestMsg>" message and send it
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    And I tap "<TestMsg>" message in conversation view
    Then I do not see "<EphemeralTimeLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timer | EphemeralTimeLabel | TestMsg |
      | user1Name | user2Name | user3Name | Epheme grp    | 15    | seconds            | hi      |