Feature: Unique Usernames

  @CC352039 @CC352040 @CC352041 @staging @fastLogin
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item @name
    Then I see Unique Username page
    When I fill in name input on Unique Username page <Empty> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page
    When I fill in name input on Unique Username page <22Chars> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page
    When I fill in name input on Unique Username page <Cyrilic> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page
    When I fill in name input on Unique Username page <Arabic> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page
    When I fill in name input on Unique Username page <Chines> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page
    When I fill in name input on Unique Username page <SpecialChars> username
    And I tap Save button on Unique Username page
    Then I see Unique Username page

    Examples:
      | Name      | Empty | 22Chars               | Cyrilic | Arabic | Chines | SpecialChars |
      | user1Name | ""    | 123456789012345789012 | МоёИмя  | اسمي   | 我的名字| %^&@#$       |