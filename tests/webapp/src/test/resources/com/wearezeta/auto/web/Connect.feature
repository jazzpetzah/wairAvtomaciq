Feature: Connect

  @smoke @id1910
  Scenario Outline: Accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connection request
    And I open connection requests list
    And I accept connection request from user <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1571
  Scenario Outline: Verify sending a connection request to user choosen from search
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from Contact List
    And I wait up to 15 seconds until <Contact> exists in backend search results
    And I search for <Contact> in People Picker
    And I see user <Contact> found on Search
    And I click on not connected user <Contact> found by Search
    And I click Connect button on Connect to popup
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2043
  Scenario Outline: Verify 1to1 conversation is successfully created for sender end after connection is accepted
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from Contact List
    And I wait up to 15 seconds until <Name2> exists in backend search results
    And I search for <Name2> in People Picker
    And I see user <Name2> found on Search
    And I click on not connected user <Name2> found by Search
    And I click Connect button on Connect to popup
    And I see Contact list with name <Name2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name2> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my name <Name2> in Contact list
    And I see connection request
    And I open connection requests list
    And I accept connection request from user <Name>
    And I see Contact list with name <Name>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Name> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    Then I see Contact list with name <Name2>
    And I open conversation with <Name2>
    And I see <Message> action for <Name2> in conversation

    Examples: 
      | Login      | Login2     | Password      | Password2     | Name      | Name2     | Message      |
      | user1Email | user2Email | user1Password | user2Password | user1Name | user2Name | CONNECTED TO |