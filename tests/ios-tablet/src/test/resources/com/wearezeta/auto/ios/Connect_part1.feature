Feature: Connect

  @C2486 @regression @id2119
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I tap on Search input on People picker page
    And I wait until <Contact> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page
    When I open conversation details
    Then I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2489 @rc @regression @id3009
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I tap on Search input on People picker page
    And I wait until <Contact> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page
    When I open conversation details
    Then I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2483 @regression @id2354 @id2610
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
    And I click Connect button on not connected user profile popover
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see first item in contact list named <UnconnectedUser>

    Examples:
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @C2490 @rc @regression @id3011
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
    And I click Connect button on not connected user profile popover
    And I dismiss popover on iPad
    Then I see first item in contact list named <UnconnectedUser>

    Examples:
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @C2435 @regression @id2330
  Scenario Outline: Verify transitions between connection requests (ignoring) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I click on Pending request link in conversations list
    And I see Pending request page
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I do not see Pending request link in conversations list
    And I do not see conversation <Contact1> in conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I tap on Search input on People picker page
    And I input in People picker search field conversation name <Contact1>
    And I tap on conversation <Contact1> in search result
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2440 @regression @id3012
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
    When I click on Pending request link in conversations list
    And I see Pending request page
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I do not see Pending request link in conversations list
    And I do not see conversation <Contact1> in conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I tap on Search input on People picker page
    And I input in People picker search field conversation name <Contact1>
    And I tap on conversation <Contact1> in search result
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2434 @regression @id2329
  Scenario Outline: Verify transitions between connection requests (accepting) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I click on Pending request link in conversations list
    And I see Pending request page
    And I click on Connect button on Pending requests page <SentRequests> times
    And I navigate back to conversations list
    Then I do not see Pending request link in conversations list
    And I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list
    And I see conversation <Contact3> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2441 @regression @id3013
  Scenario Outline: Verify transitions between connection requests (accepting) [LANDSCAPE]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I click on Pending request link in conversations list
    And I see Pending request page
    And I click on Connect button on Pending requests page <SentRequests> times
    Then I do not see Pending request link in conversations list
    And I see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list
    And I see conversation <Contact3> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C2481 @regression @id2359
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <Contact> in conversations list
    When I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page
    Then I do not see text input in conversation view

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2482 @regression @id3014
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <Contact> in conversations list
    When I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page
    Then I do not see text input in conversation view

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2455 @regression @id2341
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
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user on iPad
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2457 @rc @regression @id3015
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
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user on iPad
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2491 @regression @id3016
  Scenario Outline: Verify you cannot send the invitation message twice [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Connect button on Pending outgoing connection page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    When I open search UI
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I see <Contact> user pending profile popover on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2796 @rc @regression @id3017
  Scenario Outline: Verify you can send an invitation via mail [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I press the send an invite button
    And I press invite others button
    And I press the copy button
    And I click close Invite list button
    And I click clear button
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I paste and commit the text
    Then I verify that pasted message contains MyEmail

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |