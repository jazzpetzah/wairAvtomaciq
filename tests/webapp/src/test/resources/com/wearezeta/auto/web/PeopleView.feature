Feature: People View

  @staging @id1691
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from Contact List
    And I search for <Contact1> in People Picker
    And I select <Contact1> from People Picker results
    And I search for <Contact2> in People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
