Feature: Calling Matrix

  @calling_matrix
  Scenario Outline: Verify I can make 1:1 call to <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing call
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I hang up ongoing call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:49.0.2623.75 | 20      |
      | user1Name | user2Name | chrome:47.0.2526.73 | 20      |
      | user1Name | user2Name | firefox:44.0.2      | 20      |
      | user1Name | user2Name | firefox:43.0        | 20      |

  @calling_matrix
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing call
    When I hang up ongoing call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall:1.12  | 20      |
      | user1Name | user2Name | zcall:2.1   | 20      |

  @calling_matrix
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact1> calls me
    Then I see incoming call
    And I see incoming call from <Contact1>
    When I swipe to accept the call
    Then <Contact1> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see ongoing call
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies that all flows have greater than 0 bytes
    When <Contact1> stops calling me
    Then <Contact1> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact1  | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:49.0.2623.75 | 20      |
      | user1Name | user2Name | chrome:47.0.2526.73 | 20      |
      | user1Name | user2Name | firefox:44.0.2      | 20      |
      | user1Name | user2Name | firefox:43.0        | 20      |

  @calling_matrix
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact1> calls me
    Then I see incoming call
    And I see incoming call from <Contact1>
    When I swipe to accept the call
    Then <Contact1> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact1> stops calling me
    Then <Contact1> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call
    #BUG: If the autocall stops the call, you can get no metrics - to be fixed soon
    #And <Contact1> verifies that call to conversation <Name> was successful

    Examples:
      | Name      | Contact1  | CallBackend   | Timeout |
      | user1Name | user2Name | autocall:1.12 | 60      |
      | user1Name | user2Name | autocall:2.1  | 60      |

  @calling_matrix
  Scenario Outline: Verify I can make group call with multiple <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <WaitBackend>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact1>,<Contact2> accept next incoming call automatically
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    When I hang up ongoing call
    And I do not see ongoing call
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend         | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:49.0.2623.75 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:47.0.2526.73 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:44.0.2      | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:43.0        | 20      |

  @calling_matrix
  Scenario Outline: Verify I can make group call with multiple AVS <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <WaitBackend>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact1>,<Contact2> accept next incoming call automatically
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I hang up ongoing call
    Then I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend| Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | zcall:1.12 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | zcall:2.1  | 20      |

  @calling_matrix
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact2>,<Contact1> start instance using <Backend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact2> accept next incoming call automatically
    And <Contact1> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    And I see ongoing call
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    And I hang up ongoing call
    And I do not see ongoing call
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Backend             | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:49.0.2623.75 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:47.0.2526.73 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:44.0.2      | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:43.0        | 20      |

  @calling_matrix
  Scenario Outline: Verify I can join group call with AVS <CallBackend> and <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> start instance using <CallBackend>
    Given <Contact2> start instance using <WaitBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact2> accept next incoming call automatically
    And <Contact1> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    And I see ongoing call
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I hang up ongoing call
    And I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend         | Timeout | CallBackend   |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:49.0.2623.75 | 20      | autocall:1.12 |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:47.0.2526.73 | 20      | autocall:1.12 |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:44.0.2      | 20      | autocall:1.12 |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:43.0        | 20      | autocall:1.12 |

  @calling_matrix
  Scenario Outline: Verify I can join group call with ZCall <WaitBackend> and <CallBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> start instance using <CallBackend>
    Given <Contact2> start instance using <WaitBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact2> accept next incoming call automatically
    And <Contact1> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    And I see ongoing call
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I hang up ongoing call
    And I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend   | Timeout | WaitBackend |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:1.12 | 20      | zcall:1.12  |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:1.12 | 20      | zcall:2.1   |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:2.1  | 20      | zcall:1.12  |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:2.1  | 20      | zcall:2.1   |