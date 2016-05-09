Feature: Migration

  @migration1
  Scenario Outline: Verify migration from previous version works
    Given I initially deploy version with tag <PreviousVersion>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see Contact list with name <Contact>
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly

  Examples:
  | Login      | Password      | Name      | Contact   | PreviousVersion  |
  | user1Email | user1Password | user1Name | user2Name | 2016-04-29-12-36 |

  @migration2
  Scenario Outline: Verify migration from DB version 3 to 4 to current
    Given I initially deploy version with tag <DBVersion3>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see Contact list with name <Contact>
    When I deploy version with tag <DBVersion4>
    And I refresh page
    Then I am signed in properly
    And I see Contact list with name <Contact>
    #When I deploy version with tag 2016-05-06-12-30
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly
    And I see Contact list with name <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | DBVersion3       | DBVersion4       |
      | user1Email | user1Password | user1Name | user2Name | 2016-03-10-10-25 | 2016-04-06-15-06 |