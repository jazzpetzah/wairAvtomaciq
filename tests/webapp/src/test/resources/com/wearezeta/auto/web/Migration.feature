Feature: Migration

  @migration1
  Scenario Outline: Verify migration is successful from DB version 3 to 4 to 6 to staging
    Given I initially deploy version with tag <DBVersion3>
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion3> to user Myself
    And I see text message <DBVersion3>
    And I deploy version with tag <DBVersion4>
    And I refresh page
    Then I am signed in properly
    And I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion4> to user Myself
    And I see text message <DBVersion3>
    And I wait for 10 seconds
    And I see text message <DBVersion4>
    And I deploy version with tag <DBVersion6>
    And I refresh page
    Then I am signed in properly
    And I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion6> to user Myself
    And I see text message <DBVersion3>
    And I see text message <DBVersion4>
    And I see text message <DBVersion6>
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message Staging to user Myself
    Then I see text message <DBVersion3>
    And I see text message <DBVersion4>
    And I see text message Staging

    Examples:
      | Login      | Password      | Name      | Contact   | DBVersion3       | DBVersion4       | DBVersion6       |
      | user1Email | user1Password | user1Name | user2Name | 2016-03-10-10-25 | 2016-04-06-15-06 | 2016-05-09-15-44 |

  @migration2
  Scenario Outline: Verify migration is successful from older DB versions straight to current
    Given I initially deploy version with tag <DBVersion>
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion> to user Myself
    And I see text message <DBVersion>
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message Staging to user Myself
    Then I see text message <DBVersion>
    And I see text message Staging

    Examples:
      | Login      | Password      | Name      | Contact   | DBVersion        |
      | user1Email | user1Password | user1Name | user2Name | 2016-03-10-10-25 |
      | user1Email | user1Password | user1Name | user2Name | 2016-04-06-15-06 |