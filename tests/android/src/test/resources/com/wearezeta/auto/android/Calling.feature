Feature: Calling

  @id373 @calling_basic @rc
  Scenario Outline: Verify missed call indicator in conversations list and system message inside conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    Then I do not see calling overlay Big bar
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see dialog with missed call from <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id1503 @calling_basic @rc
  Scenario Outline: Silence an incoming call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I click the ignore call button
    Then I do not see calling overlay Big bar

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id727 @calling_basic @rc
  Scenario Outline: I can start 1:1 call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Call button
    Then I see call overlay
    Then I see calling overlay Big bar

    Examples: 
      | CallBackend | Name      | Contact   |
      | autocall    | user1Name | user2Name |
      
  @id814 @calling_basic @rc
  Scenario Outline: I can accept incoming 1:1 call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact1> calls me using <CallBackend>
    And I answer the call from the overlay bar
    And I see dialog page
    Then I see calling overlay Big bar

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @id1497 @calling_basic @rc
  Scenario Outline: Receive call while Wire is running in the background
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> calls me using <CallBackend>
    Then I see the call lock screen
    And I see a call from <Contact> in the call lock screen
    And I answer the call from the lock screen
    Then I see started call message for contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id1499 @calling_basic @rc
  Scenario Outline: Receive call while mobile in sleeping mode(screen locked)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I lock the device
    And <Contact> calls me using <CallBackend>
    Then I see the call lock screen
    And I see a call from <Contact> in the call lock screen
    And I answer the call from the lock screen
    Then I see started call message for contact <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id347 @calling_basic
  Scenario Outline: Send text, image and knock while in the call with same user
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I answer the call from the overlay bar
    And I see started call message for contact <Contact>
    When I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    And I scroll to the bottom of conversation view
    Then I see new photo in the dialog
    When I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg> in the dialog
    # There is some issue in Selendroid - we cannot swipe cursor after the keyboard was hidden once
    # That is why we send the text after photo and ping and not before
    When I close input options
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | CallBackend | Message                   | Msg        |
      | user1Name | user2Name | autocall    | simple message in english | YOU PINGED |

  @id2210 @calling_basic @rc @rc42
  Scenario Outline: Calling bar buttons are clickable and change their states
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I see call overlay
    And I answer the call from the overlay bar
    When I remember the current state of <MuteBtnName> button
    And I press <MuteBtnName> button
    Then I see <MuteBtnName> button state is changed
    When I remember the current state of <SpeakerBtnName> button
    And I press <SpeakerBtnName> button
    Then I see <SpeakerBtnName> button state is changed
    When I press Cancel call button
    Then I do not see call overlay
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend | SpeakerBtnName | MuteBtnName |
      | user1Name | user2Name | autocall    | Speaker        | Mute        |

  @id2212 @calling_basic
  Scenario Outline: Correct calling bar in different places
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact1> calls me using <CallBackend>
    And I answer the call from the overlay bar
    And I see dialog page
    Then I see calling overlay Big bar
    And I navigate back from dialog page
    And I open Search by tap
    And I see People picker page
    And I see calling overlay Micro bar
    And I press Clear button
    And I tap on my avatar
    And I see personal info page
    And I see calling overlay Micro bar
    And I close Personal Info Page
    And I see calling overlay Micro bar
    And I tap on contact name <Contact2>
    And I see dialog page
    And I see calling overlay Mini bar

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @id2211 @calling_basic
  Scenario Outline: I can dismiss calling bar by swipe
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I see call overlay
    And I answer the call from the overlay bar
    And I dismiss calling bar by swipe
    Then I do not see call overlay
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id3239 @calling_basic
  Scenario Outline: Calling bar buttons are clickable and change their states in a group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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

  @id3240 @calling_basic @rc @rc42
  Scenario Outline: I can start group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe on text input
    And I press Call button
    Then I see call overlay
    When <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see calling overlay Big bar
    When <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3172 @regression @rc @rc42
  Scenario Outline: I can join group call in foreground
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I answer the call from the overlay bar
    Then I do not see join group call overlay
    And I see calling overlay Big bar
    When <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3174 @calling_basic @rc
  Scenario Outline: I can join group call after I ignored it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    Then I see call overlay
    When I click the ignore call button
    Then I see "JOIN CALL" button
    And I wait for 45 seconds
    When I press join group call button
    Then I do not see "JOIN CALL" button
    And I see calling overlay Big bar
    And <Contact1> stops all calls to <GroupChatName>
    Then I do not see join group call overlay

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3168 @calling_basic @rc
  Scenario Outline: (BUG AN-2825) I can join group call after I leave it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    When I answer the call from the overlay bar
    Then I do not see join group call overlay
    And I see calling overlay Big bar
    When I press Cancel call button
    Then I see "JOIN CALL" button
    And I wait for 20 seconds
    When I press join group call button
    Then I do not see "JOIN CALL" button
    And I see calling overlay Big bar
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    Then I do not see join group call overlay

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3164 @calling_basic
  Scenario Outline: Verify creating the call with a maximum amount of the people
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And <Contact3> calls <GroupChatName> using <CallBackend>
    And <Contact4> calls <GroupChatName> using <CallBackend>
    When I answer the call from the overlay bar
    Then I do not see join group call overlay
    And I see calling overlay Big bar
    And I see 5 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    And <Contact3> stops all calls to <GroupChatName>
    And <Contact4> stops all calls to <GroupChatName>

    Examples: 
      | CallBackend | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    |
      | autocall    | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | MaxGroupCallChat |

  @id3165 @calling_basic
  Scenario Outline: Verify impossibility to connect 6th person to the call
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And <Contact3> calls <GroupChatName> using <CallBackend>
    And <Contact4> calls <GroupChatName> using <CallBackend>
    And <Contact5> calls <GroupChatName> using <CallBackend>
    When I answer the call from the overlay bar
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
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | GroupChatName       | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | MaxGroupCallNegChat | autocall    |

  @id3253 @calling_basic
  Scenario Outline: Verify starting outgoing 1to1 call during group call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I answer the call from the overlay bar
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
    And I see 2 users take part in call
    And I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I see incoming calling message for contact <Contact3>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @id3255 @calling_basic
  Scenario Outline: Verify cancel outgoing 1to1 call during group call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
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
    And I see 3 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @id3180 @calling_advanced
  Scenario Outline: (BUG AN-2816) Verify receiving 1to1 call during group call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    When I answer the call from the overlay bar
    Then I do not see join group call overlay
    And I see calling overlay Big bar
    And <Contact3> calls <Name> using <CallBackend>
    And I see incoming calling message for contact <Contact3>
    And I answer the call from the overlay bar
    And I see end current call alert
    And I start new call from end current call alert
    And I see calling overlay Big bar
    And I see 2 users take part in call
    And I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I see incoming calling message for contact <Contact3>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @id3176 @calling_advanced @rc
  Scenario Outline: (BUG AN-2578) Verify receiving group call during 1to1 call and accepting it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
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
    And I see 3 users take part in call
    And I navigate back from dialog page
    And I tap on contact name <Contact3>
    And I see incoming calling message for contact <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @id3181 @calling_advanced
  Scenario Outline: (AN-2578 AN-2816 AN-2864) Verify receiving 1to1 call during group call and ignoring it
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I answer the call from the overlay bar
    And I see calling overlay Big bar
    And <Contact3> calls <Name> using <CallBackend>
    And I see incoming calling message for contact <Contact3>
    And I answer the call from the overlay bar
    Then I see end current call alert
    And I cancel new call from end current call alert
    And I see incoming calling message for contact <Contact3>
    When I click the ignore call button
    Then I see calling overlay Big bar
    And I see 3 users take part in call
    When I navigate back from dialog page
    And I tap on contact name <Contact3>
    Then I see incoming calling message for contact <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupCallChat | autocall    |

  @id3170 @calling_basic @rc @rc42
  Scenario Outline: Verify accepting group call in background
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see the call lock screen
    And I see a call from <GroupChatName> in the call lock screen
    And I answer the call from the lock screen
    And I see calling overlay Big bar
    And I see 3 users take part in call
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallChat | autocall    |

  @id2649 @calling_basic
  Scenario Outline: Lock device screen after initiating call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Call button
    And I see call overlay
    When I lock the device
    And I wait for 2 seconds
    And I unlock the device
    Then I see call overlay

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id1467 @calling_basic @rc @rc42
  Scenario Outline: Put client into background when in the call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And <Contact> calls me using <CallBackend>
    And I see call overlay
    And I answer the call from the overlay bar
    When I minimize the application
    And I wait for 10 seconds
    And I restore the application
    Then I see call overlay
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id369 @calling_advanced
  Scenario Outline: Other user trying to call me while I'm already in zeta call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    And I see dialog page
    And <Contact1> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact1>
    And I answer the call from the overlay bar
    And I see started call message for contact <Contact1>
    When <Contact2> calls me using <CallBackend>
    Then I see incoming calling message for contact <Contact2>
    And <Contact1>,<Contact2> stop all calls to me

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @id3184 @regression
  Scenario Outline: Verify leaving group conversation during the call
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    And I answer the call from the overlay bar
    And I do not see join group call overlay
    And I see calling overlay Big bar
    And I tap conversation details button
    And I press options menu button
    And I press Leave conversation menu button
    And I confirm leaving
    And I see Contact list
    When I swipe up contact list
    Then I do not see contact list with name <GroupChatName>
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCallChat | autocall    |
