Feature: Ping

  @C681 @regression @rc @legacy
  Scenario Outline: Send multiple Pings to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Ping button from cursor toolbar
    Then I see Ping message "<Msg>" in the conversation view
    And I tap Ping button from cursor toolbar
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact   | Msg        | Count |
      | user1Name | user2Name | YOU PINGED | 2     |

  @C408 @regression
  Scenario Outline: Verify you can send Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap Ping button from cursor toolbar
    Then I see Ping message "<Msg>" in the conversation view
    And I tap Ping button from cursor toolbar
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Msg        | Count |
      | user1Name | user3Name | user2Name | SendPingGroupChat | YOU PINGED | 2     |

  @C701 @regression @rc
  Scenario Outline: Verify you can receive Ping in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And User <Contact1> securely pings conversation <GroupChatName>
    And I see Ping message "<Msg>" in the conversation view
    And User <Contact1> securely pings conversation <GroupChatName>
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        | Msg              | Count |
      | user1Name | user3Name | user2Name | ReceivePingGroupChat | user3Name PINGED | 2     |
