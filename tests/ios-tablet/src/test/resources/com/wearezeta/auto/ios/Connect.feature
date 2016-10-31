Feature: Connect

  @C2486 @regression @fastLogin
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I tap X button on Search UI page
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message in the conversation view
    When I open conversation details
    Then I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2489 @rc @regression @fastLogin
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I tap X button on Search UI page
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message in the conversation view
    When I open conversation details
    Then I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2483 @regression @fastLogin
  Scenario Outline: Send connection request to unconnected participant in a group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <UnconnectedUser>
    And I tap Connect button on not connected user profile popover
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see first item in contact list named <UnconnectedUser>

    Examples:
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @C2490 @rc @regression @fastLogin
  Scenario Outline: Send connection request to unconnected participant in a group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <UnconnectedUser>
    And I tap Connect button on not connected user profile popover
    And I dismiss popover on iPad
    Then I see first item in contact list named <UnconnectedUser>

    Examples:
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @C2435 @regression @fastLogin
  Scenario Outline: Verify transitions between connection requests (ignoring) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    And I tap Ignore button on Incoming Pending Requests page <SentRequests> times
    And I do not see Pending request link in conversations list
    And I do not see conversation <Contact1> in conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2440 @regression @fastLogin
  Scenario Outline: Verify transitions between connection requests (ignoring) [LANDSCAPE]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given <Contact1> is connected to <Contact4>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    And I tap Ignore button on Incoming Pending Requests page <SentRequests> times
    And I do not see Pending request link in conversations list
    And I do not see conversation <Contact1> in conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2434 @regression @fastLogin
  Scenario Outline: Verify transitions between connection requests (accepting) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    And I tap Connect button on Incoming Pending Requests page <SentRequests> times
    And I navigate back to conversations list
    Then I do not see Pending request link in conversations list
    And I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list
    And I see conversation <Contact3> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2441 @regression @fastLogin
  Scenario Outline: Verify transitions between connection requests (accepting) [LANDSCAPE]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    And I tap Connect button on Incoming Pending Requests page <SentRequests> times
    Then I do not see Pending request link in conversations list
    And I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list
    And I see conversation <Contact3> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2481 @regression @fastLogin
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <Contact> in conversations list
    When I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message in the conversation view
    Then I do not see text input in conversation view

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2482 @regression @fastLogin
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <Contact> in conversations list
    When I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message in the conversation view
    Then I do not see text input in conversation view

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2455 @regression @fastLogin
  Scenario Outline: Verify you don't receive any messages from blocked person in 1to1 chat [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact> securely pings conversation <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    When I wait for 5 seconds
    Then I do not see conversation <Contact> in conversations list
    When I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Unblock button on Search UI page
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2457 @rc @regression @fastLogin
  Scenario Outline: Verify you don't receive any messages from blocked person in 1to1 chat [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact> securely pings conversation <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    When I wait for 5 seconds
    Then I do not see conversation <Contact> in conversations list
    When I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Unblock button on Search UI page
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2491 @regression @fastLogin
  Scenario Outline: Verify you cannot send the invitation message twice [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I tap X button on Search UI page
    Then I see first item in contact list named <Contact>
    When I open search UI
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    Then I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2796 @rc @regression @fastLogin
  Scenario Outline: Verify you can send an invitation via mail [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap Send Invite button on Search UI page
    And I tap Invite Others button on Contacts UI page
    And I tap Copy Invite button on Search UI page
    And I tap Close Invite List button on Search UI page
    And I tap X button on Search UI page
    And I tap on contact name <Contact>
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I tap Send Message button in conversation view
    Then I see last message in the conversation view contains expected message MyEmail

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2461 @regression @fastLogin
  Scenario Outline: Verify possibility of disconnecting from conversation list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    When I navigate back to conversations list
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2465 @rc @regression @fastLogin
  Scenario Outline: Verify possibility of disconnecting from conversation list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2462 @regression @fastLogin
  Scenario Outline: (ZIOS-6323) Verify sending connection request after disconnecting [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    And I navigate back to conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Connect button on Pending outgoing connection page
    And I do not see Pending outgoing connection page
    And I tap X button on Search UI page
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2466 @rc @regression @fastLogin
  Scenario Outline: Verify sending connection request after disconnecting [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Connect button on Pending outgoing connection page
    And I do not see Pending outgoing connection page
    And I tap X button on Search UI page
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2463 @regression @fastLogin
  Scenario Outline: Verify possibility of disconnecting from Search UI [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact1> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I see the conversation "<Contact1>" exists in Search results

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2467 @regression @fastLogin
  Scenario Outline: Verify possibility of disconnecting from Search UI [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact1> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I see the conversation "<Contact1>" exists in Search results

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2437 @regression @fastLogin
  Scenario Outline: ZIOS-4985 Verify ignoring a connection request from another person (People view) [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap Start Conversation button on other user profile page
    And I tap Ignore button on Incoming Pending Requests page
    And I dismiss popover on iPad
    And I navigate back to conversations list
    # Workaround for ZIOS-4985
    # Then I do not see Pending request link in conversations list
    Then I see Pending request link in conversations list


    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2442 @rc @regression @fastLogin
  Scenario Outline: ZIOS-4985 Verify ignoring a connection request from another person (People view) [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap Start Conversation button on other user profile page
    And I tap Ignore button on Incoming Pending Requests page
    And I dismiss popover on iPad
    # Workaround for ZIOS-4985
    # Then I do not see Pending request link in conversations list
    Then I see Pending request link in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2513 @rc @regression @fastLogin
  Scenario Outline: Verify inbox is highlighted and opened in the list [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact> sent connection request to Me
    Given <Contact2> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    Then I see Incoming Pending Requests page

    Examples:
      | Name      | Contact   | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2430 @regression @fastLogin
  Scenario Outline: Verify displaying first and last names for the incoming connection request [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to Me
    Given User <Contact> changes name to <NewName>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    Then I see Hello connect message from user <NewName> on Incoming Pending Requests page

    Examples:
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C2431 @regression @fastLogin
  Scenario Outline: Verify displaying first and last names for the incoming connection request [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to Me
    Given User <Contact> changes name to <NewName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap Incoming Pending Requests item in conversations list
    Then I see Hello connect message from user <NewName> on Incoming Pending Requests page

    Examples:
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C2468 @regression @fastLogin
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And <Contact> cancel all outgoing connection requests
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2469 @regression @fastLogin
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And <Contact> cancel all outgoing connection requests
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2436 @regression @rc @fastLogin
  Scenario Outline: Verify accepting a connection request from another person (People view) [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,Me
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,Me
    Given <Contact2> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact2>
    And I tap Connect button on pending profile page
    And I confirm Connect action on pending profile page
    And I dismiss popover on iPad
    Then I see conversation <Contact2> in conversations list
    And I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C2470 @regression @fastLogin
  Scenario Outline: Verify copying invitation to the clipboard [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap Send Invite button on Search UI page
    And I tap Invite Others button on Contacts UI page
    And I tap Copy Invite button on Search UI page
    And I tap Close Invite List button on Search UI page
    And I tap X button on Search UI page
    And I tap on contact name <Contact>
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I tap Send Message button in conversation view
    Then I see last message in the conversation view contains expected message MyEmail

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |