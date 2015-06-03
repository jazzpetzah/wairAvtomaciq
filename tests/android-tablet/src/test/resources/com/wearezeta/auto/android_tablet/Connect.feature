Feature: Connect

  @id2281 @staging @torun
  Scenario Outline: Send connection request from search in landscape
    Given There are 2 users where <Name> is me
    Given I Sign in using my email
    And I rotate UI to landscape
    And I see the Conversations list
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Connect To <Contact> page
    And I tap Edit connection message field
    And I type connection message "<Message>"
    And I tap Connect button
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Login      | Password      | Name      | Contact   | Message       |
      | user1Email | user1Password | user1Name | user2Name | Hellow friend |

  @id2280 @staging
  Scenario Outline: Send connection request from search in portrait
    Given There are 2 users where <Name> is me
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    And I wait until <Contact> exists in backend search results
    When I swipe down on tablet contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on tablet People picker page <Contact>
    And I see tablet connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I see People picker page
    And I navigate back to Conversations List
    Then I see contact list loaded with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Message       |
      | user1Email | user1Password | user1Name | user2Name | Hellow friend |

  @id2245 @staging 
  Scenario Outline: Accept connection request in landscape mode
    Given There are 2 users where <Name> is me
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
      | Login      | Password      | Name      | Contact   | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting |

  @id2259 @staging
  Scenario Outline: Accept connection request in portrait mode
    Given There are 2 users where <Name> is me
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
      | Login      | Password      | Name      | Contact   | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting |
