Feature: Delivery

  @C232561 @C232563 @C232564 @staging
  Scenario Outline: Last sent message delivery hint is always visible in 1:1, and last liked message only show like details.
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # C232561
    When I type the message "<Msg1>" and send it by cursor Send button
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    When I type the message "<Msg2>" and send it by cursor Send button
    Then I see 1 Message status in conversation view
    And I do not see Like description in conversation view
    # C232563
    When User <Contact> likes the recent message from user Myself via device <ContactDevice>
    Then I see Like description with expected text "<Contact>" in conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Msg1 | Msg2 | MessageStatus | ContactDevice |
      | user1Name | user2Name | M1   | M2   | Sent          | D1            |

  @C232578 @regression
  Scenario Outline: Verify Resend option is shown for failed to send message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I enable Airplane mode on the device
    And I see No Internet bar in 10 seconds
    And I type the message "<Msg1>" and send it by cursor Send button
    # Wait until sending timeout
    And I wait for 5 seconds
    Then I see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Msg1 | MessageStatus          | ContactDevice |
      | user1Name | user2Name | M1   | Sending failed. Resend | D1            |


