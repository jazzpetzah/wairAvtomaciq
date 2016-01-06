Feature: Calling_Matrix

  @C0001 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I see the calling bar from user <Contact>
    And I wait for 10 seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:42.0        | 60      |

  @C0002 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And I wait for 10 seconds
    Then I see the calling bar from user <Contact>
    And I end the call
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:1.10  | 60      |
      | user1Email | user1Password | user1Name | user2Name | zcall:1.09  | 60      |

  @C0003 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me using <CallBackend>
    When I accept the incoming call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I see the calling bar from user <Contact>
    And I wait for 10 seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:42.0        | 60      |

  @C0004 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me using <CallBackend>
    When I accept the incoming call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I see the calling bar from user <Contact>
    And I end the call
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend   | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall:1.10 | 60      |
      | user1Email | user1Password | user1Name | user2Name | autocall:1.09 | 60      |

  @C0005 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar from users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 2 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
         And <Contact1> stops all waiting instances


    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:42.0        | 60      |

  @C0006 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple AVS <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar from users <Contact1>,<Contact2>
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:1.10  | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:1.09  | 60      |

  @C0007 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1> starts waiting instance using <Backend>
    Given <Contact1> accept next incoming call automatically
    Given <Contact1> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact2> calls <ChatName1> using <Backend>
    Then <Contact1> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact2> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    When I accept the incoming call
    And I see the calling bar from users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
         And <Contact1> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend             | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:42.0        | 60      |

  @C0008 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given <Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1> using <Backend>
    When I accept the incoming call
    Then <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the calling bar from users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact2> verify to have 2 flows
    And <Contact2> verify that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar
    And I wait for 10 seconds
    And <Contact2> verifies to have 1 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
         # Stops all autocall instance calls
         And <Contact1> stops all calls to <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend       | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | firefox:42.0        | 60      |

  @C0009 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given <Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1> using <Backend>
    When I accept the incoming call
    Then <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the calling bar from users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact2> verify to have 2 flows
    And <Contact2> verify that all flows have greater than 0 bytes
    And I end the call
    And I do not see the calling bar
    And I wait for 10 seconds
    And <Contact2> verifies to have 1 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
    # Stops all autocall instance calls
    And <Contact1> stops all calls to <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend       | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | chrome:48.0.2564.41 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | firefox:43.0        | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | firefox:42.0        | 60      |

  @C0010 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1> using <Backend>
    When I accept the incoming call
    Then <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the calling bar from users <Contact1>,<Contact2>
    And I end the call
    And I do not see the calling bar
    # Stops all autocall instance calls
    And <Contact1> stops all calls to <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend       | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | zcall:1.10  | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | zcall:1.10  | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.10 | zcall:1.09  | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.09 | zcall:1.09  | 60      |