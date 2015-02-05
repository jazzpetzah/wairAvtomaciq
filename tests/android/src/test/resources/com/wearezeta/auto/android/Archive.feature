Feature: Archive

  @id316 @staging
  Scenario Outline: Verify you can archive and unarchive
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I swipe archive conversation <Contact>
    Then Contact name <Contact> is not in list
    And I swipe up contact list
    And I swipe right on a <Contact>
    And I swipe archive conversation <Contact>
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
