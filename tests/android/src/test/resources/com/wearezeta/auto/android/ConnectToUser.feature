Feature: Connect to User

  @torun@smoke @nonUnicode
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I tap on edit connect request field
    And I type Connect request <Message>
    And I press Connect button
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  | Message       |
      | aqaUser | aqaPassword | aqaUser | yourUser | Hellow friend |
