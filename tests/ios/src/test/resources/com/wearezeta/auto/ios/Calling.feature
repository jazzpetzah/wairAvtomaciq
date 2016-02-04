Feature: Calling

  @C2079 @calling_basic @id1831
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>
    And I click missed call button to call contact <Contact>
    And I see calling message

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C3180 @calling_basic @rc @id908
  Scenario Outline: Verify starting outgoing call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press call button
    Then I see mute call, end call and speakers buttons
    And I see calling message

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
    And I click plus button next to text input
    And I press call button
    Then I see mute call, end call and speakers buttons
    And I see calling message
    When I end started call
    Then I dont see calling page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2108 @calling_basic @rc @id896
  Scenario Outline: Verify ignoring of incoming call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I ignore incoming call
    Then I dont see incoming call page

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C2111 @calling_basic @rc @IPv6 @id2093
  Scenario Outline: (ZIOS-5534)Verify accepting incoming call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    And I tap on contact name <Contact>
    Then I see mute call, end call and speakers buttons
    And I see started call message for contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C2072 @calling_basic @id902
  Scenario Outline: Receiving missed call notification from one user
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C348 @calling_basic @rc @id1228
  Scenario Outline: Verify missed call indicator appearance (list)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User Myself removes his avatar picture
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And <Contact> calls me using <CallBackend>
    And <Contact> stops all calls to me
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
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    And I click plus button next to text input
    And I press call button
    And I see mute call, end call and speakers buttons
    Then I lock screen for 5 seconds
    And I see mute call, end call and speakers buttons
    And I end started call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | firefox     |

  @C3164 @calling_advanced @id2645
  Scenario Outline: 3rd person tries to call me after I initiate a call to somebody
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User Myself removes his avatar picture
    Given <Contact1> starts waiting instance using <CallBackend>
    Given <Contact1> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    # This is to have this convo selected
    Given I tap on contact name <Contact1>
    Given I navigate back to conversations list
    When I remember the state of <Contact2> conversation item
    And I tap on contact name <Contact1>
    And I click plus button next to text input
    And I press call button
    And I see mute call, end call and speakers buttons
    And <Contact2> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact2>
    And I ignore incoming call
    And I see mute call, end call buttons
    And <Contact2> stops all calls to me
    And I end started call
    And I navigate back to conversations list
    And I see the state of <Contact2> conversation item is changed
    And I tap on contact name <Contact2>
    Then I see missed call from contact <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | firefox     | autocall     |

  @C2082 @calling_basic @id2646
  Scenario Outline: Put app into background after initiating call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press call button
    And I see mute call, end call and speakers buttons
    Then I close the app for 5 seconds
    And I see mute call, end call and speakers buttons

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | firefox     |

  @C2077 @calling_basic @id933
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call and speakers buttons
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | firefox     | autocall     | 30      |

  @C2074 @calling_basic @id913
  Scenario Outline: I want to end the call from the ongoing voice overlay
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press call button
    And I see mute call, end call and speakers buttons
    And I end started call
    Then I dont see calling page
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    And I see mute call, end call and speakers buttons
    And <Contact> stops all calls to me
    And I dont see calling page

    Examples:
      | Name      | Contact   | CallBackend | CallBackend2 |
      | user1Name | user2Name | firefox     | autocall     |

  @C2046 @calling_basic @rc @IPv6 @id2682
  Scenario Outline: Verify accepting group call in foreground
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar
    And I wait for 10 seconds
    Then <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    Then <Contact2>,<Contact3>,<Contact4> verify that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName      | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | AcceptingGROUPCALL | firefox     | autocall     | 5               |

  @C2047 @calling_basic @id2683
  Scenario Outline: Verify ignoring group call in foreground
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I ignore incoming call
    Then I dont see incoming call page
    Then I see Join Call bar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | IgnoringGROUPCALL | firefox     | autocall     |

  @C2050 @calling_advanced @rc @id2686
  Scenario Outline: (ZIOS-5587)Verify receiving group call during 1-to-1 call (and accepting it)
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact1>
    When <Contact1> calls me using <CallBackend2>
    And I accept incoming call
    Then I see mute call, end call and speakers buttons
    When <Contact2> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I see Accept second call alert
    And I press Accept button on alert
    And I navigate back to conversations list
    And I tap on chat I am in a call with name <GroupChatName>
    Then I see <NumberOfAvatars> avatars in the group call bar
    Then I see mute call, end call and speakers buttons

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | GROUPCALL     | chrome      | autocall     | 4               |

  @C2062 @calling_advanced @id2700
  Scenario Outline: Verify renaming group during group call
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact2> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I see mute call, end call and speakers buttons
    And I open group conversation details
    And I change group conversation name to <ChatName>
    Then I see correct conversation name <ChatName>
    And I close group info page
    And I see you renamed conversation to <ChatName> message shown in Group Chat
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName   | CallBackend | CallBackend2 | ChatName | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | RenameGROUPCALL | firefox     | autocall     | NewName  | 5               |

  @C2058 @calling_advanced @id2696
  Scenario Outline: Verify leaving group conversation during the group call
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I open group conversation details
    And I press leave conversation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I dont see calling page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | LEAVEINGROUPCALL | firefox     | autocall     |

  @C2042 @calling_advanced @rc @id2678
  Scenario Outline: Verify leaving and coming back to the call in 20 sec
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact2> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I see mute call, end call and speakers buttons
    And I see <NumberOfAvatars> avatars in the group call bar
    And I end started call
    Then I see Join Call bar
    And I wait for 20 seconds
    And I see Join Call bar
    And I rejoin call by clicking Join button
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName   | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | RejoinGROUPCALL | firefox     | autocall     | 5               |

  @C2054 @calling_advanced @rc @id2690
  Scenario Outline: (ZIOS-5587)Verify receiving 1-to-1 call during group call (and accepting it)
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact2> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    And I wait for 3 seconds
    And I tap on group chat with name <GroupChatName>
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar
    When <Contact5> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact5>
    And I accept incoming call
    And I see Accept second call alert
    And I press Accept button on alert
    And I swipe right on Dialog page
    And I tap on contact name <Contact5>
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOf1on1CallAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | GroupChatName | CallBackend | CallBackend2 | NumberOfAvatars | NumberOf1on1CallAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | GROUPCALL     | firefox     | autocall     | 5               | 2                       |

  @C2065 @calling_basic @rc @IPv6 @id3270
  Scenario Outline: Verify possibility of starting group call
    Given There are 10 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    And I press call button
    Then I see mute call, end call and speakers buttons
    Then I see calling to a group message

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Contact8  | Contact9   | GroupChatName  |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | user9Name | user10Name | StartGROUPCALL |

  @C2048 @calling_advanced @rc @id2684
  Scenario Outline: Verify possibility to join call after 45 seconds of starting it
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    And <Contact2> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I wait for 45 seconds
    When I tap on group chat with name <GroupChatName>
    And I see Join Call bar
    And I rejoin call by clicking Join button
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | WaitGROUPCALL | firefox     | autocall     | 5               |

  @C2059 @calling_advanced @id2697
  Scenario Outline: Verify removing people from the conversation who joined the group call
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    And I press call button
    And I wait for 5 seconds
    And I see mute call, end call and speakers buttons
    And I wait for 3 seconds
    And I see <NumberOfAvatars> avatars in the group call bar
    And I open group conversation details
    And I select participant <Contact2>
    And I click Remove
    And I confirm remove
    And I click close user profile page button
    Then I see that <Contact2> is not present on group chat info page
    And I close group info page
    # The person is still in the call after removal. This is bug-feature in iOS
    Then I see <NewNumberOfAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName   | CallBackend | NumberOfAvatars | NewNumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | RemoveGROUPCALL | firefox     | 5               | 5                  |

  @C2039 @calling_advanced @id2673 @noAcceptAlert
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I dismiss alert
    Given I accept First Time overlay if it is visible
    Given I accept alert
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact5> calls <GroupChatName> using <CallBackend2>
    And I see incoming group calling message
    And I accept incoming call
    Then I see group call is Full message

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | GroupChatName | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | FullGROUPCALL | firefox     | autocall     |

  @C2068 @calling_basic @rc @id880
  Scenario Outline: Verify putting client to the background during 1-to-1 call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call and speakers buttons
    And I see started call message for contact <Contact>
    When I close the app for 5 seconds
    Then I see mute call, end call and speakers buttons
    And I see started call message for contact <Contact>
    And <Contact> verifies that call status to me is changed to active in 2 seconds

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C2061 @calling_advanced @id2699
  Scenario Outline: (Bug ZIOS-5436)Verify adding people to group conversation during the group call (Me gets added)
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> is connected to <Contact2>,<Contact3>,<Contact4>
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact1>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact2> calls <GroupChatName> using <CallBackend2>
    And User <Contact1> adds User <Name> to group chat <GroupChatName>
    And I see user <GroupChatName> in contact list
    When I tap on group chat with name <GroupChatName>
    And I see Join Call bar
    And I rejoin call by clicking Join button
    Then I see mute call, end call and speakers buttons
    Then I see <NumberOfAvatars> avatars in the group call bar

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName  | CallBackend | CallBackend2 | NumberOfAvatars |
      | user1Name | user2Name | user3Name | user4Name | user5Name | AddMeGROUPCALL | chrome      | autocall     | 4               |