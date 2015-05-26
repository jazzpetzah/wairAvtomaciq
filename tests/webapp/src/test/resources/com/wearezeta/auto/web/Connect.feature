Feature: Connect

  @smoke @id1910
  Scenario Outline: Accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I see connection request from one user
    And I open the list of incoming connection requests
    And I accept connection request from user <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1546
  Scenario Outline: Verify pending user profiles contain all the info required by spec
    Given There are 2 users where <Name> is me
    Given <UnknownContact> sent connection request to me
    Given I Sign in using login <Login> and password <Password>
    Given User me change accent color to VividRed
    Then I see connection request from one user
    When I open the list of incoming connection requests
    Then I see mail <UnknownContactMail> in connection request from user <UnknownContact>
    And I see connection message "Hello!" in connection request from user <UnknownContact>
    And I see avatar in connection request from user <UnknownContact>
    And I see accept button in connection request from user <UnknownContact>
    And I see ignore button in connection request from user <UnknownContact>
    And I see correct color for accept button in connection request from user <UnknownContact>
    And I see correct color for ignore button in connection request from user <UnknownContact>

    Examples: 
      | Login      | Password      | Name      | UnknownContact | UnknownContactMail | Message   |
      | user1Email | user1Password | user1Name | user2Name      | user2Email         | YOU ADDED |

  @smoke @id1571
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I wait until <Contact> exists in backend search results
    When I open People Picker from Contact List
    And I type <Contact> in search field of People Picker
    And I see user <Contact> found in People Picker
    And I click on not connected user <Contact> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id2043
  Scenario Outline: Verify 1to1 conversation is successfully created for sender end after connection is accepted
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I wait until <Login2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Login2> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I close People Picker
    And I see Contact list with name <Name2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I switch to Sign In page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I accept connection request from user <Name>
    And I see Contact list with name <Name>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Name2>
    And I open conversation with <Name2>
    And I see <Message> action for <Name2> in conversation

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     | Message      |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name | CONNECTED TO |

  @smoke @id1553
  Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I wait until <Login2> exists in backend search results
    When I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I open People Picker from Contact List
    And I type <Login2> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Name2>
    And I close People Picker
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I switch to Sign In page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I ignore connection request from user <Name>
    And I do not see Contact list with name <Name>
    And I close People Picker
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Name2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |

  @regression @id1554
  Scenario Outline: Verify you can block a person from profile view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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

  @regression @id1548
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user (People view)
    Given There are 3 users where <Name> is me
    Given <Contact> is connected to Me,<Contact2>
    Given <Contact> has group chat <ChatName> with Me,<Contact2>
    Given I sent connection request to <Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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

  @regression @id1555
  Scenario Outline: Verify you still receive messages from blocked person in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact1>
    And I click People button in one to one conversation
    And I see Single User Profile popover
    And I see Block button on Single User Profile popover
    And I click Block button on Single User Profile popover
    And I confirm user blocking on Single User Profile popover
    And I open conversation with <ChatName>
    And I write random message
    And I send message
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    Then I see random message in conversation
    And I write random message
    And I send message
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And User <Name> is me
    And I open conversation with <ChatName>
    And I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     | ChatName                 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | GroupChatWithBlockedUser |

  @staging @id1563
  Scenario Outline: Verify you dont receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <User1> is me
    Given Myself is connected to <User2>
    Given Contact <User2> sends image <Picture1> to single user conversation <User1>
    Given <User2> pinged the conversation with <User1>
    Given User <User2> sent message <Msg1> to conversation <User1>
    When I Sign in using login <User1> and password <User1Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <User2>
    Then I see text message <Msg1>
    And <User1> blocked <User2>
    And Contact <User2> sends image <Picture2> to single user conversation <User1>
    And <User2> pinged the conversation with <User1>
    And User <User2> sent message <Msg2> to conversation <User1>
    And I do not see Contact list with name <Name>
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <User2> is me
    And I Sign in using login <User2Email> and password <User2Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <User1>
    Then I see text message <Msg2>
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And <User1> unblocks user <User2>
    And User <User1> is me
    And I Sign in using login <User1Email> and password <User1Password>
    Then I see Contact list with name <User2>
    When I open conversation with <User2>
    Then I see text message <Msg2>

    Examples: 
      | User1     | User1Email | User1Password | User2     | User2Email | User2Password | Msg1       | Msg2     | Picture1 | Picture2    |
      | user1Name | user1Email | user2Password | user2Name | user2Email | user2Password | Message1   | Message2 | cat.jpg  | puppies.jpg |

  @staging @id2317
  Scenario Outline: Verify you can dismiss user suggestion in PYMK list
    Given There are 3 users where <Me> is me
    Given User <Contact1> has contact <Me> in address book
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact2>
    Given There are suggestions for user <Me> on backend
    Given I Sign in using login <MyEmail> and password <MyPassword>
    And I see my avatar on top of Contact list
    When I open People Picker from Contact List
    And I see user <Contact1> found in People Picker
    And I remove user <Contact1> from suggestions in People Picker
    And I do not see user <Contact1> found in People Picker

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact1  | Contact2  |
      | user1Name | user1Email | user1Password | user2Name | user3Name |

  @staging @id2318
  Scenario Outline: Verify you can add a user from PYMK list
    Given There are 3 users where <Me> is me
    Given User <Contact1> has contact <Me> in address book
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact2>
    Given There are suggestions for user <Me> on backend
    Given I Sign in using login <MyEmail> and password <MyPassword>
    Given I see my avatar on top of Contact list
    When I open People Picker from Contact List
    Then I see user <Contact1> found in People Picker
    When I make a connection request for user <Contact1> directly from People Picker
    And I close People Picker
    And I open conversation with <Contact1>
    Then I see CONNECTING TO action for <Contact1> in conversation

    Examples: 
      | Me        | MyEmail    | MyPassword    | Contact1  | Contact2  |
      | user1Name | user1Email | user1Password | user2Name | user3Name |

  @regression @id2548
  Scenario Outline: Verify you get auto-connected to people on sign-in
    Given There is 2 user where <Me> is me
    Given User <Me> has contact <Contact> in address book
    Given User <Contact> has contact <Me> in address book
    Given I Sign in using login <MyEmail> and password <MyPassword>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    Then I see CONNECTED TO action for <Contact> in conversation
    Then I see START A CONVERSATION action for <Contact> in conversation
    Then I do not see text message

    Examples:
      | Me        | MyEmail    | MyPassword    | Contact   |
      | user1Name | user1Email | user1Password | user2Name |

  @torun @id1564 
  Scenario Outline: Impossibility of starting 1:1 conversation with pending user (Search view)
    Given There are 2 users where <Name> is me
    Given I sent connection request to <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I open People Picker from Contact List
    And I wait for 2 seconds
    And I type <Contact> in search field of People Picker
    Then I see user <Contact> found in People Picker
    When I click on pending user <Contact> found in People Picker
    And I see Pending Outgoing Connection popover
    When I click Pending button on Pending Outgoing Connection popover
    Then I see conversation with <Contact> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
