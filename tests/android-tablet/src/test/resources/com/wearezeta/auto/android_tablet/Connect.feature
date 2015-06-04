Feature: Connect

  @id2281 @smoke
  Scenario Outline: Send connection request from search in landscape
    Given There are 2 users where <Name> is me
    Given I sign in using my email
    And I rotate UI to landscape
    And I see the Conversations list
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I see the name <Contact> on Outgoing Connection popover
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id2280 @smoke
  Scenario Outline: Send connection request from search in portrait
    Given There are 2 users where <Name> is me
    Given I sign in using my email
    And I rotate UI to portrait
    And I see the Conversations list
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I see the name <Contact> on Outgoing Connection popover
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

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
