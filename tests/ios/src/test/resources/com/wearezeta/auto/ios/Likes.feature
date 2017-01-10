Feature: Likes

  @C225979 @rc @regression @fastLogin
  Scenario Outline: Verify liking/unliking a message by tapping on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225980 @rc @regression @fastLogin
  Scenario Outline: Verify liking/unliking a message from a message menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Like badge item
    When I remember the state of Like icon in the conversation
    And I long tap default message in conversation view
    And I tap on Unlike badge item
    Then I see the state of Like icon is changed in the conversation
    And I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C226008 @rc @regression @fastLogin
  Scenario Outline: Verify impossibility of liking the message after leaving (being removed) from a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 encrypted message to group conversation <Group>
    Given I see conversations list
    # Sync message delivery
    Given I wait for 5 seconds
    Given <Contact1> removes Myself from group chat <Group>
    Given I tap on contact name <Group>
    When I tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I double tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I long tap default message in conversation view
    Then I do not see Like badge item

    Examples:
      | Name      | Contact1  | Contact2  | Group            |
      | user1Name | user2Name | user3Name | RemovedFromGroup |

  @C225993 @rc @regression @fastLogin
  Scenario Outline: Verify liking a message tapping on like icon, when someone liked this message before
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    # Wait for delivery
    Given I wait for 5 seconds
    Given I see conversations list
    Given User <Contact> likes the recent message from user Myself
    Given I tap on contact name <Contact>
    # Wait for sync
    Given I wait for 3 seconds
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225999 @regression @fastLogin
  Scenario Outline: Verify deleted for myself my message doesn't reappear after someone liked it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device D1
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view
    When User <Contact> likes the recent message from user Myself
    Then I see 0 default messages in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225998 @rc @regression @fastLogin
  Scenario Outline: Verify editing already liked message and like after edit
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I tap Like icon in the conversation
    And I remember the state of Like icon in the conversation
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text>
    When I tap "<Text>" message in conversation view
    And I tap Like icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | Text  |
      | user1Name | user2Name | aloha |

  @C226004 @rc @regression @fastLogin
  Scenario Outline: Verify receiving a like in a conversation which was removed
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Picture> to group conversation <Group>
    When I swipe right on a <Group>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And User <Contact1> likes the recent message from group conversation <Group>
    Then I do not see conversation <Group> in conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Group>" in Search UI input field
    And I tap on conversation <Group> in search result
    Then I see 0 photos in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact1  | Contact2  | Picture     | Group        |
      | user1Name | user2Name | user3Name | testing.jpg | DeletedGroup |

  @C226005 @regression @fastLogin
  Scenario Outline: Verify receiving like from a blocked person
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User <Contact1> adds a new device <Contact1Device> with label <Contact1DeviceLabel>
    Given User Myself blocks user <Contact1>
    Given I sign in using my email or phone number
    Given User Myself sends file <FileName> having MIME type <FileMIME> to group conversation <Group> using device <MyDevice>
    Given I see conversations list
    Given I tap on contact name <Group>
    When I do not see Like icon in the conversation
    And User <Contact1> likes the recent message from group conversation <Group>
    # Wait for sync
    And I wait for 3 seconds
    And I tap toolbox of the recent message
    Then I see user <Contact1> in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group            | FileName | FileMIME  | Contact1Device | Contact1DeviceLabel | MyDevice |
      | user1Name | user2Name | user3Name | BlockedContGroup | test.m4a | audio/mp4 | C1Device       | C1DeviceLabel       | MyDev    |

  @C225987 @C226012 @regression @fastLogin
  Scenario Outline: Verify liking a shared file
    Given There are 3 users where <Name> is me
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to group conversation <Group> using device <Contact1Device>
    Given I see conversations list
    Given I tap on contact name <Group>
    When I do not see Like icon in the conversation
    And I long tap on file transfer placeholder in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see user Myself in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group         | FileName | FileExt | FileSize | FileMIME                 | Contact1Device |
      | user1Name | user2Name | user3Name | FileLikeGroup | testing  | tmp     | 240 KB   | application/octet-stream | C1Device       |

  @C225984 @rc @regression @fastLogin
  Scenario Outline: Verify liking a video message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <MIMEType> to group conversation <Group> using device <Contact1Device>
    Given I see conversations list
    Given I tap on contact name <Group>
    # Wait for preview load
    Given I wait for 3 seconds
    When I do not see Like icon in the conversation
    And I long tap on video message in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see user Myself in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group          | FileName    | MIMEType  | Contact1Device |
      | user1Name | user2Name | user3Name | VideoLikeGroup | testing.mp4 | video/mp4 | C1Device       |

  @C225985 @rc @regression @fastLogin
  Scenario Outline: Verify liking Soundcloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<SCLink>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait for sync
    Given I wait for 5 seconds
    When I tap at 10% of width and 10% of height of the recent message
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | SCLink                                                           |
      | user1Name | user2Name | https://soundcloud.com/trevorjasper14/lateef-two-birds-one-stone |

  @C226006 @regression @fastLogin
  Scenario Outline: Verify archived conversation stays in archive after receiving like for the message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Picture> to group conversation <Group>
    When I swipe right on a <Group>
    And I tap Archive conversation action button
    Then I do not see conversation <Group> in conversations list
    When User <Contact1> likes the recent message from group conversation <Group>
    #Lets wait a bit to give a chance to Like action
    And I wait for 5 seconds
    Then I do not see conversation <Group> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Picture     | Group        |
      | user1Name | user2Name | user3Name | testing.jpg | ArchiveGroup |

  @C225992 @C225996 @regression @fastLogin
  Scenario Outline: Verify liking/unliking a message by double tapping
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    When I double tap default message in conversation view
    Then I see the state of Like icon is changed in the conversation
    And I double tap default message in conversation view
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C226000 @regression @fastLogin
  Scenario Outline: Verify deleted for myself someone else message doesn't reappear after someone liked it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view
    When User <Contact> likes the recent message from user Myself
    Then I see 0 default messages in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225981 @regression @fastLogin
  Scenario Outline: Verify liking a link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<Link>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Load the link
    Given I wait for 5 seconds
    When I tap at 5% of width and 5% of height of the recent message
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @C225982 @regression @fastLogin
  Scenario Outline: Verify liking a picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap at 5% of width and 5% of height of the recent message
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C225983 @regression @fastLogin
  Scenario Outline: Verify liking an audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap on audio message placeholder in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see user Myself in likers list

    Examples:
      | Name      | Contact   | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | ContactDevice |

  @C225989 @regression @fastLogin
  Scenario Outline: Verify liking a location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <ContactDevice>
    # Let it to receive the message
    Given I wait for 5 seconds
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap at 5% of width and 5% of height of the recent message
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | ContactDevice |

  @C226011 @rc @regression @fastLogin
  Scenario Outline: Verify total number of likers is shown, when names are too wide
    Given There are 5 users where <Name> is me
    Given User <Contact2> changes name to <LongName>2
    Given User <Contact3> changes name to <LongName>3
    Given User <Contact4> changes name to <LongName>4
    Given User <Contact2> adds new device D2
    Given User <Contact3> adds new device D3
    Given User <Contact4> adds new device D4
    Given Myself is connected to all other
    Given Myself has group chat <Group> with all other
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 encrypted message to group conversation <Group>
    # Wait for sync
    Given I wait for 5 seconds
    Given User <Contact2> likes the recent message from group conversation <Group>
    Given User <Contact3> likes the recent message from group conversation <Group>
    Given User <Contact4> likes the recent message from group conversation <Group>
    Given I see conversations list
    When I tap on contact name <Group>
    Then I see "<LikersLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Group    | LikersLabel | LongName                                 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | BigGroup | 3 people    | VeeeeeeeeerrrrrrryyyyyyyLooooongNaaaaame |
