Feature: Search

  @smoke @id1691
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @regression @id1722
  Scenario Outline: Verify the new conversation is created on the other end (Search UI source)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open People Picker from Contact List
    And I wait until <Contact1> exists in backend search results
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait until <Contact2> exists in backend search results
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Contact1>,<Contact2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Name>,<Contact2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact2> is me
    And I see Sign In page
    And I Sign in using login <Contact2Email> and password <Contact2Password>
    And I see my avatar on top of Contact list
    Then I see Contact list with name <Name>,<Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact1Email | Contact1Password | Contact2  | Contact2Email | Contact2Password |
      | user1Email | user1Password | user1Name | user2Name | user2Email    | user2Password    | user3Name | user3Email    | user3Password    |

  @regression @id1566
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
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
    When I open People Picker from Contact List
    And I type <Contact> in search field of People Picker
    And I select <Contact> from People Picker results
    And I see Unblock button on Single User Profile popover
    When I click Unblock button on Single User popover
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1742
  Scenario Outline: Verify possibility of invitation accepting
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I click Show Search button on Contacts Upload dialog
    And I see Send Invitation button on People Picker page
    When I click Send Invitation button on People Picker page
    Then I see Send Invitation popover
    When I remember invitation link on Send Invitation popover
    And I click Send Invitation button on People Picker page
    Then I do not see Send Invitation popover
    When I navigate to previously remembered invitation link
    Then I see You are invited page

    # We don't go further since invitation flow will be changed soon
    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id1721
  Scenario Outline: Verify you can add new user from search results from the other end
    Given There are 2 users where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And I wait until <Login2> exists in backend search results
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I open People Picker from Contact List
    And I type <Login2> in search field of People Picker
    Then I see user <Name2> found in People Picker
    When I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see Contact list with name <Name2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I see connection request from one user
    And I open the list of incoming connection requests
    When I accept connection request from user <Name>
    Then I see conversation with <Name> is selected in conversations list
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name> is me
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I see Contact list with name <Name2>
    And I open conversation with <Name2>

    Examples: 
      | Login      | Password      | Name      | Name2     | Login2     | Password2     |
      | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password |

  @staging @id1721
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given User Me sent message <Message1> to conversation <Contact1>
    Given User <Contact1> sent message <Message1> to conversation <Name>
    Given User Me sent message <Message1> to conversation <Contact2>
    Given User <Contact2> sent message <Message1> to conversation <Name>
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And I open People Picker from Contact List
    And I wait till Top People list appears
    When I select <Contact1> from Top People
    And I choose to create conversation from People Picker
    Then I see conversation with <Contact1> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Message1 |
