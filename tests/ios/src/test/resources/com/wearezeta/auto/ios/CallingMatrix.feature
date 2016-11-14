Feature: Calling Matrix

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can make 1:1 call to <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I accept alert
    And I accept alert
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    When <Contact> calls me
    # Wait until the call is connected
    And I wait for 5 seconds
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And I accept alert
    And I see call status message contains "<Contact>"
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can make group call with callbackend <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    And I accept alert
    And I accept alert
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    And <Contact1>,<Contact2> verifies to have 2 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    And I wait for 5 seconds
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can join group call with callbackend <Backend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    # Wait until the call is connected
    And I wait for 5 seconds
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And I accept alert
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verify that call status to <GroupChatName> is changed to active in <Timeout> seconds
    And <Contact1>,<Contact2> verifies to have 2 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    And I wait for 5 seconds
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Backend | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome  | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Put app into background after initiating call with user <WaitBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <WaitBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I accept alert
    And I accept alert
    Then I close the app for 5 seconds
    And I see Calling overlay
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | WaitBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify putting client to the background during 1-to-1 call <CallBackend> to me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When <Contact> calls me
    # Wait until the call is connected
    And I wait for 5 seconds
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And I accept alert
    Then I see call status message contains "<Contact>"
    When I close the app for 5 seconds
    Then I see call status message contains "<Contact>"
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend  | Timeout |
      | user1Name | user2Name | chrome       | 20      |