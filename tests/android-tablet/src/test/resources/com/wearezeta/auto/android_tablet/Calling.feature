Feature: Calling

  @id3175 @regression
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

  @id2910 @regression @rc
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

  @id3123 @regression @rc
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

  @id2842 @regression
  Scenario Outline: I see miss call notification on the list and inside conversation view (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation <Contact2> in my conversations list
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

  @id3125 @regression
  Scenario Outline: I see miss call notification on the list and inside conversation view (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation <Contact2> in my conversations list
    When <Contact2> calls me using <CallBackend>
    And I see calling overlay Big bar
    And <Contact2> stops all calls to me
    Then I do not see calling overlay Big bar
    And I see missed call notification near <Contact2> conversations list item
    When I tap the conversation <Contact2>
    Then I see missed call notification in the conversation view
    When I navigate back
    Then I do not see missed call notification near <Contact2> conversations list item

    Examples:
      | CallBackend | Name      | Contact1  | Contact2  |
      | autocall    | user1Name | user2Name | user3Name |

  @id3124 @regression @rc
  Scenario Outline: I can dismiss calling bar by swipe (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    When I swipe up on the calling overlay
    Then I do not see calling overlay Big bar

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id2911 @regression @rc
  Scenario Outline: I can dismiss calling bar by swipe (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And <Contact> calls me using <CallBackend>
    And I see calling overlay Big bar
    When I swipe up on the calling overlay
    Then I do not see calling overlay Big bar

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id2840 @regression
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
    When I swipe left on text input in the conversation view
    And I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    When I scroll to the bottom of the conversation view
    And I swipe left on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<PingMessage>" in the conversation view
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend | TextMessage  | PingMessage | AcceptBtnName |
      | user1Name | user2Name | autocall    | text message | YOU PINGED  | Accept        |

  @id3113 @regression
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
    When I swipe left on text input in the conversation view
    And I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    When I scroll to the bottom of the conversation view
    And I swipe left on text input in the conversation view
    And I tap Ping button in the conversation view
    Then I see the ping message "<PingMessage>" in the conversation view
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend | TextMessage  | PingMessage | AcceptBtnName |
      | user1Name | user2Name | autocall    | text message | YOU PINGED  | Accept        |

  @id3259 @regression @rc
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

  @id2875 @staging
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
