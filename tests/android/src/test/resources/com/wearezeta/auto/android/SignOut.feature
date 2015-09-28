Feature: Sign Out

  @id329 @regression @rc @rc42
  Scenario Outline: Sign out from Wire
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on my avatar
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
