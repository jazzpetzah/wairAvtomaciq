Feature: Forward Message

  @C345370 @regression @fastLogin
  Scenario Outline: Verify forwarding own picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Wait for the picture to be loaded
    Given I wait for 3 seconds
    Given I long tap on image in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3name | testing.jpg |

  @C345383 @regression @fastLogin
  Scenario Outline: Verify outgoing/incoming connection requests/ left conversations are not in a forward list
    Given There are 6 users where <Name> is me
    Given Myself is connected to <ConnectedUser1>,<ConnectedUser2>,<BlockedUser>
    Given Myself has group chat <GroupChatName> with <ConnectedUser1>,<ConnectedUser2>
    Given User Myself blocks user <BlockedUser>
    Given <NonConnectedIncomingUser> sent connection request to Me
    Given Myself sent connection request to <NonConnectedOutgoingUser>
    Given I sign in using my email or phone number
    Given <ConnectedUser1> removes Me from group chat <GroupChatName>
    Given User <ConnectedUser1> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <ConnectedUser1>
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    Then I do not see <NonConnectedIncomingUser> conversation on Forward page
    And I do not see <NonConnectedOutgoingUser> conversation on Forward page
    And I do not see <GroupChatName> conversation on Forward page
    And I do not see <BlockedUser> conversation on Forward page

    Examples:
      | Name      | ConnectedUser1 | ConnectedUser2 | NonConnectedIncomingUser | NonConnectedOutgoingUser | BlockedUser | GroupChatName |
      | user1Name | user2Name      | user3name      | user4name                | user5Name                | user6Name   | Group         |

  @C345382 @regression @fastLogin
  Scenario Outline: Verify message is sent as normal when ephemeral keyboard is chosen in the destination conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact2> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I navigate back to conversations list
    Given I tap on contact name <Contact2>
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    And I select <Contact1> conversation on Forward page
    And I tap Send button on Forward page
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    And I wait for <Timeout> seconds
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Timeout |
      | user1Name | user2Name | user3name | 5       |

  @C345384 @regression @fastLogin
  Scenario Outline: ZIOS-7674 Verify forwarding to archived conversation unarchive it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User Myself archives single user conversation <Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I do not see conversation <Contact2> in conversations list
    Given I tap on contact name <Contact1>
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    And I navigate back to conversations list
    Then I see conversation <Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3name |

  @C345368 @regression @fastLogin
  Scenario Outline: Verify forwarding own text message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I type the default message and send it
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3name |

  @C345373 @regression @fastLogin
  Scenario Outline: Verify forwarding someone else audio message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 encrypted message to user <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    When I long tap on audio message placeholder in conversation view
    Then I do not see Forward badge item
    When I tap Play audio message button
    # Small wait to make sure download is completed
    And I wait for 5 seconds
    And I long tap on audio message placeholder in conversation view
    And I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see audio message container in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | user3name | test.m4a | audio/mp4 | Device1       |

  @C345371 @regression @fastLogin
  Scenario Outline: Verify forwarding someone else video message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <MIMEType> to single user conversation <Name> using device <DeviceName>
    # Given User Me sends 1 encrypted message to user <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    # Have to tap play video message to download video. Otherwise Forward button is missing.
    Given I tap on video message in conversation view
    Given I tap Done button on video message player page
    When I long tap on video message in conversation view
    And I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see video message container in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | user3name | testing.mp4 | video/mp4 | Device1    |