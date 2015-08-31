Feature: Calling

  @regression @calling @debug @id1860
  Scenario Outline: Verify I can send text, image and ping while in the same convo
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
    And I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see ping message <PING>
    And I see sent picture <PictureName> in the conversation view
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PictureName               | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | userpicture_landscape.jpg | chrome      | 60      |

  @regression @calling @debug @id2080
  Scenario Outline: Verify I can get pinged by callee during call
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
    And User <Contact> pinged in the conversation with <Contact>
    And I see ping message <PING>
    And User <Contact> pinged twice in the conversation with <Contact>
    And I see ping message <HOTPING>
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | HOTPING      | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again | chrome      | 60      |

  @regression @calling @debug @id1892
  Scenario Outline: Verify the corresponding conversations list item gets sticky on outgoing call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts waiting instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top
    And I call
    And I see the calling bar
    And I see conversation <Contact1> is on the top
    Then <Contact1> accepts next incoming call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar from user <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact1> is on the top
    And I end the call
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | chrome      | 60      |

  @regression @calling @debug @id1891
  Scenario Outline: Verify the corresponding conversations list item gets sticky on incoming call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top
    And <Contact1> calls me using <CallBackend>
    And I see the calling bar
    And I see conversation <Contact1> is on the top
    When I accept the incoming call
    Then I see the calling bar from user <Contact1>
    And I see conversation <Contact1> is on the top
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact1> is on the top
    And I end the call
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact2> is on the top

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | autocall    | 60      |

  @smoke @calling @debug @id2237
  Scenario Outline: Verify I can call a user twice in a row
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
    And I end the call
    Then <Contact> verifies that waiting instance status is changed to ready in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name | firefox     | 60      |

  @regression @calling @id1866
  Scenario Outline: Verify I can call a user for more than 15 mins
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @regression @calling @id1902
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as caller)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <CallBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> accepts next incoming call automatically
    Given <Contact1> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
	And I call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar from user <Contact1>
    And I open conversation with <Contact2>
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact1>
    And I see the calling bar
    When I open conversation with <Contact2>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I click on "End Call" button in another call warning modal
    Then I do not see another call warning modal
    Then I do not see the calling bar
    When I call
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar from user <Contact2>
    And I open conversation with <Contact1>
    Then I do not see the calling bar
    When I open conversation with <Contact2>
	And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | chrome      | 60      |

  @smoke @calling @debug @id1839
  Scenario Outline: Verify I can not call in browsers without WebRTC
    Given My browser does not support calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me using <CallBackend>
    Then I do not see the calling bar
    And I wait for 3 seconds
    And I see a warning
    And I see "Learn more" link in warning
    When I close the warning
    Then I do not see a warning
    And I see calling button
    When I call
    Then I see a warning
    And I see "Learn more" link in warning
    And I verify browser log is empty

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 60      |

  @regression @calling @id3083
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as callee)
	Given My browser supports calling
	Given There are 3 users where <Name> is me
	Given Myself is connected to <Contact1>,<Contact2>
	Given <Contact2> starts waiting instance using <WaitBackend>
	Given <Contact2> accepts next incoming call automatically
	Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
	Given I switch to Sign In page
	Given I Sign in using login <Login> and password <Password>
	And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    And <Contact1> calls me using <CallBackend>
	And I accept the incoming call
	Then <Contact1> verifies that call status to Myself is changed to active in <Timeout> seconds
	Then I see the calling bar from user <Contact1>
	And I open conversation with <Contact2>
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact1>
    And I see the calling bar
    When I open conversation with <Contact2>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I click on "End Call" button in another call warning modal
    Then I do not see another call warning modal
    Then I do not see the calling bar
    When I call
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar from user <Contact2>
    And I open conversation with <Contact1>
    Then I do not see the calling bar
    When I open conversation with <Contact2>
    And I end the call


    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | autocall    | chrome      | 60      |

  @regression @calling @debug @id2013
  Scenario Outline: Verify I get missed call notification when I call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I call
    Then I wait for 2 seconds
    And I end the call
    When I open conversation with <Contact>
    Then I see conversation with my missed call

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  # This has to work even in browsers, which don't support calling
  @regression @calling @debug @id2014
  Scenario Outline: Verify I get missed call notification when someone calls me
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open self profile
    When <Contact1> calls me using <CallBackend>
    And I wait for 1 seconds
    And <Contact1> stops all calls to me
    And I wait for 1 seconds
    Then I see missed call notification for conversation <Contact1>
    When I open conversation with <Contact1>
    Then I do not see missed call notification for conversation <Contact1>
    Then I see conversation with missed call from <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name  | autocall    |

  @regression @calling @debug @id1882
  Scenario Outline: People trying to call me while I'm not signed in
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    When <Contact1> calls me using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I refresh page
    And I open conversation with <Contact2>
    Then I see the calling bar from user <Contact1>
    And <Contact1> stops all calls to me
    Then I do not see the calling bar
    And I see missed call notification for conversation <Contact1>
    When I open conversation with <Contact1>
    Then I do not see missed call notification for conversation <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | autocall    | 60      |

  @regression @calling @debug @id2477
   Scenario Outline: Already on call and try to make another call (callee)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<OtherContact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When <Contact> calls me using <CallBackend>
    And I see the calling bar from user <Contact>
    And I accept the incoming call
    When I open conversation with <OtherContact>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact>
    And I see the calling bar
    When I open conversation with <OtherContact>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I click on "End Call" button in another call warning modal
    Then I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact>
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | autocall    | 60      |

  @regression @calling @debug @id1906
  Scenario Outline: Verify I can make another call while current one is ignored
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <CallWaitBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When <Contact1> calls me using <CallBackend>
    And I see the calling bar
    When I silence the incoming call
    When I open conversation with <Contact2>
    Then I do not see the calling bar
    When I call
    And I see the calling bar
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar from user <Contact2>
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | CallBackend | CallWaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | autocall    | chrome          | 60      |

  @regression @calling @debug @id1883
  Scenario Outline: Verify I can not see blocked contact trying to call me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | autocall    | 60      |

  @regression @calling @debug @id1884
  Scenario Outline: Verify I can see muted conversation person trying to call me
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    Given I muted conversation with <Contact>
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 60      |

  @staging @calling @id1907
  Scenario Outline: Verify call button is not visible in the conversation view while incoming call is in progress
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see the calling bar
    And I accept the incoming call
    And I do not see calling button
    When I end the call
    Then I do not see the calling bar
    And I see calling button

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 60      |

  @staging @calling @id1905
  Scenario Outline: Verify that outgoing call is terminated after within 1 minute timeout if nobody responds
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    And I see the calling bar
    And I wait for 60 seconds
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @calling @debug @id1875
  Scenario Outline: Already on call and try to make another call (caller)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<OtherContact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    When I open conversation with <OtherContact>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I close the another call warning modal
    And I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact>
    And I see the calling bar
    When I open conversation with <OtherContact>
    Then I do not see the calling bar
    When I call
    Then I see another call warning modal
    And I click on "End Call" button in another call warning modal
    Then I do not see another call warning modal
    Then I do not see the calling bar
    And I open conversation with <Contact>
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | chrome      | 60      |

  @regression @calling @group @debug @id3058
  Scenario Outline: Verify initiator is not a host for the call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <CallBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar from user <Contact1>
    And I see the calling bar from user <Contact2>
    When I end the call
    Then I do not see the calling bar
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | ChatName              | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | GroupCallConversation | chrome      | 60      |


  @regression @calling @group @debug @id3064
  Scenario Outline: Verify accepting group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact2>,<Contact3>,<Contact4> starts waiting instance using <WaitBackend>
    Given <Contact2> accepts next incoming call automatically
    Given <Contact3> accepts next incoming call automatically
    Given <Contact4> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName> using <CallBackend>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact3> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact4> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the calling bar
    When I accept the incoming call
    Then I see the calling bar from user <Contact1>
    And I see the calling bar from user <Contact2>
    And I see the calling bar from user <Contact3>
    And I see the calling bar from user <Contact4>
    And I wait for 20 seconds
    And <Contact2> verifies to have 4 flows
    And <Contact3> verifies to have 4 flows
    And <Contact4> verifies to have 4 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
    And <Contact3> verifies that all flows have greater than 0 bytes
    And <Contact4> verifies that all flows have greater than 0 bytes
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | Contact3  | Contact4  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | user5Name | GroupCallConversation | autocall    | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | user5Name | GroupCallConversation | autocall    | firefox     | 60      |

  @staging @calling @group @debug @id3057
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given My browser supports calling
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Contact2>,<Contact3>,<Contact4>,<Contact5> starts waiting instance using <WaitBackend>
    Given <Contact2> accepts next incoming call automatically
    Given <Contact3> accepts next incoming call automatically
    Given <Contact4> accepts next incoming call automatically
    Given <Contact5> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName> using <CallBackend>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact3> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact4> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact5> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the calling bar
    When I accept the incoming call
    Then I see full call warning modal
    And I close the full call warning modal
    When I call
    Then I see full call warning modal
    And I click on "Ok" button in full call warning modal

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | Contact3  | Contact4  | Contact5  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | user5Name | user6Name | GroupCallConversation | autocall    | chrome      | 60      |


  @regression @calling @group @debug @id3231
  Scenario Outline: Verify initiating group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts waiting instance using <WaitBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> accepts next incoming call automatically
    Given <Contact3> accepts next incoming call automatically
    Given <Contact4> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I call
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact3> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact4> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar from user <Contact1>
    And I see the calling bar from user <Contact2>
    And I see the calling bar from user <Contact3>
    And I see the calling bar from user <Contact4>
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | Contact3  | Contact4  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | user5Name | GroupCallConversation | chrome      | 60      |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | user5Name | GroupCallConversation | firefox     | 60      |


  @regression @calling @group @debug @id3065
  Scenario Outline: Verify ignoring group call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <WaitBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName> using <CallBackend>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    When I silence the incoming call
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | GroupCallConversation | autocall    | chrome      | 60      |

  @staging @calling @group @debug @id3060
  Scenario Outline: Verify leaving and coming back to the call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts waiting instance using <WaitBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I call
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar
    When I end the call
    Then I do not see the calling bar
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    When I join call
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | GroupCallConversation | chrome      | 60      |
