Feature: Calling

  @staging @id1007
  Scenario Outline: I want to accept a call through the incoming voice dialogue Button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And <Contact> calls me
    And I see incoming call from <Contact>
    And I accept call
    Then I see ongoing call with <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
