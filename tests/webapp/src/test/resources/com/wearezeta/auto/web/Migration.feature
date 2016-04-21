Feature: Migration

  @migration
  Scenario Outline: Verify registration screen has German-localized strings
    Given I deploy latest production version
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Then I see Contact list with name <Contact>
    When I deploy latest staging version
    And I wait until latest staging version is deployed
    Then I refresh page

  Examples:
  | Login      | Password      | Name      | Contact   |
  | user1Email | user1Password | user1Name | user2Name |