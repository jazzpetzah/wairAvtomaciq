Feature: Self Profile

  @id328 @staging
    Scenario Outline: ZClient change name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in to self profile using login <Login> and password <Password>
    When I tap on my name
    And I change <Name> to <NewName>
    Then I see personal info page loaded with my name <NewName>
    When I tap on my name
    And I change <NewName> to <Name>
    Then I see personal info page loaded with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewTestName | user2Name |