Feature: Ping

  @C737 @id2253 @regression @rc
  Scenario Outline: (AN-2955) Send ping and ping again to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Ping button in the conversation view
    Then I see the ping message "<Message1>" in the conversation view
    When I swipe right on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1   | Message2         |
      | user1Name | user2Name | YOU PINGED | YOU PINGED AGAIN |

  @C727 @id2239 @regression @rc
  Scenario Outline: (AN-2955) Send ping and ping again to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Ping button in the conversation view
    Then I see the ping message "<Message1>" in the conversation view
    When I swipe right on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1   | Message2         |
      | user1Name | user2Name | YOU PINGED | YOU PINGED AGAIN |

  @C766 @id2863 @regression @rc
  Scenario Outline: Receive "Ping" and "Ping Again" in group conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see the ping message "<PingMessage>" in the conversation view
    When User <Contact1> securely hotpings conversation <GroupChatName>
    Then I see the ping message "<HotPingMessage>" in the conversation view
    And I do not see the ping message "<PingMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | PingMessage      | HotPingMessage         |
      | user1Name | user2Name | user3Name | PingChat      | user2Name PINGED | user2Name PINGED AGAIN |

  @C812 @id3262 @regression @rc
  Scenario Outline: Receive "Ping" and "Ping Again" in group conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see the ping message "<PingMessage>" in the conversation view
    When User <Contact1> securely hotpings conversation <GroupChatName>
    Then I see the ping message "<HotPingMessage>" in the conversation view
    And I do not see the ping message "<PingMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | PingMessage      | HotPingMessage         |
      | user1Name | user2Name | user3Name | PingChat      | user2Name PINGED | user2Name PINGED AGAIN |