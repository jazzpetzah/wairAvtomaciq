Feature: Ping

  @C737 @regression @rc
  Scenario Outline: Send ping and ping again to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <Contact>
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<Message1>" in the conversation view
    When I tap Ping button from cursor toolbar
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact   | Message1   | Count |
      | user1Name | user2Name | YOU PINGED | 2     |

  @C727 @regression @rc
  Scenario Outline: Send ping and ping again to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <Contact>
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<Message1>" in the conversation view
    And I tap Ping button from cursor toolbar
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact   | Message1   | Count |
      | user1Name | user2Name | YOU PINGED | 2     |

  @C766 @regression @rc
  Scenario Outline: Receive "Ping" and "Ping Again" in group conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see Ping message "<PingMessage>" in the conversation view
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | PingMessage      | Count |
      | user1Name | user2Name | user3Name | PingChat      | user2Name PINGED | 2     |

  @C812 @regression @rc
  Scenario Outline: Receive "Ping" and "Ping Again" in group conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see Ping message "<PingMessage>" in the conversation view
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see <Count> Ping messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | PingMessage      | Count |
      | user1Name | user2Name | user3Name | PingChat      | user2Name PINGED | 2     |