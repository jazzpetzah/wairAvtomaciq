Feature: Search

  @staging @id1722
  Scenario Outline: Verify the new conversation is created on the other end (Search UI source)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open People Picker from Contact List
    And I wait up to 15 seconds until <Contact1> exists in backend search results
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I see my name on top of Contact list
    Then I see Contact list with name <Contact1>,<Contact2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact1> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    And I see my name on top of Contact list
    Then I see Contact list with name <Name>,<Contact2>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact2> is me
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Contact2Email> and password <Contact2Password>
    And I see my name on top of Contact list
    Then I see Contact list with name <Name>,<Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact1Email | Contact1Password | Contact2  | Contact2Email | Contact2Password |
      | user1Email | user1Password | user1Name | user2Name | user2Email    | user2Password    | user3Name | user3Email    | user3Password    |