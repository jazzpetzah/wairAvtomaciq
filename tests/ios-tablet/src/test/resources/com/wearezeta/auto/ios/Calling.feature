Feature: Calling

  @C2400 @C2409 @calling_basic @fastLogin
  Scenario Outline: Verify starting and ending outgoing call by same person [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2407 @rc @calling_basic @fastLogin
  Scenario Outline: Verify calling from missed call indicator in conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>
    And I tap missed call button to call contact <Contact>
    And I see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2410 @calling_basic @fastLogin
  Scenario Outline: Verify ignoring of incoming call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2411 @rc @calling_basic @fastLogin
  Scenario Outline: Verify accepting incoming call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    Then I see call status message contains "<Contact>"

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2399 @calling_basic @fastLogin
  Scenario Outline: Receiving missed call notification from one user [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    And I tap on contact name <Contact>
    Then I see missed call from contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2408 @calling_basic @fastLogin
  Scenario Outline: Screenlock device when in the call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    Then I lock screen for 5 seconds
    And I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2427 @rc @calling_advanced @fastLogin
  Scenario Outline: 3rd person tries to call me after I initate a call to somebody [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> starts instance using <CallBackend2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on contact name <Contact1>
    And I remember the left side state of <Contact2> conversation item on iPad
    And I tap Audio Call button
    And I see Calling overlay
    And <Contact2> calls me
    And I see call status message contains "<Contact2> calling"
    And I tap Ignore button on Calling overlay
    And I see Calling overlay
    And <Contact2> stops calling me
    And I tap Leave button on Calling overlay
    And I do not see Calling overlay
    And I see the state of <Contact2> conversation item is changed on iPad
    And I tap on contact name <Contact2>
    Then I see missed call from contact <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | chrome      | chrome       |

  @C2395 @calling_basic @fastLogin
  Scenario Outline: Put app into background after initiating call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    Then I close the app for 5 seconds
    And I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2404 @calling_basic @fastLogin
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    Then I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2404 @calling_basic @fastLogin
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button) [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    Then I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2401 @calling_basic @fastLogin
  Scenario Outline: I want to end the call from the ongoing voice overlay [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I see Calling overlay
    And I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    And I wait for 5 seconds
    And <Contact> stops calling me
    And I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C2413 @rc @calling_basic @fastLogin
  Scenario Outline: Verify putting client to the background during 1-to-1 call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on contact name <Contact>
    When <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Accept button on Calling overlay
    And I see Leave button on Calling overlay
    When I close the app for 5 seconds
    Then I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C145968 @rc @calling_basic @fastLogin
  Scenario Outline: Verify starting a group call [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Then I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GROUPCALL     |

  @C145969 @rc @calling_advanced @fastLogin
  Scenario Outline: Verify leaving and coming back to the call in 20 seconds [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given <Contact1> accepts next incoming call automatically
    Given <Contact2> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I remember the state of <GroupChatName> conversation item
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    # Wait for the call to be established
    And I wait for 5 seconds
    And I see <NumberOfAvatars> avatars on the Calling overlay
    Then I tap Leave button on Calling overlay
    And I do not see Calling overlay
    Then I see the state of <GroupChatName> conversation item is changed
    And I wait for 20 seconds
    And I tap Audio Call button
    # Wait for the call to be established
    And I wait for 5 seconds
    Then I see <NumberOfAvatars> avatars on the Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | GROUPCALL     | chrome      | 2               |

  @C145950 @rc @calling_basic @fastLogin
  Scenario Outline: Verify joining 2 other people on the group call [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    Then I see call status message contains "<GroupChatName> ringing"
    And I tap Accept button on Calling overlay
    # Wait for the call to be established
    And I wait for 5 seconds
    And I see <NumberOfAvatars> avatars on the Calling overlay

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | CallBackend | NumberOfAvatars |
      | user1Name | user2Name | user3Name | GROUPCALL     | chrome      | 2               |