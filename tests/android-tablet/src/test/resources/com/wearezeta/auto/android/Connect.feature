Feature: Connect

  @id2281 @staging
  Scenario Outline: Send connection request from search in landscape
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I swipe down on tablet contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on tablet People picker page <Contact>
    And I wait for 20 seconds
    And I see tablet connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I see People picker page
    And I press Clear button
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | Message       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hellow friend |

  @id2280 @staging
  Scenario Outline: Send connection request from search in portrait
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I swipe down on tablet contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on tablet People picker page <Contact>
    And I see tablet connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I see People picker page
    And I navigate back to Conversations List
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | Message       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hellow friend |

  @id2245 @staging 
  Scenario Outline: Accept connection request in landscape mode
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact> sent connection request to me
    And I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    And I wait for 10 seconds
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 1 person waiting |

  @id2259 @staging
  Scenario Outline: Accept connection request in portrait mode
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact> sent connection request to me
    And I rotate UI to portrait
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    And I wait for 10 seconds
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 1 person waiting |
