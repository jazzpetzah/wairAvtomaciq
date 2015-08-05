Feature: Calling

  #CallBackend available values: 'autocall', 'webdriver'
  @id373 @calling_basic
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see dialog with missed call from <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | webdriver   |

  @id1503 @calling_basic
  Scenario Outline: Silence an incoming call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I click the ignore call button
    Then I cannot see the call bar

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @id1497 @calling_basic
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
      | user1Name | user2Name | webdriver   |

  @id1499 @calling_basic
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
      | user1Name | user2Name | webdriver   |

  @id347 @regression
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
    When I swipe left on text input
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | CallBackend | Message                   | Msg        |
      | user1Name | user2Name | webdriver   | simple message in english | YOU PINGED |

  @id2210 @regression
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
      | user1Name | user2Name | webdriver   | Speaker        | Mute        |

  @id2212 @regression
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
    And I see Contact list
    And I press Open StartUI
    And I see People picker page
    And I see calling overlay Micro bar
    And I press Clear button
    Then I see Contact list
    And I tap on my avatar
    And I see personal info page
    And I see calling overlay Micro bar
    And I close Personal Info Page
    And I see calling overlay Micro bar
    And I see Contact list
    And I tap on contact name <Contact2>
    And I see dialog page
    And I see calling overlay Mini bar

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | webdriver   |

  @id2211 @regression
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
      | user1Name | user2Name | webdriver   |

  @id3239 @staging
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

  @id3240 @staging
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
    #alternative accept call method for webdriver backend
    #And <Contact2> starts waiting instance using <CallBackend>
    #And <Contact2> accepts next incoming call automatically
    Then I see calling overlay Big bar
    When <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    #And <Contact2> stops all waiting instances
    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3172 @staging
  Scenario Outline: I can join group call in foreground
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    #alternative accept call method for webdriver backend
    #And <Contact2> starts waiting instance using <CallBackend>
    #And <Contact2> accepts next incoming call automatically
    Then I see call overlay
    And I answer the call from the overlay bar
    Then I do not see join group call overlay
    And I see calling overlay Big bar
    When <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>

    #And <Contact2> stops all waiting instances
    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3174 @staging
  Scenario Outline: I can join group call after I ignored it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see call overlay
    When I click the ignore call button
    Then I see "JOIN CALL" button
    And I wait for 45 seconds
    When I press join group call button
    Then I do not see "JOIN CALL" button
    And I see calling overlay Big bar
    And <Contact1> stops all calls to <GroupChatName>
    And <Contact2> stops all calls to <GroupChatName>
    Then I do not see join group call overlay

    Examples: 
      | CallBackend | Name      | Contact1  | Contact2  | GroupChatName    |
      | autocall    | user1Name | user2Name | user3Name | ChatForGroupCall |

  @id3168 @staging
  Scenario Outline: I can join group call after I leave it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And <Contact1> calls <GroupChatName> using <CallBackend>
    And <Contact2> calls <GroupChatName> using <CallBackend>
    Then I see call overlay
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
