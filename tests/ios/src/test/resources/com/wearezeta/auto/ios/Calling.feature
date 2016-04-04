Feature: Calling

  @C2079 @calling_basic @id1831
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>
    And I click missed call button to call contact <Contact>
    And I see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C3180 @rc @calling_basic @clumsy @id908
  Scenario Outline: Verify starting outgoing call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    Then I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2107 @C2073 @calling_basic @IPv6 @id2067 @id909
  Scenario Outline: Verify starting and ending outgoing call by same person
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2108 @rc @calling_basic @clumsy @id896
  Scenario Outline: Verify ignoring of incoming call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C2111 @rc @calling_basic @clumsy @IPv6 @id2093
  Scenario Outline: (ZIOS-5534) Verify acepting and ending incoming call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    And I see call status message contains "<Contact>"
    And I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C2072 @calling_basic @id902
  Scenario Outline: Receiving missed call notification from one user
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C348 @calling_basic @id1228
  Scenario Outline: Verify missed call indicator appearance (list)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User Myself removes his avatar picture
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    And User <Contact> sends <Number> encrypted messages to user Myself
    Then I see the state of <Contact> conversation item is not changed
    When I remember the state of <Contact> conversation item
    And User <Contact1> sends <Number> encrypted messages to user Myself
    And I see first item in contact list named <Contact1>
    Then I see the state of <Contact> conversation item is not changed

    Examples:
      | Name      | Contact   | Contact1  | Number | CallBackend |
      | user1Name | user2Name | user3Name | 2      | autocall    |

  @C2080 @calling_basic @id2296
  Scenario Outline: Screenlock device when in the call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    And I tap Audio Call button
    When I lock screen for 5 seconds
    Then I see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C3164 @calling_advanced @id2645
  Scenario Outline: 3rd person tries to call me after I initiate a call to somebody
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User Myself removes his avatar picture
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> starts instance using <CallBackend2>
    Given <Contact1> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    # This is to have this convo selected
    Given I tap on contact name <Contact1>
    Given I navigate back to conversations list
    When I remember the state of <Contact2> conversation item
    And I tap on contact name <Contact1>
    And I tap Audio Call button
    And I see Calling overlay
    And <Contact2> calls me
    And I see call status message contains "<Contact2> calling"
    And I tap Ignore button on Calling overlay
    And <Contact2> stops calling me
    And I tap Leave button on Calling overlay
    And I navigate back to conversations list
    And I see the state of <Contact2> conversation item is changed
    And I tap on contact name <Contact2>
    Then I see missed call from contact <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | chrome      | autocall     |

  @C2082 @calling_basic @id2646
  Scenario Outline: Put app into background after initiating call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    Then I close the app for 5 seconds
    And I see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2077 @calling_basic @id933
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> starts instance using <CallBackend2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | chrome      | autocall     | 30      |

  @C2074 @calling_basic @id913
  Scenario Outline: Verify starting and ending outgoing call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    And I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | CallBackend2 |
      | user1Name | user2Name | chrome      | autocall     |

  @C2046 @rc @calling_basic @IPv6 @id2682
  Scenario Outline: Verify accepting group call in foreground
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend2>
    Given <Contact2> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOfAvatars> avatars on the Calling overlay
    # And I wait for 10 seconds
    # Then <Contact2> verify to have 2 flows
    # Then <Contact2> verify that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName      | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | AcceptingGROUPCALL | chrome      | autocall     | 2               |

  @C2047 @calling_basic @id2683
  Scenario Outline: Verify ignoring group call in foreground
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Ignore button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | CallBackend |
      | user1Name | user2Name | user3Name | IgnoringGROUPCALL | autocall    |

  @C2050 @rc @calling_advanced @id2686
  Scenario Outline: (ZIOS-5587)Verify receiving group call during 1-to-1 call (and accepting it)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact1>
    When <Contact1> calls me
    And I tap Accept button on Calling overlay
    When <Contact2>,<Contact3> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    # And I see Accept second call alert
    # And I press Accept button on alert
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOfAvatars> avatars on the Calling overlay
    And I see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | GROUPCALL     | autocall    | 2               |

  @C2042 @rc @calling_advanced @id2678
  Scenario Outline: Verify leaving and coming back to the call in 20 sec
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Ignore button on Calling overlay
    Then I do not see Calling overlay
    And I wait for 20 seconds
    And I tap Audio Call button
    Then I see Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOfAvatars> avatars on the Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | RejoinGROUPCALL | autocall    | 2               |

  @C2054 @rc @calling_advanced @id2690 @ZIOS-6010
  Scenario Outline: Verify receiving 1-to-1 call during group call (and accepting it)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on group chat with name <GroupChatName>
    When <Contact1>,<Contact2> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    And I see Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOfAvatars> avatars on the Calling overlay
    When <Contact3> calls me
    And I see call status message contains "<Contact3> calling"
    And I tap Accept button on Calling overlay
    # And I see Accept second call alert
    # And I press Accept button on alert
    Then I see Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOf1on1CallAvatars> avatars on the Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend | NumberOfAvatars | NumberOf1on1CallAvatars |
      | user1Name | user2Name | user3Name | user4Name | GROUPCALL     | autocall    | 2               | 1                       |

  @C2065 @rc @calling_basic @clumsy @IPv6 @id3270
  Scenario Outline: Verify possibility of starting group call
    Given There are 7 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    Then I see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | GroupChatName  |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | StartGROUPCALL |

  @C2048 @rc @calling_advanced @id2684
  Scenario Outline: Verify possibility to join call after 45 seconds of starting it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I wait for 45 seconds
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    Then I see Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    # Then I see <NumberOfAvatars> avatars on the Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | WaitGROUPCALL | autocall    | 2               |

  @C2039 @calling_advanced @id2673 @noAcceptAlert @ZIOS-6069
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    Then I see group call is Full message

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | FullGROUPCALL | autocall    |

  @C2068 @rc @calling_basic @id880
  Scenario Outline: Verify putting client to the background during 1-to-1 call
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
    And <Contact> verifies that call status to me is changed to active in 2 seconds

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C77933 @calling_advanced @noAcceptAlert
  Scenario Outline: Verify calling from an upper toolbar in a group conversation with more than 10 people
    Given There are <UsersAmount> users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    Then I see Too many people alert

    Examples:
      | Name      | GroupChatName  | UsersAmount |
      | user1Name | StartGROUPCALL | 11          |

  @C2040 @staging
  Scenario Outline: Verify initiator is not a host for the call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see call status message contains "<GroupChatName> RINGING"
    And I tap Accept button on Calling overlay
    Then I see Calling overlay
    # FIXME: There is an AVS<>iOS bug, which prevents autocall instances to be properly connected being in the same network
    #Then I see <NumberOfAvatars> avatars on the Calling overlay
    And I wait for 5 seconds
    Then <Contact1> stops calling <GroupChatName>
    And <Contact1> verifies that call status to <GroupChatName> is changed to destroyed in 2 seconds
    And <Contact2> verifies that call status to <GroupChatName> is changed to active in 2 seconds
    And I wait for 5 seconds

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName      | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | AcceptingGROUPCALL | autocall    | 2               |
