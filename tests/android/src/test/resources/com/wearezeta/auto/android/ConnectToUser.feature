Feature: Connect to User

  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I input message <Message> in connect to dialog
    And I tap connect dialog Send button
    Then I see contact list loaded with User name <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page

    Examples: 
      | Login   | Password    | Name    | Contact  | Message |
      | aqaUser | aqaPassword | aqaUser | yourUser | Hello   |
