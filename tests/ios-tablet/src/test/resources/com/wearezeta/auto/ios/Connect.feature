Feature: Connect

  @regression @id2355
  Scenario Outline: Verify sending a connection request to user chosen from search [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    And I see connect to <UnconnectedUser> dialog
    And I delete all connect message content
    And I see that connect button is disabled
    And I input message in connect dialog with <NumOfMessageChars> characters
    And I fill in 3 characters in connect dialog
    And I see message with max number of characters
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I open pending user conversation details
    And I see <UnconnectedUser> user pending profile popover on iPad

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | NumOfMessageChars | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | 140               | T           | BrightOrange |

  @regression @id3008
  Scenario Outline: Verify sending a connection request to user chosen from search [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    And I see connect to <UnconnectedUser> dialog
    And I delete all connect message content
    And I see that connect button is disabled
    And I input message in connect dialog with <NumOfMessageChars> characters
    And I fill in 3 characters in connect dialog
    And I see message with max number of characters
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I open pending user conversation details
    And I see <UnconnectedUser> user pending profile popover on iPad

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | NumOfMessageChars | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | 140               | T           | BrightOrange |

  @staging @id2119
  Scenario Outline: Send invitation message to a user [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page from user <Name>

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |

  @staging @id3009
  Scenario Outline: Send invitation message to a user [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page from user <Name>

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |


