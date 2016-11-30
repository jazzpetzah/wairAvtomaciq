Feature: Usernames

  @CC352039 @staging @fastLogin
  Scenario Outline: Verify empty username is impossible to save
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Add @name

    Examples:
      | Name      |
      | user1Name |