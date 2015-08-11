Feature: Sign Out

  @id329 @smoke
  Scenario Outline: Sign out from Wire
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Name      |
      | user1Name |
