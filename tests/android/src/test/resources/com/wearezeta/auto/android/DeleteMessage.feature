Feature: Delete Message

  @C111638 @staging
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message1>" and send it
    And I type the message "<Message2>" and send it
    When I long tap the message "<Message1>" in the conversation view
    And I tap the message "<Message2>" in the conversation view
    And I tap Delete button on the action mode bar
    And I see alert message containing "<AlertText>" in the title
    And I tap Delete button on the alert
    Then I do not see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 | AlertText  |
      | user1Name | user2Name | Yo1      | Yo2      | 2 messages |

  @C111644 @staging
  Scenario Outline: Verify deleting is synchronised across own devices when they are online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    When User Myself adds new device <Device>
    And User <Contact1> adds new device <ContactDevice>
    And I see Contact list with contacts
    And I tap on contact name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap back button in upper toolbar
    And I tap on contact name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       |