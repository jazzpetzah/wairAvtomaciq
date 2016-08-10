Feature: Calling_Matrix

  @C5359 @calling_matrix
  Scenario Outline: Verify I can make 1:1 audio call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5360 @calling_matrix
  Scenario Outline: Verify I can make 1:1 video call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
#    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    And I end the video call
#    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5361 @calling_matrix
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    #And I see row of avatars on call controls with user <Contact>
    And I wait for 5 seconds
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> verifies that incoming call was successful

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend     | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:2.7.26    | 20      |
# not necessary due to same versions in android and ios
#      | user1Email | user1Password | user1Name | user2Name | zcall:2.3.8    | 20      |

  @C5362 @calling_matrix
  Scenario Outline: Verify I can receive 1:1 audio call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5363 @calling_matrix
  Scenario Outline: Verify I can receive 1:1 video call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And <Contact> starts a video call to me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
#    Then I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    And I end the video call
#    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5364 @calling_matrix
  Scenario Outline: Verify I can receive 1:1 audio call from AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And <Contact> verifies that call to conversation <Contact> was successful

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend       | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:2.7.26      | 20      |
# not necessary due to same versions in android and ios
#      | user1Email | user1Password | user1Name | user2Name | zcall:2.3.8   | 20      |

  @C5365 @calling_matrix
  Scenario Outline: Verify I can make audio group call with multiple <WaitBackend>
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify to get audio data from me
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that all audio flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify to have 3 flows
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that all audio flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1>,<Contact2>,<Contact3> stops calling

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName1 | WaitBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome:52.0.2743.82  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome:51.0.2704.106 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | firefox:46.0.1       | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | firefox:45.0.1       | 30      |

  @C5366 @calling_matrix
  Scenario Outline: Verify I can make audio group call with multiple AVS <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    When I hang up call with conversation <ChatName1>
    Then I see the join call controls for conversation <ChatName1>
# Stops all incoming instance calls
# Leaving out last participant - call gets destroyed automatically
    When <Contact1> stops calling
    And <Contact1>,<Contact2> verify that waiting instance status is changed to destroyed in <Timeout> seconds
    Then <Contact1>,<Contact2> verify that incoming call was successful

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend    | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:2.7.26   | 30      |
# not necessary due to same versions in android and ios
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:2.3.8    | 30      |

  @C5367 @calling_matrix
  Scenario Outline: Verify I can join audio group call with multiple <Backend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact1> accept next incoming call automatically
    Given <Contact1> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And <Contact2> calls <ChatName1>
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact2> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    When I accept the call from conversation <ChatName1>
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify to get audio data from me
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact1>,<Contact2> verify to have 1 flow
    And <Contact1>,<Contact2> verify that all audio flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1> stops calling

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName1 | Backend              | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome:52.0.2743.82  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | chrome:51.0.2704.106 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | firefox:46.0.1       | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | firefox:45.0.1       | 30      |
      

  @C5368 @calling_matrix
  Scenario Outline: Verify I can join audio group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> accept next incoming call automatically
    Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the call from conversation <ChatName1>
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I see row of avatars on call controls with users <Contact1>
    And I wait for 10 seconds
    And <Contact2> verifies to have 2 flows
    And <Contact2> verifies to get audio data from me
    And <Contact2> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 10 seconds
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies that all audio flows have greater than 0 bytes
# Stops all outgoing instance calls
# Leaving out last participant - call gets destroyed automatically
    And <Contact1> stops calling <ChatName1>
    And <Contact1> verifies that call status to <ChatName1> is changed to destroyed in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    Then <Contact1> verifies that call to conversation <ChatName1> was successful

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName1 | Backend       | WaitBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | zcall:2.7.26  | chrome:52.0.2743.82  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | zcall:2.7.26  | chrome:51.0.2704.106 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | zcall:2.7.26  | firefox:46.0.1       | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | zcall:2.7.26  | firefox:45.0.1       | 30      |
# not necessary due to same versions in android and ios
#      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall:2.2.38 | chrome:52.0.2743.82  | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall:2.2.38 | chrome:51.0.2704.106 | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall:2.2.38 | firefox:46.0.1       | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall:2.2.38 | firefox:45.0.1       | 30      |

  @C5369 @calling_matrix
  Scenario Outline: Verify I can join audio group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the incoming call
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I hang up call with conversation <ChatName1>
    Then I see the join call controls for conversation <ChatName1>
# Stops all outgoing instance calls
# Leaving out last participant - call gets destroyed automatically
    When <Contact1> stops calling <ChatName1>
    And <Contact1> verifies that call status to <ChatName1> is changed to destroyed in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    Then <Contact1> verifies that call to conversation <ChatName1> was successful
    And <Contact2> verify that incoming call was successful

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend        | WaitBackend   | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:2.7.26   | zcall:2.7.26  | 30      |
# not necessary due to same versions in android and ios
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:2.2.46  | zcall:2.2.38 | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:2.2.38  | zcall:2.2.38 | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:2.2.38  | zcall:2.2.46 | 30      |

  @C5370 @calling_matrix
  Scenario Outline: Verify I can create, leave and rejoin an audio group call with <WaitBackend>
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
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | WaitBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome:52.0.2743.82  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome:51.0.2704.106 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | firefox:46.0.1       | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | firefox:45.0.1       | 30      |

  @C5371 @calling_matrix
  Scenario Outline: Verify I can create, leave and rejoin an audio group call with AVS <WaitBackend>
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
# Stops all outgoing instance calls
    When <Contact1>,<Contact2> stops calling
    And <Contact1>,<Contact2> verify that waiting instance status is changed to destroyed in <Timeout> seconds
    Then <Contact1>,<Contact2> verify that incoming call was successful

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | WaitBackend  | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | zcall:2.7.26 | 30      |

  @C5372 @calling_matrix
  Scenario Outline: Verify I can 1:1 audio call a user with <CallBackend> twice in a row
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
    And <Contact> verify that all audio flows have greater than 0 bytes    
    And <Contact> verifies to get audio data from me
    When I hang up call with conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flow
    And <Contact> verify that all audio flows have greater than 0 bytes    
    And <Contact> verifies to get audio data from me

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5373 @calling_matrix
  Scenario Outline: Verify I can 1:1 audio call a user with AVS <CallBackend> twice in a row
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
    When I hang up call with conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    Then <Contact> verify that incoming call was successful
    And <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    When I call
    And I see the ongoing call controls for conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    When <Contact> stops calling
    Then <Contact> verify that incoming call was successful

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend  | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:2.7.26 | 20      |

  @C5374 @calling_matrix
  Scenario Outline: Verify I can 1:1 video call a user with <CallBackend> twice in a row
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I start a video call
    Then <Contact> accepts next incoming video call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flow
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verify that all video flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I hang up call with conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> accepts next incoming video call automatically
    And <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    And I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flow
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verify that all video flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:51.0.2704.106 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:46.0.1       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |