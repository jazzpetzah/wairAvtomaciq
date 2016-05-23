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
    When I call
    Then I see the outgoing call controls for conversation <Contact>
    When <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    When I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see <PING> action in conversation
    And I see sent picture <PictureName> in the conversation view
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
    And User <Contact> pinged in the conversation with <Contact>
    And I see <PING> action in conversation
    And User <Contact> pinged twice in the conversation with <Contact>
    And I see <HOTPING> action in conversation
    And I hang up call with conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | PING   | HOTPING      | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again | chrome      | 20      |

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
    #And I see conversation <Contact1> is on the top
    When User <Contact2> pinged in the conversation with <Contact2>
    And I see conversation <Contact1> is on the top
    And I hang up call with conversation <Contact1>
    When User <Contact2> pinged in the conversation with <Contact2>
    #And I see conversation <Contact2> is on the top

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | autocall    |

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
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    When I hang up call with conversation <Contact>
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox     | 20      |

  @C1747 @calling
  Scenario Outline: Verify I can call a user for more than 15 mins
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
    And I hang up call with conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |

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
    And I hang up call with conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | autocall    | chrome      | 20      |

  @C119432 @regression @calling @group
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


  @staging @calling @group @calling_debug
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
    And <Contact1>,<Contact2> accept next incoming call automatically
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the outgoing call controls for conversation <ChatName1>
    And I see joined group call notification for conversation <ChatName1>
    When <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <ChatName2>
    When I ignore the call from conversation <ChatName2>
    And I open conversation with <ChatName1>
    And I see the outgoing call controls for conversation <ChatName1>
    Then I do not see the incoming call controls for conversation  <ChatName2>
    When <Contact4> stops calling <ChatName2>
    And <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <ChatName2>
    When I accept the call from conversation <ChatName2>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact4> stops calling <ChatName2>
    And <Contact4> calls <ChatName2>
    Then I see the incoming call controls for conversation <Contact4>
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
    And I see joined group call notification for conversation <ChatName2>
    And I do not see joined group call notification for conversation <ChatName1>
    And I see unjoined group call notification for conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName1 | ChatName2 | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GC1       | GC2       | autocall    | chrome      | 20      |

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
      | user1Email | user1Password | user1Name | user2Name | called | autocall    |

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
    When I hang up call with conversation <Contact2>
    Then I do not see the call controls for conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallWaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome          | 20      |

  @C1750 @regression @calling @calling_debug
  Scenario Outline: Verify I can not see blocked contact trying to call me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given <Contact> starts instance using <CallBackend>
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact> calls me
    Then <Contact> verifies that call status to Myself is changed to connecting in <Timeout> seconds
    And I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | autocall    | 20      |

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
      | user1Email | user1Password | user1Name | user2Name | autocall    | 20      |

  @staging @calling
  Scenario Outline: Verify that outgoing call is terminated after within 1 minute timeout if nobody responds
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I call
    And I see the outgoing call controls for conversation <Contact>
    And I wait for 20 seconds
    And I do not see the outgoing call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

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
    Given <Contact2>,<Contact3>,<Contact4> starts instance using <WaitBackend>
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
    And <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    And <Contact2>,<Contact3>,<Contact4> verify that all flows have greater than 0 bytes
    When I hang up call with conversation <ChatName>
    Then I see the join call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName  | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall    | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | GroupCall | autocall    | firefox     | 20      |

  @staging @calling @group @calling_debug
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given My browser supports calling
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Contact2>,<Contact3>,<Contact4>,<Contact5> starts instance using <WaitBackend>
    Given <Contact2>,<Contact3>,<Contact4>,<Contact5> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When <Contact1> calls <ChatName> using <CallBackend>
    And <Contact2>,<Contact3>,<Contact4>,<Contact5> verify that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And I see the incoming call controls for conversation <ChatName>
    When I accept the call from conversation <ChatName>
    And I wait for 1 seconds
    Then I see full call warning modal
    And I close the full call warning modal
    When I join call of conversation <ChatName>
    And I wait for 1 seconds
    Then I see full call warning modal
    And I click on "Ok" button in full call warning modal

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | GroupCallConversation | autocall    | chrome      | 20      |

  @C1813 @regression @calling @group @calling_debug
  Scenario Outline: Verify initiating group call
    Given My browser supports calling
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I call
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName>
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
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | autocall    | chrome      | 20      |

  @regression @calling @group @calling_debug
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
    And <Contact1>,<Contact2> verify to have 3 flows
    And <Contact1>,<Contact2> verify that all flows have greater than 0 bytes
    And I see the ongoing call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | chrome      | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | firefox     | 20      |

  @staging @calling @group @calling_debug
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
    And I wait for 20 seconds
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    Then I join call of conversation <ChatName>
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies that call status to <ChatName> is changed to active in <Timeout> seconds
    And <Contact1> verify to have 3 flows
    And <Contact1> verify that all flows have greater than 0 bytes
    And I see the ongoing call controls for conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName              | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallConversation | autocall    | chrome      | 20      |

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


  @staging @calling @group @debug @id3073
  Scenario Outline: Verify receiving 1-to-1 call during group call
    Given My browser supports calling
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the outgoing call controls for conversation <ChatName1>
    And I see joined group call notification for conversation <ChatName1>
    When <Contact3> calls me using <CallBackend>
    Then I see the incoming call controls for conversation <Contact3>
    When I ignore the call from conversation <Contact3>
    Then I see the outgoing call controls for conversation <ChatName1>
    Then I do not see the incoming call controls for conversation <Contact3>
    When <Contact3> stops all calls to me
    And <Contact3> calls me using <CallBackend>
    Then I see the incoming call controls for conversation <Contact3>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact3> stops all calls to me
    And <Contact3> calls me using <CallBackend>
    Then I see the incoming call controls for conversation <Contact3>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    When <Contact3> stops all calls to me
    And <Contact3> calls me using <CallBackend>
    When I accept the call from conversation <Contact3>
    Then I see another call warning modal
    When I click on "Answer" button in another call warning modal
    Then I do not see another call warning modal
    And I see the incoming call controls for conversation <ChatName1>
    And I see joined group call notification for conversation <Contact3>
    And I do not see joined group call notification for conversation <ChatName1>
    And I see unjoined group call notification for conversation <ChatName1>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | ChatName1 | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GC1       | autocall    | chrome      | 20      |

  @C129912 @staging
  Scenario Outline: Verify I can not call in a conversation with no one else left
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instances using <WaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password1>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I call
    Then I see the outgoing call controls for conversation <ChatName>
    When I hang up call with conversation <ChatName>
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login2> and password <Password2>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see calling button
    When I click People button in group conversation
    And I click Leave button on Group Participants popover
    And I click confirm leave group conversation on Group Participants popover
    Then I do not see Contact list with name <ChatName>
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login3> and password <Password3>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see calling button
    When I click People button in group conversation
    And I click Leave button on Group Participants popover
    And I click confirm leave group conversation on Group Participants popover
    Then I do not see Contact list with name <ChatName>
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login1> and password <Password1>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see 0 participants in the Group Participants popover
    And I do not see calling button

    Examples:
      | Login1      | Password1      | Login2      | Password2      | Login3      | Password3      | Name      | Contact1  | Contact2  | ChatName  | WaitBackend |
      | user1Email  | user1Password  | user2Email  | user2Password  | user3Email  | user3Password  | user1Name | user2Name | user3Name | EMPTY     | chrome      |