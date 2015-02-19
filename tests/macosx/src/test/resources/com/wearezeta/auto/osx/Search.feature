Feature: Search

  @regression @id1391
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    And I see Top People list in People Picker
    And I choose person from Top People
    And I press create conversation to enter conversation
    Then I see message CONNECTED TO <Contact> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
