Feature: Connect to User

@smoke
@regression
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I tap on create conversation
    Then I see <Login> added <Contact> message on Dialog page

    Examples: 
      | Login   | Password    | Name    | Contact  |
      | aqaUser | aqaPassword | aqaUser | yourUser |
