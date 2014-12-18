Feature: Archive

  @smoke @id1041
  Scenario Outline: Archive and unarchive conversation
    Given I have 1 users and 1 contacts for 1 users
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
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
