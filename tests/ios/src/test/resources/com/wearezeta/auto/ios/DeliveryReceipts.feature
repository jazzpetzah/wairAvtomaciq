Feature: Delivery Receipts

  @C226452 @rc @regression @fastLogin
  Scenario Outline: Verify status is changed to Sent with a timestamp when message reached the server
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    And I see "<SentLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | SentLabel |
      | user1Name | user2Name | Sent      |

  @C226460 @rc @regression  @fastLogin
  Scenario Outline: Verify Delivered status isn't shown in the group conversation until tap on message
    Given There are 3 users where <Name> is me
    Given User adds the following device: {"<Contact1>": [{}]}
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I type the default message and send it
    When I see 1 default message in the conversation view
    Then I do not see "<SentLabel>" on the message toolbox in conversation view
    When I tap at 5% of width and 5% of height of the recent message
    Then I see "<SentLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | SentLabel |
      | user1Name | user2Name | user3Name | MessageToGroup | Sent      |

  @C226453 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify status is changed to Delivered when message has reached at least one of the other person's devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I see 1 default message in the conversation view
    Then I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | DeliveredLabel |
      | user1Name | user2Name | Delivered      |