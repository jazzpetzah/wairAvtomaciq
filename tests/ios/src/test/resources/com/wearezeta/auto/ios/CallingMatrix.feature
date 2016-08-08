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
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend          | Timeout |
      | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Name | user2Name | chrome:50.0.2661.75  | 20      |
      | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Name | user2Name | firefox:45.0.1       | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend    | Timeout |
      | user1Name | user2Name | zcall:2.7.26   | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    And I see call status message contains "<Contact>"
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend          | Timeout |
      | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Name | user2Name | chrome:50.0.2661.75  | 20      |
      | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Name | user2Name | firefox:45.0.1       | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    And I see call status message contains "<Contact>"
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    And <Contact> verifies that call to conversation <Name> was successful

    Examples:
      | Name      | Contact   | CallBackend      | Timeout |
      | user1Name | user2Name | autocall:2.7.26  | 60      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can make group call with multiple <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
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
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend          | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:51.0.2704.106 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:50.0.2661.75  | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:46.0.1       | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:45.0.1       | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can make group call with multiple AVS <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend   | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | zcall:2.7.26  | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    Then I see Calling overlay
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
      | Name      | Contact1  | Contact2  | GroupChatName | Backend             | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:51.0.2704.106| 20      |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:50.0.2661.75 | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:46.0.1      | 20      |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:45.0.1      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can join group call with AVS <CallBackend> and <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    Then I see Calling overlay
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verify that call status to <GroupChatName> is changed to active in <Timeout> seconds
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend         | Timeout | CallBackend     |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:51.0.2704.106| 20      | autocall:2.7.26 |
      | user1Name | user2Name | user3Name | GroupCall     | chrome:50.0.2661.75 | 20      | autocall:2.7.26 |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:46.0.1      | 20      | autocall:2.7.26 |
      | user1Name | user2Name | user3Name | GroupCall     | firefox:45.0.1      | 20      | autocall:2.7.26 |

  @calling_matrix @fastLogin
  Scenario Outline: Verify I can join group call with ZCall <WaitBackend> and <CallBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    Then I see Calling overlay
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verify that call status to <GroupChatName> is changed to active in <Timeout> seconds
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend      | Timeout | WaitBackend  |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:2.7.26  | 20      | zcall:2.7.21 |
      | user1Name | user2Name | user3Name | GroupCall     | autocall:2.7.26  | 20      | zcall:2.7.26 |

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
    Then I close the app for 5 seconds
    And I see Calling overlay
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | WaitBackend         | Timeout |
      | user1Name | user2Name | chrome:51.0.2704.106| 20      |
      | user1Name | user2Name | chrome:50.0.2661.75 | 20      |
      | user1Name | user2Name | firefox:46.0.1      | 20      |
      | user1Name | user2Name | firefox:45.0.1      | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Verify putting client to the background during 1-to-1 call <CallBackend> to me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    Then I see call status message contains "<Contact>"
    When I close the app for 5 seconds
    Then I see call status message contains "<Contact>"
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend      | Timeout |
      | user1Name | user2Name | autocall:2.7.21  | 20      |
      | user1Name | user2Name | autocall:2.7.26  | 20      |

  @calling_matrix @fastLogin
  Scenario Outline: Lock device screen when in call with user <WaitBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <WaitBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    And I tap Audio Call button
    When I lock screen for 5 seconds
    Then I see Calling overlay
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | WaitBackend         | Timeout |
      | user1Name | user2Name | chrome:51.0.2704.106| 20      |
      | user1Name | user2Name | chrome:50.0.2661.75 | 20      |
      | user1Name | user2Name | firefox:46.0.1      | 20      |
      | user1Name | user2Name | firefox:45.0.1      | 20      |

  #Commented because its not working to answer from APNS so far on iphone
  #But want to keep it in, for more investigation
  #@calling_matrix
  #Scenario Outline: Answer 1-to-1 call <CallBackend> from APNS
    #Given There are 2 users where <Name> is me
    #Given Myself is connected to <Contact>
    #Given <Contact> starts instance using <CallBackend>
    #Given I sign in using my email or phone number
    #Given I see conversations list
    #When I tap on contact name <Contact>
    #And I lock screen on real device
    #When I lock screen for 20 seconds
    #And <Contact> calls me
    #And I wait for 10 seconds
    #And I answer call from APNS
    #Then I see Calling overlay
    #And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    #Examples:
      #| Name      | Contact   | CallBackend      | Timeout |
      #| user1Name | user2Name | autocall:2.2.46  | 20      |
      #| user1Name | user2Name | autocall:2.2.38  | 20      |
