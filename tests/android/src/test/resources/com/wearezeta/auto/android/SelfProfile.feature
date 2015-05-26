Feature: Self Profile

  @id205 @smoke
  Scenario Outline: Change user picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on my avatar
    And I tap on personal info screen
    And I tap change photo button
    And I press Gallery button
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
    And I tap conversation details button
    Then I see <Contact> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id328 @smoke
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Then Contact list appears
    When I tap on my avatar
    And I see personal info page
    And I tap on my name
    Then I see edit name field
    When I clear name field 
    And I change <Name> to <NewName>
    Then I see my new name <NewName>

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewTestName | user2Name |
