Feature: Calling

  @C426 @id3175 @calling_basic
  Scenario Outline: Verify receiving "missed call" notification (GCM) after ending group call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    When <Contact1> calls <GroupChatName> using <CallBackend>
    And I see calling overlay Big bar
    And <Contact1> stops all calls to <GroupChatName>
    Then I do not see calling overlay Big bar
    And I see missed group call notification in the conversation view

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @C783 @id2910 @calling_basic @rc @rc44
  Scenario Outline: Calling bar buttons are clickable and change its state (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    And I tap <AcceptBtnName> button on the calling overlay
    When I remember the current state of <MuteBtnName> button on the calling overlay
    And I tap <MuteBtnName> button on the calling overlay
    Then I see <MuteBtnName> button state is changed on the calling overlay
    And I tap <MuteBtnName> button on the calling overlay
    Then I see <MuteBtnName> button state is not changed on the calling overlay
    And I do not see <SpeakerBtnName> button on the calling overlay
    When I tap <DismissBtnName> button on the calling overlay
    Then I do not see calling overlay Big bar

    Examples:
      | Name      | Contact   | CallBackend | SpeakerBtnName | MuteBtnName | AcceptBtnName | DismissBtnName |
      | user1Name | user2Name | autocall    | Speaker        | Mute        | Accept        | Dismiss        |

  @C821 @id4009 @regression @rc @rc44
  Scenario Outline: I can join group call in foreground (landscape)
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <Contact2> starts waiting instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given <Contact3> starts waiting instance using <CallBackend>
    Given <Contact3> accepts next incoming call automatically
    Given <Contact4> starts waiting instance using <CallBackend>
    Given <Contact4> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    When I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And <Contact1> calls <GroupChatName> using <CallBackend2>
    And I tap <AcceptBtnName> button on the calling overlay
    And I see calling overlay Big bar
    # FIXME: Wait until webapp calling issues are fixed on staging
    # And I wait for 10 seconds
    # Then <Contact2>,<Contact3>,<Contact4> verify to have 4 flows
    # Then <Contact2>,<Contact3>,<Contact4> verify that all flows have greater than 0 bytes

    Examples:
      | CallBackend | CallBackend2 | Name      | Contact1  | Contact2  | Contact3  | Contact4  | GroupChatName    | AcceptBtnName |
      | chrome      | autocall     | user1Name | user2Name | user3Name | user4Name | user5Name | ChatForGroupCall | Accept        |

  @C794 @id3123 @calling_basic @rc @rc44
  Scenario Outline: Calling bar buttons are clickable and change its state (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    And I tap <AcceptBtnName> button on the calling overlay
    When I remember the current state of <MuteBtnName> button on the calling overlay
    And I tap <MuteBtnName> button on the calling overlay
    Then I see <MuteBtnName> button state is changed on the calling overlay
    And I tap <MuteBtnName> button on the calling overlay
    Then I see <MuteBtnName> button state is not changed on the calling overlay
    And I do not see <SpeakerBtnName> button on the calling overlay
    When I tap <DismissBtnName> button on the calling overlay
    Then I do not see calling overlay Big bar

    Examples:
      | Name      | Contact   | CallBackend | SpeakerBtnName | MuteBtnName | AcceptBtnName | DismissBtnName |
      | user1Name | user2Name | autocall    | Speaker        | Mute        | Accept        | Dismiss        |

  @C487 @id2842 @calling_basic
  Scenario Outline: (AN-3145) I see miss call notification on the list and inside conversation view (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I see the conversation <Contact2> in my conversations list
    And I tap the conversation <Contact1>
    When <Contact2> calls me using <CallBackend>
    And I see calling overlay Big bar
    And <Contact2> stops all calls to me
    Then I do not see calling overlay Big bar
    When I swipe right to show the conversations list
    Then I see missed call notification near <Contact2> conversations list item
    When I tap the conversation <Contact2>
    Then I see missed call notification in the conversation view
    When I navigate back
    Then I do not see missed call notification near <Contact2> conversations list item

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  |
      | autocall    | user1Name | user2Name | user3Name |

  @C521 @id3125 @calling_basic
  Scenario Outline: (AN-3145) I see miss call notification on the list and inside conversation view (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I see the conversation <Contact2> in my conversations list
    And I tap the conversation <Contact1>
    When <Contact2> calls me using <CallBackend>
    And I see calling overlay Big bar
    And <Contact2> stops all calls to me
    Then I do not see calling overlay Big bar
    And I see missed call notification near <Contact2> conversations list item
    When I tap the conversation <Contact2>
    Then I see missed call notification in the conversation view
    And I do not see missed call notification near <Contact2> conversations list item

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  |
      | autocall    | user1Name | user2Name | user3Name |

  @C485 @id2840 @calling_basic
  Scenario Outline: Send text, image and ping while in the call with same user (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    And I tap <AcceptBtnName> button on the calling overlay
    And I tap the text input in the conversation view
    When I type the message "<TextMessage>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<TextMessage>" in the conversation view
    And I hide keyboard
    When I swipe right on text input in the conversation view
    And I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    When I scroll to the bottom of the conversation view
    And I swipe right on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<PingMessage>" in the conversation view
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend | TextMessage  | PingMessage | AcceptBtnName |
      | user1Name | user2Name | autocall    | text message | YOU PINGED  | Accept        |

  @C516 @id3113 @calling_basic
  Scenario Outline: Send text, image and ping while in the call with same user (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    And I tap <AcceptBtnName> button on the calling overlay
    And I tap the text input in the conversation view
    When I type the message "<TextMessage>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<TextMessage>" in the conversation view
    And I hide keyboard
    When I swipe right on text input in the conversation view
    And I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    When I scroll to the bottom of the conversation view
    And I swipe right on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<PingMessage>" in the conversation view
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend | TextMessage  | PingMessage | AcceptBtnName |
      | user1Name | user2Name | autocall    | text message | YOU PINGED  | Accept        |

  @C811 @id3259 @calling_basic @rc
  Scenario Outline: Receive call while Wire is running in the background (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I minimize the application
    When <Contact> calls me using <CallBackend>
    Then I see full screen calling overlay
    When I accept call on full screen calling overlay
    Then I see calling overlay Big bar
    And I see call participants Myself,<Contact> on the calling overlay
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C770 @id2875 @calling_basic @rc
  Scenario Outline: Receive call while tablet in sleeping mode (screen locked) (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I lock the device
    When <Contact> calls me using <CallBackend>
    Then I see full screen calling overlay
    When I accept call on full screen calling overlay
    Then I see calling overlay Big bar
    And I see call participants Myself,<Contact> on the calling overlay
    And <Contact> stops all calls to me

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @C486 @id2841 @calling_advanced
  Scenario Outline: (BUG AN-2578) Other wire user trying to call me while I'm already in wire call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I see the conversation <Contact2> in my conversations list
    When <Contact1> calls me using <CallBackend>
    Then I see calling overlay Big bar
    When I tap <AcceptBtnName> button on the calling overlay
    And I see call participants Myself,<Contact1> on the calling overlay
    And <Contact2> calls me using <CallBackend>
    Then I see calling overlay Big bar
    And I see call participant <Contact2> on the calling overlay
    And I see the conversation <Contact1> in my conversations list
    And I see the conversation <Contact2> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | AcceptBtnName |
      | user1Name | user2Name | user3Name | autocall    | Accept        |

  @C813 @id3801 @calling_basic @rc
  Scenario Outline: Silence an incoming call (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    When I tap <SilenceBtn> button on the calling overlay
    Then I do not see calling overlay Big bar

    Examples:
      | Name      | Contact   | CallBackend | SilenceBtn |
      | user1Name | user2Name | autocall    | Ignore     |

  @C814 @id3802 @calling_basic @rc
  Scenario Outline: Silence an incoming call (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    When I tap <SilenceBtn> button on the calling overlay
    Then I do not see calling overlay Big bar

    Examples:
      | Name      | Contact   | CallBackend | SilenceBtn |
      | user1Name | user2Name | autocall    | Ignore     |