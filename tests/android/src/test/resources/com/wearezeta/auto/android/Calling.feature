Feature: Calling

  @C693 @id373 @calling_basic @rc
  Scenario Outline: Verify missed call indicator in conversations list and system message inside conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see incoming call
    And <Contact> stops all calls to me
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
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see incoming call
    And I see incoming call from <Contact>
    And I swipe to ignore the call
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | autocall    | 60      |

  @C698 @id727 @calling_basic @rc
  Scenario Outline: I can start 1:1 call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I swipe on text input
    And I press Call button
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
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact1> calls me using <CallBackend>
    And I swipe to accept the call
#TODO activity check
    And I see outgoing call

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @C710 @id1497 @calling_basic @rc
  Scenario Outline: Receive call while Wire is running in the background
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> calls me using <CallBackend>
    Then I see incoming call
    And I see incoming call from <Contact>
    And I swipe to accept the call
#TODO activity check

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C711 @id1499 @calling_basic @rc
  Scenario Outline: Receive call while mobile in sleeping mode(screen locked)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I lock the device
    And <Contact> calls me using <CallBackend>
    Then I see incoming call
    And I see incoming call from <Contact>
    And I swipe to accept the call
    Then I see ongoing call

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
  @C404 @id347 @calling_basic @mute
  Scenario Outline: Send text, image and knock while in the call with same user
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And <Contact> calls me using <CallBackend>
    And I see incoming call
    And I see incoming call from <Contact>
    And I swipe to accept the call
#TODO check activity
#    When I swipe on text input
#    And I press Ping button
#    Then I see Ping message <Msg> in the dialog
#    When I tap on text input
#    And I type the message "<Message>" and send it
#    Then I see my message "<Message>" in the dialog
#    When I swipe on text input
#    And I press Add Picture button
#    And I press "Take Photo" button
#    And I press "Confirm" button
#    And I scroll to the bottom of conversation view
#    Then I see new photo in the dialog


    Examples:
      | Name      | Contact   | CallBackend | Message                   | Msg        |
      | user1Name | user2Name | autocall    | simple message in english | YOU PINGED |

#TODO Postponed button state check
  @C721 @id2210 @calling_basic @rc @rc42
  Scenario Outline: Calling bar buttons are clickable and change their states
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And <Contact> calls me using <CallBackend>
    And I swipe to accept the call
    When I remember the current state of <MuteBtnName> button
    And I press <MuteBtnName> button
    Then I see <MuteBtnName> button state is changed
    When I remember the current state of <SpeakerBtnName> button
    And I press <SpeakerBtnName> button
    Then I see <SpeakerBtnName> button state is changed
    When I hang up ongoing call
    And I do not see ongoing call
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend | SpeakerBtnName | MuteBtnName |
      | user1Name | user2Name | autocall    | Speaker        | Mute        |

# DEPRECATED
  @C422 @id2212 @calling_basic @rc @mute
  Scenario Outline: Correct calling bar in different places
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact1> calls me using <CallBackend>
    And I answer the call from the overlay bar
    Then I see calling overlay Big bar
    And I navigate back from dialog page
    And I open Search by tap
    And I see calling overlay Micro bar
    And I press Clear button
    And I tap on my avatar
    And I see personal info page
    And I see calling overlay Micro bar
    And I close Personal Info Page
    And I see calling overlay Micro bar
    And I tap on contact name <Contact2>
    And I see calling overlay Mini bar

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

#TODO Postponed button state check
  @C431 @id3239 @calling_basic
  Scenario Outline: Calling bar buttons are clickable and change their states in a group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe on text input
    And I press Call button
    Then I see call overlay
    When I remember the current state of <MuteBtnName> button
    And I press Mute button
    Then I see <MuteBtnName> button state is changed
    When I remember the current state of <SpeakerBtnName> button
    And I press Speaker button
    Then I see <SpeakerBtnName> button state is changed
    When I press Cancel call button
    Then I do not see call overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    | SpeakerBtnName | MuteBtnName |
      | user1Name | user2Name | user3Name | ChatForGroupCall | Speaker        | Mute        |

#TODO use waiting instances instead of autocall
  @C807 @id3240 @calling_basic @rc @rc42
  Scenario Outline: I can start group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I swipe on text input
    And I press Call button
    Then I see outgoing call
    When <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
#TODO check activity
    Then I see ongoing call
    When <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    Then I do not see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @C804 @id3172 @regression @rc @rc42
  Scenario Outline: I can join group call in foreground
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact2>,<Contact3>,<Contact4> start waiting instance using <CallBackend>
    Given <Contact2>,<Contact3>,<Contact4> accept next incoming call automatically
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend2>
#TODO activity check
    Then I see incoming call
    When I swipe to accept the call
#TODO activity check
    Then I see ongoing call
    # FIXME: Temporarily disable calling flows verification since this is unstable on webapp side
    # And <Contact2>,<Contact3>,<Contact4> verify that waiting instance status is changed to active in <Timeout> seconds
    # And I wait for 5 seconds
    # Then <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    # Then <Contact2>,<Contact3>,<Contact4> verify that all flows have greater than 0 bytes

    Examples:
      | CallBackend | CallBackend2 | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    | Timeout |
      | chrome      | autocall     | user1Name | user2Name | user3Name | user4Name | user5Name | ChatForGroupCall | 60      |

#TODO Probably flaky because of https://wearezeta.atlassian.net/browse/AN-3480
  @C805 @id3174 @calling_basic @rc
  Scenario Outline: (AN-3396) I can join group call after I ignored it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using chrome
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    Then I see incoming call
    When I swipe to ignore the call
    And I swipe on text input
    And I press Call button
    Then I see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

#TODO DEFECT https://wearezeta.atlassian.net/browse/AN-3480
  @C802 @id3168 @calling_basic @rc
  Scenario Outline: I can join group call after I leave it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    When I hang up ongoing call
    Then I do not see incoming call
    And I wait for 20 seconds
    When I swipe on text input
    And I press Call button
    Then I see ongoing call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    Then I do not see ongoing call

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

# https://wearezeta.atlassian.net/browse/AN-3480
  @C424 @id3164 @calling_basic
  Scenario Outline: Verify creating the call with a maximum amount of the people
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And <Contact3> calls <GroupChatName> using <CallBackend>
    And <Contact4> calls <GroupChatName> using <CallBackend>
    Then I see incoming call
    When I swipe to accept the call
    Then I see ongoing call
    And I see 4 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    And <Contact3> stops all calls to <GroupChatName>
    And <Contact4> stops all calls to <GroupChatName>

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | user4Name | user5Name | MaxGroupCallChat |

#https://wearezeta.atlassian.net/browse/AN-3480
  @C425 @id3165 @calling_basic
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And <Contact3> calls <GroupChatName> using <CallBackend>
    And <Contact4> calls <GroupChatName> using <CallBackend>
    And <Contact5> calls <GroupChatName> using <CallBackend>
    When I swipe to accept the call
#TODO alerts
    Then I see group call is full alert
    And I close group call is full alert
    And I swipe on text input
    And I press Call button
    Then I see group call is full alert
    And I close group call is full alert
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    And <Contact3> stops all calls to <GroupChatName>
    And <Contact4> stops all calls to <GroupChatName>
    And <Contact5> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | GroupChatName       | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | MaxGroupCallNegChat | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
  @C434 @id3253 @calling_basic @mute
  Scenario Outline: Verify starting outgoing 1to1 call during group call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I see incoming call
    And I do not see join group call overlay
    And I see calling overlay Big bar
    And I navigate back from dialog page
    And I tap on contact name <Contact3>
    And I swipe on text input
    And I press Call button
    And I see answer call alert
    And I start new call from answer call alert
    Then I see calling overlay Big bar
    And <Contact3> calls <Name> using <CallBackend>
    And I see 1 user take part in call
    And I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I see incoming calling message for contact <Contact3>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
  @C435 @id3255 @calling_basic @mute
  Scenario Outline: Verify cancel outgoing 1to1 call during group call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    When I answer the call from the overlay bar
    And I see calling overlay Big bar
    And I navigate back from dialog page
    And I tap on contact name <Contact3>
    And I swipe on text input
    And I press Call button
    And I see answer call alert
    And I cancel new call from answer call alert
    And I see incoming calling message for contact <GroupChatName>
    And I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I see calling overlay Big bar
    And I see 2 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
# also there's no alert
  @C427 @id3180 @calling_advanced @mute
  Scenario Outline: Verify receiving 1to1 call during group call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I see incoming call
    When I swipe to accept the call
    And I see ongoing call
    And <Contact3> calls <Name> using <CallBackend>
    And I see incoming calling message for contact <Contact3>
    And I answer the call from the overlay bar
    And I see end current call alert
    And I start new call from end current call alert
    And I see calling overlay Big bar
    And I see 1 user take part in call
    And I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I see incoming calling message for contact <Contact3>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
# also there's no alert
  @C806 @id3176 @calling_advanced @rc @mute
  Scenario Outline: (AN-3140) Verify receiving group call during 1to1 call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact3>
    And <Contact3> calls <Name> using <CallBackend>
    And I see incoming calling message for contact <Contact3>
    Then I see call overlay
    When I answer the call from the overlay bar
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I see incoming calling message for contact <GroupChatName>
    And I answer the call from the overlay bar
    And I see end current call alert
    And I start new call from end current call alert
    And I see calling overlay Big bar
    And I see 2 users take part in call
    And I navigate back from dialog page
    And I tap on contact name <Contact3>
    And I see incoming calling message for contact <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
# also there's no alert
  @C428 @id3181 @calling_advanced @mute
  Scenario Outline: Verify receiving 1to1 call during group call and ignoring it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I answer the call from the overlay bar
    And I see calling overlay Big bar
    And <Contact3> calls <Name> using <CallBackend>
    And I see incoming calling message for contact <Contact3>
    And I swipe to accept the call
    Then I see end current call alert
    And I cancel new call from end current call alert
    And I see incoming calling message for contact <Contact3>
    When I swipe to ignore the call
    Then I see ongoing call
    And I see 2 users take part in call
    When I navigate back from dialog page
    And I tap on contact name <Contact3>
    Then I see incoming calling message for contact <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

# https://wearezeta.atlassian.net/browse/AN-3480
  @C803 @id3170 @calling_basic @rc @rc42
  Scenario Outline: Verify accepting group call in background
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see incoming call
    Then I see incoming call from <GroupChatName>
    And I swipe to accept the call
    And I see ongoing call
    And I see 2 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

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
    And I swipe on text input
    And I press Call button
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
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    And I see incoming call
    And I swipe to accept the call
    Then I see ongoing call
    When I minimize the application
    And I wait for 10 seconds
    And I restore the application
    Then I see ongoing call
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C405 @id369 @calling_advanced
  Scenario Outline: Other user trying to call me while I'm already in zeta call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    And <Contact1> calls me using <CallBackend>
    And I see incoming call from <Contact1>
    And I swipe to accept the call
    And I see ongoing call
    When <Contact2> calls me using <CallBackend>
    Then I do not see incoming call
    And <Contact1> stop all calls to me
    And I see incoming call from <Contact2>
    And <Contact2> stop all calls to me

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

# DEFECT: can not implement until I can leave the call overlay while calling
  @C429 @id3184 @regression @mute
  Scenario Outline: Verify leaving group conversation during the call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I swipe to accept the call
    And I do not see join group call overlay
    And I see ongoing call
    And I tap conversation details button
    And I press options menu button
    And I press Leave conversation menu button
    When I confirm leaving
    Then I see Contact list
    And I do not see contact list with name <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend |
      | user1Name | user2Name | user3Name | GroupCallChat | autocall    |
