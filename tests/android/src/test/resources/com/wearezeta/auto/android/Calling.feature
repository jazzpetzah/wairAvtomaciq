Feature: Calling

  @C693 @id373 @calling_basic @rc
  Scenario Outline: Verify missed call indicator in conversations list and system message inside conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me
    And I see incoming call
    And <Contact> stops calling me
    Then <Contact> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see incoming call
    When I tap on contact name <Contact>
    Then I see dialog with missed call from <Contact>

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | autocall    | 60      |

  @C713 @id1503 @calling_basic @rc
  Scenario Outline: Silence an incoming call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me
    And I see incoming call
    And I see incoming call from <Contact>
    And I swipe to ignore the call
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C698 @id727 @calling_basic @rc
  Scenario Outline: I can start 1:1 call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
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

  @C699 @id814 @calling_basic @rc
  Scenario Outline: I can accept incoming 1:1 call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact1> calls me
    Then I see incoming call
    And I see incoming call from <Contact1>
    When I swipe to accept the call
    Then <Contact1> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact1> stops calling me
    Then <Contact1> verifies that call status to <Name> is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact1  | CallBackend | Timeout |
      | user1Name | user2Name | autocall    | 60      |

  @C710 @id1497 @calling_basic @rc
  Scenario Outline: Receive call while Wire is running in the background
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> calls me
    # Wait for the call to appear in UI
    And I wait for 7 seconds
    Then I see incoming call from <Contact>
    When I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    When I restore the application
    Then I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | autocall    | 60      |

  @C711 @id1499 @calling_basic @rc
  Scenario Outline: Receive call while mobile in sleeping mode(screen locked)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I lock the device
    And <Contact> calls me
    # Wait for the call to appear in UI
    And I wait for 7 seconds
    Then I see incoming call from <Contact>
    When I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    When <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    When I unlock the device
    Then I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | autocall    | 60      |

  @C721 @id2210 @calling_basic @rc @rc42
  Scenario Outline: Calling bar buttons are clickable and change their states
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
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
      | user1Name | user2Name | autocall    |

  @C431 @id3239 @calling_basic
  Scenario Outline: Calling bar buttons are clickable and change their states in a group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
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
      | user1Name | user2Name | user3Name | ChatForGroupCall | autocall    |

  @C807 @id3240 @calling_basic @rc @rc42
  Scenario Outline: I can start group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When <Contact1>,<Contact2> accept next incoming call automatically
    And I see ongoing call
    And I hang up ongoing call
    Then I do not see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    | CallBackend |
      | user1Name | user2Name | user3Name | ChatForGroupCall | chrome      |

  @C804 @id3172 @regression @rc @rc42
  Scenario Outline: I can join group call in foreground
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> starts instance using <CallBackend2>
    Given <Contact2>,<Contact3>,<Contact4> start instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
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
      | chrome      | autocall     | user1Name | user2Name | user3Name | user4Name | user5Name | ChatForGroupCall | 60      |

  @C805 @id3174 @calling_basic @rc
  Scenario Outline: (AN-3396) I can join group call after I ignored it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> starts instance using chrome
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact2> accepts next incoming call automatically
    And <Contact1> calls <GroupChatName>
    Then I see incoming call
    When I swipe to ignore the call
    And I tap Audio Call button from top toolbar
    Then I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @C802 @id3168 @calling_basic @rc
  Scenario Outline: I can join group call after I leave it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    When I hang up ongoing call
    Then I do not see incoming call
    And I wait for 20 seconds
    And I tap Audio Call button from top toolbar
    Then I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @C424 @id3164 @calling_basic
  Scenario Outline: Verify creating the call with a maximum amount of the people
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1>,<Contact2>,<Contact3>,<Contact4> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <GroupChatName>
    And <Contact1>,<Contact2>,<Contact3>,<Contact4> calls <GroupChatName>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | user4Name | user5Name | MaxGroupCallChat |

  @C425 @id3165 @calling_basic
  Scenario Outline: (Disable this test, no spec for it) Verify too many people in the group call
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>,<Contact10>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>,<Contact7>,<Contact8>,<Contact9>, <Contact10>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap Audio Call button from top toolbar
    Then I see alert message containing "<AlertTitle>" in the title

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Contact8  | Contact9   | Contact10  | GroupChatName       | AlertTitle              |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | user9Name | user10Name | user11Name | MaxGroupCallNegChat | Too many people to call |

  @C427 @id3180 @calling_advanced
  Scenario Outline: Verify receiving 1to1 call during group call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see incoming call
    When I swipe to accept the call
    And I see ongoing call
    And <Contact3> calls me
    Then I do not see incoming call

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @C806 @id3176 @calling_advanced @rc
  Scenario Outline: (AN-3140, AN-3510) Verify receiving group call during 1to1 call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact3>
    And <Contact3> calls <Name>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    When <Contact1>,<Contact2> calls <GroupChatName>
    Then I do not see incoming call
#    Then I see incoming call
#    And I see incoming call from <GroupChatName>
#    When I swipe to accept the call
#    Then I see ongoing call
#    And I see 2 users take part in call

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @C428 @id3181 @calling_advanced
  Scenario Outline: (AN-3510) Verify receiving 1to1 call during group call and ignoring it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given <Contact1>,<Contact2>,<Contact3> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1>,<Contact2> calls <GroupChatName>
    And I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    When <Contact3> calls <Name>
    Then I do not see incoming call
#    And I see incoming call
#    When I swipe to ignore the call
#    Then I see ongoing call
#    And I see 2 users take part in call

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @C803 @id3170 @calling_basic @rc @rc42
  Scenario Outline: Verify accepting group call in background
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact1>,<Contact2> calls <GroupChatName>
    Then I see incoming call
    Then I see incoming call from <GroupChatName>
    And I swipe to accept the call
    And I see ongoing call

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | GroupCallChat | autocall    |

  @C121 @id2649 @calling_basic
  Scenario Outline: Lock device screen after initiating call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap Audio Call button from top toolbar
    And I see outgoing call
    When I lock the device
    And I wait for 2 seconds
    And I unlock the device
    Then I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C708 @id1467 @calling_basic @rc @rc42
  Scenario Outline: Put client into background when in the call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me
    And I see incoming call
    And I swipe to accept the call
    Then I see ongoing call
    When I minimize the application
    And I wait for 10 seconds
    And I restore the application
    Then I see ongoing call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C405 @id369 @calling_advanced
  Scenario Outline: (AN-3510) Other user trying to call me while I'm already in zeta call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    And <Contact1> calls me
    And I see incoming call from <Contact1>
    And I swipe to accept the call
    And I see ongoing call
    When <Contact2> calls me
    Then I do not see incoming call
#    Then I see incoming call
    And <Contact1>,<Contact2> stop calling me

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @calling_autoAnswer
  Scenario Outline: Auto Answer Call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me
    Then I see ongoing call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |


