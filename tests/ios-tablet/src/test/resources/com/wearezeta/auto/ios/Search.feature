Feature: Search

  @staging @id2147
  Scenario Outline: Verify search by email [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Login      | Password      | Name      | ContactEmail | ContactName |
      | user1Email | user1Password | user1Name | user2Email   | user2Name   |

  @staging @id2147
  Scenario Outline: Verify search by email [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Login      | Password      | Name      | ContactEmail | ContactName |
      | user1Email | user1Password | user1Name | user2Email   | user2Name   |

  @staging @id2148
  Scenario Outline: Verify search by name [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

