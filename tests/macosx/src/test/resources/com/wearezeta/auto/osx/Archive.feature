Feature: Archive

  @smoke @id1041
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And I archive conversation with <Contact>
    And I go to user <Name> profile
    Then I do not see conversation <Contact> in contact list
    When I go to archive
    And I open conversation with <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
