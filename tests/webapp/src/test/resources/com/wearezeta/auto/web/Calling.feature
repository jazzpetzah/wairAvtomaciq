Feature: Calling

  @C1745 @regression @calling @calling_debug
  Scenario Outline: Verify I can send text, image and ping while in the same convo
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I see call button
    Then I see correct call button tooltip
    When I call
    Then I see the outgoing call controls for conversation <Contact>
    When <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    When I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see <PING> action in conversation
    And I see sent picture <PictureName> in the conversation view
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    When I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | PING       | PictureName               | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | you pinged | userpicture_landscape.jpg | chrome      | 20      |

  @C1772 @regression @calling @calling_debug
  Scenario Outline: Verify I can get pinged by callee during call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I call
    And <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verify that all audio flows have greater than 0 bytes
    And User <Contact> pinged in the conversation with <Contact>
    And I see <PING> action in conversation
    And <Contact> verifies to get audio data from me
    And I hang up call with conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | PING   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | chrome      | 20      |

  @C1753 @regression @calling @calling_debug
  Scenario Outline: Verify the corresponding conversations list item gets sticky on outgoing call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top
    And I call
    And I see the outgoing call controls for conversation <Contact1>
    And I see conversation <Contact1> is on the top
    Then <Contact1> accepts next incoming call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verify that all audio flows have greater than 0 bytes
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact1> is on the top
    And I hang up call with conversation <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | 20      |

  @C1752 @regression @calling @calling_debug
  Scenario Outline: Verify the corresponding conversations list item gets sticky on incoming call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top
    And <Contact1> calls me
    And I see the incoming call controls for conversation <Contact1>
    And I see conversation <Contact1> is on the top
    When I accept the call from conversation <Contact1>
    Then I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verify that all audio flows have greater than 0 bytes
#    And I see conversation <Contact1> is on the top
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact1> is on the top
    And I hang up call with conversation <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    #And I see conversation <Contact2> is on the top

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      |

  @C1776 @regression @calling @calling_debug
  Scenario Outline: Verify I can call a user twice in a row
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verify that all audio flows have greater than 0 bytes
    When I hang up call with conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flow
    And <Contact> verifies to get audio data from me

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox     | 20      |

  @C1747 @calling @long-call
  Scenario Outline: Verify I can call a user for more than 30 mins
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verify that all audio flows have greater than 0 bytes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 5 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 10 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 15 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 20 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 25 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
# 30 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flow
    And <Contact> verifies to get audio data from me
    And I hang up call with conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |

  @C147868 @calling @long-call
  Scenario Outline: Verify I can call a group for more than 30 mins with browsers
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact>,<Contact2>
    Given <Contact>,<Contact2> starts instance using <CallBackend>
    Given <Contact>,<Contact2> accepts next incoming call automatically
    Given <Contact>,<Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName1>
    And I call
    Then <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact>,<Contact2> verify to have 2 flows
    And <Contact>,<Contact2> verify to get audio data from me
    And <Contact>,<Contact2> verify that all audio flows have greater than 0 bytes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 5 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 10 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 15 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 20 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 25 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 30 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact>,<Contact2> verify to have 2 flows
    And <Contact>,<Contact2> verify to get audio data from me
    And I hang up call with conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact   | Contact2   | ChatName1 | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name  | GC1       | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name  | GC1       | firefox     | 20      |

  @C147869 @calling @long-call
  Scenario Outline: Verify I can call a group for more than 30 mins with AVS
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact>,<Contact2>
    Given <Contact>,<Contact2> starts instance using <CallBackend>
    Given <Contact>,<Contact2> accepts next incoming call automatically
    Given <Contact>,<Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName1>
    And I call
    Then <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 5 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 10 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 15 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 20 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 25 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
# 30 minutes
    And I wait for 60 seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I hang up call with conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact   | Contact2   | ChatName1 | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name  | GC1       | zcall       | 20      |

  @C1754 @regression @calling @calling_debug
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as caller)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>    
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    Then <Contact1>,<Contact2> accept next incoming call automatically
    And <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    When I call
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And I open conversation with <Contact2>
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    And I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    When I click on "Hang Up" button in another call warning modal
    Then I do not see another call warning modal
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact2>
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies to get audio data from me
    And I hang up call with conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | 20      |

  @C1801 @regression @calling @calling_debug
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as callee)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    When <Contact2> accepts next incoming call automatically
    Then <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    When <Contact1> calls me
    And I see the incoming call controls for conversation <Contact1>
    When I accept the call from conversation <Contact1>
    Then <Contact1> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    Then I see the ongoing call controls for conversation <Contact1>
    And I open conversation with <Contact2>
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    Then I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    Then I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    Then I see the ongoing call controls for conversation <Contact1>
    When I call
    Then I see another call warning modal
    Then I click on "Hang Up" button in another call warning modal
    Then I do not see another call warning modal
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact2>
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies to get audio data from me
    And I hang up call with conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | chrome      | 20      |

  @C119432 @mute @calling @group
  Scenario Outline: Verify I can not make a call in group chat with more than 10 participants
    Given My browser supports calling
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given <Contact1> starts instance using <WaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And <Contact1> accept next incoming call automatically
    Then <Contact1> verify that waiting instance status is changed to waiting in <Timeout> seconds
    And I call
    Then I see full call conversation warning modal
    Then I click on "Ok" button in full call conversation warning modal
    Then I do not see the ongoing call controls for conversation <ChatName1>
    And <Contact1> verify that waiting instance status is changed to waiting in <Timeout> seconds
    And I click People button in group conversation
    And I see Group Participants popover
    When I click on participant <Contact10> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    When I call
    Then I do not see full call conversation warning modal
    Then <Contact1> verify that waiting instance status is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <ChatName1>
    
    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Contact8  | Contact9   | Contact10  | ChatName1 | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | user9Name | user10Name | user11Name | GC1       | chrome      | 20      |


  @C165112 @regression @calling @group @calling_debug
  Scenario Outline: Verify receiving group call during group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given Myself has group chat <ChatName2> with <Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <WaitBackend>
    Given <Contact4> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    And I see the outgoing call controls for conversation <ChatName1>
    And <Contact1>,<Contact2> accept next incoming call automatically
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact1>,<Contact2> verifies to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    When <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <ChatName2>
    When I ignore the call from conversation <ChatName2>
    And I see the ongoing call controls for conversation <ChatName1>
    Then I do not see the incoming call controls for conversation  <ChatName2>
    And I see the join call controls for conversation <ChatName2>
    When <Contact4> stops calling <ChatName2>
    And <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <ChatName2>
    When I accept the call from conversation <ChatName2>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact4> stops calling <ChatName2>
    And <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <ChatName2>
    When I accept the call from conversation <ChatName2>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact4> stops calling <ChatName2>
    And <Contact3> accepts next incoming call automatically
    And <Contact4> calls <ChatName2>
    Then <Contact3> verifies that waiting instance status is changed to active in <Timeout> seconds
    When I accept the call from conversation <ChatName2>
    Then I see another call warning modal
    When I click on "Answer" button in another call warning modal
    Then I do not see another call warning modal
    And I see the ongoing call controls for conversation <ChatName2>
    And <Contact3>,<Contact4> verifies to have 2 flows
    And <Contact3>,<Contact4> verifies to get audio data from me
    And I see the join call controls for conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName1 | ChatName2 | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GC1       | GC2       | chrome      | chrome      | 20      |

  @C1765 @regression @calling @calling_debug
  Scenario Outline: Verify I get missed call notification when I call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I call
    Then I wait for 5 seconds
    And I hang up call with conversation <Contact>
    Then I see <Message> action in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message    |
      | user1Email | user1Password | user1Name | user2Name | you called |

  @C1766 @regression @calling @calling_debug
  Scenario Outline: Verify I get missed call notification when someone calls me
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open self profile
    When <Contact1> calls me
    And I wait for 1 seconds
    And <Contact1> stops calling me
    And I wait for 1 seconds
    Then I see missed call notification in the conversation list for conversation <Contact1>
    When I open conversation with <Contact1>
    Then I do not see missed call notification in the conversation list for conversation <Contact1>
    Then I see <MISSED> action for <Contact1> in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | MISSED | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | called | chrome      |

  @C1755 @regression @calling @calling_debug
  Scenario Outline: Verify I can make another call while current one is ignored
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallWaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact1> calls me
    And I see the incoming call controls for conversation <Contact1>
    When I ignore the call from conversation <Contact1>
    Then I do not see the incoming call controls for conversation <Contact2>
    When I call
    And I see the outgoing call controls for conversation <Contact2>
    When <Contact2> accepts next incoming call automatically
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact2>
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies to get audio data from me
    And <Contact2> verifies that all audio flows have greater than 0 bytes
    When I hang up call with conversation <Contact2>
    Then I do not see the call controls for conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallWaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome          | 20      |

  @C1750 @regression @calling @calling_debug
  Scenario Outline: Verify I can not see blocked contact trying to call me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact> calls me
    Then <Contact> verifies that call status to Myself is changed to connecting in <Timeout> seconds
    And I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | chrome      | 20      |

  @C1751 @regression @calling @calling_debug
  Scenario Outline: Verify I can see muted conversation person trying to call me
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts instance using <CallBackend>
    And I am signed in properly
    And I set muted state for conversation <Contact>
    When <Contact> calls me
    Then <Contact> verifies that call status to Myself is changed to connecting in <Timeout> seconds
    And I see the incoming call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |

  @C1798 @regression @calling @group @calling_debug
  Scenario Outline: Verify initiator is not a host for the call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName>
    And <Contact1>,<Contact2> verifies to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    When I hang up call with conversation <ChatName>
    And I see the join call controls for conversation <ChatName>
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome      | 20      |

  @C1799 @regression @calling @group @calling_debug @WEBAPP-2548
  Scenario Outline: Verify accepting group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    # Splitting large instance creation requests to double the timeout
    Given <Contact2>,<Contact3> starts instance using <WaitBackend>
    Given <Contact4> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName>
    And <Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the incoming call controls for conversation <ChatName>
    When I accept the call from conversation <ChatName>
    Then I see the ongoing call controls for conversation <ChatName>
    And I wait for 10 seconds
    And <Contact1> verifies to have 4 flows
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    And <Contact2>,<Contact3>,<Contact4> verify to get audio data from me
    And <Contact2>,<Contact3>,<Contact4> verify that all audio flows have greater than 0 bytes
    When I hang up call with conversation <ChatName>
    Then I see the join call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName  | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome      | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome      | firefox     | 20      |

  @C167027 @regression @calling @group @calling_debug
  Scenario Outline: Verify impossibility to connect 11th person to the call
    Given My browser supports calling
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2>,<Contact3>,<Contact4> starts instance using <WaitBackend>
    Given <Contact5>,<Contact6>,<Contact7> starts instance using <WaitBackend>
    Given <Contact8>,<Contact9>,<Contact10> starts instance using <WaitBackend>
    Given <Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName>
    And <Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the incoming call controls for conversation <ChatName>
    When I accept the call from conversation <ChatName>
    And I wait for 1 seconds
    Then I see full call warning modal
    And I close the full call warning modal
    When I accept the call from conversation <ChatName>
    And I wait for 1 seconds
    Then I see full call warning modal
    And I click on "Ok" button in full call warning modal
    When <Contact1> stops calling <ChatName>
    And I accept the call from conversation <ChatName>
    Then I see the ongoing call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Contact8  | Contact9   | Contact10  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | user9Name | user10Name | user11Name | GroupCallConversation | chrome      | chrome      | 20      |

  @C1813 @regression @calling @group @calling_debug
  Scenario Outline: Verify initiating group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    # Splitting large instance creation requests to double the timeout
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact3>,<Contact4> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I call
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName>
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify to get audio data from me
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that all audio flows have greater than 0 bytes
    When I hang up call with conversation <ChatName>
    Then I see the join call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCallConversation | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCallConversation | firefox     | 20      |

  @C1800 @regression @calling @group @calling_debug
  Scenario Outline: Verify ignoring group call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact1> calls <ChatName>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    When I ignore the call from conversation <ChatName>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    Then I see the join call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome      | chrome      | 20      |

  @C150016 @regression @calling @group @calling_debug
  Scenario Outline: Verify leaving and coming back to the call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I call
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName>
    When I hang up call with conversation <ChatName>
    And I see the join call controls for conversation <ChatName>
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    When I join call of conversation <ChatName>
    And <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName>
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | firefox     | 20      |

  @C165123 @regression @calling @group @calling_debug
  Scenario Outline: Verify possibility to join call after 1 minutes of starting it
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact1> calls <ChatName>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the incoming call controls for conversation <ChatName>
    And I wait for 65 seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    When I accept the call from conversation <ChatName>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    And I see the ongoing call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome      | chrome      | 20      |

  @calling @group @durational
  Scenario Outline: Verify initiating group call several times
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts instance using <WaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I call 100 times for 1 minutes with <Contact1>,<Contact2>,<Contact3>,<Contact4>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCallConversation | chrome      | 20      |

  @calling @group @durational2
  Scenario Outline: Verify 5 min group call several times
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I call 10 times for 5 minutes with <Contact1>,<Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCallConversation | chrome      | 20      |

  @C165115 @regression @calling @group @debug
  Scenario Outline: Verify receiving 1-to-1 call during group call
    Given My browser supports calling
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact3> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    And I see the outgoing call controls for conversation <ChatName1>
    When <Contact1>,<Contact2> accept next incoming call automatically
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    When <Contact3> calls me
    Then I see the incoming call controls for conversation <Contact3>
    When I ignore the call from conversation <Contact3>
    Then I see the ongoing call controls for conversation <ChatName1>
    Then I do not see the incoming call controls for conversation <Contact3>
    When <Contact3> stops calling me
    And <Contact3> calls me
    Then I see the incoming call controls for conversation <Contact3>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact3> stops calling me
    And <Contact3> calls me
    Then I see the incoming call controls for conversation <Contact3>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact3> stops calling me
    And <Contact3> calls me
    Then I see the incoming call controls for conversation <Contact3>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Answer" button in another call warning modal
    Then I do not see another call warning modal
    And I see the ongoing call controls for conversation <Contact3>
    And <Contact3> verifies to have 1 flow
    And <Contact3> verifies to get audio data from me
    And <Contact3> verifies that all audio flows have greater than 0 bytes
    And I see the join call controls for conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | ChatName1 | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GC1       | chrome      | chrome      | 20      |

  @C129912 @regression
  Scenario Outline: Verify I see an error when I try to call in a conversation with no one else left
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password1>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <Contact1> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    And I click back button on Group Participants popover
    And I click on participant <Contact2> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    And I click back button on Group Participants popover
    Then I see 0 participants in the Group Participants popover
    And I do not see call button
    When I type shortcut combination to start a call
    Then I see nobody to call message

    Examples:
      | Login1      | Password1      | Name      | Contact1  | Contact2  | ChatName  |
      | user1Email  | user1Password  | user1Name | user2Name | user3Name | EMPTY     |