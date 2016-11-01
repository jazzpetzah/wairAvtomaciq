Feature: Delivery

  @C232561 @C232563 @regression
  Scenario Outline: Verify I can see message status for latest message and the previous no-like message will hide status completely
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Msg1>" and send it by cursor Send button
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    When I type the message "<Msg2>" and send it by cursor Send button
    Then I see 1 Message status in conversation view
    And I do not see Like description in conversation view
    When User <Contact> likes the recent message from user Myself via device <ContactDevice>
    Then I see Like description with expected text "<Contact>" in conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Msg1 | Msg2 | MessageStatus | ContactDevice |
      | user1Name | user2Name | M1   | M2   | Delivered     | D1            |

  @C232568 @regression
  Scenario Outline: Verify status is changed to Sent and Delivered
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Msg>" and send it by cursor Send button
    Then I see Message status with expected text "<MessageStatus1>" in conversation view
    When User <Contact> adds new devices <ContactDevice>
    # Wait until Sync
    And I wait for 5 seconds
    Then I see Message status with expected text "<MessageStatus1>" in conversation view

    Examples:
      | Name      | Contact   | Msg | MessageStatus1 | ContactDevice | MessageStatus1 |
      | user1Name | user2Name | M1  | Sent           | D1            | Delivered      |

  @C232574 @regression
  Scenario Outline: Delivery status in group for the last sent message is not shown by default and when I open it - it is 'Sent'
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Group>
    And I type the message "<Message>" and send it by cursor Send button
    Then I do not see Message status in conversation view
    And I tap the Text message "<Message>" in the conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message | Group   | MessageStatus |
      | user1Name | user2Name | user3Name | M1      | YoGroup | Sent          |

  @C232565 @regression
  Scenario Outline: Verify the message status of not last message is changed when someone likes the message in 1:1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Msg>" and send it by cursor Send button
    And I type the message "<Msg2>" and send it by cursor Send button
    And I long tap the Text message "<Msg>" in the conversation view
    And I tap Like button on the message bottom menu
    Then I see 1 Message status in conversation view
    And I see Like description with expected text "Myself" in conversation view
    And I see Like button in conversation view
    When I type the message "<Msg3>" and send it by cursor Send button
    Then I see 1 Message status in conversation view
    And I see Like description with expected text "Myself" in conversation view
    And I see Like button in conversation view

    Examples:
      | Name      | Contact   | Msg | Msg2 | ContactDevice | Msg3 |
      | user1Name | user2Name | M1  | M2   | D1            | M3   |