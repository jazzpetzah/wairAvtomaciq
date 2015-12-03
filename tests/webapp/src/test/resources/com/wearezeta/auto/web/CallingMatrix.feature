Feature: Calling_Matrix

  @id0001 @calling_matrix @calling @calling_debug
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

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox     | 60      |

  @id0002 @calling_matrix @calling @calling_debug
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

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox     | 60      |

  @id0003 @calling_matrix @calling @calling_debug
  Scenario Outline: Verify I can receive 1:1 call from AVS
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

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 60      |

  @id0004 @calling_matrix @calling @calling_debug
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

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox     | 60      |

  @id0005 @calling_matrix @calling @calling_debug
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <Backend>
    Given <Contact1> accept next incoming call automatically
    Given <Contact1> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact2> calls <ChatName1> using <Backend>
    Then <Contact1> verify that waiting instance status is changed to active in <Timeout> seconds
    When I accept the incoming call
    And I see the calling bar from users <Contact1>,<Contact2>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify that all flows have greater than 0 bytes
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend  | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome   | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox  | 60      |

  @id0006 @calling_matrix @calling @calling_debug
  Scenario Outline: Verify I can join group call with AVS and <WaitBackend>
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
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend  | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall | firefox     | 60      |