Feature: Self Profile

  @smoke @id1743
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open self profile
    And I see user name on self profile page <Name>
    And I change username to <NewName>
    Then I see user name on self profile page <NewName>
    And I see my name <NewName> in Contact list

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewUserName | user2Name |
