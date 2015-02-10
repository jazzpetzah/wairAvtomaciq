Feature: Connect

  @staging @id1910
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

  @staging @id1571
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
