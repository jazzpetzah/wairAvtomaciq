Feature: Calling

  @C693 @calling_basic @rc
  Scenario Outline: Verify missed call indicator in conversations list and system message inside conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When <Contact> calls me
    Then I see incoming call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see incoming call
    When I tap on conversation name <Contact>
    Then I see missed call from <Contact> in the conversation

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall       | 60      |

  @C698 @calling_basic @rc
  Scenario Outline: I can start 1:1 call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing call
    When I hang up ongoing call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call
#TODO check for system messages

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C710 @calling_basic @rc
  Scenario Outline: I can join 1:1 call while Wire in the background or screen locked
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I minimize the application
    And <Contact> calls me
    Then I wait up to <Timeout> seconds for incoming call from <Contact>
    When I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    When I restore the application
    Then I do not see ongoing call
    When I lock the device
    And <Contact> calls me
    Then I wait up to <Timeout> seconds for incoming call from <Contact>
    When I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    When I unlock the device
    Then I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall       | 60      |

  @C721 @calling_basic @rc @legacy
  Scenario Outline: Calling bar buttons are clickable and change their states
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And <Contact> calls me
    Then I see incoming call
    And I see incoming call from <Contact>
    When I swipe to accept the call
    Then I see ongoing call
    When I remember state of mute button for ongoing call
    And I tap mute button for ongoing call
    Then I see state of mute button has changed for ongoing call
    When I remember state of speaker button for ongoing call
    And I tap speaker button for ongoing call
    Then I see state of speaker button has changed for ongoing call
    When I hang up ongoing call
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | zcall       |

  @C431 @calling_basic
  Scenario Outline: Calling bar buttons are clickable and change their states in a group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    And <Contact2> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    When I remember state of mute button for ongoing call
    And I tap mute button for ongoing call
    Then I see state of mute button has changed for ongoing call
    When I remember state of speaker button for ongoing call
    And I tap speaker button for ongoing call
    Then I see state of speaker button has changed for ongoing call
    When I hang up ongoing call
    Then I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    | CallBackend |
      | user1Name | user2Name | user3Name | ChatForGroupCall | zcall       |

  @C807 @calling_basic @rc @legacy
  Scenario Outline: I can start group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Users <Contact1>,<Contact2> set the unique username
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact1>,<Contact2> accept next incoming call automatically
    And I see ongoing call
    And I hang up ongoing call
    Then I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    | CallBackend |
      | user1Name | user2Name | user3Name | ChatForGroupCall | chrome      |

  @C804 @regression @rc @legacy
  Scenario Outline: I can join group call in foreground
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> starts instance using <CallBackend2>
    Given Users <Contact2>,<Contact3>,<Contact4> set the unique username
    Given <Contact2>,<Contact3>,<Contact4> start instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And <Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    And <Contact1> calls <GroupChatName>
    # TODO: activity check
    Then I see incoming call
    When I swipe to accept the call
    # TODO: activity check
    Then I see ongoing call
    # FIXME: Temporarily disable calling flows verification since this is unstable on webapp side
    # And <Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    # And I wait for 5 seconds
    # Then <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    # Then <Contact2>,<Contact3>,<Contact4> verify that all flows have greater than 0 bytes

    Examples:
      | CallBackend | CallBackend2 | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    | Timeout |
      | chrome      | zcall        | user1Name | user2Name | user3Name | user4Name | user5Name | ChatForGroupCall | 60      |

  @C803 @calling_basic @rc @legacy
  Scenario Outline: I can join group call while Wire in the background
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I minimize the application
    And <Contact1>,<Contact2> calls <GroupChatName>
    Then I wait up to <Timeout> seconds for incoming call from <GroupChatName>
    When I swipe to accept the call
    And I see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend | Timeout |
      | user1Name | user2Name | user3Name | GroupCallChat | zcall       | 60      |

  @C802 @calling_basic @rc
  Scenario Outline: I can join group call after I ignore or leave it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    Then I see incoming call
    When I swipe to ignore the call
    And I wait for 10 seconds
    And I tap Audio Call button from top toolbar
    Then I see ongoing call
    When I hang up ongoing call
    Then I do not see incoming call
    And I wait for 10 seconds
    And I tap Audio Call button from top toolbar
    And I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | zcall       | user1Name | user2Name | user3Name | ChatForGroupCall |

  @C424 @calling_basic
  Scenario Outline: Verify creating the call with a maximum amount of the people
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    |
      | zcall       | user1Name | user2Name | user3Name | user4Name | user5Name | MaxGroupCallChat |

  @C425 @calling_basic
  Scenario Outline: (AN-4376) Verify too many people in the group call
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>, <Contact10>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10> calls <GroupChatName>
    And I see incoming call
    And I wait for 5 seconds
    And I swipe to accept the call
    Then I see alert message containing "<AlertTitle>" in the title

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Contact8  | Contact9   | Contact10  | GroupChatName       | AlertTitle              | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | user9Name | user10Name | user11Name | MaxGroupCallNegChat | Too many people to call | zcall       |

  @C806 @calling_advanced @regression
  Scenario Outline: Verify incoming group call not showed during ongoing 1:1 call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact3>
    And <Contact3> calls <Name>
    Then I see incoming call from <Contact3>
    When I swipe to accept the call
    Then I see ongoing call
    When <Contact1>,<Contact2> calls <GroupChatName>
    Then I do not see incoming call
    When <Contact3> stop calling me
    Then I see incoming call from <GroupChatName>
    When <Contact1> stop calling <GroupChatName>
    Then I do not see incoming call

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | zcall       |

  @C428 @calling_advanced
  Scenario Outline: Verify incoming 1:1 call not showed during ongoing group call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    Then I see incoming call from <GroupChatName>
    When I swipe to accept the call
    Then I see ongoing call
    When <Contact3> calls <Name>
    Then I do not see incoming call
    When <Contact1>,<Contact2> stop calling <GroupChatName>
    Then I see incoming call from <Contact3>
    When <Contact3> stop calling me
    Then I do not see incoming call

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | zcall       |

  @C121 @calling_basic
  Scenario Outline: Lock device screen after initiating call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    And I tap Audio Call button from top toolbar
    And I see outgoing call
    When I lock the device
    And I unlock the device
    Then I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C699 @calling_basic @rc @legacy
  Scenario Outline: I can accept incoming 1:1 call, put wire to background and restore
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When <Contact> calls me
    Then I see incoming call
    When I swipe to accept the call
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    Then I see ongoing call
    When I minimize the application
    And I restore the application
    Then I see ongoing call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall       | 60      |

  @calling_autoAnswer
  Scenario Outline: Auto Answer Call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When <Contact> calls me
    Then I see ongoing call
    And I wait for 10 seconds
    And I hang up ongoing call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | zcall       |

  @C378051 @calling_advanced
  Scenario Outline: (AUDIO-1144)Make 1:1 call loop to AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I call 10 times for 1 minute with <Contact>

    Examples:
      | Name      | Contact   | CallBackend  |
      | user1Name | user2Name | zcall:3.0.130 |
