Feature: People Picker

  @smoke
  Scenario Outline: Add contact
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I add user <Contact> from search results
    And I send invitation to user
    Then I see Contact list with name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  |
      | aqaUser | aqaPassword | aqaUser | yourUser |
