Feature: Self Profile

  @id205 @smoke @regression
  Scenario Outline: Change user picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on my name <Name>
    And I tap on personal info screen
    And I tap change photo button
    And I press Gallery button
    And I wait for 10 seconds
    And I select picture
    And I press Confirm button
    And I wait for 60 seconds
    And I tap on personal info screen
    Then I see changed user picture

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id325 @smoke
  Scenario Outline: Check contact personal info
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    Then I see <Contact> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id328 @smoke @regression
  Scenario Outline: ZClient change name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on my name <Name>
    And I see personal info page
    And I tap on my name
    And I change <Name> to <NewName>
    And I swipe right to contact list
    Then I see contact list loaded with User name <NewName>
    And I tap on my name <NewName>
    Then I see my new name <NewName> and return old <Name>

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewTestName | user2Name |
