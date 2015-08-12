Feature: Calling

  @id3175 @staging
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

  @id2910 @staging
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

  @id3123 @staging
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

  @id3124 @staging
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

  @id2911 @staging
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

  @id2874 @staging @torun
  Scenario Outline: Receive call while Wire is running in the background (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I minimize the application
    When <Contact> calls me using <CallBackend>
    Then I see full screen calling overlay
    And I do not see <DismissBtnName> button on full screen calling overlay
    When I accept call on full screen calling overlay
    Then I see <DismissBtnName> button on full screen calling overlay
    And <Contact> stops all calls to me

    Examples: 
      | Name      | Contact   | CallBackend | DismissBtnName |
      | user1Name | user2Name | autocall    | Dismiss        |
