Feature: Connect

  @C1756 @smoke
  Scenario Outline: Accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given <Contact> sent connection request to <Name>
    Given User me change accent color to VividRed
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I see connection request from one user
    And I open the list of incoming connection requests
    And I see avatar in connection request from user <Contact>
    And I see correct color for accept button in connection request from user <Contact>
    Then I see unique username in connection request from user <Contact>
    And I accept connection request from user <Contact>
    Then I see Contact list with name <Contact>
    And I see unique username of <Contact> in conversation
    When I write message <Message>
    And I send message
    Then I see text message <Message>

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | message |

  @C1691 @regression
  Scenario Outline: Verify pending user profiles contain all the info required by spec
    Given There are 2 users where <Name> is me
    Given <UnknownContact> has unique username
    Given <UnknownContact> sent connection request to me
    Given User me change accent color to VividRed
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see connection request from one user
    When I open the list of incoming connection requests
    Then I see unique username in connection request from user <UnknownContact>
    And I do not see mail in connection request from user <UnknownContact>
    And I see avatar in connection request from user <UnknownContact>
    And I see accept button in connection request from user <UnknownContact>
    And I see ignore button in connection request from user <UnknownContact>
    And I see correct color for accept button in connection request from user <UnknownContact>
    And I see correct color for ignore button in connection request from user <UnknownContact>

    Examples: 
      | Login      | Password      | Name      | UnknownContact |
      | user1Email | user1Password | user1Name | user2Name      |

  @C1815 @mute
  Scenario Outline: Verify pending user profiles contain known people information
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <UnknownContact1> is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <UnknownContact2> is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <UnknownContact3> is connected to <Contact1>,<Contact2>,<Contact3>
    Given <UnknownContact4> is connected to <Contact1>,<Contact2>
    Given <UnknownContact5> is connected to <Contact1>
    Given <UnknownContact1> sent connection request to me
    Given <UnknownContact2> sent connection request to me
    Given <UnknownContact3> sent connection request to me
    Given <UnknownContact4> sent connection request to me
    Given <UnknownContact5> sent connection request to me
    # We need to wait for the backend
    Given I wait for 20 seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see connection request from 5 user
    When I open the list of incoming connection requests
    Then I see an amount of 3 avatars in known connections in connection request from user <UnknownContact1>
    And I see an amount of 2 others in known connections in connection request from user <UnknownContact1>
    And I see an amount of 4 avatars in known connections in connection request from user <UnknownContact2>
    And I see an amount of 3 avatars in known connections in connection request from user <UnknownContact3>
    And I see an amount of 2 avatars in known connections in connection request from user <UnknownContact4>
    And I see an amount of 1 avatars in known connections in connection request from user <UnknownContact5>

    Examples:
      | Login      | Password      | Name      | UnknownContact1 | UnknownContact2 | UnknownContact3 | UnknownContact4 | UnknownContact5 | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  |
      | user1Email | user1Password | user1Name | user2Name       | user3Name       | user4Name       | user5Name       | user6Name       | user7Name | user8Name | user9Name | user10Name | user11Name |

  @C1699 @smoke
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given I wait until <Contact> exists in backend search results
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Bring Your Friends or Invite People button
    And I type <ContactUniqueUsername> in search field of People Picker
    And I see user <Contact> found in People Picker
    And I click on not connected user <Contact> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see Contact list with name <Contact>
    And I see unique username in outgoing connection request to user <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | ContactUniqueUsername |
      | user1Email | user1Password | user1Name | user2Name | user2UniqueUsername   |

  @C1817 @regression
  Scenario Outline: Verify sending a connection request to user from conversation view
    Given There are 3 users where <Name> is me
    Given <Contact2> has unique username
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <ChatName> with <Name>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I click on avatar of user <Contact2> in conversation view
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see Contact list with name <Contact2>
    And I see unique username in outgoing connection request to user <Contact2>
    And I see cancel pending request button in the conversation view
    And I verify that conversation input and buttons are not visible

    Examples: 
      | Login      | Password      | Name      | ChatName | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | id3322   | user2Name | user3Name |

  @C1767 @smoke
  Scenario Outline: Verify 1to1 conversation is successfully created for sender end after connection is accepted
    Given There are 2 users where <Name> is me
    Given User <Name2> changes unique username to <Name2>
    Given I switch to Sign In page
    When I wait until <Name2> exists in backend search results
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open search by clicking the people button
    And I type <Name2UniqueUsername> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Name2>
    And I open conversation with <Name2>
    And I see unique username in outgoing connection request to user <Name2>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name2> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I see unique username in connection request from user <Name>
    And I accept connection request from user <Name>
    And I see Contact list with name <Name>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name> is me
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    Then I see Contact list with name <Name2>
    And I open conversation with <Name2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     | Name2UniqueUsername |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name | user2UniqueUsername |

  @C1694 @regression
  Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request
    Given There are 2 users where <Name> is me
    Given <Name2> has unique username
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <Name2> exists in backend search results
    When I am signed in properly
    Then I open search by clicking the people button
    When I see Search is opened
    And I see Bring Your Friends or Invite People button
    And I type <Name2UniqueUsername> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Name2>
    And I see unique username in outgoing connection request to user <Name2>
    And I open conversation with <Name2>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name2> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I see unique username in connection request from user <Name>
    And I ignore connection request from user <Name>
    And I do not see Contact list with name <Name>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name> is me
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    Then I see Contact list with name <Name2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     | Name2UniqueUsername |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name | user2UniqueUsername |

  @C1695 @regression
  Scenario Outline: Verify you can block a person from profile view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    And I see Block button on Single User Profile popover
    When I click Block button on Single User Profile popover
    And I confirm user blocking on Single User Profile popover
    Then I do not see Contact list with name <Contact>
    Then I do not see Single User Profile popover

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1692 @regression
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user (People view)
    Given There are 3 users where <Name> is me
    Given <Contact> is connected to Me,<Contact2>
    Given <Contact> has group chat <ChatName> with Me,<Contact2>
    Given I sent connection request to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click on participant <Contact2> on Group Participants popover
    Then I see Pending button on Group Participants popover
    When I click Pending button on Group Participants popover
    Then I see conversation with <Contact2> is selected in conversations list
    Then I do not see Group Participants popover

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName                 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChatWithPendingUser |

  @C1696 @regression
  Scenario Outline: Verify you still receive messages from blocked person in a group chat
    Given There are 3 users where <Name> is me
    Given <Contact1> has unique username
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    And I see Block button on Single User Profile popover
    When I click Block button on Single User Profile popover
    And I confirm user blocking on Single User Profile popover
    And I open conversation with <ChatName>
    And I write random message
    And I send message
    Then I see random message in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Then I see Sign In page
    And User <Name2> is me
    When I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see random message in conversation
    And I write random message
    And I send message
    Then I see random message in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login> and password <Password>
    And User <Name> is me
    And I open conversation with <ChatName>
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     | ChatName                 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | GroupChatWithBlockedUser |

  @C1805 @smoke
  Scenario Outline: Verify you dont receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <User1> is me
    Given User <User2> changes unique username to <User2>
    Given Myself is connected to <User2>
    Given User <User2> changes avatar picture to default
    Given I switch to Sign In page
    Given I Sign in using login <User2Email> and password <User2Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <User1> and password <User1Password>
    And I am signed in properly
    When I open conversation with <User2>
    And Contact <User2> sends message <Msg1> to user <User1>
    Then I see text message <Msg1>
    And <User1> blocked <User2>
    And User <User2> sends image <Picture1> to single user conversation <User1>
    And User <User2> pinged in the conversation with <User1>
    And Contact <User2> sends message <Msg2> to user <User1>
    And I do not see Contact list with name <Name>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <User2> is me
    And I see Sign In page
    And I Sign in using login <User2Email> and password <User2Password>
    And I am signed in properly
    When I open conversation with <User1>
    Then I see text message <Msg2>
    When I open preferences by clicking the gear button
    And I see connected devices dialog
    And I click OK on connected devices dialog
    And I do not see connected devices dialog
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And <User1> unblocks user <User2>
    And User <User1> is me
    And I see Sign In page
    And I Sign in using login <User1Email> and password <User1Password>
    Then I see Contact list with name <User2>
    When I open conversation with <User2>

    # Uncommented last step because of WEBAPP-862
    # Then I see text message <Msg2>
    Examples: 
      | User1     | User1Email | User1Password | User2     | User2Email | User2Password | Msg1     | Msg2     | Picture1                 |
      | user1Name | user1Email | user2Password | user2Name | user2Email | user2Password | Message1 | Message2 | userpicture_portrait.jpg |

  @C1782 @mute
  Scenario Outline: Verify you can dismiss user suggestion in PYMK list
    Given There are 3 users where <Me> is me
    # we need to wait a bit, otherwise backend throws a 429 status
    Given I wait for 10 seconds
    Given User <Contact1> has contact <Me> in address book
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact2>
    Given There are suggestions for user <Me> on backend
    Given I switch to Sign In page
    Given I Sign in using login <MyEmail> and password <MyPassword>
    Given I am signed in properly
    Given I am signed in properly
    When I open search by clicking the people button
    And I see user <Contact1> found in People Picker
    And I remove user <Contact1> from suggestions in People Picker
    And I do not see user <Contact1> found in People Picker

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact1  | Contact2  |
      | user1Name | user1Email | user1Password | user2Name | user3Name |

  @C1783 @mute
  Scenario Outline: Verify you can add a user from PYMK list
    Given There are 3 users where <Me> is me
    # we need to wait a bit, otherwise backend throws a 429 status
    Given I wait for 10 seconds
    Given User <Contact1> has contact <Me> in address book
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact2>
    Given There are suggestions for user <Me> on backend
    Given I switch to Sign In page
    Given I Sign in using login <MyEmail> and password <MyPassword>
    Given I am signed in properly
    When I open search by clicking the people button
    Then I see user <Contact1> found in People Picker
    When I make a connection request for user <Contact1> directly from People Picker
    And I close People Picker
    And I open conversation with <Contact1>
    Then I see connecting message for <Contact1> in conversation

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact1  | Contact2  |
      | user1Name | user1Email | user1Password | user2Name | user3Name |

  @C1785 @legacy @C147867
  Scenario Outline: Verify you get auto-connected to people on sign-in
    Given There is 2 user where <Me> is me
    # we need to wait a bit, otherwise backend throws a 429 status
    Given I wait for 10 seconds
    Given User <Me> has contact <Contact> in address book
    # we need to wait a bit, otherwise backend throws a 429 status
    Given I wait for 10 seconds
    Given User <Contact> has contact <Me> in address book
    Given I switch to Sign In page
    Given I Sign in using login <MyEmail> and password <MyPassword>
    And I am signed in properly
    When I open conversation with <Contact>
    Then I see CONNECTED TO action for <Contact> in conversation
    Then I see START A CONVERSATION action for <Contact> in conversation
    Then I do not see text message

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact   |
      | user1Name | user1Email | user1Password | user2Name |

  @legacy
  Scenario Outline: Verify you get auto-connected to people while being logged-in
    Given There is 2 user where <Me> is me
    # we need to wait a bit, otherwise backend throws a 429 status
    Given I wait for 10 seconds
    Given User <Me> has contact <Contact> in address book
    Given I switch to Sign In page
    Given I Sign in using login <MyEmail> and password <MyPassword>
    And I am signed in properly
    # we need to wait a bit, otherwise backend throws a 429 status
    And I wait for 10 seconds
    When User <Contact> has contact <Me> in address book
    And I open conversation with <Contact>
    Then I see CONNECTED TO action for <Contact> in conversation
    And I see START A CONVERSATION action for <Contact> in conversation
    And I do not see text message

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact   |
      | user1Name | user1Email | user1Password | user2Name |

  @C1790 @regression
  Scenario Outline: I want to cancel a pending request from search
    Given There are 3 users where <Name> is me
    Given <Contact1> has unique username
    Given I sent connection request to <Contact1>
    Given Myself is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I wait until <Contact1> exists in backend search results
    When I open search by clicking the people button
    And I wait for the search field of People Picker to be empty
    And I type <Contact1Username> in search field of People Picker
    Then I see user <Contact1> found in People Picker
    When I click on pending user <Contact1> found in People Picker
    And I see Pending Outgoing Connection popover
    And I see unique username on Pending Outgoing Connection popover to user <Contact1>
    When I click Cancel request on Pending Outgoing Connection popover
    Then I see Cancel request confirmation popover
    When I click No button on Cancel request confirmation popover
    Then I see Pending Outgoing Connection popover
    When I click Cancel request on Pending Outgoing Connection popover
    Then I see Cancel request confirmation popover
    When I click Yes button on Cancel request confirmation popover
    Then I do not see Pending Outgoing Connection popover
    When I close People Picker
    Then I do not see Contact list with name <Contact1>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    Then I am signed in properly
    And I do not see Contact list with name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact1Email | Contact1Password | Contact1Username    | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user2Email    | user2Password    | user2UniqueUsername | user3Name |

  @C145959 @regression
  Scenario Outline: I want to cancel a pending request from conversation list
    Given There are 3 users where <Name> is me
    Given I sent connection request to <Contact1>
    Given <Contact1> has unique username
    Given Myself is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    And I see unique username in outgoing connection request to user <Contact1>
    When I click on options button for conversation <Contact1>
    Then I see a conversation option <ConvOption1> on the page
    And I see a conversation option <ConvOption2> on the page
    And I see a conversation option <ConvOption3> on the page
    When I click cancel request in the options popover
    Then I do not see Contact list with name <Contact1>
    When I open preferences by clicking the gear button
    Then I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And User <Contact1> is me
    When I Sign in using login <Contact1Email> and password <Contact1Password>
    And I am signed in properly
    Then I do not see Contact list with name <Name>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ConvOption1 | ConvOption2    | ConvOption3 | Contact1Email | Contact1Password |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Archive     | Cancel request | Block       | user2Email    | user2Password    |

  @C145967 @regression
  Scenario Outline: I want to archive a pending request from conversation list
    Given There are 3 users where <Name> is me
    Given I sent connection request to <Contact1>
    Given Myself is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    When I click on options button for conversation <Contact1>
    Then I see a conversation option <ConvOption1> on the page
    And I see a conversation option <ConvOption2> on the page
    And I see a conversation option <ConvOption3> on the page
    When I click archive in the options popover
    Then I do not see connection request from one user
    When I open archive
    Then I see archive list with name <Contact1>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ConvOption1 | ConvOption2    | ConvOption3 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Archive     | Cancel request | Block       |

  @C147863 @regression
  Scenario Outline: Verify you can cancel a pending request from conversation view
    Given There are 3 users where <Name> is me
    Given <Contact1> has unique username
    Given I sent connection request to <Contact1>
    Given Myself is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact1>
    And I see unique username in outgoing connection request to user <Contact1>
    Then I see cancel pending request button in the conversation view
    When I click cancel pending request button in the conversation view
    Then I do not see connection request from one user

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C145966 @regression
  Scenario Outline: Verify you can block a user who sent you an incoming connection request from conversation list
    Given There are 3 users where <Name> is me
    Given I sent connection request to <Contact1>
    Given Myself is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact1>
    When I click on options button for conversation <Contact1>
    Then I see a conversation option <ConvOption1> on the page
    And I see a conversation option <ConvOption2> on the page
    And I see a conversation option <ConvOption3> on the page
    When I click the option to block in the options popover
    Then I see a block warning modal
    When I click cancel button in the block warning
    Then I see Contact list with name <Contact1>
    When I click on options button for conversation <Contact1>
    And I click the option to block in the options popover
    Then I see a block warning modal
    When I click block button in the block warning
    Then I do not see connection request from one user

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ConvOption1 | ConvOption2    | ConvOption3 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Archive     | Cancel request | Block       |

  @C352248 @regression
  Scenario Outline: Verify number of common friends is shown on the outgoing connection request
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I wait until <Contact2> has 1 common friends on the backend
    And I sent connection request to <Contact2>
    And I open conversation with <Contact2>
    Then I see conversation with <Contact2> is selected in conversations list
    And I see 1 common friends

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C352249 @regression
  Scenario Outline: Verify number of common friends is shown on the incoming connection request
    Given There are 11 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <UnknownContact1>, <UnknownContact2>, <UnknownContact3>, <UnknownContact4>, <UnknownContact5> have unique username
    Given <UnknownContact1> is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <UnknownContact2> is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <UnknownContact3> is connected to <Contact1>,<Contact2>,<Contact3>
    Given <UnknownContact4> is connected to <Contact1>,<Contact2>
    Given <UnknownContact5> is connected to <Contact1>
    Given <UnknownContact1> sent connection request to me
    Given <UnknownContact2> sent connection request to me
    Given <UnknownContact3> sent connection request to me
    Given <UnknownContact4> sent connection request to me
    Given <UnknownContact5> sent connection request to me
 # We need to wait for the backend
    Given I wait until <UnknownContact1> has 5 common friends on the backend
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see connection request from 5 user
    When I open the list of incoming connection requests
    And I see 5 common friends in connection request from user <UnknownContact1>
    And I see 4 common friends in connection request from user <UnknownContact2>
    And I see 3 common friends in connection request from user <UnknownContact3>
    And I see 2 common friends in connection request from user <UnknownContact4>
    And I see 1 common friend in connection request from user <UnknownContact5>

    Examples:
      | Login      | Password      | Name      | UnknownContact1 | UnknownContact2 | UnknownContact3 | UnknownContact4 | UnknownContact5 | Contact1  | Contact2  | Contact3  | Contact4   | Contact5   |
      | user1Email | user1Password | user1Name | user2Name       | user3Name       | user4Name       | user5Name       | user6Name       | user7Name | user8Name | user9Name | user10Name | user11Name |

