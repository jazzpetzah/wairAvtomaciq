Feature: Connect

  @smoke @id1910
  Scenario Outline: Accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I see connection request from one user
    And I open the list of incoming connection requests
    And I accept connection request from user <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1571
  Scenario Outline: Verify sending a connection request to user choosen from search
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait up to 15 seconds until <Contact> exists in backend search results
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
    And I see my name on top of Contact list
    And I wait up to 15 seconds until <Login2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Login2> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Name2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I switch to Sign In page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my name on top of Contact list
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
    And I see my name on top of Contact list
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
    And I see my name on top of Contact list
    And I wait up to 15 seconds until <Login2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Login2> in search field of People Picker
    And I see user <Name2> found in People Picker
    And I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Name2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I switch to Sign In page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my name on top of Contact list
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I ignore connection request from user <Name>
    And I do not see Contact list with name <Name>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    Then I see Contact list with name <Name2>

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name |

  @torun @id1554
  Scenario Outline: Verify you can block a person from profile view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    And I see Block button on Single User Profile popover
    When I click Block button on Single User Profile popover
    And I confirm user blocking on Single User Profile popover
    Then I do not see Contact list with name <Contact>
    Then I do not see Single User Profile popover
    Then I see user name on self profile page <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
