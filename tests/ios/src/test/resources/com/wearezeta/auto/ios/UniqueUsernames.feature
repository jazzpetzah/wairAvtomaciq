Feature: Unique Usernames

  @C352039 @staging @fastLogin
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item @name
    When I enter "<Empty>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    When I enter "<MinChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    When I attempt to enter over max allowed <MaxChars> chars as name on Unique Username page
    Then I see that name length is less than <MaxChars> chars on Unique Username page
    When I enter "<Cyrillic>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    When I enter "<Arabic>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    When I enter "<Chines>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    When I enter "<SpecialChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page

    Examples:
      | Name      | Empty | MinChars | MaxChars | Cyrillic | Arabic | Chines | SpecialChars |
      | user1Name | ""    | 1        | 22       | МоёИмя   | اسمي   | 我的名字   | %^&@#$    |