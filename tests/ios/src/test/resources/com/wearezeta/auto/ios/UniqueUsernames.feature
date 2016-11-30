Feature: Unique Usernames

  @C352039 @staging @fastLogin
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Add @name
    Then I see Unique Username page
    When I enter "<Empty>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<MinChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<22Chars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<Cyrillic>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<Arabic>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<Chines>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page
    When I enter "<SpecialChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I see Unique Username page

    Examples:
      | Name      | Empty | MinChars | 22Chars               | Cyrillic | Arabic | Chines | SpecialChars |
      | user1Name | ""    | 1        | 123456789012345789012 | МоёИмя   | اسمي   | 我的名字| %^&@#$       |