Feature: Calling_Matrix

  @C5359 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And I wait for 10 seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:49.0.2623.75 | 60      |
      | user1Email | user1Password | user1Name | user2Name | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:44.0.2      | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:43.0        | 60      |

  @C5360 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    #And I see row of avatars on call controls with user <Contact>
    And I wait for 10 seconds
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:1.12  | 60      |

  @C5361 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I wait for 10 seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:49.0.2623.75 | 60      |
      | user1Email | user1Password | user1Name | user2Name | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:44.0.2      | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox:43.0        | 60      |

  @C5362 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend   | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall:1.12 | 60      |

  @C5363 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 2 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I do not see the call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1> stops calling


    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:49.0.2623.75 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:44.0.2      | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:43.0        | 60      |

  @C5364 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple AVS <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    When I hang up call with conversation <ChatName1>
    Then I do not see the call controls for conversation <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:1.12  | 60      |

  @C5365 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact1> accept next incoming call automatically
    Given <Contact1> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact2> calls <ChatName1>
    Then <Contact1> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact2> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    When I accept the call from conversation <ChatName1>
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I do not see the call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1> stops calling

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend             | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:49.0.2623.75 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:44.0.2      | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:43.0        | 60      |

  @C5366 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> accept next incoming call automatically
    Given <Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the call from conversation <ChatName1>
    Then <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I see row of avatars on call controls with users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact2> verify to have 2 flows
    And <Contact2> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I do not see the call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact2> verifies to have 1 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
    # Stops all autocall instance calls
    And <Contact1> stops calling <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend       | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.12 | chrome:49.0.2623.75 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.12 | chrome:47.0.2526.73 | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.12 | firefox:44.0.2      | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.12 | firefox:43.0        | 60      |

  @C5367 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the incoming call
    Then <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verify that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I hang up call with conversation <ChatName1>
    Then I do not see the call controls for conversation <ChatName1>
    # Stops all autocall instance calls
    And <Contact1> stops calling <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend       | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:1.12 | zcall:1.12  | 60      |